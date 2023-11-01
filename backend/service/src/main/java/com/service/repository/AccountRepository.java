package com.service.repository;

import com.service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUsername(String username); //Spring JPA automatically makes query from this method name
    // https://www.javaguides.net/2018/11/spring-data-jpa-query-creation-from-method-names.html
}
