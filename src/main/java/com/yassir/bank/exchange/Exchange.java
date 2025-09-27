package com.yassir.bank.exchange;

import com.yassir.bank.currency.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
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

    @DecimalMin(value = "0", inclusive = false, message = "Exchange rate must be greater than 0")
    private BigDecimal exchangeRate;
}

