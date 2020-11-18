package com.adamspokes.demo;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    private int code;
    private String category;
    private String currency; // enum?
    private String identifier;
    private String status; // enum?
    private String valueType; // enum! credit or debit
    private String name;
    private String type; // enum!
    private String systemAccount; // enum?
    private BigDecimal value;
    private String accountTypeBank;

    public Account() {
    }

    public Account(int code, String category, String currency, String identifier, String status,
    String valueType, String name, String type, String systemAccount, BigDecimal value) {
        this.code = code;
        this.category = category;
        this.currency = currency;
        this.identifier = identifier;
        this.status = status;
        this.valueType = valueType;
        this.name = name;
        this.type = type;
        this.systemAccount = systemAccount;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    @JsonSetter("account_category")
    public void setCategory(String category) {
        this.category = category;
    }
    
    public int getCode() {
        return code;
    }

    @JsonSetter("account_code")
    public void setCode(int code) {
        this.code = code;
    }

    public String getCurrency() {
        return currency;
    }

    @JsonSetter("account_currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIdentifier() {
        return identifier;
    }

    @JsonSetter("account_identifier")
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getStatus() {
        return status;
    }

    @JsonSetter("account_status")
    public void setStatus(String status) {
        this.status = status;
    }

    public String getValueType() {
        return valueType;
    }

    @JsonSetter("value_type")
    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getName() {
        return name;
    }

    @JsonSetter("account_name")
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    @JsonSetter("account_type")
    public void setType(String type) {
        this.type = type;
    }

    public String getSystemAccount() {
        return systemAccount;
    }

    @JsonSetter("system_account")
    public void setSystemAccount(String systemAccount) {
        this.systemAccount = systemAccount;
    }

    public BigDecimal getValue() {
        return value;
    }

    @JsonSetter("total_value")
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getAccountTypeBank() {
        return accountTypeBank;
    }

    @JsonSetter("account_type_bank")
    public void setAccountTypeBank(String accountTypeBank) {
        this.accountTypeBank = accountTypeBank;
    }

}
