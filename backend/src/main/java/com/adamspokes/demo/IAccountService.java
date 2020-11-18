package com.adamspokes.demo;

import java.math.BigDecimal;

public interface IAccountService {
    public void parseJsonToAccounts(String jsonFileString);
    public Account saveAccount(Account account);
    public BigDecimal calculateRevenue();
    public BigDecimal calculateExpenses();
    public BigDecimal calculateGrossProfitMargin();
    public BigDecimal calculateNetProfitMargin();
    public BigDecimal calculateWorkingCapitalRatio();
}
