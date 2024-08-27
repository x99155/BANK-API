package com.process.ebanking.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("SA")
public class SavingAccount extends BankAccount {
    private double interestRate;
}
