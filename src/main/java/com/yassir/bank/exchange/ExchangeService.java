package com.yassir.bank.exchange;

import com.yassir.bank.currency.Currency;
import com.yassir.bank.currency.CurrencyRepository;
import com.yassir.bank.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ExchangeService {

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    //find exchange by currency
    public List<Exchange> findByCurrencyExchanges(Long currencyId) {
        Currency currency = currencyRepository.findById(currencyId).orElseThrow(() -> new ResourceNotFoundException("Currency not found " + currencyId));
        return exchangeRepository.findByFromCurrencyOrToCurrency(currency);
    }

    //creation
    public List<Exchange> createExchange(Exchange exchange){
        Currency currencyFrom = currencyRepository.findById(exchange.getFromCurrency().getCurrencyId()).orElseThrow(() -> new ResourceNotFoundException("Currency from not found " + exchange.getFromCurrency().getCurrencyId()));
        Currency currencyTo = currencyRepository.findById(exchange.getToCurrency().getCurrencyId()).orElseThrow(() -> new ResourceNotFoundException("Currency not found " + exchange.getToCurrency().getCurrencyId()));
        Exchange reverseExchange = new Exchange();

        reverseExchange.setFromCurrency(currencyTo);
        reverseExchange.setToCurrency(currencyFrom);
        reverseExchange.setExchangeRate(new BigDecimal(1).divide(exchange.getExchangeRate()));

        return exchangeRepository.saveAll(List.of(exchange,reverseExchange));
    }

    //update updated by currencyFrom to currencyTo (this updates both)
    public List<Exchange> updateExchange(Exchange updated) {
        Currency currencyFrom = currencyRepository.findById(updated.getFromCurrency().getCurrencyId()).orElseThrow(() -> new ResourceNotFoundException("Currency from not found " + updated.getFromCurrency().getCurrencyId()));
        Currency currencyTo = currencyRepository.findById(updated.getToCurrency().getCurrencyId()).orElseThrow(() -> new ResourceNotFoundException("Currency not found " + updated.getToCurrency().getCurrencyId()));

        Exchange exists = exchangeRepository.findByFromCurrencyAndToCurrency(currencyFrom,currencyTo);
        exists.setExchangeRate(updated.getExchangeRate());

        //update reverse
        Exchange existsReverse = exchangeRepository.findByFromCurrencyAndToCurrency(currencyTo,currencyFrom);
        existsReverse.setExchangeRate(new BigDecimal(1).divide(updated.getExchangeRate()));

        return List.of(exists,existsReverse);
    }


}
