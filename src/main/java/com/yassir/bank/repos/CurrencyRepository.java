package com.yassir.bank.repos;


import com.yassir.bank.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency,Long> {
    Optional<Currency> findByValue(String value);
}
