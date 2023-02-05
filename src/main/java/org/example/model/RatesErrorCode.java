package org.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum RatesErrorCode {

    HTTPS_ACCESS_RESTRICTED("https_access_restricted"),
    MISSING_ACCESS_KEY("missing_access_key"),
    INVALID_ACCESS_KEY("invalid_access_key"),
    INVALID_BASE_CURRENCY("invalid_base_currency"),
    INVALID_CURRENCY_CODES("invalid_currency_codes"),
    BASE_CURRENCY_ACCESS_RESTRICTED("base_currency_access_restricted"),
    UNKNOWN;

    @JsonCreator
    public static RatesErrorCode json(String string) {
        if (string == null || string.isEmpty()) return null;
        return Arrays.stream(values())
                .filter(errorCode -> errorCode.name.equals(string))
                .findFirst()
                .orElse(UNKNOWN);
    }

    private final String name;

    RatesErrorCode() {
        this.name = name();
    }

    RatesErrorCode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
