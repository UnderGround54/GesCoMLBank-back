package org.gescomlbank.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * Compte epargne
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("2")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class SavingAccount extends BankAccount implements Serializable {
    /**
     * Taux d'intérêt
     */
    private double interestRate;
}
