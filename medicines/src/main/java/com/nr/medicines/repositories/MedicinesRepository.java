package com.nr.medicines.repositories;

import com.nr.medicines.models.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MedicinesRepository extends JpaRepository<Medicine, Integer> {
    Optional<Medicine> findByBarcode(Integer barcode);

    @Query("SELECT m FROM Medicine m " +
            "WHERE UPPER(m.name) like UPPER(:name)" +
            "OR UPPER(m.dosage) like UPPER(:dosage)" +
            "OR UPPER(m.internationalCommonName) like UPPER(:internationalCommonName)" +
            "OR UPPER(m.form) like UPPER(:form)" +
            "OR UPPER(m.packagingSize) like UPPER(:packagingSize)" +
            "OR CAST(m.barcode as String) like %:barcode%")
    Page<Medicine> findByNameContainingIgnoreCaseOrDosageContainingIgnoreCaseOrInternationalCommonNameContainingIgnoreCaseOrFormContainingIgnoreCaseOrPackagingSizeContainingIgnoreCaseOrBarcode(
            Pageable pageable,
            String name,
            String dosage,
            String internationalCommonName,
            String form,
            String packagingSize,
            Integer barcode
    );

    Page<Medicine> findByNameContainingIgnoreCaseOrDosageContainingIgnoreCaseOrInternationalCommonNameContainingIgnoreCaseOrFormContainingIgnoreCaseOrPackagingSizeContainingIgnoreCase(
            Pageable pageable,
            String name,
            String dosage,
            String internationalCommonName,
            String form,
            String packagingSize
    );

}
