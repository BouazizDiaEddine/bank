package com.yassir.bank.transaction;

import com.yassir.bank.account.Account;
import com.yassir.bank.transaction.Transaction;
import com.yassir.bank.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findTransactionByFromAccount(Account account);
}
