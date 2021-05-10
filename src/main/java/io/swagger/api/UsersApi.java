/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.25).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-10T12:34:22.652Z[GMT]")
@Validated
public interface UsersApi {

    @Operation(summary = "Create new user", description = "Calling this allows you to create a new user", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Users" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "User created", content = @Content(schema = @Schema(implementation = User.class))),
        
        @ApiResponse(responseCode = "400", description = "bad input parameter") })
    @RequestMapping(value = "/Users",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<User> createUser(@Parameter(in = ParameterIn.DEFAULT, description = "User object", required=true, schema=@Schema()) @Valid @RequestBody User body);


    @Operation(summary = "Get all Users, with the option to filter.", description = "Calling this allows you to fetch all users. Apply query's to filter results.", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Users" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "The users", content = @Content(schema = @Schema(implementation = User.class))),
        
        @ApiResponse(responseCode = "400", description = "bad input parameter") })
    @RequestMapping(value = "/Users",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<User> getAllUsers(@Parameter(in = ParameterIn.QUERY, description = "first-/lastname or both" ,schema=@Schema()) @Valid @RequestParam(value = "name", required = false) String name, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "email", required = false) String email, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema(allowableValues={ "Customer", "Employee" }
)) @Valid @RequestParam(value = "role", required = false) String role, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema(allowableValues={ "Active", "Inactive" }
)) @Valid @RequestParam(value = "status", required = false) String status);


    @Operation(summary = "Get a user by id", description = "Calling this allows you to fetch a specific user by id", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Users" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "the user", content = @Content(schema = @Schema(implementation = User.class))),
        
        @ApiResponse(responseCode = "400", description = "bad input parameter") })
    @RequestMapping(value = "/Users/{id}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<User> getUserById(@Parameter(in = ParameterIn.PATH, description = "Numeric ID of the user to get", required=true, schema=@Schema()) @PathVariable("id") Integer id);


    @Operation(summary = "Makes a user inactive", description = "Calling this allows you to make a user inactive", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Users" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "User is now inactive"),
        
        @ApiResponse(responseCode = "400", description = "bad input parameter") })
    @RequestMapping(value = "/Users/{id}",
        method = RequestMethod.DELETE)
    ResponseEntity<Void> makeUserInactive(@Parameter(in = ParameterIn.PATH, description = "Numeric ID of the user to make inactive", required=true, schema=@Schema()) @PathVariable("id") Integer id);


    @Operation(summary = "Update a user", description = "Calling this allows you to update a user", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Users" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "User updated", content = @Content(schema = @Schema(implementation = User.class))),
        
        @ApiResponse(responseCode = "400", description = "bad input parameter") })
    @RequestMapping(value = "/Users/{id}",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<User> updateUser(@Parameter(in = ParameterIn.PATH, description = "Numeric ID of the user to update", required=true, schema=@Schema()) @PathVariable("id") Integer id, @Parameter(in = ParameterIn.DEFAULT, description = "User object", required=true, schema=@Schema()) @Valid @RequestBody User body);

}

