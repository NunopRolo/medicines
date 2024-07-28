package com.nr.medicines.controllers;

import com.nr.medicines.services.StockService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ValidityCronJob {
    private final StockService stockService;

    public ValidityCronJob(
            StockService stockService
    ){
        this.stockService = stockService;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Lisbon")
    public void execute(){
        this.stockService.runCronValidation();
    }
}
