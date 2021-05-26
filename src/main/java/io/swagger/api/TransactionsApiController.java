package io.swagger.api;

import io.swagger.model.ArrayOfTransactions;
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
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-26T11:40:47.282Z[GMT]")
@RestController
public class TransactionsApiController implements TransactionsApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Transaction> createTransaction(@Parameter(in = ParameterIn.DEFAULT, description = "Transaction object", required=true, schema=@Schema()) @Valid @RequestBody Transaction body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Transaction>(objectMapper.readValue("{\n  \"accountTo\" : \"NL01INHO0000000002\",\n  \"amount\" : 13.37,\n  \"userPerforming\" : 1,\n  \"id\" : 1,\n  \"accountFrom\" : \"NL01INHO0000000001\",\n  \"timestamp\" : \"2000-01-23T04:56:07.000+00:00\"\n}", Transaction.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Transaction>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Transaction>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<ArrayOfTransactions> getAllTransactions(@Parameter(in = ParameterIn.QUERY, description = "Get all the transactions for a specific user" ,schema=@Schema()) @Valid @RequestParam(value = "userId", required = false) Integer userId,@Pattern(regexp="^[a-z]{2}[0-9]{2}[a-z0-9]{4}[0-9]{7}([a-z0-9]?){0,16}$") @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "ibanFrom", required = false) String ibanFrom,@Pattern(regexp="^[a-z]{2}[0-9]{2}[a-z0-9]{4}[0-9]{7}([a-z0-9]?){0,16}$") @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "ibanTo", required = false) String ibanTo,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "userPerforming", required = false) Integer userPerforming,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "timestamp", required = false) OffsetDateTime timestamp,@Pattern(regexp="^[a-z]{2}[0-9]{2}[a-z0-9]{4}[0-9]{7}([a-z0-9]?){0,16}$") @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "ibanToOrFrom", required = false) String ibanToOrFrom) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<ArrayOfTransactions>(objectMapper.readValue("[ {\n  \"accountTo\" : \"NL01INHO0000000002\",\n  \"amount\" : 13.37,\n  \"userPerforming\" : 1,\n  \"id\" : 1,\n  \"accountFrom\" : \"NL01INHO0000000001\",\n  \"timestamp\" : \"2000-01-23T04:56:07.000+00:00\"\n}, {\n  \"accountTo\" : \"NL01INHO0000000002\",\n  \"amount\" : 13.37,\n  \"userPerforming\" : 1,\n  \"id\" : 1,\n  \"accountFrom\" : \"NL01INHO0000000001\",\n  \"timestamp\" : \"2000-01-23T04:56:07.000+00:00\"\n} ]", ArrayOfTransactions.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<ArrayOfTransactions>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<ArrayOfTransactions>(HttpStatus.NOT_IMPLEMENTED);
    }

}
