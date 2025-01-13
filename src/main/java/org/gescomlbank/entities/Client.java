package org.gescomlbank.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;

    private String lastName;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date birthDate;

    private String telephone;

    private String email;

    private String address;

    @JsonBackReference
    @OneToMany(mappedBy = "client")
    Collection<BankAccount> bankAccount = new ArrayList<BankAccount>();
}
