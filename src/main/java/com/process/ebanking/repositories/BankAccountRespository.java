package com.process.ebanking.repositories;

import com.process.ebanking.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRespository extends JpaRepository<BankAccount, String> {
}
