package com.yassir.bank.exchange;


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
@RequestMapping("/api/v1/exchanges")
public class ExchangeController {


    @Autowired
    private ExchangeService exchangeService;

    @Operation(
            summary = "Get exchanges by currency",
            description = "Retrieve all exchange records where the specified currency is either the source or target."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exchanges retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Exchange.class)))),
            @ApiResponse(responseCode = "404", description = "Currency not found",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"Currency not found 99\" }"))),
    })
    @GetMapping("currency/{id}")
    public ResponseEntity<List<Exchange>> findByCurrencyExchanges(Long currencyId){
        log.info("getting exchanges for currency with id"+currencyId);
        return ResponseEntity.ok(exchangeService.findByCurrencyExchanges(currencyId));
    }

    @Operation(
            summary = "Create an exchange",
            description = "Creates a new exchange rate between two currencies. Also creates the reverse exchange automatically with 1/first exchange rate."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exchanges created successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Exchange.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid request body (validation failed)",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"Exchange rate must be greater than 0\" }"))),
            @ApiResponse(responseCode = "404", description = "Currency not found",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"Currency not found 99\" }"))),
    })
    @PostMapping
    public ResponseEntity<List<Exchange>> createExchange(@Valid @RequestBody Exchange exchange) {
        log.info("creating exchanges for currencies "+exchange.getToCurrency().getCurrencyId()+" and "+exchange.getFromCurrency().getCurrencyId());
        return ResponseEntity.ok(exchangeService.createExchange(exchange));
    }


    @Operation(
            summary = "Update an exchange",
            description = "Updates the exchange rate between two currencies and also updates the reverse exchange automatically with the 1/updated first exchange rate."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exchanges updated successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Exchange.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid request body (validation failed)",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"Exchange rate must be greater than 0\" }"))),
            @ApiResponse(responseCode = "404", description = "Currency not found",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"Currency from not found 99\" }"))),
    })
    @PutMapping
    public ResponseEntity<List<Exchange>> updateExchange(@Valid @RequestBody Exchange exchange){
        log.info("updating exchanges for currencies "+exchange.getToCurrency().getCurrencyId()+" and "+exchange.getFromCurrency().getCurrencyId());
        return ResponseEntity.ok(exchangeService.updateExchange(exchange));
    }


}
