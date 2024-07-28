package com.nr.medicines.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nr.medicines.Utils;
import com.nr.medicines.models.Stock;
import com.nr.medicines.models.ValidityStatusEnum;
import com.nr.medicines.models.dto.MedicineRequest;
import com.nr.medicines.repositories.MedicinesRepository;
import com.nr.medicines.repositories.PersonsRepository;
import com.nr.medicines.repositories.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-junit.properties"
)
class StockControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private MedicinesRepository medicinesRepository;

    @Autowired
    private PersonsRepository personsRepository;

    @AfterEach
    public void resetDb() {
        stockRepository.deleteAll();
        medicinesRepository.deleteAll();
        personsRepository.deleteAll();
    }

    @Test
    void getAllMedicinesInStock() throws Exception {
        Stock stock = Utils.createStockObj();
        medicinesRepository.save(stock.getMedicine());
        personsRepository.save(stock.getPerson());
        stockRepository.save(stock);

        mockMvc
                .perform(get("/stock/all/"+stock.getPerson().getPersonUuid())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(equalTo(1))))
                .andExpect(jsonPath("$.totalElements", equalTo(1)))
                .andExpect(jsonPath("$.content[0].id", is(stock.getUuid().toString())));
    }

    @Test
    void search() throws Exception {
        Stock stock = Utils.createStockObj();
        medicinesRepository.save(stock.getMedicine());
        personsRepository.save(stock.getPerson());
        stockRepository.save(stock);

        mockMvc
                .perform(get("/stock/search/"+stock.getPerson().getPersonUuid()+"/"+stock.getMedicine().getName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(equalTo(1))))
                .andExpect(jsonPath("$.totalElements", equalTo(1)))
                .andExpect(jsonPath("$.content[0].id", is(stock.getUuid().toString())));
    }

    @Test
    void insertNewMedicineInStock() throws Exception {
        Stock stock = Utils.createStockObj();
        medicinesRepository.save(stock.getMedicine());
        personsRepository.save(stock.getPerson());

        MedicineRequest medicineRequest = new MedicineRequest();
        medicineRequest.setBarcode(stock.getMedicine().getBarcode());
        medicineRequest.setValidity( new Date() );

        mockMvc
                .perform(
                        post("/stock/add/"+stock.getPerson().getPersonUuid())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(medicineRequest)))
                .andExpect(status().isOk());

        List<Stock> found = stockRepository.findAll();
        assertEquals(stock.getMedicine().getBarcode(), found.get(0).getMedicine().getBarcode());
    }

    @Test
    void deleteStockEntry() throws Exception {
        Stock stock = Utils.createStockObj();
        medicinesRepository.save(stock.getMedicine());
        personsRepository.save(stock.getPerson());
        stockRepository.save(stock);

        mockMvc
                .perform(
                        delete("/stock/delete/"+stock.getUuid())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<Stock> found = stockRepository.findAll();
        assertEquals(Collections.emptyList(), found);
    }

    @Test
    void getStockMedicinesWithFilter() throws Exception {
        Stock stock = Utils.createStockObj();
        medicinesRepository.save(stock.getMedicine());
        personsRepository.save(stock.getPerson());
        stockRepository.save(stock);

        mockMvc
                .perform(get("/stock/filter/"+stock.getPerson().getPersonUuid()+"/"+ ValidityStatusEnum.IN_VALIDITY)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(equalTo(1))))
                .andExpect(jsonPath("$.totalElements", equalTo(1)))
                .andExpect(jsonPath("$.content[0].id", is(stock.getUuid().toString())));
    }
}
