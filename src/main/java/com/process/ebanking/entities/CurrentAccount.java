package com.process.ebanking.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("CA") // cette valeur sera mise dans TYPE
public class CurrentAccount extends BankAccount {
    private double overDraft;
}
