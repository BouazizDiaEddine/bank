package com.yassir.bank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.Instant;
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

    private BigDecimal balance;
    private Instant lastUpdate;

    @OneToMany(mappedBy = "fromAccount",cascade = CascadeType.REMOVE)
    private Set<Transaction> transactionsFrom;

    @OneToMany(mappedBy = "toAccount",cascade = CascadeType.REMOVE)
    private Set<Transaction> transactionsTo;

}
