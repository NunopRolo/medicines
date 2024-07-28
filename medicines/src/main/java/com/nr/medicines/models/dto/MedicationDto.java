package com.nr.medicines.models.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MedicationDto {
    private UUID uuid;
    private MedicineDto medicine;
    private String observations;
}
