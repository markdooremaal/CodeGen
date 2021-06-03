package io.swagger.api;

import io.swagger.model.*;
import io.swagger.model.enums.AccountType;
import io.swagger.model.enums.Role;
import io.swagger.model.enums.Type;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.BankAccountService;
import io.swagger.service.TransferService;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import org.threeten.bp.OffsetDateTime;
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
import java.util.List;
import java.util.Map;
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

    public ResponseEntity<Transfer> createTransfer(@Parameter(in = ParameterIn.DEFAULT, description = "Transfer object", required = true, schema = @Schema()) @Valid @RequestBody Transfer body) {
        String accept = request.getHeader("Accept");
        if (accept != null && (accept.contains("application/json") || accept.contains("*/*"))) {

            BankAccount bankAccount = bankAccountService.getBankAccountByIban(body.getAccount());
            User user = userService.findByToken(tokenProvider.resolveToken(request));

            if (bankAccount.getAccountType() == AccountType.SAVINGS)
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Can't create a transfer for a savings account");

            if (user.getRole() != Role.EMPLOYEE && user.getId() != bankAccount.getUserId())
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Can not create transfer for other account");

            Transfer transfer = new Transfer();
            transfer.setAccount(body.getAccount());
            transfer.setType(body.getType());
            transfer.setAmount(body.getAmount());
            transfer.setUserPerforming(body.getUserPerforming());

            if (transfer.getType() == Type.DEPOSIT) {
                bankAccount.setBalance(bankAccount.getBalance() + transfer.getAmount());
                transferService.storeTransfer(transfer);
                bankAccountService.updateBankAccount(bankAccount);
            } else if (transfer.getType() == Type.WITHDRAWAL) {
                User accountHolder = userService.findById(bankAccount.getUserId());

                if (user.getRole() == Role.CUSTOMER) {
                    if (transfer.getAmount() >= accountHolder.getTransactionLimit())
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Transaction limit would be exceeded");

                    Double newCurrentDayLimit = accountHolder.getCurrentDayLimit() + transfer.getAmount();
                    if (newCurrentDayLimit > accountHolder.getDayLimit())
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Day limit would be exceeded");

                    accountHolder.setCurrentDayLimit(newCurrentDayLimit);
                }

                Double newBalance = bankAccount.getBalance() - transfer.getAmount();
                if (newBalance <= bankAccount.getAbsoluteLimit())
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Accounts absolute limit would be exceeded");

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

    public ResponseEntity<ArrayOfTransfers> getAllTransfers(@Parameter(in = ParameterIn.QUERY, description = "Get all the transfers for a specific user", schema = @Schema()) @Valid @RequestParam(value = "userId", required = false) Integer userId, @Pattern(regexp = "^[a-z]{2}[0-9]{2}[a-z0-9]{4}[0-9]{7}([a-z0-9]?){0,16}$") @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "account", required = false) String account, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema(allowableValues = {"deposit", "withdrawal"}
    )) @Valid @RequestParam(value = "type", required = false) String type, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "userPerforming", required = false) Integer userPerforming, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "timestamp", required = false) OffsetDateTime timestamp) {
        String accept = request.getHeader("Accept");
        if (accept != null && (accept.contains("application/json") || accept.contains("*/*"))) {
            User user = userService.findByToken(tokenProvider.resolveToken(request));
            ArrayOfBankAccounts bankAccounts = new ArrayOfBankAccounts();
            ArrayOfTransfers transfers = transferService.getAllTransfers();

            if (user.getRole() == Role.EMPLOYEE && userId != null) {
                bankAccounts = bankAccountService.getBankAccountByUserId(userId);
                for (BankAccount bankAccount : bankAccounts)
                    transfers.removeIf(transfer -> transfer.getAccount().equals(bankAccount.getIban()));
            } else if (user.getRole() == Role.CUSTOMER) {
                bankAccounts = bankAccountService.getBankAccountByUserId(user.getId());
                for (BankAccount bankAccount : bankAccounts)
                    transfers.removeIf(transfer -> transfer.getAccount().equals(bankAccount.getIban()));
            }

            if (transfers.isEmpty())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transfers found");

            return ResponseEntity.status(HttpStatus.OK).body(transfers);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Return type not accepted");
        }
    }
}
