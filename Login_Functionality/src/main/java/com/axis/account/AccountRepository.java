package com.axis.account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findAccountByUserId(Integer userId);
    Optional<Account> findAccountByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);

}