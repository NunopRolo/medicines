package com.nr.medicines.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nr.medicines.Utils;
import com.nr.medicines.models.*;
import com.nr.medicines.models.dto.DayPeriodRequest;
import com.nr.medicines.models.dto.MedicationRequest;
import com.nr.medicines.models.dto.TemporaryMedicationRequest;
import com.nr.medicines.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

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
class MedicationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MedicinesRepository medicinesRepository;

    @Autowired
    private PersonsRepository personsRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private TemporaryMedicationRepository temporaryMedicationRepository;

    @Autowired
    private DayPeriodsRepository dayPeriodsRepository;

    @AfterEach
    public void resetDb() {
        temporaryMedicationRepository.deleteAll();
        medicationRepository.deleteAll();
        dayPeriodsRepository.deleteAll();
        medicinesRepository.deleteAll();
        personsRepository.deleteAll();
    }

    @Test
    void addMedication() throws Exception {
        Medication medication = Utils.createMedicationObj();
        medicinesRepository.save(medication.getMedicine());
        personsRepository.save(medication.getPerson());
        dayPeriodsRepository.save(medication.getDayPeriod());

        MedicationRequest medicationRequest = new MedicationRequest();
        medicationRequest.setBarcode(medication.getMedicine().getBarcode());
        medicationRequest.setObservations(medication.getObservations());
        medicationRequest.setDayPeriodId(medication.getDayPeriod().getId());
        medicationRequest.setPersonUuid(String.valueOf(medication.getPerson().getPersonUuid()));

        mockMvc
                .perform(
                        post("/medication/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(medicationRequest)))
                .andExpect(status().isOk());

        List<Medication> found = medicationRepository.findAll();
        assertEquals(medication.getMedicine().getBarcode(), found.get(0).getMedicine().getBarcode());
    }

    @Test
    void addTemporaryMedication() throws Exception {
        TemporaryMedication temporaryMedication = Utils.createTemporaryMedicationObj();
        medicinesRepository.save(temporaryMedication.getMedication().getMedicine());
        personsRepository.save(temporaryMedication.getMedication().getPerson());
        dayPeriodsRepository.save(temporaryMedication.getMedication().getDayPeriod());
        medicationRepository.save(temporaryMedication.getMedication());
        temporaryMedicationRepository.save(temporaryMedication);

        MedicationRequest medicationRequest = new MedicationRequest();
        medicationRequest.setBarcode(temporaryMedication.getMedication().getMedicine().getBarcode());
        medicationRequest.setObservations(temporaryMedication.getMedication().getObservations());
        medicationRequest.setDayPeriodId(temporaryMedication.getMedication().getDayPeriod().getId());
        medicationRequest.setPersonUuid(String.valueOf(temporaryMedication.getMedication().getPerson().getPersonUuid()));

        TemporaryMedicationRequest temporaryMedicationRequest = new TemporaryMedicationRequest();
        temporaryMedicationRequest.setMedication(medicationRequest);
        temporaryMedicationRequest.setDays(temporaryMedication.getDays());
        temporaryMedicationRequest.setStartDate(temporaryMedication.getStartDate());

        mockMvc
                .perform(
                        post("/medication/temporary-medication/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(temporaryMedicationRequest)))
                .andExpect(status().isOk());

        List<TemporaryMedication> found = temporaryMedicationRepository.findAll();
        assertEquals(temporaryMedication.getMedication().getMedicine().getBarcode(), found.get(0).getMedication().getMedicine().getBarcode());
        assertEquals(temporaryMedication.getDays(), found.get(0).getDays());
    }

    @Test
    void updateMedication() throws Exception {
        Medication medication = Utils.createMedicationObj();
        medicinesRepository.save(medication.getMedicine());
        personsRepository.save(medication.getPerson());
        dayPeriodsRepository.save(medication.getDayPeriod());
        medicationRepository.save(medication);

        MedicationRequest medicationRequest = new MedicationRequest();
        medicationRequest.setBarcode(medication.getMedicine().getBarcode());
        medicationRequest.setObservations("new observations");
        medicationRequest.setDayPeriodId(medication.getDayPeriod().getId());
        medicationRequest.setPersonUuid(String.valueOf(medication.getPerson().getPersonUuid()));

        mockMvc
                .perform(
                        put("/medication/update/"+medication.getUuid())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(medicationRequest)))
                .andExpect(status().isOk());

        List<Medication> found = medicationRepository.findAll();
        assertEquals(medication.getMedicine().getBarcode(), found.get(0).getMedicine().getBarcode());
        assertEquals(medicationRequest.getObservations(), found.get(0).getObservations());
    }

    @Test
    void getMedication() throws Exception {
        Medication medication = Utils.createMedicationObj();
        medicinesRepository.save(medication.getMedicine());
        personsRepository.save(medication.getPerson());
        dayPeriodsRepository.save(medication.getDayPeriod());
        medicationRepository.save(medication);

        mockMvc
                .perform(
                        get("/medication/get/"+medication.getPerson().getPersonUuid())
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$", hasSize(equalTo(1))))
                        .andExpect(jsonPath("$[0].dayPeriod.id", equalTo(medication.getDayPeriod().getId())))
                        .andExpect(jsonPath("$[0].medications", hasSize(equalTo(1))))
                        .andExpect(jsonPath("$[0].medications[0].uuid", equalTo(medication.getUuid().toString())));
    }

    @Test
    void deleteMedication() throws Exception {
        Medication medication = Utils.createMedicationObj();
        medicinesRepository.save(medication.getMedicine());
        personsRepository.save(medication.getPerson());
        dayPeriodsRepository.save(medication.getDayPeriod());
        medicationRepository.save(medication);

        mockMvc
                .perform(
                        delete("/medication/delete/"+medication.getUuid())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<Medication> found = medicationRepository.findAll();
        assertEquals(Collections.emptyList(), found);
    }

    @Test
    void deleteTemporaryMedication() throws Exception {
        TemporaryMedication temporaryMedication = Utils.createTemporaryMedicationObj();
        Medication medication = temporaryMedication.getMedication();
        medicinesRepository.save(medication.getMedicine());
        personsRepository.save(medication.getPerson());
        dayPeriodsRepository.save(medication.getDayPeriod());
        medicationRepository.save(medication);
        temporaryMedicationRepository.save(temporaryMedication);

        mockMvc
                .perform(
                        delete("/medication/temporary-medication/delete/"+temporaryMedication.getUuid())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<TemporaryMedication> found = temporaryMedicationRepository.findAll();
        assertEquals(Collections.emptyList(), found);
    }

    @Test
    void getDayPeriods() throws Exception {
        Medication medication = Utils.createMedicationObj();
        dayPeriodsRepository.save(medication.getDayPeriod());

        mockMvc
                .perform(
                        get("/medication/day-periods")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(equalTo(1))))
                .andExpect(jsonPath("$[0].id", equalTo(medication.getDayPeriod().getId())));
    }

    @Test
    void addDayPeriod() throws Exception {
        DayPeriod dayPeriod = Utils.createDayPeriodObj();
        DayPeriodRequest dayPeriodRequest = new DayPeriodRequest();
        dayPeriodRequest.setName(dayPeriod.getDescription());
        dayPeriodRequest.setHour(dayPeriod.getHour());

        mockMvc
                .perform(
                        post("/medication/day-period")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(dayPeriodRequest)))
                .andExpect(status().isOk());

        List<DayPeriod> found = dayPeriodsRepository.findAll();
        assertEquals(dayPeriod.getId(), found.get(0).getId());
    }

    @Test
    void deleteDayPeriod() throws Exception {
        DayPeriod dayPeriod = Utils.createDayPeriodObj();
        dayPeriodsRepository.save(dayPeriod);

        mockMvc
                .perform(
                        delete("/medication/day-period/"+dayPeriod.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<DayPeriod> found = dayPeriodsRepository.findAll();
        assertEquals(Collections.emptyList(), found);
    }

    @Test
    void pdf() throws Exception {
        Medication medication = Utils.createMedicationObj();
        medicinesRepository.save(medication.getMedicine());
        personsRepository.save(medication.getPerson());
        dayPeriodsRepository.save(medication.getDayPeriod());
        medicationRepository.save(medication);

        mockMvc
                .perform(
                        get("/medication/pdf/"+ PdfContentEnum.INFO+"/"+medication.getPerson().getPersonUuid())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PDF_VALUE));
    }
}
