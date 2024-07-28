package com.nr.medicines.models.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MedicineRequest {
    private Integer barcode;
    private Date validity;
}
