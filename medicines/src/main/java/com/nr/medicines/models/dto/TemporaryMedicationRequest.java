package com.nr.medicines.models.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TemporaryMedicationRequest {
    private MedicationRequest medication;
    private Integer days;
    private Date startDate;
}
