package com.yassir.bank.transaction.insertion;

import com.yassir.bank.account.Account;
import com.yassir.bank.exception.InvalidInputException;
import com.yassir.bank.transaction.Status;
import com.yassir.bank.transaction.Transaction;

import java.math.BigDecimal;
import java.util.List;


public class InsertTransactionSend implements InsertTransaction {

    @Override
    public List<Transaction> insertTrx(Transaction transaction) {

        Account accountFrom = transaction.getFromAccount();
        Account accountTo = transaction.getToAccount();

        if (!accountFrom.getCurrency().equals(accountTo.getCurrency()))
            throw new InvalidInputException("you can't send from a currency to another");
        accountFrom.setBalance(accountFrom.getBalance().subtract(transaction.getAmount()));
        accountTo.setBalance(accountTo.getBalance().add(transaction.getAmount()));

        transaction.setFromAccount(accountFrom);
        transaction.setToAccount(accountTo);


        Transaction reverseTransaction = reverseTransaction(transaction);
        return List.of(reverseTransaction,transaction);
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
