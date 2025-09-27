package com.yassir.bank.transaction.insertion;

import com.yassir.bank.transaction.Transaction;

import java.util.List;

public class TransactionInsertionService {

    private final InsertTransaction insertTransaction;

    public TransactionInsertionService(InsertTransaction insertTransaction) {
        this.insertTransaction=insertTransaction;
    }


    public List<Transaction> getInsertTransaction(Transaction transaction) {
        return insertTransaction.insertTrx(transaction);
    }
}
