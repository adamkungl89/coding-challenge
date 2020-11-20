package com.adamspokes.demo.unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import com.adamspokes.demo.Account;
import com.adamspokes.demo.AccountRepository;
import com.adamspokes.demo.enums.AccType;
import com.adamspokes.demo.enums.Category;
import com.adamspokes.demo.enums.ValueType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class AccountRepositoryUnitTests {
    
    @Autowired 
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository repo;

    @BeforeEach
    public void setup() {
        Account a1 = new Account(100, Category.EXPENSE, "AUD", "acc-1", "active", ValueType.DEBIT, "account1", AccType.OVERHEADS, "", BigDecimal.ONE);
        entityManager.persist(a1);
        Account a2 = new Account(200, Category.REVENUE, "AUD", "acc-2", "active", ValueType.CREDIT, "account2", AccType.SALES, "", BigDecimal.TEN);
        entityManager.persist(a2);
        entityManager.flush();
    }

    @Test 
    public void testAccountFindAllCount() {
        List<Account> accs = repo.findAll();
        assertEquals(2, accs.size());
    }

    @Test 
    public void testDelete() {
        List<Account> accs = repo.findByCategory(Category.EXPENSE);
        repo.delete(accs.get(0));
        assertEquals(1, accs.size());
    }

    @Test 
    public void testFindByCategory() {
        List<Account> accs = repo.findByCategory(Category.EXPENSE);
        assertEquals(1, accs.size());
    }

    @Test 
    public void testFindbyTypeAndValueType(){
        List<Account> accs = repo.findByTypeAndValueType(AccType.SALES, ValueType.CREDIT);
        assertEquals(1, accs.size());
    }
}
