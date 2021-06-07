package io.swagger.api;

import io.swagger.model.ArrayOfUsers;
import io.swagger.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.enums.Role;
import io.swagger.model.enums.Status;
import io.swagger.model.searches.UserSearch;
import io.swagger.model.searches.UserSpecification;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

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

    //region createUser
    //Create a user
    public ResponseEntity<User> createUser(@Parameter(in = ParameterIn.DEFAULT, description = "User object", required=true, schema=@Schema()) @Valid @RequestBody User body) {
        //Validate accept header
        String accept = request.getHeader("Accept");
        if (accept != null && (accept.contains("application/json")) || accept.contains("*/*")) {
            //Add and return user
            User addedUser = userService.add(body);
            return new ResponseEntity<User>(addedUser, HttpStatus.CREATED);
        } else
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Accept header invalid");
    }
    //endregion

    //region getAllUsers | Optional filters
    //Get all useres with an optional filter
    public ResponseEntity<ArrayOfUsers> getAllUsers(@Parameter(in = ParameterIn.QUERY, description = "first-/lastname or both" ,schema=@Schema()) @Valid @RequestParam(value = "name", required = false) String name,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "email", required = false) String email,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema(allowableValues={ "Customer", "Employee" }
)) @Valid @RequestParam(value = "role", required = false) String role,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema(allowableValues={ "Active", "Inactive" }
)) @Valid @RequestParam(value = "status", required = false) String status) {
        //Validate accept header
        String accept = request.getHeader("Accept");
        if (accept != null && (accept.contains("application/json") || accept.contains("*/*"))) {
            //Add search criteria based on set parameters
            UserSearch criteria = new UserSearch();
            if(name != null)
                criteria.setFirstName(name);

            if(name != null)
                criteria.setLastName(name);

            if(email != null)
                criteria.setEmail(email);

            if(role != null)
                criteria.setRole(Role.fromValue(role));

            if(status != null)
                criteria.setStatus(Status.fromValue(status));

            //Find users based on specification
            Specification<User> specification = new UserSpecification(criteria);
            ArrayOfUsers users = userService.findAll(specification);

            return new ResponseEntity<ArrayOfUsers>(users, HttpStatus.OK);
        }
        else
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Accept header invalid");
    }
    //endregion
}