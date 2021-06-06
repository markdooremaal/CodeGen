package io.swagger.api;

import io.swagger.model.*;
import io.swagger.model.enums.AccountType;
import io.swagger.model.enums.Role;
import io.swagger.model.enums.Status;
import io.swagger.model.enums.Type;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.BankAccountService;
import io.swagger.service.TransferService;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import org.threeten.bp.OffsetDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-26T11:40:47.282Z[GMT]")
@RestController
public class TransfersApiController implements TransfersApi {

    private static final Logger log = LoggerFactory.getLogger(TransfersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private TransferService transferService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private BankAccountService bankAccountService;

    @org.springframework.beans.factory.annotation.Autowired
    public TransfersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    /**
     * Create a new transfer
     * @param body
     * @return Transfer
     */
    public ResponseEntity<Transfer> createTransfer(@Parameter(in = ParameterIn.DEFAULT, description = "Transfer object", required = true, schema = @Schema()) @Valid @RequestBody Transfer body) {
        String accept = request.getHeader("Accept");
        if (accept != null && (accept.contains("application/json") || accept.contains("*/*"))) {

            BankAccount bankAccount = bankAccountService.getBankAccountByIban(body.getAccount());
            User user = userService.findByToken(tokenProvider.resolveToken(request));

            // Perform security checks on account status, account type and account ownership
            if (bankAccount.getStatus() == Status.INACTIVE)
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account inactive");

            if (bankAccount.getAccountType() == AccountType.SAVINGS)
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Can't create a transfer for a savings account");

            if (user.getRole() != Role.EMPLOYEE && user.getId() != bankAccount.getUserId())
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Can not create transfer for other account");

            Transfer transfer = new Transfer();
            transfer.setAccount(body.getAccount());
            transfer.setType(body.getType());
            transfer.setAmount(body.getAmount());
            transfer.setUserPerforming(body.getUserPerforming());

            // If the transfer is of a Deposit type, create the transer. Else perform checks.
            if (transfer.getType() == Type.DEPOSIT) {
                bankAccount.setBalance(bankAccount.getBalance() + transfer.getAmount());
                transferService.storeTransfer(transfer);
                bankAccountService.updateBankAccount(bankAccount);
            } else if (transfer.getType() == Type.WITHDRAWAL) {
                User accountHolder = userService.findById(bankAccount.getUserId());

                // Only perform these checks if logged-in user is a customer.
                if (user.getRole() == Role.CUSTOMER) {
                    // Check if transaction limit will be exceeded.
                    if (transfer.getAmount() >= accountHolder.getTransactionLimit())
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Transaction limit would be exceeded");

                    // Check if the daylimit will be exceeded.
                    Double newCurrentDayLimit = accountHolder.getCurrentDayLimit() + transfer.getAmount();
                    if (newCurrentDayLimit > accountHolder.getDayLimit())
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Day limit would be exceeded");

                    accountHolder.setCurrentDayLimit(newCurrentDayLimit);
                }
                // Check if the Absolute limit will be exceeded.
                Double newBalance = bankAccount.getBalance() - transfer.getAmount();
                if (newBalance <= bankAccount.getAbsoluteLimit())
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Accounts absolute limit would be exceeded");

                // Store new transfer and update balance.
                bankAccount.setBalance(newBalance);
                bankAccountService.updateBankAccount(bankAccount);
                transferService.storeTransfer(transfer);
                userService.update(accountHolder);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(transfer);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Return type not accepted");
        }
    }

    /**
     * Show a listing of transfers
     *
     * @param userId
     * @param account
     * @param type
     * @param userPerforming
     * @param timestamp
     * @return ArrayOfTransfers
     */
    public ResponseEntity<ArrayOfTransfers> getAllTransfers(@Parameter(in = ParameterIn.QUERY, description = "Get all the transfers for a specific user", schema = @Schema()) @Valid @RequestParam(value = "userId", required = false) Integer userId, @Pattern(regexp = "^[a-z]{2}[0-9]{2}[a-z0-9]{4}[0-9]{7}([a-z0-9]?){0,16}$") @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "account", required = false) String account, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema(allowableValues = {"deposit", "withdrawal"}
    )) @Valid @RequestParam(value = "type", required = false) String type, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "userPerforming", required = false) Integer userPerforming, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "timestamp", required = false) String timestamp) {
        String accept = request.getHeader("Accept");
        if (accept != null && (accept.contains("application/json") || accept.contains("*/*"))) {
            User user = userService.findByToken(tokenProvider.resolveToken(request));
            ArrayOfBankAccounts bankAccounts = new ArrayOfBankAccounts();
            ArrayOfTransfers transfers = transferService.getAllTransfers();

            /* Get transfers for a specific user if the logged-in user is an employee and userIs is set.
             * Else only get transfers for the logged in customer.
             */
            if (user.getRole() == Role.EMPLOYEE && userId != null) {
                bankAccounts = bankAccountService.getBankAccountByUserId(userId);
                for (BankAccount bankAccount : bankAccounts)
                    transfers.removeIf(transfer -> transfer.getAccount().equals(bankAccount.getIban()));
            } else if (user.getRole() == Role.CUSTOMER) {
                bankAccounts = bankAccountService.getBankAccountByUserId(user.getId());
                for (BankAccount bankAccount : bankAccounts)
                    transfers.removeIf(transfer -> transfer.getAccount().equals(bankAccount.getIban()));
            }

            // Filter on accounts
            if (account != null)
                transfers = transfers.stream().filter(transfer -> account.equals(transfer.getAccount())).collect(Collectors.toCollection(ArrayOfTransfers::new));

            // Filter on transfer type
            if (type != null)
                transfers = transfers.stream().filter(transfer -> Type.valueOf(type.toUpperCase()).equals(transfer.getType())).collect(Collectors.toCollection(ArrayOfTransfers::new));

            // Filter on user performing
            if (userPerforming != null)
                transfers = transfers.stream().filter(transfer -> userPerforming.equals(transfer.getUserPerforming())).collect(Collectors.toCollection(ArrayOfTransfers::new));

            // Filter on timestamp
            if (timestamp != null)
                transfers = transfers.stream().filter(transfer -> OffsetDateTime.parse(timestamp).equals(transfer.getTimestamp())).collect(Collectors.toCollection(ArrayOfTransfers::new));

            if (transfers.isEmpty())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transfers found");

            return ResponseEntity.status(HttpStatus.OK).body(transfers);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Return type not accepted");
        }
    }
}
