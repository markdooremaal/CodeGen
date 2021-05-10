/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.25).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import org.threeten.bp.OffsetDateTime;
import io.swagger.model.Transaction;
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
public interface TransactionsApi {

    @Operation(summary = "Create a new transaction", description = "Calling this allows you to create a transaction", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Transactions and Transfers" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "The transaction is made", content = @Content(schema = @Schema(implementation = Transaction.class))),
        
        @ApiResponse(responseCode = "400", description = "bad input parameter") })
    @RequestMapping(value = "/Transactions",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<Transaction> createTransaction(@Parameter(in = ParameterIn.DEFAULT, description = "Transaction object", required=true, schema=@Schema()) @Valid @RequestBody Transaction body);


    @Operation(summary = "Get all Transactions, with the option to filter.", description = "Calling this allows you to fetch all transactions. Apply query's to filter results.", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Transactions and Transfers" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Array of relevant transactions", content = @Content(schema = @Schema(implementation = Transaction.class))),
        
        @ApiResponse(responseCode = "400", description = "bad input parameter") })
    @RequestMapping(value = "/Transactions",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Transaction> getAllTransactions(@Pattern(regexp="/^[a-zA-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[0-9]{7}([a-zA-Z0-9]?){0,16}$/") @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "ibanFrom", required = false) String ibanFrom, @Pattern(regexp="/^[a-zA-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[0-9]{7}([a-zA-Z0-9]?){0,16}$/") @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "ibanTo", required = false) String ibanTo, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "userPerforming", required = false) String userPerforming, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "timestamp", required = false) OffsetDateTime timestamp, @Pattern(regexp="/^[a-zA-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[0-9]{7}([a-zA-Z0-9]?){0,16}$/") @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "ibanToOrFrom", required = false) String ibanToOrFrom);


    @Operation(summary = "Get a transaction by id", description = "Calling this allows you to fetch a specific transaction by id", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Transactions and Transfers" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "the transaction", content = @Content(schema = @Schema(implementation = Transaction.class))),
        
        @ApiResponse(responseCode = "400", description = "bad input parameter") })
    @RequestMapping(value = "/Transactions/{id}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Transaction> getTransactionById(@Parameter(in = ParameterIn.PATH, description = "Numeric ID of the transaction to get", required=true, schema=@Schema()) @PathVariable("id") Integer id);

}

