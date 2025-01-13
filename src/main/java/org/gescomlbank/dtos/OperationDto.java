package org.gescomlbank.dtos;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationDto {
    private String numAccountSource;

    private String numAccountDestination;

    private double amount;

}
