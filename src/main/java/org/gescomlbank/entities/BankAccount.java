package org.gescomlbank.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.gescomlbank.enums.AccountStatus;

import java.util.ArrayList;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER)
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class BankAccount extends AbstractEntity {

    @Column(nullable = false)
    private double balance;

    @Column(nullable = false)
    private String numAccount;

    @Column(nullable = false)
    private String currency = "AR";

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @JoinColumn(name = "clientId")
    @ManyToOne
    private Client client;

    @JsonBackReference
    @OneToMany(mappedBy = "bankAccount")
    Collection<Operation> operation = new ArrayList<Operation>();
}
