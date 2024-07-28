package com.nr.medicines.models.dto;

import com.nr.medicines.models.DayPeriod;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MedicationResult {
    private DayPeriod dayPeriod;
    private List<MedicationDto> medications;
    private List<TemporaryMedicationDto> temporaryMedications;
}
