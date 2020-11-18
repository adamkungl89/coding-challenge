package com.adamspokes.demo;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.transaction.Transactional;

import com.adamspokes.demo.enums.AccType;
import com.adamspokes.demo.enums.Category;
import com.adamspokes.demo.enums.ValueType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class AccountServiceIntegrationTests {
    
    @Autowired
    AccountService service;

    @Test
    @Transactional
    public void testRevenue() {
        BigDecimal val1 = BigDecimal.valueOf(100.00);
        BigDecimal val2 = BigDecimal.valueOf(130.21);
        BigDecimal notval3 = BigDecimal.valueOf(200.00);
        Account rev1 = new Account(11, Category.REVENUE, "AUD", "rev1", "active", ValueType.CREDIT, "rev1", AccType.SALES, "", val1);
        Account rev2 = new Account(12, Category.REVENUE, "AUD", "rev2", "active", ValueType.CREDIT, "rev2", AccType.SALES, "", val2);
        Account notrev = new Account(13, Category.ASSETS, "AUD", "notrev", "active", ValueType.CREDIT, "notrev", AccType.BANK, "", notval3);
        service.saveAccount(rev1);
        service.saveAccount(rev2);
        service.saveAccount(notrev);
        assertEquals(3, service.getAccounts().size());
        assertEquals(val1.add(val2), service.calculateRevenue());
    }

    @Test
    @Transactional
    public void testExpenses() {
        BigDecimal val1 = BigDecimal.valueOf(500.00);
        BigDecimal val2 = BigDecimal.valueOf(80.19);
        BigDecimal notval3 = BigDecimal.valueOf(100.00);
        Account exp1 = new Account(11, Category.EXPENSE, "AUD", "exp1", "active", ValueType.DEBIT, "exp1", AccType.OVERHEADS, "", val1);
        Account exp2 = new Account(12, Category.EXPENSE, "AUD", "exp2", "active", ValueType.DEBIT, "exp2", AccType.OVERHEADS, "", val2);
        Account notexp = new Account(13, Category.REVENUE, "AUD", "notexp", "active", ValueType.CREDIT, "notexp", AccType.SALES, "", notval3);
        service.saveAccount(exp1);
        service.saveAccount(exp2);
        service.saveAccount(notexp);
        assertEquals(3, service.getAccounts().size());
        assertEquals(val1.add(val2), service.calculateExpenses());
    }

    @Test 
    @Transactional
    public void testGrossProfitMargin() {
        BigDecimal rev = BigDecimal.valueOf(500.00);
        BigDecimal cost = BigDecimal.valueOf(333.00);
        Account acc1 = new Account(11, Category.EXPENSE, "AUD", "gpm1", "active", ValueType.DEBIT, "gpm1", AccType.SALES, "", cost);
        Account acc2 = new Account(12, Category.REVENUE, "AUD", "gpm2", "active", ValueType.CREDIT, "gpm2", AccType.SALES, "", rev);
        service.saveAccount(acc1);
        service.saveAccount(acc2);
        BigDecimal expected = rev.subtract(cost);
        expected = expected.setScale(3);
        expected = expected.divide(rev, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        assertEquals(expected, service.calculateGrossProfitMargin());
        assertEquals(BigDecimal.valueOf(33.400).setScale(3), service.calculateGrossProfitMargin());
    }

    @Test 
    @Transactional
    public void testNetProfitMargin() {
        BigDecimal rev = BigDecimal.valueOf(700.00);
        BigDecimal cost = BigDecimal.valueOf(400.00);
        BigDecimal overheads = BigDecimal.valueOf(200.00);
        Account acc1 = new Account(11, Category.EXPENSE, "AUD", "npm1", "active", ValueType.DEBIT, "npm1", AccType.SALES, "", cost);
        Account acc2 = new Account(12, Category.REVENUE, "AUD", "npm2", "active", ValueType.CREDIT, "npm2", AccType.SALES, "", rev);
        Account acc3 = new Account(13, Category.EXPENSE, "AUD", "npm3", "active", ValueType.DEBIT, "npm3", AccType.OVERHEADS, "", overheads);
        service.saveAccount(acc1);
        service.saveAccount(acc2);
        service.saveAccount(acc3);
        BigDecimal expected = rev.subtract(cost).subtract(overheads);
        expected = expected.setScale(3);
        expected = expected.divide(rev, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        assertEquals(expected, service.calculateNetProfitMargin());
        assertEquals(BigDecimal.valueOf(14.300).setScale(3), service.calculateNetProfitMargin());
    }

    @Test
    @Transactional
    public void testWorkingCapitalRatio() {
        BigDecimal assetplus = BigDecimal.valueOf(1000);
        BigDecimal assetminus = BigDecimal.valueOf(750);
        BigDecimal liabilityplus = BigDecimal.valueOf(800);
        BigDecimal liabilityminus = BigDecimal.valueOf(650);
        Account acc1 = new Account(11, Category.ASSETS, "AUD", "wcr1", "active", ValueType.DEBIT, "wcr1", AccType.BANK, "", assetplus);
        Account acc2 = new Account(12, Category.ASSETS, "AUD", "wcr2", "active", ValueType.CREDIT, "wcr2", AccType.CURRENT, "", assetminus);
        Account acc3 = new Account(13, Category.LIABILITY, "AUD", "wcr3", "active", ValueType.CREDIT, "wcr3", AccType.CURRENT, "", liabilityplus);
        Account acc4 = new Account(14, Category.LIABILITY, "AUD", "wcr4", "active", ValueType.DEBIT, "wcr4", AccType.CURRENT_ACCOUNTS_PAYABLE, "", liabilityminus);
        service.saveAccount(acc1);
        service.saveAccount(acc2);
        service.saveAccount(acc3);
        service.saveAccount(acc4);
        BigDecimal assets = assetplus.subtract(assetminus);
        assets = assets.setScale(3);
        BigDecimal liabilities = liabilityplus.subtract(liabilityminus);
        BigDecimal expected = assets.divide(liabilities, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        assertEquals(expected, service.calculateWorkingCapitalRatio());
        assertEquals(BigDecimal.valueOf(166.700).setScale(3), service.calculateWorkingCapitalRatio()); 
        //was trying to avoid 'magic numbers' but found rounding errors in the service calulation were being replicated in the test due to the BigDecimal type
    }

}
