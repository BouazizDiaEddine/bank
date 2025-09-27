package com.yassir.bank.account;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("user/{id}")
    public ResponseEntity<List<Account>> getAllAccounts(@PathVariable Long id) {
        log.info("Returning Accounts for user with ID : "+id);
        return ResponseEntity.ok(accountService.findByUser(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        log.info("Returning Account with ID : "+id);
        return ResponseEntity.ok(accountService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account) {
        log.info("creating new account  : "+account.toString());
        return ResponseEntity.ok(accountService.createAccount(account));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @Valid @RequestBody Account account) {
        log.info("updating account with ID "+id+" to "+account.toString());
        return ResponseEntity.ok(accountService.updateAccount(id, account));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        log.info("Deleting account with ID "+id);
        accountService.deleteById(id);
        log.info("the account with ID "+id+" was deleted successfully");
        return ResponseEntity.noContent().build();
    }

}
