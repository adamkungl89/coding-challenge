package com.adamspokes.demo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BigDecimal calculateNetProfitMargin() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BigDecimal calculateWorkingCapitalRatio() {
        // TODO Auto-generated method stub
        return null;
    }

    private BigDecimal calculateTotalByCategory(String accountCategory) {
        BigDecimal total = new BigDecimal(0);
        List<Account> accounts = repo.findByCategory(accountCategory);
        for (Account a : accounts) {
            total = total.add(a.getValue());
        }
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
