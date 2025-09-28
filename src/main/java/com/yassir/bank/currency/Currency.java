package com.yassir.bank.currency;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yassir.bank.account.Account;
import com.yassir.bank.exchange.Exchange;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@ToString
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
            generator = "currency_id_sequence"
    )
    private Long currencyId;

    @OneToMany(mappedBy = "currency",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Account> account;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String value;
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "fromCurrency",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Exchange> exchangeFrom;

    @OneToMany(mappedBy = "toCurrency",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Exchange> exchangeTo;
}
