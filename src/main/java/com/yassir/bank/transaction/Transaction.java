package com.yassir.bank.transaction;


import com.yassir.bank.account.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Transaction {
    @Id
    @SequenceGenerator(
            name = "transaction_id_sequence",
            sequenceName = "transaction_id_sequence",
            initialValue = 1000,
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "transaction_id_sequence"
    )
    private long transaction_id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "to_account_id")
    private Account toAccount;

    @Enumerated(EnumType.STRING)
    private Status trxType;

    private BigDecimal amount;

}
