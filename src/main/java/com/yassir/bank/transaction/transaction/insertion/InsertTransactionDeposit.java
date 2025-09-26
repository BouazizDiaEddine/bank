package com.yassir.bank.transaction.transaction.insertion;

import com.yassir.bank.account.Account;
import com.yassir.bank.transaction.Transaction;

import java.util.List;

public class InsertTransactionDeposit implements InsertTransaction {

    @Override
    public List<Transaction> insertTrx(Transaction transaction) {


        Account accountTo = transaction.getToAccount();

        accountTo.setBalance(accountTo.getBalance().add(transaction.getAmount()));

        transaction.setToAccount(accountTo);

        return List.of(transaction);
    }


}
