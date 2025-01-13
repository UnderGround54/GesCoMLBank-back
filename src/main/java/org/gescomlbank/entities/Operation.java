package org.gescomlbank.entities;

import jakarta.persistence.*;
import lombok.*;
import org.gescomlbank.enums.OperationType;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Operation implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
