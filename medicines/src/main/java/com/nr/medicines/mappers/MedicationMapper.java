package com.nr.medicines.mappers;

import com.nr.medicines.models.Medication;
import com.nr.medicines.models.TemporaryMedication;
import com.nr.medicines.models.dto.MedicationDto;
import com.nr.medicines.models.dto.TemporaryMedicationDto;
import com.nr.medicines.repositories.MedicineImageRepository;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        uses = MedicineMapper.class,
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface MedicationMapper {
    @Mapping(source = "medicine", target = "medicine")
    MedicationDto medicationToMedicationDto(
            Medication medication,
            @Context MedicineImageRepository medicineImageRepository
    );

    List<MedicationDto> medicationListToMedicationDtoList(
            List<Medication> medicationsList,
            @Context MedicineImageRepository medicineImageRepository
    );

    @Mapping(source = "medication", target = "medication")
    TemporaryMedicationDto temporaryMedicationToTemporaryMedicationDto(TemporaryMedication temporaryMedication,
                                                                       @Context MedicineImageRepository medicineImageRepository);

    List<TemporaryMedicationDto> temporaryMedicationListToTemporaryMedicationDtoList(List<TemporaryMedication> temporaryMedicationList,
                                                                                     @Context MedicineImageRepository medicineImageRepository);

}
