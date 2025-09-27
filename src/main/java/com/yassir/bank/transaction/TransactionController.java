package com.yassir.bank.transaction;

import com.yassir.bank.exchange.Exchange;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping(("/account/{id}"))
    public ResponseEntity<List<Transaction>> TransactionHistory(@PathVariable Long id) {
        log.info("getting transactions for account :"+id);
        return ResponseEntity.ok(transactionService.accountTransactions(id));
    }

    @PostMapping(("/send"))
    public ResponseEntity<List<Transaction>> createTransactionSend(@Valid @RequestBody Transaction transaction) {
        log.info("sending from account :"+transaction.getFromAccount().getAccountId()+"to account"+transaction.getFromAccount().getAccountId());
        return ResponseEntity.ok(transactionService.insertTransactionSend(transaction));
    }

    @PostMapping(("/deposit"))
    public ResponseEntity<List<Transaction>> createTransactionDeposit(@Valid @RequestBody Transaction transaction) {
        log.info("Deposit to account :"+transaction.getToAccount().getAccountId());
        return ResponseEntity.ok(transactionService.insertTransactionDeposit(transaction));
    }

    @PostMapping(("/withdrawal"))
    public ResponseEntity<List<Transaction>> createTransactionWithdrawal(@Valid @RequestBody Transaction transaction) {
        log.info("withdrawal from account :"+transaction.getToAccount().getAccountId());
        return ResponseEntity.ok(transactionService.insertTransactionWithdrawal(transaction));
    }
}
