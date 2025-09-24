package com.yassir.bank.service;

import com.yassir.bank.exception.ResourceNotFoundException;
import com.yassir.bank.model.Currency;
import com.yassir.bank.repos.CurrencyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepository CurrencyRepository;

    public List<Currency> findAll() {
        return CurrencyRepository.findAll();
    }

    public Currency findById(Long id) {
        return CurrencyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Currency not found with id " + id));
    }

    public Currency createCurrency(Currency currency) {
        //TODO check Currency exists
        return CurrencyRepository.save(currency);
    }

    public Currency updateCurrency(Long id, Currency updated) {
        Currency existing = CurrencyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Currency not found with id " + id));

        return CurrencyRepository.save(existing);
    }

    @Transactional
    public void deleteById(Long id) {
        Currency existing = CurrencyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Currency not found with id " + id));
        CurrencyRepository.delete(existing);
    }

}
