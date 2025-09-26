package com.yassir.bank.transaction;

import com.yassir.bank.exchange.Exchange;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping
    public ResponseEntity<List<Transaction>> createTransaction(@Valid @RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.insertTransaction(transaction));
    }
}
