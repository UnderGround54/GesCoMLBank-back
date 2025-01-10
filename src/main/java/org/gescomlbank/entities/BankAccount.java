package org.gescomlbank.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.gescomlbank.enums.AccountStatus;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER)
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public abstract class BankAccount implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private double balance;
    private String numAccount;
    private String currency = "AR";
    private AccountStatus status;
    private Date dateCreated = new Date();
}
