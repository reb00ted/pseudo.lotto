package com.toyproject.pseudo.lotto.domain.transaction;

public enum TransactionType {
    CHARGE("충전"), WITHDRAW("출금"), BUY("구입"), WINNING("당첨");

    private final String value;

    private TransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
