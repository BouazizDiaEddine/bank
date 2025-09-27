package com.yassir.bank.exchange;


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


    @GetMapping("currency/{id}")
    public ResponseEntity<List<Exchange>> findByCurrencyExchanges(Long currencyId){
        log.info("getting exchanges for currency with id"+currencyId);
        return ResponseEntity.ok(exchangeService.findByCurrencyExchanges(currencyId));
    }

    @PostMapping
    public ResponseEntity<List<Exchange>> createExchange(@Valid @RequestBody Exchange exchange) {
        log.info("creating exchanges for currencies "+exchange.getToCurrency().getCurrencyId()+" and "+exchange.getFromCurrency().getCurrencyId());
        return ResponseEntity.ok(exchangeService.createExchange(exchange));
    }

    @PutMapping
    public ResponseEntity<List<Exchange>> updateExchange(@Valid @RequestBody Exchange exchange){
        log.info("updating exchanges for currencies "+exchange.getToCurrency().getCurrencyId()+" and "+exchange.getFromCurrency().getCurrencyId());
        return ResponseEntity.ok(exchangeService.updateExchange(exchange));
    }


}
