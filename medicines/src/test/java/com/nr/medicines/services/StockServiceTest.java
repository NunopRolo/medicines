package com.nr.medicines.services;

import com.nr.medicines.Utils;
import com.nr.medicines.mappers.MedicineMapper;
import com.nr.medicines.mappers.MedicineMapperImpl;
import com.nr.medicines.mappers.StockMapper;
import com.nr.medicines.mappers.StockMapperImpl;
import com.nr.medicines.models.Stock;
import com.nr.medicines.models.StockNotifications;
import com.nr.medicines.models.ValidityStatusEnum;
import com.nr.medicines.models.dto.MedicineRequest;
import com.nr.medicines.models.dto.StockDto;
import com.nr.medicines.repositories.MedicineImageRepository;
import com.nr.medicines.repositories.StockNotificationsRepository;
import com.nr.medicines.repositories.StockRepository;
import com.nr.medicines.utils.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {
    @Mock
    private StockRepository stockRepository;

    @Mock
    private MedicineService medicineService;

    @Mock
    private MedicineImageRepository medicineImageRepository;

    private final MedicineMapper medicineMapper = new MedicineMapperImpl();
    private final StockMapper stockMapper = new StockMapperImpl(medicineMapper);

    @Mock
    private PersonsService personsService;

    @Mock
    private DateTime dateTime;

    @Mock
    private NotificationSystem notificationSystem;

    @Mock
    private StockNotificationsRepository stockNotificationsRepository;

    private StockService stockService;

    @BeforeEach
    void setUp(){
        this.stockService = new StockService(stockRepository, medicineService, medicineImageRepository, stockMapper, personsService, dateTime, notificationSystem, stockNotificationsRepository);
    }

    //getAllMedicinesInStock
    @Test
    void getAllMedicinesInStock(){
        Stock stock = Utils.createStockInValidityObj();
        StockDto stockDto = stockMapper.stockToStockDto(stock, medicineImageRepository);

        Page<Stock> page = new PageImpl<>( List.of(stock) );
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(personsService.getPersonByUuid(stock.getPerson().getPersonUuid()))
                .thenReturn(stock.getPerson());

        when(stockRepository.findByPerson(pageRequest, stock.getPerson()))
                .thenReturn(page);

        Page<StockDto> result = this.stockService.getAllMedicinesInStock(pageRequest, stock.getPerson().getPersonUuid());
        assertEquals(stockDto, result.getContent().get(0));
        verify(stockRepository).findByPerson(pageRequest, stock.getPerson());
    }

    @Test
    void getAllMedicinesInStock_Empty(){
        Stock stock = Utils.createStockObj();

        PageRequest pageRequest = PageRequest.of(0, 10);

        when(personsService.getPersonByUuid(stock.getPerson().getPersonUuid()))
                .thenReturn(stock.getPerson());

        when(stockRepository.findByPerson(pageRequest, stock.getPerson()))
                .thenReturn(Page.empty());

        Page<StockDto> result = this.stockService.getAllMedicinesInStock(pageRequest, stock.getPerson().getPersonUuid());
        assertEquals(0, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        verify(stockRepository).findByPerson(pageRequest, stock.getPerson());
    }

    @Test
    void getAllMedicinesInStock_NullResponse(){
        Stock stock = Utils.createStockInValidityObj();

        ArrayList<Stock> stockArrayList = new ArrayList<>();
        stockArrayList.add(null);

        Page<Stock> page = new PageImpl<>( stockArrayList );
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(personsService.getPersonByUuid(stock.getPerson().getPersonUuid()))
                .thenReturn(stock.getPerson());

        when(stockRepository.findByPerson(pageRequest, stock.getPerson()))
                .thenReturn(page);

        Page<StockDto> result = this.stockService.getAllMedicinesInStock(pageRequest, stock.getPerson().getPersonUuid());
        assertNull(result.getContent().get(0));
        verify(stockRepository).findByPerson(pageRequest, stock.getPerson());
    }

    //search
    @Test
    void search(){
        Stock stock = Utils.createStockInValidityObj();
        StockDto stockDto = stockMapper.stockToStockDto(stock, medicineImageRepository);
        String searchTerm = "Dumyrox";

        Page<Stock> page = new PageImpl<>( List.of(stock) );
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(personsService.getPersonByUuid(stock.getPerson().getPersonUuid()))
                .thenReturn(stock.getPerson());

        when(stockRepository.findByPersonAndMedicineNameContainingIgnoreCase(pageRequest, stock.getPerson(), searchTerm))
                .thenReturn(page);

        Page<StockDto> result = this.stockService.search(pageRequest, searchTerm, stock.getPerson().getPersonUuid());
        assertEquals(stockDto, result.getContent().get(0));
        verify(stockRepository).findByPersonAndMedicineNameContainingIgnoreCase(pageRequest, stock.getPerson(), searchTerm);
    }

    @Test
    void search_NotFound(){
        Stock stock = Utils.createStockObj();
        String searchTerm = "nuno";

        PageRequest pageRequest = PageRequest.of(0, 10);

        when(personsService.getPersonByUuid(stock.getPerson().getPersonUuid()))
                .thenReturn(stock.getPerson());

        when(stockRepository.findByPersonAndMedicineNameContainingIgnoreCase(pageRequest, stock.getPerson(), searchTerm))
                .thenReturn(Page.empty());

        Page<StockDto> result = this.stockService.search(pageRequest, searchTerm, stock.getPerson().getPersonUuid());
        assertEquals(0, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        verify(stockRepository).findByPersonAndMedicineNameContainingIgnoreCase(pageRequest, stock.getPerson(), searchTerm);
    }

    //getStockMedicinesWithFilter
    @Test
    void getStockMedicinesWithFilter_IN_VALIDITY(){
        Stock stock = Utils.createStockInValidityObj();
        StockDto stockDto = stockMapper.stockToStockDto(stock, medicineImageRepository);

        Page<Stock> page = new PageImpl<>( List.of(stock) );
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(stockRepository.findAllInValidity(pageRequest))
                .thenReturn(page);

        Page<StockDto> result = this.stockService.getStockMedicinesWithFilter(pageRequest, ValidityStatusEnum.IN_VALIDITY);
        assertEquals(stockDto, result.getContent().get(0));
        verify(stockRepository).findAllInValidity(pageRequest);
    }

    @Test
    void getStockMedicinesWithFilter_WITHOUT_VALIDITY(){
        Stock stock = Utils.createStockWithoutValidityObj();
        StockDto stockDto = stockMapper.stockToStockDto(stock, medicineImageRepository);

        Page<Stock> page = new PageImpl<>( List.of(stock) );
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(stockRepository.findAllWithoutValidity(pageRequest))
                .thenReturn(page);

        Page<StockDto> result = this.stockService.getStockMedicinesWithFilter(pageRequest, ValidityStatusEnum.WITHOUT_VALIDITY);
        assertEquals(stockDto, result.getContent().get(0));
        verify(stockRepository).findAllWithoutValidity(pageRequest);
    }

    @Test
    void getStockMedicinesWithFilter_LESS_THAN_1_MONTH(){
        Stock stock = Utils.createStockAlmostNoValidityObj();
        StockDto stockDto = stockMapper.stockToStockDto(stock, medicineImageRepository);

        Page<Stock> page = new PageImpl<>( List.of(stock) );
        PageRequest pageRequest = PageRequest.of(0, 10);

        Date date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, +1);
        Date datePlus1Month = calendar.getTime();

        when(stockRepository.findAllWithLessThanOneMonthValidity(pageRequest, datePlus1Month))
                .thenReturn(page);

        when(dateTime.getDate())
                .thenReturn(date);

        Page<StockDto> result = this.stockService.getStockMedicinesWithFilter(pageRequest, ValidityStatusEnum.LESS_THAN_1_MONTH);
        assertEquals(stockDto, result.getContent().get(0));
        verify(stockRepository).findAllWithLessThanOneMonthValidity(pageRequest, datePlus1Month);
    }

    //insertNewMedicineInStock
    @Test
    void insertNewMedicineInStock(){
        Stock stock = Utils.createStockObj();

        Date date = new Date();

        MedicineRequest medicineRequest = new MedicineRequest();
        medicineRequest.setBarcode(stock.getMedicine().getBarcode());
        medicineRequest.setValidity(date);

        when(medicineService.getMedicineByBarcode(stock.getMedicine().getBarcode()))
                .thenReturn(stock.getMedicine());

        when(personsService.getPersonByUuid(stock.getPerson().getPersonUuid()))
                .thenReturn(stock.getPerson());

        when(dateTime.getDate())
                .thenReturn(date);

        this.stockService.insertNewMedicineInStock(medicineRequest, stock.getPerson().getPersonUuid());

        ArgumentCaptor<Stock> stockArgumentCaptor = ArgumentCaptor.forClass(Stock.class);
        verify(stockRepository).save(stockArgumentCaptor.capture());

        Stock capturedStock = stockArgumentCaptor.getValue();
        assertEquals(capturedStock.getMedicine().getBarcode(), stock.getMedicine().getBarcode());
    }

    //deleteStockEntry()
    @Test
    void deleteStockEntry(){
        Stock stock = Utils.createStockObj();
        when(stockRepository.findById(stock.getUuid()))
                .thenReturn(Optional.of(stock));

        this.stockService.deleteStockEntry(stock.getUuid());
        verify(stockRepository).delete(stock);
        verify(stockRepository).findById(stock.getUuid());
    }

    @Test
    void deleteStockEntry_NotFound(){
        Stock stock = Utils.createStockObj();
        when(stockRepository.findById(stock.getUuid()))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> this.stockService.deleteStockEntry(stock.getUuid()));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(stockRepository).findById(stock.getUuid());
        verify(stockRepository, never()).save(any(Stock.class));
    }

    //runCronValidation
    @Test
    void runCronValidation(){
        Stock stock = Utils.createStockAlmostNoValidityObj();
        Page<Stock> page = new PageImpl<>( List.of(stock) );

        Date date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, +1);
        Date datePlus1Month = calendar.getTime();
        when(stockRepository.findAllWithLessThanOneMonthValidityAndWithoutNotification(null, datePlus1Month))
                .thenReturn(page);

        when(dateTime.getDate())
                .thenReturn(date);

        this.stockService.runCronValidation();

        ArgumentCaptor<StockNotifications> stockArgumentCaptor = ArgumentCaptor.forClass(StockNotifications.class);
        verify(stockNotificationsRepository).save(stockArgumentCaptor.capture());

        StockNotifications capturedStockNotifications = stockArgumentCaptor.getValue();
        assertEquals("Medicine Validity Alert", capturedStockNotifications.getTitle());
        assertEquals(stock.getUuid(), capturedStockNotifications.getStock().getUuid());
    }

    @Test
    void runCronValidation_WithNoNotificationToSent(){
        Date date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, +1);
        Date datePlus1Month = calendar.getTime();
        when(stockRepository.findAllWithLessThanOneMonthValidityAndWithoutNotification(null, datePlus1Month))
                .thenReturn(Page.empty());

        when(dateTime.getDate())
                .thenReturn(date);

        this.stockService.runCronValidation();
        verify(stockNotificationsRepository, never()).save(any(StockNotifications.class));
    }

}
