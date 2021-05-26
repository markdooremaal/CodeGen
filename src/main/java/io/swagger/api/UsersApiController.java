package io.swagger.api;

import io.swagger.model.ArrayOfUsers;
import io.swagger.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-26T11:40:47.282Z[GMT]")
@RestController
public class UsersApiController implements UsersApi {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<User> createUser(@Parameter(in = ParameterIn.DEFAULT, description = "User object", required=true, schema=@Schema()) @Valid @RequestBody User body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<User>(objectMapper.readValue("{\n  \"firstName\" : \"John\",\n  \"lastName\" : \"Doe\",\n  \"password\" : \"goedWachtwoord94!\",\n  \"role\" : \"customer\",\n  \"dayLimit\" : 499.9,\n  \"bankAccounts\" : [ {\n    \"balance\" : 500.5,\n    \"absoluteLimit\" : -1000,\n    \"iban\" : \"NL01INHO0000000001\",\n    \"type\" : \"regular\",\n    \"userId\" : 1,\n    \"status\" : \"Open\"\n  }, {\n    \"balance\" : 500.5,\n    \"absoluteLimit\" : -1000,\n    \"iban\" : \"NL01INHO0000000001\",\n    \"type\" : \"regular\",\n    \"userId\" : 1,\n    \"status\" : \"Open\"\n  } ],\n  \"id\" : 1,\n  \"transactionLimit\" : 499.9,\n  \"email\" : \"johndoe@example.dev\",\n  \"status\" : \"active\"\n}", User.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<User>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<ArrayOfUsers> getAllUsers(@Parameter(in = ParameterIn.QUERY, description = "first-/lastname or both" ,schema=@Schema()) @Valid @RequestParam(value = "name", required = false) String name,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "email", required = false) String email,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema(allowableValues={ "Customer", "Employee" }
)) @Valid @RequestParam(value = "role", required = false) String role,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema(allowableValues={ "Active", "Inactive" }
)) @Valid @RequestParam(value = "status", required = false) String status) {
        String accept = request.getHeader("Accept");
        if (accept != null && (accept.contains("application/json") || accept.contains("*/*"))) {
            ArrayOfUsers users = userService.findAll();

            return new ResponseEntity<ArrayOfUsers>(users, HttpStatus.OK);
        }

        return new ResponseEntity<ArrayOfUsers>(HttpStatus.NOT_IMPLEMENTED);
    }

}