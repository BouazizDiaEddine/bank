package com.yassir.bank;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.yassir.bank.currency.Currency;
import com.yassir.bank.currency.CurrencyRepository;
import com.yassir.bank.currency.CurrencyService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyService currencyService;

    public CurrencyServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCurrency_successfully() {

        Currency currency = new Currency();
        currency.setValue("USD");

        when(currencyRepository.findByValue("USD")).thenReturn(Optional.empty());
        when(currencyRepository.save(any(Currency.class))).thenAnswer(invocation -> {
            Currency saved = invocation.getArgument(0);
            saved.setCurrencyId(1L); // simulate DB-generated id
            return saved;
        });


        Currency result = currencyService.createCurrency(currency);

        assertThat(result.getCurrencyId()).isEqualTo(1L);
        assertThat(result.getValue()).isEqualTo("USD");

    }
}
