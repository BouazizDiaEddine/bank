package com.yassir.bank.currency;

import com.yassir.bank.account.Account;
import com.yassir.bank.model.Exchange;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @Column(unique = true, nullable = false)
    @NotBlank
    private String value;
    private String name;

    @OneToMany(mappedBy = "fromCurrency",cascade = CascadeType.REMOVE)
    private Set<Exchange> exchangeFrom;

    @OneToMany(mappedBy = "toCurrency",cascade = CascadeType.REMOVE)
    private Set<Exchange> exchangeTo;
}
