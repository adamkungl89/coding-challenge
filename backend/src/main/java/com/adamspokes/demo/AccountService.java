package com.adamspokes.demo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import com.adamspokes.demo.enums.AccType;
import com.adamspokes.demo.enums.Category;
import com.adamspokes.demo.enums.ValueType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountService implements IAccountService {

    @Autowired
    private AccountRepository repo;

    public AccountService() {}

    @Override
    @Transactional
    public Account saveAccount(Account account) {
        return repo.save(account);
    }

    @Override
    public BigDecimal calculateRevenue() {
        return calculateTotalByCategory(Category.REVENUE);
    }

    @Override
    public BigDecimal calculateExpenses() {
        return calculateTotalByCategory(Category.EXPENSE);
    }

    @Override
    public BigDecimal calculateGrossProfitMargin() {
        return calculateGrossProfitMarginSensible();
        /*as per spec, I can't make it make sense
        "Gross Profit Margin
        This is calculated in two steps: 
        first by adding all the total_value fields where the account_type is set to sales and the value_type is set to debit; 
        then dividing that by the revenue value calculated earlier to generate a percentage value."
        It doesn't make real life sense, nor does any of the provided sample data give us a sensible answer. No data points fit the critieria. 
        Should be? (revenue - costofsales)/revenue, where costofsales = sum of all account_type=sales, value_type=debit
        */
        /*
        BigDecimal total = new BigDecimal(0);
        List<Account> accounts = repo.findByTypeOrValueType("sales", "debit");
        for (Account a : accounts) {
            System.out.println("Account  " + a.getType() + "  " + a.getValueType() + "  " + a.getValue());
            total = total.add(a.getValue());
        }
        System.out.println("-----------");
        List<Account> accounts2 = repo.findByTypeAndValueType("sales", "debit");
        for (Account a : accounts2) {
            System.out.println("Account2  " + a.getType() + "  " + a.getValueType() + "  " + a.getValue());
            total = total.add(a.getValue());
        }
        //System.out.println("Total sales + debits " + total);
        //System.out.println("Revenue " + calculateRevenue());
        //System.out.println("Expenses " + calculateExpenses());
        total = total.divide(calculateRevenue(), RoundingMode.HALF_UP);
        total = total.multiply(new BigDecimal(100));
        return total;
        */
    }

    public BigDecimal calculateGrossProfitMarginSensible() {
        /* (Revenue - Cost of Sales)/Revenue
        Cost of Sales, totals where type=sales&&value_type=debit

        Added an account "Cost of sales" to test it. 
        */
        BigDecimal total = new BigDecimal(0);
        BigDecimal costOfSales = new BigDecimal(0);
        List<Account> accounts = repo.findByTypeAndValueType(AccType.SALES, ValueType.DEBIT);
        costOfSales = accounts.stream().map(Account::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal revenue = calculateRevenue();
        if (revenue.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        total = revenue.subtract(costOfSales).divide(revenue, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        return total;
    }

    @Override
    public BigDecimal calculateNetProfitMargin() {
        BigDecimal total = new BigDecimal(0);
        BigDecimal revenue = calculateRevenue();
        if (revenue.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        total = revenue.subtract(calculateExpenses()).divide(revenue, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        return total;
    }

    @Override
    public BigDecimal calculateWorkingCapitalRatio() {
        BigDecimal total = new BigDecimal(0);

        BigDecimal assets = new BigDecimal(0);
        Set<AccType> types = new HashSet<AccType>();
        types.add(AccType.CURRENT);
        types.add(AccType.BANK);
        types.add(AccType.CURRENT_ACCOUNTS_RECEIVEABLE);
        List<Account> accounts = repo.findAssetsLiabilities(Category.ASSETS, ValueType.DEBIT, types);
        assets = accounts.stream().map(Account::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
        accounts = repo.findAssetsLiabilities(Category.ASSETS, ValueType.CREDIT, types);
        assets.subtract(accounts.stream().map(Account::getValue).reduce(BigDecimal.ZERO, BigDecimal::add));

        BigDecimal liabilities = new BigDecimal(0);
        types = new HashSet<AccType>();
        types.add(AccType.CURRENT);
        types.add(AccType.CURRENT_ACCOUNTS_PAYABLE);
        accounts = repo.findAssetsLiabilities(Category.LIABILITY, ValueType.CREDIT, types);
        liabilities = accounts.stream().map(Account::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
        accounts = repo.findAssetsLiabilities(Category.LIABILITY, ValueType.DEBIT, types);
        liabilities.subtract(accounts.stream().map(Account::getValue).reduce(BigDecimal.ZERO, BigDecimal::add));

        if (liabilities.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        total = assets.divide(liabilities, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        return total;
    }

    private BigDecimal calculateTotalByCategory(Category accountCategory) {
        BigDecimal total = new BigDecimal(0);
        List<Account> accounts = repo.findByCategory(accountCategory);
        total = accounts.stream().map(Account::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
        return total;
    }

    public void parseJsonToAccounts(String jsonString) {
        ObjectMapper om = new ObjectMapper();
        List<Account> accountList = new ArrayList<Account>();
        try {
            JsonNode dataNode = om.readTree(jsonString).get("data");
            accountList = om.readValue(dataNode.toString(), new TypeReference<List<Account>>(){});
        } catch (JsonMappingException e) {
            throw new RuntimeException("Error parsing Json File");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing Json File");
        }
        for (Account a : accountList) {
            saveAccount(a);
        }
        
    }

    @Override
    public List<Account> getAccounts() {
        return repo.findAll();
    }


}
