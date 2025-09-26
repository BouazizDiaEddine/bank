package com.yassir.bank.transaction.transaction.insertion;

import com.yassir.bank.account.Account;
import com.yassir.bank.account.AccountRepository;
import com.yassir.bank.transaction.Status;
import com.yassir.bank.transaction.Transaction;
import com.yassir.bank.transaction.TransactionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;


public class InsertTransactionSend implements InsertTransaction {

    @Override
    public List<Transaction> insertTrx(Transaction transaction) {

        Account accountFrom = transaction.getFromAccount();
        Account accountTo = transaction.getToAccount();

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
