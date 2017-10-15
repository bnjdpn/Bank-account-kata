package com.bank.account.util;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.Currency;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Data;

public class CurrencyUtil {

    // TODO : Get rates from a currency data API like XE...
    private static final Map<RatesKey, Double>
        RATES =
        Collections.unmodifiableMap(Stream
            .of(new SimpleEntry<>(new RatesKey("EUR", "USD"), 1.18214),
                new SimpleEntry<>(new RatesKey("USD", "EUR"), 0.845945))
            .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)));

    private CurrencyUtil() {
        // Util
    }

    public static double convertAmount(double amount, Currency from, Currency to) {

        if (from.equals(to)) {
            return amount;
        } else {
            return amount * RATES.getOrDefault(new RatesKey(from, to), 1D);
        }
    }

    @Data
    private static class RatesKey {
        private Currency from;
        private Currency to;

        RatesKey(String from, String to) {
            this.from = Currency.getInstance(from);
            this.to = Currency.getInstance(to);
        }

        RatesKey(Currency from, Currency to) {
            this.from = from;
            this.to = to;
        }
    }
}
