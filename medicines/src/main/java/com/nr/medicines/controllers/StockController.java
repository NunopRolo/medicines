package com.nr.medicines.controllers;

import com.nr.medicines.models.Stock;
import com.nr.medicines.models.ValidityStatusEnum;
import com.nr.medicines.models.dto.MedicineRequest;
import com.nr.medicines.models.dto.StockDto;
import com.nr.medicines.services.StockService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/stock")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService){
        this.stockService = stockService;
    }

    @GetMapping({"/all/{personUuid}", "/search/{personUuid}/"})
    public Page<StockDto> getAllMedicinesInStock(
            @PageableDefault Pageable pageable,
            @PathVariable String personUuid
    ){
        return this.stockService.getAllMedicinesInStock(pageable, UUID.fromString(personUuid));
    }

    @GetMapping("/search/{personUuid}/{term}")
    public Page<StockDto> search(
            @PageableDefault Pageable pageable,
            @PathVariable String term,
            @PathVariable String personUuid
    ){
        return this.stockService.search(pageable, term, UUID.fromString(personUuid));
    }

    @PostMapping("/add/{personUuid}")
    public Stock insertNewMedicineInStock(
            @RequestBody MedicineRequest medicineRequest,
            @PathVariable String personUuid
    ){
        return this.stockService.insertNewMedicineInStock(medicineRequest, UUID.fromString(personUuid));
    }

    @DeleteMapping("/delete/{uuid}")
    public void deleteStockEntry(@PathVariable String uuid){
        this.stockService.deleteStockEntry(UUID.fromString(uuid));
    }

    @GetMapping("/filter/{personUuid}/{filter}")
    public Page<StockDto> getStockMedicinesWithFilter(
            @PageableDefault Pageable pageable,
            @PathVariable ValidityStatusEnum filter,
            @PathVariable String personUuid
    ){
        return this.stockService.getStockMedicinesWithFilter(pageable, filter);
    }

}
