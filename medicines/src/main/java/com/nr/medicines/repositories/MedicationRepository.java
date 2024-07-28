package com.nr.medicines.repositories;

import com.nr.medicines.models.DayPeriod;
import com.nr.medicines.models.Person;
import com.nr.medicines.models.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MedicationRepository extends JpaRepository<Medication, UUID> {

    @Query("SELECT md FROM Medication as md " +
            "WHERE md.person = :person " +
            "AND md.dayPeriod = :dayPeriod " +
            "AND md NOT IN (SELECT tmd.medication FROM TemporaryMedication AS tmd WHERE tmd.medication=md)" +
            "ORDER BY md.created ASC")
    List<Medication> findByPersonAndDayPeriodOrderByCreated(Person person, DayPeriod dayPeriod);

}
