package com.nr.medicines.repositories;

import com.nr.medicines.models.MedicineImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MedicineImageRepository extends JpaRepository<MedicineImage, UUID> {

    Optional<MedicineImage> findByMedicineBarcode(Integer barcode);
}
