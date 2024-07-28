package com.nr.medicines.repositories;

import com.nr.medicines.models.StockNotifications;
import com.nr.medicines.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StockNotificationsRepository extends JpaRepository<StockNotifications, UUID> {

    Optional<StockNotifications> findByStock(Stock stock);
}
