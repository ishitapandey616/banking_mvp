package com.nagarro.accountservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.nagarro.accountservice.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
