package com.yassir.bank;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.yassir.bank.currency.Currency;
import com.yassir.bank.currency.CurrencyRepository;
import com.yassir.bank.exchange.Exchange;
import com.yassir.bank.exchange.ExchangeRepository;
import com.yassir.bank.exchange.ExchangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

class ExchangeServiceTest {

    @Mock
    private ExchangeRepository exchangeRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private ExchangeService exchangeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createExchange_shouldCreateReverseExchange() {
        // given
        Currency usd = new Currency();
        usd.setCurrencyId(1L);
        usd.setValue("USD");

        Currency eur = new Currency();
        eur.setCurrencyId(2L);
        eur.setValue("EUR");

        Exchange exchange = new Exchange();
        exchange.setFromCurrency(usd);
        exchange.setToCurrency(eur);
        exchange.setExchangeRate(new BigDecimal("2.0")); // 1 USD = 2 EUR

        when(currencyRepository.findById(1L)).thenReturn(Optional.of(usd));
        when(currencyRepository.findById(2L)).thenReturn(Optional.of(eur));

        when(exchangeRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        List<Exchange> result = exchangeService.createExchange(exchange);

        // then
        assertThat(result).hasSize(2);

        Exchange normal = result.get(0);
        Exchange reverse = result.get(1);

        assertThat(normal.getFromCurrency()).isEqualTo(usd);
        assertThat(normal.getToCurrency()).isEqualTo(eur);
        assertThat(normal.getExchangeRate()).isEqualByComparingTo("2.0");

        assertThat(reverse.getFromCurrency()).isEqualTo(eur);
        assertThat(reverse.getToCurrency()).isEqualTo(usd);
        assertThat(reverse.getExchangeRate()).isEqualByComparingTo("0.5"); // 1 / 2.0

        verify(currencyRepository).findById(1L);
        verify(currencyRepository).findById(2L);
        verify(exchangeRepository).saveAll(anyList());
    }
}
