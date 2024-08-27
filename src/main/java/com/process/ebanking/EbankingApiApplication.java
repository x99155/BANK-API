package com.process.ebanking;

import com.process.ebanking.dtos.BankAccountDTO;
import com.process.ebanking.dtos.CurrentBankAccountDTO;
import com.process.ebanking.dtos.CustomerDTO;
import com.process.ebanking.dtos.SavingBankAccountDTO;
import com.process.ebanking.entities.*;
import com.process.ebanking.enums.AccountStatus;
import com.process.ebanking.enums.OperationType;
import com.process.ebanking.exceptions.BalanceNotSufficientException;
import com.process.ebanking.exceptions.BankAccountNotFoundException;
import com.process.ebanking.exceptions.CustomerNotFoundException;
import com.process.ebanking.repositories.AccountOperationRespository;
import com.process.ebanking.repositories.BankAccountRespository;
import com.process.ebanking.repositories.CustomerRepository;
import com.process.ebanking.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingApiApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(BankAccountService bankAccountService){
		return args -> {
			Stream.of("Boris", "Malik", "Juana").forEach(name -> {
				CustomerDTO customer = new CustomerDTO();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
				bankAccountService.saveCustomer(customer);
			});

			bankAccountService.listCustomers().forEach(customer->{
				try {
					bankAccountService.saveCurrentBankAccount(Math.random()*90000,9000,customer.getId());
					bankAccountService.saveSavingBankAccount(Math.random()*120000,5.5,customer.getId());

				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				}
			});
			List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList();
			for (BankAccountDTO bankAccount:bankAccounts){
				for (int i = 0; i <10 ; i++) {
					String accountId;
					if(bankAccount instanceof SavingBankAccountDTO){
						accountId=((SavingBankAccountDTO) bankAccount).getId();
					} else{
						accountId=((CurrentBankAccountDTO) bankAccount).getId();
					}
					bankAccountService.credit(accountId,10000+Math.random()*120000,"Credit");
					bankAccountService.debit(accountId,1000+Math.random()*9000,"Debit");
				}
			}
		};
	}


}
