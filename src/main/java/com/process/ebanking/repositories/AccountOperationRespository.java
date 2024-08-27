package com.process.ebanking.repositories;

import com.process.ebanking.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountOperationRespository extends JpaRepository<AccountOperation, Long> {
}
