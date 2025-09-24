package com.yassir.bank.service;

import com.yassir.bank.exception.ResourceNotFoundException;
import com.yassir.bank.model.Account;
import com.yassir.bank.user.User;
import com.yassir.bank.repos.AccountRepository;
import com.yassir.bank.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    public List<Account> findByUser(Long userId) {
        User user = userService.findById(userId);
        return accountRepository.findAccountsByUser(user);
    }

    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + id));
    }

    public Account createAccount(Account account) {
        //TODO check account
        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, Account updated) {
        Account existing = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + id));

        /*if (updated.getEmail() == null || updated.getName() == null) {
            throw new IllegalArgumentException("Name and email must not be null");
        }*/

        //TODO if email changed, ensure no other user has it

        //set updates

        return accountRepository.save(existing);
    }

    @Transactional
    public void deleteById(Long id) {
        Account existing = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        accountRepository.delete(existing);
    }
}
