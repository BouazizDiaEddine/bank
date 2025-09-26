package com.yassir.bank.transaction.transaction.insertion;

import com.yassir.bank.transaction.Transaction;

import java.util.List;

public interface InsertTransaction {

    List<Transaction> insertTrx(Transaction transaction);

}
