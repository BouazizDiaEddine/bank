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
import java.math.RoundingMode;
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


    @Test
    void updateExchange_shouldUpdateReverseRate() {
        // given
        Currency usd = new Currency();
        usd.setCurrencyId(1L);
        usd.setValue("USD");

        Currency eur = new Currency();
        eur.setCurrencyId(2L);
        eur.setValue("EUR");

        // existing exchange: 1 USD = 2 EUR
        Exchange exists = new Exchange();
        exists.setExchangeId(10L);
        exists.setFromCurrency(usd);
        exists.setToCurrency(eur);
        exists.setExchangeRate(new BigDecimal("2.0"));

        // reverse exchange: 1 EUR = 0.5 USD
        Exchange existsReverse = new Exchange();
        existsReverse.setExchangeId(11L);
        existsReverse.setFromCurrency(eur);
        existsReverse.setToCurrency(usd);
        existsReverse.setExchangeRate(new BigDecimal("0.5"));

        // new updated rate: 1 USD = 3 EUR
        Exchange updated = new Exchange();
        updated.setFromCurrency(usd);
        updated.setToCurrency(eur);
        updated.setExchangeRate(new BigDecimal("3.0"));

        when(exchangeRepository.findById(10L)).thenReturn(Optional.of(exists));
        when(currencyRepository.findById(1L)).thenReturn(Optional.of(usd));
        when(currencyRepository.findById(2L)).thenReturn(Optional.of(eur));
        when(exchangeRepository.findByFromCurrencyAndToCurrency(eur, usd))
                .thenReturn(Optional.of(existsReverse));
        when(exchangeRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        List<Exchange> result = exchangeService.updateExchange(10L, updated);

        // then
        assertThat(result).hasSize(2);

        Exchange savedNormal = result.get(0);
        Exchange savedReverse = result.get(1);

        // check updated exchange
        assertThat(savedNormal.getExchangeRate()).isEqualByComparingTo("3.0");

        // check reverse got updated too
        BigDecimal expectedReverse = BigDecimal.ONE.divide(new BigDecimal("3.0"), 10, RoundingMode.HALF_UP);
        assertThat(savedReverse.getExchangeRate()).isEqualByComparingTo(expectedReverse);

        verify(exchangeRepository).findById(10L);
        verify(exchangeRepository).findByFromCurrencyAndToCurrency(eur, usd);
        verify(exchangeRepository).saveAll(anyList());
    }
}
