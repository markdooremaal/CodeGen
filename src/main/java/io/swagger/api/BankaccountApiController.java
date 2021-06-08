package io.swagger.api;

import io.swagger.model.BankAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.User;
import io.swagger.model.enums.Role;
import io.swagger.model.enums.Status;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.BankAccountService;
import io.swagger.service.UserService;
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
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-26T11:40:47.282Z[GMT]")
@RestController
public class BankaccountApiController implements BankaccountApi {

    private static final Logger log = LoggerFactory.getLogger(BankaccountApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @org.springframework.beans.factory.annotation.Autowired
    public BankaccountApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    /**
     * Close an account
     * Can only be done by an employee
     * @param id
     * @return BankAccount
     */
    public ResponseEntity<Void> closeAccountById(@Parameter(in = ParameterIn.PATH, description = "IBAN of the Bank Account to close", required = true, schema = @Schema()) @PathVariable("id") String id) {
        User user = userService.findByToken(tokenProvider.resolveToken(request));
        if (user.getRole() == Role.CUSTOMER)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Customers are not allowed to close accounts");

        if (id.equals("nl01inho0000000001"))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to view account");

        BankAccount bankAccount = bankAccountService.getBankAccountByIban(id);

        if (bankAccount == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No bankaccount found with this id");

        bankAccount.setStatus(Status.INACTIVE);
        bankAccountService.updateBankAccount(bankAccount);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * Get an account by id = iban
     * @param id
     * @return BankAccount
     */
    public ResponseEntity<BankAccount> getAccountById(@Parameter(in = ParameterIn.PATH, description = "IBAN of the Bank Account to get", required = true, schema = @Schema()) @PathVariable("id") String id) {
        String accept = request.getHeader("Accept");
        if (accept != null && (accept.contains("application/json") || accept.contains("*/*"))) {
            if (id.equals("nl01inho0000000001"))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to view account");

            User user = userService.findByToken(tokenProvider.resolveToken(request));
            BankAccount bankAccount = bankAccountService.getBankAccountByIban(id);

            if (bankAccount == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No bankaccount found with this id");

            // Check if logged-in customer owns specified account
            if (user.getRole() == Role.CUSTOMER) {
                if (user.getBankAccounts().contains(bankAccount)) {
                    return ResponseEntity.status(HttpStatus.OK).body(bankAccount);
                } else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to view this bankaccount");
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(bankAccount);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Return type not accepted");
        }
    }

    /**
     * Update BankAccount by id = iban
     * Can only be executed by employee
     * @param id
     * @param body
     * @return BankAccount
     */
    public ResponseEntity<BankAccount> updateAccountById(@Parameter(in = ParameterIn.PATH, description = "IBAN of the Bank Account to update", required = true, schema = @Schema()) @PathVariable("id") String id, @Parameter(in = ParameterIn.DEFAULT, description = "BankAccount object", required = true, schema = @Schema()) @Valid @RequestBody BankAccount body) {
        String accept = request.getHeader("Accept");
        if (accept != null && (accept.contains("application/json") || accept.contains("*/*"))) {
            User user = userService.findByToken(tokenProvider.resolveToken(request));

            if (user.getRole() == Role.CUSTOMER)
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Customers are not allowed to update accounts");

            if (id.equals("nl01inho0000000001"))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to view account");

            BankAccount bankAccount = bankAccountService.getBankAccountByIban(id);

            if (bankAccount == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No bankaccount found with this id");

            bankAccount.setUserId(body.getUserId());
            bankAccount.setStatus(body.getStatus());
            bankAccount.setAccountType(body.getAccountType());
            bankAccount.setBalance(body.getBalance());
            bankAccount.setAbsoluteLimit(body.getAbsoluteLimit());
            bankAccountService.updateBankAccount(bankAccount);

            return ResponseEntity.status(HttpStatus.OK).body(bankAccount);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Return type not accepted");
        }
    }

}
