package org.gescomlbank.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.gescomlbank.enums.AccountStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER)
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public abstract class BankAccount implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private double balance;

    @Column(nullable = false)
    private String numAccount;

    @Column(nullable = false)
    private String currency = "AR";

    @Column(nullable = false)
    private AccountStatus status;

    private Date createdAt = new Date();

    @JoinColumn(nullable = false)
    @ManyToOne
    private Client client;

    @JsonBackReference
    @OneToMany(mappedBy = "bank_account")
    Collection<Operation> operation = new ArrayList<Operation>();
}
