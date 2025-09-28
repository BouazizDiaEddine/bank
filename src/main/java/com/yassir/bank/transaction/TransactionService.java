package com.yassir.bank.transaction;

import com.yassir.bank.account.Account;
import com.yassir.bank.account.AccountRepository;
import com.yassir.bank.exception.InvalidInputException;
import com.yassir.bank.exception.ResourceNotFoundException;
import com.yassir.bank.transaction.insertion.InsertTransactionDeposit;
import com.yassir.bank.transaction.insertion.InsertTransactionSend;
import com.yassir.bank.transaction.insertion.InsertTransactionWithdrawal;
import com.yassir.bank.transaction.insertion.TransactionInsertionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<Transaction> accountTransactions(Long id){
        Account account=accountRepository.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("Account Not Found "+ id );
        });
        return transactionRepository.findTransactionByFromAccount(account);
    }

    @Transactional
    public List<Transaction> insertTransactionSend(Transaction transaction){

        accountRepository.findById(transaction.getFromAccount().getAccountId()).orElseThrow(() -> {
            throw new ResourceNotFoundException("account not found "+transaction.getFromAccount().getAccountId());
        });

        accountRepository.findById(transaction.getToAccount().getAccountId()).orElseThrow(() -> {
            throw new ResourceNotFoundException("account not found " + transaction.getToAccount().getAccountId());
        });

        if(!transaction.getTrxType().equals(Status.SEND))
            throw new InvalidInputException("the transaction type should be"+Status.SEND);

        TransactionInsertionService transactionInsertionService = new TransactionInsertionService(new InsertTransactionSend());

        return processTransaction(transactionInsertionService,transaction);
    }


    @Transactional
    public List<Transaction> insertTransactionDeposit(Transaction transaction){

        accountRepository.findById(transaction.getToAccount().getAccountId()).orElseThrow(() -> {
            throw new ResourceNotFoundException("account not found "+transaction.getFromAccount().getAccountId());
        });


        if(!transaction.getTrxType().equals(Status.DEPOSIT)){
            throw new InvalidInputException("the transaction type should be"+Status.DEPOSIT);
        }

        TransactionInsertionService transactionInsertionService = new TransactionInsertionService(new InsertTransactionDeposit());

        return processTransaction(transactionInsertionService,transaction);
    }

    @Transactional
    public List<Transaction> insertTransactionWithdrawal(Transaction transaction){

        accountRepository.findById(transaction.getToAccount().getAccountId()).orElseThrow(() -> {
            throw new ResourceNotFoundException("account not found "+transaction.getFromAccount().getAccountId());
        });

        if(!transaction.getTrxType().equals(Status.WITHDRAWAL)){
            throw new InvalidInputException("the transaction type should be"+Status.WITHDRAWAL);
        }

        TransactionInsertionService transactionInsertionService = new TransactionInsertionService(new InsertTransactionWithdrawal());

        return processTransaction(transactionInsertionService,transaction);
    }


    private List<Transaction> processTransaction(TransactionInsertionService transactionInsertionService,Transaction transaction){
        List<Transaction> results = transactionInsertionService.getInsertTransaction(transaction);

        for (Transaction result : results) {
            accountRepository.save(result.getToAccount());
        }
        return transactionRepository.saveAll(results);
    }

}
