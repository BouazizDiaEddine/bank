package com.yassir.bank.exchange;

import com.yassir.bank.currency.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange,Long> {

    List<Exchange> findByFromCurrencyOrToCurrency(Currency currencyFrom,Currency currencyTo);
    Exchange findByFromCurrencyAndToCurrency (Currency currencyFrom,Currency currencyTo);

}
