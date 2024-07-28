package com.nr.medicines.repositories;

import com.nr.medicines.models.Person;
import com.nr.medicines.models.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.UUID;

public interface StockRepository extends JpaRepository<Stock, UUID> {
    Page<Stock> findByPersonAndMedicineNameContainingIgnoreCase(Pageable pageable, Person person, String term);

    @Query("SELECT s FROM Stock s WHERE s.validityEndDate >= current date")
    Page<Stock> findAllInValidity(Pageable pageable);

    @Query("SELECT s FROM Stock s WHERE s.validityEndDate BETWEEN current date and :datePlus1Month")
    Page<Stock> findAllWithLessThanOneMonthValidity(Pageable pageable, Date datePlus1Month);

    @Query("SELECT s FROM Stock s WHERE s.validityEndDate <= current date")
    Page<Stock> findAllWithoutValidity(Pageable pageable);

    @Query("SELECT s FROM Stock s WHERE s.validityEndDate BETWEEN current date and :datePlus1Month AND s.uuid NOT IN (SELECT en.stock.uuid FROM StockNotifications en WHERE en.stock.uuid=s.uuid)")
    Page<Stock> findAllWithLessThanOneMonthValidityAndWithoutNotification(Pageable pageable, Date datePlus1Month);

    Page<Stock> findByPerson(Pageable pageable, Person person);
}
