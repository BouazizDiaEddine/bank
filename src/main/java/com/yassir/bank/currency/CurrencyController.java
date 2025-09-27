package com.yassir.bank.currency;

import com.yassir.bank.currency.Currency;
import com.yassir.bank.currency.CurrencyService;
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

    @GetMapping
    public ResponseEntity<List<Currency>> getAllCurrencies() {
        log.info("Returning all currencies");
        return ResponseEntity.ok(currencyService.findAll());
    }

    @GetMapping("/{value}")
    public ResponseEntity<Currency> getCurrencyByValue(@PathVariable String value) {
        log.info("Returning currency : "+value);
        return ResponseEntity.ok(currencyService.findByValue(value));
    }

    @PostMapping
    public ResponseEntity<Currency> createCurrency(@Valid @RequestBody Currency currency) {
        log.info("Creating currency"+currency.toString());
        return ResponseEntity.ok(currencyService.createCurrency(currency));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Currency> updateCurrency(@PathVariable Long id, @Valid @RequestBody Currency currency) {
        log.info("updating currency with "+id+"to "+currency.toString());
        return ResponseEntity.ok(currencyService.updateCurrency(id, currency));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCurrency(@PathVariable Long id) {
        log.info("Deleting Currency with ID "+id);
        currencyService.deleteById(id);
        log.info("the Currency with ID "+id+" was deleted successfully");
        return ResponseEntity.ok("Currency "+ id +" was deleted successfully ");
    }


}
