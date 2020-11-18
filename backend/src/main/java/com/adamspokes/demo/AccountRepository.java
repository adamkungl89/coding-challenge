package com.adamspokes.demo;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface AccountRepository extends CrudRepository<Account,Long> {
    List<Account> findByCategory(String category);
    List<Account> findByType(String type);
    List<Account> findByValueType(String valueType);
    List<Account> findByTypeOrValueType(String type, String valueType);
    List<Account> findByTypeAndValueType(String type, String valueType);

    @Query("select a from Account a where a.category = :category and a.valueType = :valueType and a.type in :accountTypes")
    List<Account> findAssetsLiabilities(    @Param("category") String category, 
                                            @Param("valueType") String valueType, 
                                            @Param("accountTypes") Set<String> accountTypes);
}
