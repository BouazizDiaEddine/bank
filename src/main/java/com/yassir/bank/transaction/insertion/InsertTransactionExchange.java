package com.yassir.bank.transaction.insertion;

import com.yassir.bank.account.Account;
import com.yassir.bank.exchange.Exchange;
import com.yassir.bank.transaction.Status;
import com.yassir.bank.transaction.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class InsertTransactionExchange implements InsertTransaction{
    @Override
    public List<Transaction> insertTrx(Transaction transaction) {
        throw new UnsupportedOperationException("insertTrx(Transaction transaction, Exchange exchange) should be used here");
    }

    @Override
    public List<Transaction> insertTrx(Transaction transaction, Exchange exchange) {


        Account accountFrom = transaction.getFromAccount();
        Account accountTo = transaction.getToAccount();

        BigDecimal trxToSend = transaction.getAmount().multiply(exchange.getExchangeRate());

        accountFrom.setBalance(accountFrom.getBalance().subtract(transaction.getAmount()));
        accountTo.setBalance(accountTo.getBalance().add(trxToSend));

        Transaction reverseTransaction = reverseTransaction(transaction);

        transaction.setAmount(trxToSend);
        transaction.setTransaction_id(null);
        reverseTransaction.setTransaction_id(null);

        return List.of(transaction,reverseTransaction);
    }

    private Transaction reverseTransaction(Transaction transaction){
        Transaction reverseTransaction = new Transaction();
        reverseTransaction.setAmount(transaction.getAmount().multiply(new BigDecimal("-1")));
        reverseTransaction.setTrxType(Status.RECEIVED);
        reverseTransaction.setFromAccount(transaction.getToAccount());
        reverseTransaction.setToAccount(transaction.getFromAccount());
        return reverseTransaction;
    }
}
