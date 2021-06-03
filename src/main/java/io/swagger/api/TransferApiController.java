package io.swagger.api;

import io.swagger.model.BankAccount;
import io.swagger.model.Transaction;
import io.swagger.model.Transfer;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.User;
import io.swagger.model.enums.Role;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.TransferService;
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
import org.junit.Assert;
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
public class TransferApiController implements TransferApi {

    private static final Logger log = LoggerFactory.getLogger(TransferApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private TransferService transferService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @org.springframework.beans.factory.annotation.Autowired
    public TransferApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    /**
     * Display the specified transfer.
     */
    public ResponseEntity<Transfer> getTransferById(@Parameter(in = ParameterIn.PATH, description = "Numeric ID of the transfer to get", required = true, schema = @Schema()) @PathVariable("id") Integer id) {
        String accept = request.getHeader("Accept");
        if (accept != null && (accept.contains("application/json") || accept.contains("*/*"))) {
            Transfer transfer = transferService.getTransferById(id);

            if (transfer == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transfer found with this id");

            User user = userService.findByToken(tokenProvider.resolveToken(request));

            if (user.getRole() == Role.EMPLOYEE)
                return ResponseEntity.status(HttpStatus.OK).body(transfer);

            if (user.getBankAccounts().stream().anyMatch(bankAccount -> bankAccount.getIban() == transfer.getAccount())) {
                return ResponseEntity.status(HttpStatus.OK).body(transfer);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to view transfer");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Return type not accepted");
        }
    }
}
