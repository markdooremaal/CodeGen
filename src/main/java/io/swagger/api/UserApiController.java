package io.swagger.api;

import io.swagger.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-26T11:40:47.282Z[GMT]")
@RestController
public class UserApiController implements UserApi {

    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public UserApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    //region getUserById(int id)
    public ResponseEntity<User> getUserById(@Parameter(in = ParameterIn.PATH, description = "Numeric ID of the user to get", required=true, schema=@Schema()) @PathVariable("id") Integer id) {
        String accept = request.getHeader("Accept");
        if (accept != null && (accept.contains("application/json") || accept.contains("*/*"))) {
            User user = userService.findById(id);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }

        return new ResponseEntity<User>(HttpStatus.NOT_IMPLEMENTED);
    }
    //endregion

    //region makeUserInactive(int id)
    public ResponseEntity<Void> makeUserInactive(@Parameter(in = ParameterIn.PATH, description = "Numeric ID of the user to make inactive", required=true, schema=@Schema()) @PathVariable("id") Integer id) {
        String accept = request.getHeader("Accept");
        userService.makeInactive(id); //@TODO: correct response
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    //endregion

    //region updateUser(int id, User user)
    public ResponseEntity<User> updateUser(@Parameter(in = ParameterIn.PATH, description = "Numeric ID of the user to update", required=true, schema=@Schema()) @PathVariable("id") Integer id,@Parameter(in = ParameterIn.DEFAULT, description = "User object", required=true, schema=@Schema()) @Valid @RequestBody User body) {
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
    //endregion
}