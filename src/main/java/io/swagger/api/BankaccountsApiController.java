package io.swagger.api;

import io.swagger.model.BankAccount;
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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-25T09:30:53.687Z[GMT]")
@RestController
public class BankaccountsApiController implements BankaccountsApi {

    private static final Logger log = LoggerFactory.getLogger(BankaccountsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public BankaccountsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> closeAccountById(@Parameter(in = ParameterIn.PATH, description = "IBAN of the Bank Account to close", required=true, schema=@Schema()) @PathVariable("id") String id) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
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

    public ResponseEntity<BankAccount> getAccountById(@Parameter(in = ParameterIn.PATH, description = "IBAN of the Bank Account to get", required=true, schema=@Schema()) @PathVariable("id") String id) {
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

    public ResponseEntity<BankAccount> getAllAccounts(@Parameter(in = ParameterIn.QUERY, description = "Get all the accounts for a specific user" ,schema=@Schema()) @Valid @RequestParam(value = "userId", required = false) Integer userId) {
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

    public ResponseEntity<BankAccount> updateAccountById(@Parameter(in = ParameterIn.PATH, description = "IBAN of the Bank Account to update", required=true, schema=@Schema()) @PathVariable("id") String id,@Parameter(in = ParameterIn.DEFAULT, description = "BankAccount object", required=true, schema=@Schema()) @Valid @RequestBody BankAccount body) {
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

}
