package com.yassir.bank;

import com.yassir.bank.account.Account;
import com.yassir.bank.currency.Currency;
import com.yassir.bank.exception.InvalidInputException;
import com.yassir.bank.transaction.Status;
import com.yassir.bank.transaction.Transaction;
import com.yassir.bank.transaction.insertion.InsertTransactionSend;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class InsertTransactionSendTest {

    private InsertTransactionSend insertTransactionSend;

    private Account accountFrom;
    private Account accountTo;
    private Currency usd;

    @BeforeEach
    void setUp() {
        insertTransactionSend = new InsertTransactionSend();

        usd = new Currency();
        usd.setCurrencyId(1L);
        usd.setValue("USD");

        accountFrom = new Account();
        accountFrom.setAccountId(1L);
        accountFrom.setCurrency(usd);
        accountFrom.setBalance(new BigDecimal("500"));

        accountTo = new Account();
        accountTo.setAccountId(2L);
        accountTo.setCurrency(usd);
        accountTo.setBalance(new BigDecimal("200"));
    }

    @Test
    void insertTrx_shouldTransferAmountAndCreateReverseTransaction() {
        // given
        Transaction trx = new Transaction();
        trx.setFromAccount(accountFrom);
        trx.setToAccount(accountTo);
        trx.setAmount(new BigDecimal("100"));
        trx.setTrxType(Status.SEND);

        // when
        List<Transaction> result = insertTransactionSend.insertTrx(trx);

        // then
        assertThat(result).hasSize(2);

        Transaction reverse = result.get(0);
        Transaction original = result.get(1);

        // balances updated
        assertThat(accountFrom.getBalance()).isEqualByComparingTo("400"); // 500 - 100
        assertThat(accountTo.getBalance()).isEqualByComparingTo("300"); // 200 + 100

        // original transaction
        assertThat(original.getAmount()).isEqualByComparingTo("100");
        assertThat(original.getFromAccount()).isEqualTo(accountFrom);
        assertThat(original.getToAccount()).isEqualTo(accountTo);

        // reverse transaction
        assertThat(reverse.getAmount()).isEqualByComparingTo("-100");
        assertThat(reverse.getTrxType()).isEqualTo(Status.RECEIVED);
        assertThat(reverse.getFromAccount()).isEqualTo(accountTo);
        assertThat(reverse.getToAccount()).isEqualTo(accountFrom);
    }

    @Test
    void insertTrx_shouldThrowIfDifferentCurrencies() {
        // given
        Currency eur = new Currency();
        eur.setCurrencyId(2L);
        eur.setValue("EUR");

        accountTo.setCurrency(eur);

        Transaction trx = new Transaction();
        trx.setFromAccount(accountFrom);
        trx.setToAccount(accountTo);
        trx.setAmount(new BigDecimal("50"));
        trx.setTrxType(Status.SEND);

        // expect
        assertThatThrownBy(() -> insertTransactionSend.insertTrx(trx))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining("you can't send from a currency to another");
    }
}
