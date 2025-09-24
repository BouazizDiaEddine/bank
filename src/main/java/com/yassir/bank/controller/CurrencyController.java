package com.yassir.bank.controller;

import com.yassir.bank.model.Currency;
import com.yassir.bank.service.CurrencyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/currency")
public class CurrencyController {


    @Autowired
    private CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<Currency>> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.findAll());
    }

    @GetMapping("/{value}")
    public ResponseEntity<Currency> getCurrencyByValue(@PathVariable String value) {
        Currency currency = currencyService.findByValue(value);
        return ResponseEntity.ok(currency);
    }

    @PostMapping
    public ResponseEntity<Currency> createCurrency(@Valid @RequestBody Currency currency) {

        currencyService.createCurrency(currency);

        return ResponseEntity.ok(currency);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Currency> updateCurrency(@PathVariable Long id, @Valid @RequestBody Currency currency) {
        return ResponseEntity.ok(currencyService.updateCurrency(id, currency));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCurrency(@PathVariable Long id) {
        currencyService.deleteById(id);
        return ResponseEntity.ok("Currency "+ id +" was deleted successfully ");
    }


}
