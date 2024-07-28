package com.nr.medicines.models.dto;

import lombok.Data;

@Data
public class MedicationRequest {
    private Integer barcode;
    private Integer dayPeriodId;
    private String observations;
    private String personUuid;
}
