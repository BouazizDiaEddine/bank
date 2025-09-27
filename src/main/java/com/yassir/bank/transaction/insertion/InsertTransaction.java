package com.yassir.bank.transaction.insertion;

import com.yassir.bank.transaction.Transaction;

import java.util.List;

public interface InsertTransaction {

    List<Transaction> insertTrx(Transaction transaction);

}
