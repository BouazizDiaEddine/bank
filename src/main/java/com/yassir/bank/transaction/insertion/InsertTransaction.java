package com.yassir.bank.transaction.insertion;

import com.yassir.bank.exchange.Exchange;
import com.yassir.bank.transaction.Transaction;

import java.util.List;

public interface InsertTransaction {

    List<Transaction> insertTrx(Transaction transaction);
    default List<Transaction> insertTrx(Transaction transaction, Exchange exchange) {

        return insertTrx(transaction);
    }

}
