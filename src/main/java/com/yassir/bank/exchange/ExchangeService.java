package com.yassir.bank.exchange;

import com.yassir.bank.currency.Currency;
import com.yassir.bank.currency.CurrencyRepository;
import com.yassir.bank.exception.InvalidInputException;
import com.yassir.bank.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
public class ExchangeService {

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    //find exchange by currency
    public List<Exchange> findByCurrencyExchanges(Long currencyId) {
        Currency currency = currencyRepository.findById(currencyId).orElseThrow(() -> new ResourceNotFoundException("Currency not found " + currencyId));
        return exchangeRepository.findByFromCurrencyOrToCurrency(currency,currency);
    }

    //creation
    public List<Exchange> createExchange(Exchange exchange){
        log.info("checking the existence if the currencies of "+exchange.toString());
        Currency currencyFrom = currencyRepository.findById(exchange.getFromCurrency().getCurrencyId()).orElseThrow(() -> new ResourceNotFoundException("Currency from not found " + exchange.getFromCurrency().getCurrencyId()));
        Currency currencyTo = currencyRepository.findById(exchange.getToCurrency().getCurrencyId()).orElseThrow(() -> new ResourceNotFoundException("Currency not found " + exchange.getToCurrency().getCurrencyId()));
        Exchange reverseExchange = new Exchange();

        reverseExchange.setFromCurrency(currencyTo);
        reverseExchange.setToCurrency(currencyFrom);
        reverseExchange.setExchangeRate(new BigDecimal(1).divide(exchange.getExchangeRate()));

        exchange.setExchangeId(null);
        reverseExchange.setExchangeId(null);

        return exchangeRepository.saveAll(List.of(exchange,reverseExchange));
    }

    //update updated by currencyFrom to currencyTo (this updates both)
    public List<Exchange> updateExchange(Long id,Exchange updated) {
        log.info("checking the existence if the currencies of and the exchange "+updated.toString());

        Exchange exists = exchangeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("exchange for the given currencies not found" + updated.getFromCurrency().getCurrencyId()+" "+updated.getToCurrency().getCurrencyId())) ;
        Currency currencyFrom = currencyRepository.findById(exists.getFromCurrency().getCurrencyId()).orElseThrow(() -> new ResourceNotFoundException("Currency from not found " + updated.getFromCurrency().getCurrencyId()));
        Currency currencyTo = currencyRepository.findById(exists.getToCurrency().getCurrencyId()).orElseThrow(() -> new ResourceNotFoundException("Currency not found " + updated.getToCurrency().getCurrencyId()));

        if(!exists.getToCurrency().getValue().equals(updated.getToCurrency().getValue()) ||
                !exists.getFromCurrency().getValue().equals(updated.getFromCurrency().getValue()))
            throw new InvalidInputException("Currencies of the body "+updated+"dont match the ones with the id :" +id);

        exists.setExchangeRate(updated.getExchangeRate());

        //update reverse
        Exchange existsReverse = exchangeRepository.findByFromCurrencyAndToCurrency(currencyTo,currencyFrom);

        existsReverse.setExchangeRate(new BigDecimal(1).divide(updated.getExchangeRate(),10, RoundingMode.HALF_UP));

        return exchangeRepository.saveAll(List.of(exists,existsReverse));
    }


}
