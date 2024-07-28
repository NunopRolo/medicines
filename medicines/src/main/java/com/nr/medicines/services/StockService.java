package com.nr.medicines.services;

import com.nr.medicines.mappers.StockMapper;
import com.nr.medicines.models.*;
import com.nr.medicines.models.dto.MedicineRequest;
import com.nr.medicines.models.dto.NotificationDto;
import com.nr.medicines.models.dto.StockDto;
import com.nr.medicines.repositories.MedicineImageRepository;
import com.nr.medicines.repositories.StockNotificationsRepository;
import com.nr.medicines.repositories.StockRepository;
import com.nr.medicines.utils.DateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@Slf4j
public class StockService {
    private static final String STOCK_NOT_FOUND = "Stock Not Found";
    private final StockRepository stockRepository;
    private final MedicineService medicineService;
    private final MedicineImageRepository medicineImageRepository;
    private final StockMapper stockMapper;
    private final PersonsService personsService;
    private final DateTime dateTime;
    private final NotificationSystem notificationSystem;
    private final StockNotificationsRepository stockNotificationsRepository;

    public StockService(
            StockRepository stockRepository,
            MedicineService medicineService,
            MedicineImageRepository medicineImageRepository,
            StockMapper stockMapper,
            PersonsService personsService,
            DateTime dateTime,
            NotificationSystem notificationSystem,
            StockNotificationsRepository stockNotificationsRepository
    ){
        this.stockRepository = stockRepository;
        this.medicineService = medicineService;
        this.medicineImageRepository = medicineImageRepository;
        this.stockMapper = stockMapper;
        this.personsService = personsService;
        this.dateTime = dateTime;
        this.notificationSystem = notificationSystem;
        this.stockNotificationsRepository = stockNotificationsRepository;
    }

    public Page<StockDto> getAllMedicinesInStock(Pageable pageable, UUID personUuid){
        Person person = this.personsService.getPersonByUuid(personUuid);

        return this.stockRepository.findByPerson(pageable, person).map(
                stock -> stockMapper.stockToStockDto(stock, this.medicineImageRepository)
        );
    }

    public Page<StockDto> search(Pageable pageable, String term, UUID personUuid){
        Person person = this.personsService.getPersonByUuid(personUuid);

        return this.stockRepository.findByPersonAndMedicineNameContainingIgnoreCase(pageable, person, term).map(
               stock -> stockMapper.stockToStockDto(stock, this.medicineImageRepository)
        );
    }

    public Stock insertNewMedicineInStock(MedicineRequest medicineRequest, UUID personUuid){
        Medicine medicine = this.medicineService.getMedicineByBarcode(medicineRequest.getBarcode());

        Person person = this.personsService.getPersonByUuid(personUuid);
        Stock stock = Stock.builder()
                .medicine(medicine)
                .validityEndDate(medicineRequest.getValidity())
                .person(person)
                .created(dateTime.getDate())
                .build();

        return stockRepository.save(stock);
    }

    public void deleteStockEntry(UUID stockUuid){
        Optional<Stock> stockOptional = this.stockRepository.findById(stockUuid);
        if( stockOptional.isEmpty() ){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, STOCK_NOT_FOUND);
        }

        this.stockRepository.delete( stockOptional.get() );
    }

    public Page<StockDto> getStockMedicinesWithFilter(Pageable pageable, ValidityStatusEnum filter){
        if(filter.equals(ValidityStatusEnum.IN_VALIDITY)){
            return this.stockRepository.findAllInValidity(pageable).map(
                    stock -> stockMapper.stockToStockDto(stock, this.medicineImageRepository)
            );
        }else if(filter.equals(ValidityStatusEnum.LESS_THAN_1_MONTH)){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateTime.getDate());
            calendar.add(Calendar.MONTH, +1);
            Date datePlus1Month = calendar.getTime();
            return this.stockRepository.findAllWithLessThanOneMonthValidity(pageable, datePlus1Month).map(
                    stock -> stockMapper.stockToStockDto(stock, this.medicineImageRepository)
            );
        }else{
            return this.stockRepository.findAllWithoutValidity(pageable).map(
                    stock -> stockMapper.stockToStockDto(stock, this.medicineImageRepository)
            );
        }
    }

    public void runCronValidation(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime.getDate());
        calendar.add(Calendar.MONTH, +1);
        Date datePlus1Month = calendar.getTime();
        Page<Stock> pageStockDto = this.stockRepository.findAllWithLessThanOneMonthValidityAndWithoutNotification(null, datePlus1Month);

        if(!pageStockDto.isEmpty()){
            StringBuilder message = new StringBuilder();
            List<Stock> stockList = new ArrayList<>();
            for(Stock stock: pageStockDto.getContent()){
                message.append("Medicine ").append(stock.getMedicine().getName()).append(", with barcode ").append(stock.getMedicine().getBarcode()).append(", has only 1 month of validity!\nValidity: ").append(stock.getValidityEndDate()).append("\n");
                stockList.add(stock);
            }

            NotificationDto notificationDto = NotificationDto.builder()
                    .title("Medicine Validity Alert")
                    .message(message.toString())
                    .build();

            notificationSystem.sendNotification(notificationDto);
            for(Stock stock: stockList) {
                this.storeNotification(stock, notificationDto.getTitle(), notificationDto.getMessage());
            }
        }
    }

    private void storeNotification(Stock stock, String title, String body){
        StockNotifications stockNotifications = StockNotifications.builder()
                .body(body)
                .title(title)
                .stock(stock)
                .created(dateTime.getDate())
                .build();

        this.stockNotificationsRepository.save(stockNotifications);
    }

}
