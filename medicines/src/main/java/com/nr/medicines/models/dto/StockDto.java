package com.nr.medicines.models.dto;

import com.nr.medicines.models.ValidityStatusEnum;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class StockDto {
    private UUID id;
    private MedicineDto medicine;
    private Date validityEndDate;
    private ValidityStatusEnum validityStatus;
}
