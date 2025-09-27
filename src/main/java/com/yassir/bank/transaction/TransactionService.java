package com.yassir.bank.transaction;

import com.yassir.bank.account.Account;
import com.yassir.bank.account.AccountRepository;
import com.yassir.bank.exception.ResourceNotFoundException;
import com.yassir.bank.transaction.insertion.InsertTransactionDeposit;
import com.yassir.bank.transaction.insertion.InsertTransactionSend;
import com.yassir.bank.transaction.insertion.InsertTransactionWithdrawal;
import com.yassir.bank.transaction.insertion.TransactionInsertionService;
import com.yassir.bank.transaction.transaction.insertion.*;
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

    public List<Transaction> accountTransactions(Account account){
        accountRepository.findById(account.getAccountId()).orElseThrow(() -> {
            throw new ResourceNotFoundException("Account Not Found "+ account.getAccountId() );
        });
        return transactionRepository.findTransactionByFromAccount(account);
    }

    @Transactional
    public List<Transaction> insertTransaction(Transaction transaction){

        accountRepository.findById(transaction.getFromAccount().getAccountId()).orElseThrow(() -> {
            throw new ResourceNotFoundException("account not found "+transaction.getFromAccount().getAccountId());
        });

        if(transaction.getTrxType().equals(Status.SEND)) {
            accountRepository.findById(transaction.getToAccount().getAccountId()).orElseThrow(() -> {
                throw new ResourceNotFoundException("account not found " + transaction.getToAccount().getAccountId());
            });

            TransactionInsertionService transactionInsertionService = new TransactionInsertionService(new InsertTransactionSend());

            List<Transaction> results = transactionInsertionService.getInsertTransaction(transaction);

            for (Transaction result : results) {
                accountRepository.save(result.getToAccount());
            }
            return transactionRepository.saveAll(results);

        } else if (transaction.getTrxType().equals(Status.DEPOSIT)) {
            TransactionInsertionService transactionInsertionService = new TransactionInsertionService(new InsertTransactionDeposit());

            List<Transaction> results = transactionInsertionService.getInsertTransaction(transaction);

            for (Transaction result : results) {
                accountRepository.save(result.getToAccount());
            }
            return transactionRepository.saveAll(results);

        } else if (transaction.getTrxType().equals(Status.WITHDRAWAL)) {
            TransactionInsertionService transactionInsertionService = new TransactionInsertionService(new InsertTransactionWithdrawal());

            List<Transaction> results = transactionInsertionService.getInsertTransaction(transaction);

            for (Transaction result : results) {
                accountRepository.save(result.getToAccount());
            }
            return transactionRepository.saveAll(results);

        }else return null;
    }

}
