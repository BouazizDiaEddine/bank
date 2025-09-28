package com.yassir.bank.account;

import com.yassir.bank.currency.Currency;

import com.yassir.bank.user.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    List<Account> findAccountsByUser(User user);
    Optional<Account> findAccountsByUserAndCurrency(User user, Currency currency);

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Account> findById(Long id);
}
