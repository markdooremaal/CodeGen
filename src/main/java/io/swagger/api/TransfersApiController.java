package io.swagger.api;

import io.swagger.model.ArrayOfTransfers;
import io.swagger.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import org.threeten.bp.OffsetDateTime;
import io.swagger.model.Transfer;
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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-26T11:40:47.282Z[GMT]")
@RestController
public class TransfersApiController implements TransfersApi {

    private static final Logger log = LoggerFactory.getLogger(TransfersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    TransferService transferService;

    @org.springframework.beans.factory.annotation.Autowired
    public TransfersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Transfer> createTransfer(@Parameter(in = ParameterIn.DEFAULT, description = "Transfer object", required = true, schema = @Schema()) @Valid @RequestBody Transfer body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                Transfer transfer = new Transfer();
                transfer.setAccount(body.getAccount());
                transfer.setType(body.getType());
                transfer.setAmount(body.getAmount());
                transfer.setUserPerforming(body.getUserPerforming());


                //:TODO Needs same checks as in transactions
                transferService.storeTransfer(transfer);

                return ResponseEntity.status(HttpStatus.OK).body(transfer);
            } catch (IllegalArgumentException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Transfer>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Transfer>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<ArrayOfTransfers> getAllTransfers(@Parameter(in = ParameterIn.QUERY, description = "Get all the transfers for a specific user", schema = @Schema()) @Valid @RequestParam(value = "userId", required = false) Integer userId, @Pattern(regexp = "^[a-z]{2}[0-9]{2}[a-z0-9]{4}[0-9]{7}([a-z0-9]?){0,16}$") @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "account", required = false) String account, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema(allowableValues = {"deposit", "withdrawal"}
    )) @Valid @RequestParam(value = "type", required = false) String type, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "userPerforming", required = false) Integer userPerforming, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "timestamp", required = false) OffsetDateTime timestamp) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {

                ArrayOfTransfers transfers = new ArrayOfTransfers();
                transfers = transferService.getAllTransfers();


                return ResponseEntity.status(200).body(transfers);
            } catch (IllegalArgumentException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<ArrayOfTransfers>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<ArrayOfTransfers>(HttpStatus.NOT_IMPLEMENTED);
    }

}
