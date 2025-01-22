package org.gescomlbank.entities;

import jakarta.persistence.*;
import lombok.*;
import org.gescomlbank.enums.OperationType;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Operation extends AbstractEntity {

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private Date dateOperation;

    private String numOperation;

    private String numAccountSource;

    @ManyToOne
    private BankAccount bankAccount;

    @Column(nullable = false)
    private OperationType operationType;
}
