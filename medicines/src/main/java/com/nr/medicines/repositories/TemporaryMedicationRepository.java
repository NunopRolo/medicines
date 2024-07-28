package com.nr.medicines.repositories;

import com.nr.medicines.models.DayPeriod;
import com.nr.medicines.models.Person;
import com.nr.medicines.models.TemporaryMedication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TemporaryMedicationRepository extends JpaRepository<TemporaryMedication, UUID> {
    @Query("SELECT md FROM TemporaryMedication as md " +
            "WHERE md.medication.person = :person " +
            "AND md.medication.dayPeriod = :dayPeriod " +
            "ORDER BY md.medication.created ASC")
    List<TemporaryMedication> findTemporaryMedications(Person person, DayPeriod dayPeriod);
}
