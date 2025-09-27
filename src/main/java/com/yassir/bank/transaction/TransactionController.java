package com.yassir.bank.transaction;

import com.yassir.bank.exchange.Exchange;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping(("/currency/{id}"))
    public ResponseEntity<List<Transaction>> TransactionHistory(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.accountTransactions(id));
    }

    @PostMapping(("/send"))
    public ResponseEntity<List<Transaction>> createTransactionSend(@Valid @RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.insertTransactionSend(transaction));
    }

    @PostMapping(("/deposit"))
    public ResponseEntity<List<Transaction>> createTransactionDeposit(@Valid @RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.insertTransactionDeposit(transaction));
    }

    @PostMapping(("/withdrawal"))
    public ResponseEntity<List<Transaction>> createTransactionWithdrawal(@Valid @RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.insertTransactionWithdrawal(transaction));
    }
}
