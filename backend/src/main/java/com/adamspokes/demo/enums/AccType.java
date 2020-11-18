package com.adamspokes.demo.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AccType {
    SALES("sales"),
    OVERHEADS("overheads"),
    CURRENT_ACCOUNTS_RECEIVEABLE("current_accounts_receivable"),
    CURRENT_ACCOUNTS_PAYABLE("current_accounts_payable"),
    BANK("bank"),
    FIXED("fixed"),
    TAX("tax"),
    CURRENT("current"),
    PAYROLL("payroll");

    private String type;

    AccType(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType(){
        return type;
    }
}
