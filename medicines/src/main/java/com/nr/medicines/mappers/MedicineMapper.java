package com.nr.medicines.mappers;

import com.nr.medicines.models.Medicine;
import com.nr.medicines.models.MedicineImage;
import com.nr.medicines.models.dto.MedicineDto;
import com.nr.medicines.repositories.MedicineImageRepository;
import org.mapstruct.*;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface MedicineMapper {
    @Mapping(target = "image", ignore = true)
    MedicineDto medicineToMedicineDto(Medicine medicine,
                                      @Context MedicineImageRepository medicineImageRepository);

    @AfterMapping
    default void map(@MappingTarget MedicineDto target, Medicine source, @Context MedicineImageRepository medicineImageRepository) {
        String image = "";
        Optional<MedicineImage> medicineImageOptional = medicineImageRepository.findByMedicineBarcode( source.getBarcode() );
        if(medicineImageOptional.isPresent()){
            image = medicineImageOptional.get().getImage();
        }

        target.setImage( image );
    }
}
