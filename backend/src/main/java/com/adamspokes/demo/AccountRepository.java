package com.adamspokes.demo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface AccountRepository extends CrudRepository<Account,Long> {
    List<Account> findByCategory(String category);
}
