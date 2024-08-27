package com.process.ebanking.services;

import com.process.ebanking.dtos.CustomerDTO;
import com.process.ebanking.entities.BankAccount;
import com.process.ebanking.entities.Customer;
import com.process.ebanking.exceptions.BalanceNotSufficientException;
import com.process.ebanking.exceptions.BankAccountNotFoundException;
import com.process.ebanking.exceptions.CustomerNotFoundException;

import java.util.List;

/*
Définition des besoins fonctionnelles
 */
public interface BankAccountService {
    // Créer un client
    void saveCustomer(CustomerDTO customerDTO);

    // Créer un compte courant
    void saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;

    // Créer un compte épargne
    void saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

    // Consulter une liste de client
    List<CustomerDTO> listCustomers();

    // Consulter un compte
    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;

    // Créer les opérations de débit, de crédit, et de virement
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    // Consulter la liste de compte
    List<BankAccount> bankAccountList();

    // Consulter un client
    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    // mis à jour d'un client
    void updateCustomer(CustomerDTO customerDTO);

    // Suppression d'un client
    void deleteCustomer(Long customerId) throws CustomerNotFoundException;
}
