package com.yassir.bank.transaction;

import com.yassir.bank.account.Account;
import com.yassir.bank.account.AccountRepository;
import com.yassir.bank.exception.InvalidInputException;
import com.yassir.bank.exception.ResourceNotFoundException;
import com.yassir.bank.exchange.Exchange;
import com.yassir.bank.exchange.ExchangeRepository;
import com.yassir.bank.transaction.insertion.*;
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

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Transactional
    public List<Transaction> accountTransactions(Long id){
        Account account=accountRepository.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("Account Not Found "+ id );
        });
        return transactionRepository.findTransactionByFromAccount(account);
    }

    @Transactional
    public List<Transaction> insertTransactionSend(Transaction transaction){

        Account fromAccount =accountRepository.findById(transaction.getFromAccount().getAccountId()).orElseThrow(() -> {
            throw new ResourceNotFoundException("account not found "+transaction.getFromAccount().getAccountId());
        });

        Account toAccount =accountRepository.findById(transaction.getToAccount().getAccountId()).orElseThrow(() -> {
            throw new ResourceNotFoundException("account not found " + transaction.getToAccount().getAccountId());
        });

        if(!transaction.getTrxType().equals(Status.SEND))
            throw new InvalidInputException("the transaction type should be"+Status.SEND);

        transaction.setToAccount(toAccount);
        transaction.setFromAccount(fromAccount);

        TransactionInsertionService transactionInsertionService = new TransactionInsertionService(new InsertTransactionSend());

        return processTransaction(transactionInsertionService,transaction);
    }

    @Transactional
    public List<Transaction> insertTransactionExchange(Transaction transaction){

        Account fromAccount =accountRepository.findById(transaction.getFromAccount().getAccountId()).orElseThrow(() -> {
            throw new ResourceNotFoundException("account not found "+transaction.getFromAccount().getAccountId());
        });

        Account toAccount =accountRepository.findById(transaction.getToAccount().getAccountId()).orElseThrow(() -> {
            throw new ResourceNotFoundException("account not found " + transaction.getToAccount().getAccountId());
        });

        if(!fromAccount.getUser().getUserId().equals(toAccount.getUser().getUserId()))
            throw new InvalidInputException("the accounts must be of the same user "+fromAccount.getUser().getUserId()+" "+toAccount.getUser().getUserId());

        if(!transaction.getTrxType().equals(Status.EXCHANGE))
            throw new InvalidInputException("the transaction type should be"+Status.EXCHANGE);

        transaction.setToAccount(toAccount);
        transaction.setFromAccount(fromAccount);

        Exchange exchange = exchangeRepository.findByFromCurrencyAndToCurrency(fromAccount.getCurrency(),toAccount.getCurrency())
                .orElseThrow(() -> new ResourceNotFoundException("currencies not found"+fromAccount.getCurrency().getValue()+" and "+toAccount.getCurrency().getValue()));;

        TransactionInsertionService transactionInsertionService = new TransactionInsertionService(new InsertTransactionExchange());

        return processTransaction(transactionInsertionService,transaction,exchange);
    }




    @Transactional
    public List<Transaction> insertTransactionDeposit(Transaction transaction){

        Account toAccount=accountRepository.findById(transaction.getToAccount().getAccountId()).orElseThrow(() -> {
            throw new ResourceNotFoundException("account not found "+transaction.getFromAccount().getAccountId());
        });


        if(!transaction.getTrxType().equals(Status.DEPOSIT)){
            throw new InvalidInputException("the transaction type should be"+Status.DEPOSIT);
        }

        transaction.setToAccount(toAccount);

        TransactionInsertionService transactionInsertionService = new TransactionInsertionService(new InsertTransactionDeposit());

        return processTransaction(transactionInsertionService,transaction);
    }

    @Transactional
    public List<Transaction> insertTransactionWithdrawal(Transaction transaction){

        Account toAccount=accountRepository.findById(transaction.getToAccount().getAccountId()).orElseThrow(() -> {
            throw new ResourceNotFoundException("account not found "+transaction.getFromAccount().getAccountId());
        });

        if(!transaction.getTrxType().equals(Status.WITHDRAWAL)){
            throw new InvalidInputException("the transaction type should be"+Status.WITHDRAWAL);
        }

        transaction.setToAccount(toAccount);

        TransactionInsertionService transactionInsertionService = new TransactionInsertionService(new InsertTransactionWithdrawal());

        return processTransaction(transactionInsertionService,transaction);
    }


    private List<Transaction> processTransaction(TransactionInsertionService transactionInsertionService,Transaction transaction){
        List<Transaction> results = transactionInsertionService.getInsertTransaction(transaction);

        for (Transaction result : results)
            accountRepository.save(result.getToAccount());

        return transactionRepository.saveAll(results);
    }

    private List<Transaction> processTransaction(TransactionInsertionService transactionInsertionService, Transaction transaction, Exchange exchange) {
        List<Transaction> results = transactionInsertionService.getInsertTransaction(transaction,exchange);

        for (Transaction result : results)
            accountRepository.save(result.getToAccount());

        return transactionRepository.saveAll(results);
    }
}
