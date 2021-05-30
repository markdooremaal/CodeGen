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
import org.springframework.web.server.ResponseStatusException;

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

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Return type not accepted");
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
        if (accept != null && (accept.contains("application/json") || accept.contains("*/*"))) {
            body.setId(id); //Make sure to overwrite the id of the user object to that of the id to update

            if(userService.findById(id) == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user found with this id");

            userService.update(body);
            return new ResponseEntity<User>(body, HttpStatus.OK);
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Return type not accepted");
    }
    //endregion
}