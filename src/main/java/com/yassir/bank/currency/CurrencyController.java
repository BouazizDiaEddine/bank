package com.yassir.bank.currency;

import com.yassir.bank.currency.Currency;
import com.yassir.bank.currency.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/currency")
public class CurrencyController {


    @Autowired
    private CurrencyService currencyService;

    @Operation(
            summary = "Get all currencies",
            description = "Returns the complete list of currencies stored in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of currencies retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Currency.class)))),
    })
    @GetMapping
    public ResponseEntity<List<Currency>> getAllCurrencies() {
        log.info("Returning all currencies");
        return ResponseEntity.ok(currencyService.findAll());
    }

    @Operation(
            summary = "Get a currency by value",
            description = "Fetches a specific currency using its value (e.g., USD, EUR)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currency retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Currency.class))),
            @ApiResponse(responseCode = "404", description = "Currency not found",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"Currency not found with value ABC\" }"))),
    })
    @GetMapping("/{value}")
    public ResponseEntity<Currency> getCurrencyByValue(@PathVariable String value) {
        log.info("Returning currency : "+value);
        return ResponseEntity.ok(currencyService.findByValue(value));
    }

    @Operation(
            summary = "Create a new currency",
            description = "Creates a new currency in the system. The currency value must be unique."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currency created successfully",
                    content = @Content(schema = @Schema(implementation = Currency.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body (validation failed)",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"Currency value must not be blank\" }"))),
            @ApiResponse(responseCode = "409", description = "Currency already exists",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"currency 'USD' already exists\" }"))),
    })
    @PostMapping
    public ResponseEntity<Currency> createCurrency(@Valid @RequestBody Currency currency) {
        log.info("Creating currency"+currency.toString());
        return ResponseEntity.ok(currencyService.createCurrency(currency));
    }

    @Operation(
            summary = "Update a currency",
            description = "Updates an existing currency's value and name. Ensures that the new value is not blank and not already used by another currency."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currency updated successfully",
                    content = @Content(schema = @Schema(implementation = Currency.class))),
            @ApiResponse(responseCode = "404", description = "Currency not found",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"Currency not found with id 99\" }"))),
            @ApiResponse(responseCode = "409", description = "Duplicate currency value",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"Currency 'EUR' already exists\" }"))),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Currency> updateCurrency(@PathVariable Long id, @Valid @RequestBody Currency currency) {
        log.info("updating currency with "+id+"to "+currency.toString());
        return ResponseEntity.ok(currencyService.updateCurrency(id, currency));
    }

    @Operation(
            summary = "Delete a currency",
            description = "Deletes a currency by its ID. Throws an error if the currency does not exist."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currency deleted successfully",
                    content = @Content(schema = @Schema(example = "\"Currency 1 was deleted successfully\""))),
            @ApiResponse(responseCode = "404", description = "Currency not found",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"Currency not found with id 99\" }"))),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCurrency(@PathVariable Long id) {
        log.info("Deleting user with ID "+id);
        currencyService.deleteById(id);
        log.info("the user with ID "+id+" was deleted successfully");
        return ResponseEntity.ok("Currency "+ id +" was deleted successfully ");
    }


}
