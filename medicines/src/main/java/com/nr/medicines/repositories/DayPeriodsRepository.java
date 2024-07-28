package com.nr.medicines.repositories;

import com.nr.medicines.models.DayPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DayPeriodsRepository extends JpaRepository<DayPeriod, Integer> {
    List<DayPeriod> findByOrderByOrderAsc();
}
