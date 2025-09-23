package com.yassir.bank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Exchange {
    @Id
    @SequenceGenerator(
            name = "exchange_id_sequence",
            sequenceName = "exchange_id_sequence",
            initialValue = 1000,
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exchange_id_sequence"
    )
    private Long exchangeId;

    @ManyToOne
    @JoinColumn(name = "from_currency_id")
    private Currency fromCurrency;

    @ManyToOne
    @JoinColumn(name = "to_currency_id")
    private Currency toCurrency;

    private BigDecimal exchangeRate;
}

