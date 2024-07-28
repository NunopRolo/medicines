package com.nr.medicines.models.dto;

import lombok.Data;

@Data
public class MedicineDto {
    private Integer barcode;
    private String name;
    private String dosage;
    private String generic;
    private String internationalCommonName;
    private String form;
    private String packagingSize;
    private String image;
}
