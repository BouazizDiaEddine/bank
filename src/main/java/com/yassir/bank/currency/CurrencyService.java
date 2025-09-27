package com.yassir.bank.currency;

import com.yassir.bank.exception.DuplicateResourceException;
import com.yassir.bank.exception.InvalidInputException;
import com.yassir.bank.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepository currencyRepository;

    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }


    public Currency findByValue(String value) {
        return currencyRepository.findByValue(value).orElseThrow(() -> new ResourceNotFoundException("Currency not found with value " + value));
    }

    @Transactional
    public Currency createCurrency(Currency currency) {
        log.info("checking if the currency already exists "+currency.toString());
        currencyRepository.findByValue(currency.getValue()).ifPresent(u -> {
            throw new DuplicateResourceException("currency '" + currency.getValue() + "' already exists");
        });
        return currencyRepository.save(currency);
    }

    @Transactional
    public Currency updateCurrency(Long id, Currency updated) {
        Currency exists = currencyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Currency not found with id " + id));

        //check value
        if(updated.getValue().isEmpty() || updated.getValue() == null)
            throw new InvalidInputException("value is mandatory");

        //check if value already exists
        if (!exists.getValue().equals(updated.getValue())){
            currencyRepository.findByValue(updated.getValue()).ifPresent(u -> {
                throw new DuplicateResourceException("Currency '" + updated.getValue() + "' already exists");
            });
        }

        exists.setValue(updated.getValue());
        exists.setName(updated.getName());
        return currencyRepository.save(exists);
    }

    @Transactional
    public void deleteById(Long id) {
        Currency exists = currencyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Currency not found with id " + id));
        currencyRepository.delete(exists);
    }

}
