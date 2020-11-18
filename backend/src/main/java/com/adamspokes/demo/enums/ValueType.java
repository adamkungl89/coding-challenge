package com.adamspokes.demo.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ValueType {

    DEBIT("debit"),
    CREDIT("credit");

    private String valueType;

    ValueType(String valueType) {
        this.valueType = valueType;
    }

    @JsonValue
    public String getValueType() {
        return valueType;
    }
}
