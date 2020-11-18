package com.adamspokes.demo;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import com.adamspokes.demo.enums.AccType;
import com.adamspokes.demo.enums.Category;
import com.adamspokes.demo.enums.ValueType;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface AccountRepository extends CrudRepository<Account,Long> {
    List<Account> findAll();
    List<Account> findByCategory(Category category);
    List<Account> findByType(AccType type);
    List<Account> findByValueType(AccType valueType);
    List<Account> findByTypeOrValueType(AccType type, ValueType valueType);
    List<Account> findByTypeAndValueType(AccType type, ValueType valueType);

    @Query("select a from Account a where a.category = :category and a.valueType = :valueType and a.type in :accountTypes")
    List<Account> findAssetsLiabilities(    @Param("category") Category category, 
                                            @Param("valueType") ValueType valueType, 
                                            @Param("accountTypes") Set<AccType> accountTypes);
}
