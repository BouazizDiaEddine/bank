package com.yassir.bank.account;

import com.yassir.bank.currency.Currency;
import com.yassir.bank.currency.CurrencyRepository;
import com.yassir.bank.exception.DuplicateResourceException;
import com.yassir.bank.exception.InvalidInputException;
import com.yassir.bank.exception.ResourceNotFoundException;
import com.yassir.bank.account.Account;
import com.yassir.bank.exchange.Exchange;
import com.yassir.bank.exchange.ExchangeRepository;
import com.yassir.bank.user.User;
import com.yassir.bank.account.AccountRepository;
import com.yassir.bank.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrencyRepository currencyRepository;


    @Autowired
    private ExchangeRepository exchangeRepository;
    @Value("${bank.init.balance.euro}")
    private BigDecimal initAmount;

    @Value("${bank.init.balance.Origin}")
    private String  initCurrency;


    public List<Account> findByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        return accountRepository.findAccountsByUser(user);
    }

    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + id));
    }

    @Transactional
    public Account createAccount(Account account ) {
        log.info("checking if account user has an account with this currency : "+account.getCurrency().getValue());
        accountRepository.findAccountsByUserAndCurrency(account.getUser(),account.getCurrency()).ifPresent(u -> {
            throw new DuplicateResourceException("this user already has an account with this currency"+ account.getCurrency().getValue() );
        });

        BigDecimal init;
        if(account.getCurrency().getValue().equals(initCurrency))
            init=initAmount;
        else
            init = calculateInit(account.getCurrency());

        if (account.getBalance().compareTo(init)<0)
            throw new InvalidInputException("initial balance in "+account.getCurrency().getValue()+" should be " +init+ " greater than "+account.getBalance());

        return accountRepository.save(account);
    }

    @Transactional
    public Account updateAccount(Long id, Account updated) {
        Account exists = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + id));

        if(!exists.getCurrency().equals(updated.getCurrency())) {
            log.info("updating account currency from "+exists.getCurrency().getValue()+" to "+updated.getCurrency().getValue());
            Exchange exchange = exchangeRepository.findByFromCurrencyAndToCurrency(exists.getCurrency(),updated.getCurrency());
            exists.setCurrency(updated.getCurrency());
            exists.setBalance(exchange.getExchangeRate().multiply(exists.getBalance()));
        }
        else throw new InvalidInputException("You can only change currency here");

        return accountRepository.save(exists);
    }

    @Transactional
    public void deleteById(Long id) {
        Account existing = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("account not found with id " + id));
        accountRepository.delete(existing);
    }


    private BigDecimal calculateInit(Currency currencyTo) {
        Currency currencyFrom = currencyRepository.findByValue(initCurrency)
                .orElseThrow(() -> new ResourceNotFoundException("original currency not found " + initCurrency));
        Exchange exchange = exchangeRepository.findByFromCurrencyAndToCurrency(currencyFrom,currencyTo);
        return initAmount.multiply(exchange.getExchangeRate());
    }
}
