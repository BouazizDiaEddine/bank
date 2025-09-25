package com.yassir.bank.service;

import com.yassir.bank.exception.DuplicateResourceException;
import com.yassir.bank.exception.InvalidInputException;
import com.yassir.bank.exception.ResourceNotFoundException;
import com.yassir.bank.model.Account;
import com.yassir.bank.user.User;
import com.yassir.bank.repos.AccountRepository;
import com.yassir.bank.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("$bank.init.balance.euro")
    private BigDecimal initEuro;

    public List<Account> findByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        return accountRepository.findAccountsByUser(user);
    }

    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + id));
    }

    @Transactional
    public Account createAccount(Account account ) {
        accountRepository.findAccountsByUserAndCurrency(account.getUser(),account.getCurrency()).ifPresent(u -> {
            throw new DuplicateResourceException("this user already has an account with this currency"+ account.getCurrency().getValue() );
        });

        //TODO change initial currency account currency then check the initial amount of account currency

        if (account.getBalance().compareTo(initEuro)<0)
            throw new InvalidInputException("initial balance in "+account.getCurrency().getValue()+" should be greater than "+account.getBalance());


        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, Account updated) {
        Account exists = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + id));

        if (updated.getBalance().compareTo(exists.getBalance())!=0)
            throw new InvalidInputException("you can not update balance from here");
        //TODO update currency
        return accountRepository.save(exists);
    }

    @Transactional
    public void deleteById(Long id) {
        Account existing = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("account not found with id " + id));
        accountRepository.delete(existing);
    }
}
