package com.bank.account.model;

import java.time.Instant;
import java.util.Currency;
import java.util.Locale;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @NotEmpty
    private String accountId;

    @NotNull
    @Builder.Default
    private Instant date = Instant.now();

    @NotEmpty
    private String label;

    @NotNull
    private double amount;

    @NotNull
    @Builder.Default
    private Currency currency = Currency.getInstance(Locale.getDefault());

}
