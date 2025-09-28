package com.yassir.bank;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.yassir.bank.account.Account;
import com.yassir.bank.account.AccountRepository;
import com.yassir.bank.account.AccountService;
import com.yassir.bank.currency.Currency;
import com.yassir.bank.currency.CurrencyRepository;
import com.yassir.bank.exchange.Exchange;
import com.yassir.bank.exchange.ExchangeRepository;
import com.yassir.bank.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private ExchangeRepository exchangeRepository;

    @InjectMocks
    private AccountService accountService;

    private Currency usd;
    private Currency eur;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // pretend config values in service
        ReflectionTestUtils.setField(accountService, "initCurrency", "USD");
        ReflectionTestUtils.setField(accountService, "initAmount", new BigDecimal("100.00"));


        usd = new Currency();
        usd.setCurrencyId(1L);
        usd.setValue("USD");

        eur = new Currency();
        eur.setCurrencyId(2L);
        eur.setValue("EUR");

        user = new User();
        user.setUserId(1L);
        user.setName("John Doe");
    }

    @Test
    void createAccount_shouldUseExchangeRateWhenCurrencyIsNotInitCurrency() {

        Account account = new Account();
        account.setUser(user);
        account.setCurrency(eur);
        account.setBalance(new BigDecimal("300.00")); // enough balance in EUR

        Exchange usdToEur = new Exchange();
        usdToEur.setFromCurrency(usd);
        usdToEur.setToCurrency(eur);
        usdToEur.setExchangeRate(new BigDecimal("2.5")); // 1 USD = 2.5 EUR

        when(accountRepository.findAccountsByUserAndCurrency(user, eur))
                .thenReturn(Optional.empty());
        when(currencyRepository.findByValue("USD"))
                .thenReturn(Optional.of(usd));
        when(exchangeRepository.findByFromCurrencyAndToCurrency(usd, eur))
                .thenReturn(Optional.of(usdToEur));
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> {
                    Account saved = invocation.getArgument(0);
                    saved.setAccountId(10L);
                    return saved;
                });

        // when
        Account savedAccount = accountService.createAccount(account);

        // then
        assertThat(savedAccount.getAccountId()).isEqualTo(10L);
        assertThat(savedAccount.getCurrency()).isEqualTo(eur);
        assertThat(savedAccount.getBalance()).isEqualByComparingTo("300.00");

        // verify init requirement was computed correctly
        BigDecimal expectedInit = new BigDecimal("100.00").multiply(new BigDecimal("2.5")); // = 250 EUR
        assertThat(account.getBalance()).isGreaterThanOrEqualTo(expectedInit);

        verify(currencyRepository).findByValue("USD");
        verify(exchangeRepository).findByFromCurrencyAndToCurrency(usd, eur);
        verify(accountRepository).save(account);
    }
}
