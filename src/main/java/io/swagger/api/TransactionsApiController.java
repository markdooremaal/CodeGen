package io.swagger.api;

import io.swagger.model.ArrayOfTransactions;
import io.swagger.model.BankAccount;
import io.swagger.model.User;
import io.swagger.model.enums.Role;
import io.swagger.security.JwtTokenFilter;
import io.swagger.security.JwtTokenProvider;
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

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Transaction> createTransaction(@Parameter(in = ParameterIn.DEFAULT, description = "Transaction object", required = true, schema = @Schema()) @Valid @RequestBody Transaction body) {
        String accept = request.getHeader("Accept");
        if (accept != null && (accept.contains("application/json") || accept.contains("*/*"))) {
            try {
                Transaction transaction = new Transaction();
                transaction.setAccountFrom(body.getAccountFrom()); // :TODO Check if accounts exists. Also need a function to get account object based on iban
                transaction.setAccountTo(body.getAccountTo());
                transaction.userPerforming(body.getUserPerforming());
                transaction.setAmount(body.getAmount());

                // if amount <= user.daylimit && amount <= user.transactionLimit && amount <= accountFrom.amount
                // if !(acountFrom.amount - amount) <= acountFrom.absoluteLimit
                // accountFrom.setAmount(-amount)
                // else abort
                // accountTo.setAmount(+amount)
                transactionService.storeTransaction(transaction);

                return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
            } catch (IllegalArgumentException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Transaction>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Return type not accepted");
        }
    }

    public ResponseEntity<ArrayOfTransactions> getAllTransactions(@RequestParam Map<String, String> params) {
        String accept = request.getHeader("Accept");
        if (accept != null && (accept.contains("application/json") || accept.contains("*/*"))) {
            ArrayOfTransactions transactions = new ArrayOfTransactions();
            Role role = tokenProvider.getRole(tokenProvider.resolveToken(request));

            if (role == Role.EMPLOYEE) {
                transactions = transactionService.getAllTransactions();
            } else if (role == Role.CUSTOMER) {
                User user = userService.findByToken(tokenProvider.resolveToken(request));

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
            if (params.containsKey("ibanToOrFrom") && role == Role.EMPLOYEE) {
                transactions = transactions.stream().filter(
                        t -> params.get("ibanToOrFrom").equals(t.getAccountFrom())
                                || params.get("ibanToOrFrom").equals(t.getAccountTo())).collect(Collectors.toCollection(ArrayOfTransactions::new));
            }

            /*
             * Filter based on the user performing the transaction
             * Only available if user is an employee
             */
            if (params.containsKey("userPerforming") && role == Role.EMPLOYEE) {
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
