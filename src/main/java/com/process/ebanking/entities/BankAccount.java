package com.process.ebanking.entities;

import com.process.ebanking.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4)
@Entity
@Table(name = "bank_account")
public class BankAccount {
    @Id
    private String id; // vu que c'est un string, nous allons nous-meme générer cet identifiant(rib)
    private double balance;
    @Column(name = "created_at")
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.EAGER)
    private List<AccountOperation> accountOperations;

}
