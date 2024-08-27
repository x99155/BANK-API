package com.process.ebanking.repositories;

import com.process.ebanking.dtos.BankAccountDTO;
import com.process.ebanking.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface AccountOperationRespository extends JpaRepository<AccountOperation, Long> {
    List<AccountOperation> findByBankAccountId(String bankAccount_id);
    //Page<AccountOperation> findByBankAccountId(String bankAccount_id, Pageable pageable);

}
