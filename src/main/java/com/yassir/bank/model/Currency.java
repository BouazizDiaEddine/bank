package com.yassir.bank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Currency {
    @Id
    @SequenceGenerator(
            name = "currency_id_sequence",
            sequenceName = "currency_id_sequence",
            initialValue = 1000,
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_id_sequence"
    )
    private Long currencyId;

    @OneToMany(mappedBy = "currency",cascade = CascadeType.REMOVE)
    private Set<Account> account;


    private String value;
    private String name;

    @OneToMany(mappedBy = "fromCurrency",cascade = CascadeType.REMOVE)
    private Set<Exchange> exchangeFrom;

    @OneToMany(mappedBy = "toCurrency",cascade = CascadeType.REMOVE)
    private Set<Exchange> exchangeTo;
}
