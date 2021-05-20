package io.swagger.api;

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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-10T12:34:22.652Z[GMT]")
@RestController
public class TransfersApiController implements TransfersApi {

    private static final Logger log = LoggerFactory.getLogger(TransfersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public TransfersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Transfer> createTransfer(@Parameter(in = ParameterIn.DEFAULT, description = "Transfer object", required=true, schema=@Schema()) @Valid @RequestBody Transfer body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Transfer>(objectMapper.readValue("{\n  \"amount\" : 10.42,\n  \"userPerforming\" : 1,\n  \"id\" : 1,\n  \"type\" : \"deposit\",\n  \"account\" : \"NL01INHO0000000001\",\n  \"timestamp\" : \"2000-01-23T04:56:07.000+00:00\"\n}", Transfer.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Transfer>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Transfer>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Transfer> getAllTransfers(@Pattern(regexp="/^[a-zA-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[0-9]{7}([a-zA-Z0-9]?){0,16}$/") @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "account", required = false) String account,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema(allowableValues={ "deposit", "withdrawal" }
)) @Valid @RequestParam(value = "type", required = false) String type,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "userPerforming", required = false) String userPerforming,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "timestamp", required = false) OffsetDateTime timestamp) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Transfer>(objectMapper.readValue("{\n  \"amount\" : 10.42,\n  \"userPerforming\" : 1,\n  \"id\" : 1,\n  \"type\" : \"deposit\",\n  \"account\" : \"NL01INHO0000000001\",\n  \"timestamp\" : \"2000-01-23T04:56:07.000+00:00\"\n}", Transfer.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Transfer>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Transfer>(HttpStatus.NOT_IMPLEMENTED);
    }

}
