package com.adamspokes.demo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

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
        return calculateTotalByCategory("revenue");
    }

    @Override
    public BigDecimal calculateExpenses() {
        return calculateTotalByCategory("expense");
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
        List<Account> accounts = repo.findByTypeAndValueType("sales", "debit");
        costOfSales = accounts.stream().map(Account::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal revenue = calculateRevenue();
        total = revenue.subtract(costOfSales).divide(revenue, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        return total;
    }

    @Override
    public BigDecimal calculateNetProfitMargin() {
        BigDecimal total = new BigDecimal(0);
        BigDecimal revenue = calculateRevenue();
        total = revenue.subtract(calculateExpenses()).divide(revenue, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        return total;
    }

    @Override
    public BigDecimal calculateWorkingCapitalRatio() {
        BigDecimal total = new BigDecimal(0);

        BigDecimal assets = new BigDecimal(0);
        Set<String> types = new HashSet<String>();
        types.add("current");
        types.add("bank");
        types.add("current_accounts_receivable");
        List<Account> accounts = repo.findAssetsLiabilities("assets", "debit", types);
        assets = accounts.stream().map(Account::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Assets+ " + assets);
        accounts = repo.findAssetsLiabilities("assets", "credit", types);
        assets.subtract(accounts.stream().map(Account::getValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        System.out.println("Assets " + assets);

        BigDecimal liabilities = new BigDecimal(0);
        types = new HashSet<String>();
        types.add("current");
        types.add("current_accounts_payable");
        accounts = repo.findAssetsLiabilities("liability", "credit", types);
        liabilities = accounts.stream().map(Account::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Liabilities+ " + liabilities);
        accounts = repo.findAssetsLiabilities("liability", "debit", types);
        liabilities.subtract(accounts.stream().map(Account::getValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        System.out.println("Liabilities " + liabilities);

        total = assets.divide(liabilities, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        return total;
    }

    private BigDecimal calculateTotalByCategory(String accountCategory) {
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (Account a : accountList) {
            saveAccount(a);
        }
        
    }


}
