package com.nr.medicines.models.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class TemporaryMedicationDto {
    private UUID uuid;
    private MedicationDto medication;
    private Integer days;
    private Date startDate;
}
