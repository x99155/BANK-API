package com.process.ebanking.web;

import com.process.ebanking.dtos.AccountHistoryDTO;
import com.process.ebanking.dtos.AccountOperationDTO;
import com.process.ebanking.dtos.BankAccountDTO;
import com.process.ebanking.exceptions.BankAccountNotFoundException;
import com.process.ebanking.services.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @GetMapping("/accounts/{id}")
    public BankAccountDTO getBankAccount(@PathVariable String id) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(id);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> getAllBankAccounts() {
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/accounts/{id}/operations")
    public List<AccountOperationDTO> getAllAccountOperations(@PathVariable String id) {
        return bankAccountService.accountHistory(id);
    }

    /*@GetMapping("/accounts/{id}/pageOperations")
    public List<AccountHistoryDTO> getAccountHistory(
            @PathVariable String id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        return bankAccountService.getAccountHistory(id, page, size);
    }*/

}
