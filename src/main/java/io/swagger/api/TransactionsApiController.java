package io.swagger.api;

import io.swagger.model.ArrayOfTransactions;
import io.swagger.model.BankAccount;
import io.swagger.model.User;
import io.swagger.model.enums.AccountType;
import io.swagger.model.enums.Role;
import io.swagger.security.JwtTokenFilter;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.BankAccountService;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import org.threeten.bp.OffsetDateTime;
import io.swagger.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-26T11:40:47.282Z[GMT]")
@RestController
public class TransactionsApiController implements TransactionsApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private BankAccountService bankAccountService;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Transaction> createTransaction(@Parameter(in = ParameterIn.DEFAULT, description = "Transaction object", required = true, schema = @Schema()) @Valid @RequestBody Transaction body) {
        String accept = request.getHeader("Accept");
        if (accept != null && (accept.contains("application/json") || accept.contains("*/*"))) {

            BankAccount bankAccountFrom = bankAccountService.getBankAccountByIban(body.getAccountFrom());
            BankAccount bankAccountTo = bankAccountService.getBankAccountByIban(body.getAccountTo());

            if (bankAccountFrom.getAccountType() == AccountType.SAVINGS && bankAccountTo.getUserId() != bankAccountFrom.getUserId())
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Can not transfer to foreign account from savings account");

            if (bankAccountTo.getAccountType() == AccountType.SAVINGS && bankAccountFrom.getUserId() != bankAccountTo.getUserId())
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Can not transfer directly to foreign savings account");

            User userPerforming = userService.findByToken(tokenProvider.resolveToken(request));
            User userFrom = userService.findById(bankAccountFrom.getUserId());
            User userTo = userService.findById(bankAccountTo.getUserId());

            Transaction transaction = new Transaction();
            transaction.setAccountFrom(bankAccountFrom.getIban());
            transaction.setAccountTo(bankAccountTo.getIban());
            transaction.userPerforming(body.getUserPerforming());
            transaction.setAmount(body.getAmount());

            if (userPerforming.getRole() == Role.CUSTOMER) {

                if (transaction.getAmount() > userFrom.getTransactionLimit())
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Transaction limit would be exceeded");

                Double newCurrentDayLimit = userFrom.getCurrentDayLimit() + transaction.getAmount();
                if (newCurrentDayLimit > userFrom.getDayLimit())
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Day limit would be exceeded");
            }

            Double newBalance = bankAccountFrom.getBalance() - transaction.getAmount();
            if (newBalance <= bankAccountFrom.getAbsoluteLimit())
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Accounts absolute limit would be exceeded");

            bankAccountFrom.setBalance(bankAccountFrom.getBalance() - transaction.getAmount());
            bankAccountTo.setBalance(bankAccountTo.getBalance() + transaction.getAmount());
            bankAccountService.updateBankAccount(bankAccountFrom);
            bankAccountService.updateBankAccount(bankAccountTo);
            transactionService.storeTransaction(transaction);

            return ResponseEntity.status(HttpStatus.OK).body(transaction);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Return type not accepted");
        }
    }

    public ResponseEntity<ArrayOfTransactions> getAllTransactions(@RequestParam Map<String, String> params) {
        String accept = request.getHeader("Accept");
        if (accept != null && (accept.contains("application/json") || accept.contains("*/*"))) {
            ArrayOfTransactions transactions = new ArrayOfTransactions();
            User user = userService.findByToken(tokenProvider.resolveToken(request));

            if (user.getRole() == Role.EMPLOYEE) {
                transactions = transactionService.getAllTransactions();
            } else if (user.getRole() == Role.CUSTOMER) {

                for (BankAccount bankAccount : user.getBankAccounts()) {
                    List<Transaction> accountTransactions = transactionService.getTransactionsForUser(bankAccount.getIban());
                    for (Transaction transaction : accountTransactions) {
                        if (!transactions.contains(transaction))
                            transactions.add(transaction);
                    }
                }
            }

            /*
             * Filter based on IBAN-From, IBAN-To or both
             */
            if (params.containsKey("ibanFrom") && !params.containsKey("ibanTo")) {
                transactions = transactions.stream().filter(
                        t -> params.get("ibanFrom").equals(t.getAccountFrom())).collect(Collectors.toCollection(ArrayOfTransactions::new));
            } else if (!params.containsKey("ibanFrom") && params.containsKey("ibanTo")) {
                transactions = transactions.stream().filter(
                        t -> params.get("ibanTo").equals(t.getAccountTo())).collect(Collectors.toCollection(ArrayOfTransactions::new));
            } else if (params.containsKey("ibanFrom") && params.containsKey("ibanTo")) {
                transactions = transactions.stream().filter(
                        t -> params.get("ibanFrom").equals(t.getAccountFrom())
                                && params.get("ibanTo").equals(t.getAccountTo())).collect(Collectors.toCollection(ArrayOfTransactions::new));
            }

            /*
             * Filter based on (Offset)DateTime
             */
            if (params.containsKey("timestamp")) {
                transactions = transactions.stream().filter(
                        t -> OffsetDateTime.parse(params.get("timestamp")).equals(t.getTimestamp())).collect(Collectors.toCollection(ArrayOfTransactions::new));
            }

            /*
             * Only show transactions where the IBAN-From or IBAN-To matches query
             * Only available if user is an employee
             */
            if (params.containsKey("ibanToOrFrom") && user.getRole() == Role.EMPLOYEE) {
                transactions = transactions.stream().filter(
                        t -> params.get("ibanToOrFrom").equals(t.getAccountFrom())
                                || params.get("ibanToOrFrom").equals(t.getAccountTo())).collect(Collectors.toCollection(ArrayOfTransactions::new));
            }

            /*
             * Filter based on the user performing the transaction
             * Only available if user is an employee
             */
            if (params.containsKey("userPerforming") && user.getRole() == Role.EMPLOYEE) {
                Integer userPerforming = Integer.parseInt(params.get("userPerforming"));
                transactions = transactions.stream().filter(
                        t -> userPerforming.equals(t.getUserPerforming())).collect(Collectors.toCollection(ArrayOfTransactions::new));
            }

            return ResponseEntity.status(HttpStatus.OK).body(transactions);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Return type not accepted");
        }
    }
}
