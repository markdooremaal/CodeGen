package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import io.swagger.model.enums.Role;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-26T11:40:47.282Z[GMT]")
@RestController
public class TransactionApiController implements TransactionApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    /**
     * Display the specified transaction.
     */
    public ResponseEntity<Transaction> getTransactionById(@Parameter(in = ParameterIn.PATH, description = "Numeric ID of the transaction to get", required = true, schema = @Schema()) @PathVariable("id") Integer id) {
        String accept = request.getHeader("Accept");
        if (accept != null && (accept.contains("application/json") || accept.contains("*/*"))) {
            Transaction transaction = transactionService.getTransactionById(id);

            if (transaction == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transaction found with this id");

            User user = userService.findByToken(tokenProvider.resolveToken(request));

            if (user.getRole() == Role.EMPLOYEE)
                return ResponseEntity.status(HttpStatus.OK).body(transaction);

            // Check if user owns transaction.
            if (user.getBankAccounts().stream().anyMatch(bankAccount -> bankAccount.getIban() == transaction.getAccountFrom())
                    || user.getBankAccounts().stream().anyMatch(bankAccount -> bankAccount.getIban() == transaction.getAccountTo())) {
                return ResponseEntity.status(HttpStatus.OK).body(transaction);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to view transaction");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Return type not accepted");
        }
    }

}
