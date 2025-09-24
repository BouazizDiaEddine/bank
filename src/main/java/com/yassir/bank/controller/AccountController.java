package com.yassir.bank.controller;

import com.yassir.bank.model.Account;
import com.yassir.bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("user/{id}")
    public List<Account> getAllUsers(@PathVariable Long id) {
        return accountService.findByUser(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        Account account = accountService.findById(id);
        return ResponseEntity.ok(account);
    }

    @PostMapping
    public ResponseEntity<Account> createUser(/*TODO VALIDATE USER*/ @RequestBody Account account) {

        accountService.createAccount(account);

        return ResponseEntity.ok(account);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateUser(@PathVariable Long id, /*TODO VALIDATE USER*/ @RequestBody Account account) {
        accountService.updateAccount(id, account);
        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        accountService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
