package com.yassir.bank.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yassir.bank.currency.Currency;
import com.yassir.bank.model.Transaction;
import com.yassir.bank.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "bank_acc")
public class Account {

    @Id
    @SequenceGenerator(
            name = "account_id_sequence",
            sequenceName = "account_id_sequence",
            initialValue = 1000,
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_id_sequence"
    )
    private long accountId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @DecimalMin(value = "0", inclusive = false, message = "balance must be greater than 0")
    private BigDecimal balance;
    //private Instant lastUpdate; probably not gonna user

    @OneToMany(mappedBy = "fromAccount",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Transaction> transactionsFrom;

    @OneToMany(mappedBy = "toAccount",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Transaction> transactionsTo;

}
