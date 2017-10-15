package com.bank.account.util;

import java.util.Currency;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CurrencyUtilTest {

    @Test
    public void testConvertEURtoEUR() {
        // Given
        double amount = 1234567890;
        Currency currency = Currency.getInstance("EUR");

        // When
        double converted = CurrencyUtil.convertAmount(amount, currency, currency);

        // Then
        assertThat(converted).isEqualTo(amount);
    }

    @Test
    public void testConvertEURtoUSD() {
        // Given
        double amount = 1234567890;
        Currency eur = Currency.getInstance("EUR");
        Currency usd = Currency.getInstance("USD");

        // When
        double converted = CurrencyUtil.convertAmount(amount, eur, usd);

        // Then
        assertThat(converted).isGreaterThan(amount);
    }

    @Test
    public void testConvertGBPtoINR() {
        // Given
        double amount = 1234567890;
        Currency gbp = Currency.getInstance("GBP");
        Currency inr = Currency.getInstance("INR");

        // When
        double converted = CurrencyUtil.convertAmount(amount, gbp, inr);

        // Then
        assertThat(converted).isEqualTo(amount);
    }

}
