package io.swagger.api;

import io.swagger.model.ArrayOfBankAccounts;
import io.swagger.model.ArrayOfTransactions;
import io.swagger.model.BankAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.service.BankAccountService;
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
public class BankaccountsApiController implements BankaccountsApi {

    private static final Logger log = LoggerFactory.getLogger(BankaccountsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private BankAccountService bankAccountService;

    @org.springframework.beans.factory.annotation.Autowired
    public BankaccountsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<BankAccount> createAccount(@Parameter(in = ParameterIn.DEFAULT, description = "Transaction object", required=true, schema=@Schema()) @Valid @RequestBody BankAccount body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<BankAccount>(objectMapper.readValue("{\n  \"balance\" : 500.5,\n  \"absoluteLimit\" : -1000,\n  \"iban\" : \"NL01INHO0000000001\",\n  \"type\" : \"regular\",\n  \"userId\" : 1,\n  \"status\" : \"Open\"\n}", BankAccount.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<BankAccount>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<BankAccount>(HttpStatus.NOT_IMPLEMENTED);
    }

    // :TODO same checks based on jwt/employee/customer
    public ResponseEntity<ArrayOfBankAccounts> getAllAccounts(@Parameter(in = ParameterIn.QUERY, description = "Get all the accounts for a specific user" ,schema=@Schema()) @Valid @RequestParam(value = "userId", required = false) Integer userId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            ArrayOfBankAccounts bankAccounts = bankAccountService.getAllBankAccounts();

            if (bankAccounts == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No bankaccounts found");

            return ResponseEntity.status(HttpStatus.OK).body(bankAccounts);
        } else {
            return new ResponseEntity<ArrayOfBankAccounts>(HttpStatus.NOT_IMPLEMENTED);
        }
    }

}
