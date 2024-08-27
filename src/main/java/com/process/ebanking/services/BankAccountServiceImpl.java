package com.process.ebanking.services;

import com.process.ebanking.dtos.AccountHistoryDTO;
import com.process.ebanking.dtos.AccountOperationDTO;
import com.process.ebanking.dtos.BankAccountDTO;
import com.process.ebanking.dtos.CustomerDTO;
import com.process.ebanking.entities.*;
import com.process.ebanking.enums.OperationType;
import com.process.ebanking.exceptions.BalanceNotSufficientException;
import com.process.ebanking.exceptions.BankAccountNotFoundException;
import com.process.ebanking.exceptions.CustomerNotFoundException;
import com.process.ebanking.mappers.BankAccountMapper;
import com.process.ebanking.repositories.AccountOperationRespository;
import com.process.ebanking.repositories.BankAccountRespository;
import com.process.ebanking.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor // injection de dépendance via lombok
@Slf4j // logging de message
public class BankAccountServiceImpl implements BankAccountService {

    private final CustomerRepository customerRepository;
    private final BankAccountRespository bankAccountRespository;
    private final AccountOperationRespository accountOperationRespository;
    private final BankAccountMapper dtoMapper;

    @Override
    public void saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving Customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        customerRepository.save(customer);
    }

    @Override
    public void saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        bankAccountRespository.save(currentAccount);
    }

    @Override
    public void saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        bankAccountRespository.save(savingAccount);
    }


    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(dtoMapper::fromCustomer)
                .toList();
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRespository
                .findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("BankAccount not found"));

        if (bankAccount instanceof SavingAccount) {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.fromSavingAccount(savingAccount);
        } else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRespository
                .findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("BankAccount not found"));

        if (bankAccount.getBalance() < amount) {
            throw new BalanceNotSufficientException("Balance not sufficient");
        }
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRespository.save(accountOperation);

        // Mise à jour du solde
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRespository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRespository
                .findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("BankAccount not found"));

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRespository.save(accountOperation);

        // Mise à jour du solde
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRespository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer to " + accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from " + accountIdSource);
    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        List<BankAccount> bankAccounts = bankAccountRespository.findAll();

        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtoMapper.fromSavingAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentAccount(currentAccount);
            }
        }).toList();
        return bankAccountDTOS;
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public void updateCustomer(CustomerDTO customerDTO) {
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Long customerId) throws CustomerNotFoundException {
        try {
            customerRepository.deleteById(customerId);
        } catch (Exception e) {
            throw new CustomerNotFoundException("Customer not found");
        }
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String customerId) {
        accountOperationRespository.findByBankAccountId(customerId);
        List<AccountOperation> accountOperations = accountOperationRespository.findAll();
        List<AccountOperationDTO> accountOperationDTOS = new ArrayList<>();
        for (AccountOperation accountOperation : accountOperations) {
            AccountOperationDTO accountOperationDTO = dtoMapper.fromAccountOperation(accountOperation);
            accountOperationDTOS.add(accountOperationDTO);
        }
        return accountOperationDTOS;
    }

    /*@Override
    public List<AccountHistoryDTO> getAccountHistory(String id, int page, int size) {
        accountOperationRespository.findByBankAccountId(id, (Pageable) PageRequest.of(page, size));
        List<AccountOperation> accountOperations = accountOperationRespository.findAll();
        List<AccountHistoryDTO> accountHistoryDTOS = new ArrayList<>();
        for (AccountOperation accountOperation : accountOperations) {
            AccountOperationDTO accountOperationDTO = dtoMapper.fromAccountOperation(accountOperation);
        }
        return accountHistoryDTOS;
    }*/
}
