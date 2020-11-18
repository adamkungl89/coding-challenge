package com.adamspokes.demo;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

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
    public void testRevenue(){
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
    public void testExpenses(){
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


}
