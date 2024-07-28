package com.nr.medicines.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nr.medicines.Utils;
import com.nr.medicines.models.Medicine;
import com.nr.medicines.models.MedicineImage;
import com.nr.medicines.repositories.MedicineImageRepository;
import com.nr.medicines.repositories.MedicinesRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-junit.properties"
)
class MedicineControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MedicinesRepository medicinesRepository;

    @Autowired
    private MedicineImageRepository medicineImageRepository;

    @AfterEach
    public void resetDb() {
        medicineImageRepository.deleteAll();
        medicinesRepository.deleteAll();
    }

    @Test
    void getMedicineByBarcode() throws Exception {
        Medicine medicine = Utils.createMedicineObj();
        medicinesRepository.save(medicine);

        mockMvc
                .perform(get("/medicines/"+medicine.getBarcode())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.barcode", is(medicine.getBarcode())));
    }

    @Test
    void getAllMedicines() throws Exception {
        Medicine medicine = Utils.createMedicineObj();
        medicinesRepository.save(medicine);

        mockMvc
                .perform(get("/medicines/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(equalTo(1))))
                .andExpect(jsonPath("$.totalElements", equalTo(1)))
                .andExpect(jsonPath("$.content[0].barcode", is(medicine.getBarcode())));
    }

    @Test
    void search() throws Exception {
        Medicine medicine = Utils.createMedicineObj();
        medicinesRepository.save(medicine);

        mockMvc
                .perform(get("/medicines/search/"+medicine.getName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(equalTo(1))))
                .andExpect(jsonPath("$.totalElements", equalTo(1)))
                .andExpect(jsonPath("$.content[0].barcode", is(medicine.getBarcode())));
    }

    @Test
    void uploadImage() throws Exception {
        Medicine medicine = Utils.createMedicineObj();
        medicinesRepository.save(medicine);
        MedicineImage medicineImage = Utils.createMedicineImageObj(medicine, false);
        medicineImageRepository.save(medicineImage);

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "image",
                "image.png",
                MediaType.TEXT_PLAIN_VALUE,
                medicineImage.getImage().getBytes()
        );

        mockMvc.perform(multipart("/medicines/image/"+medicine.getBarcode())
                        .file(mockMultipartFile))
                .andExpect(status().isOk());

        List<MedicineImage> found = medicineImageRepository.findAll();
        assertEquals(medicineImage.getImage(), found.get(0).getImage());
    }

    @Test
    void getMedicationImage() throws Exception {
        Medicine medicine = Utils.createMedicineObj();
        medicinesRepository.save(medicine);
        MedicineImage medicineImage = Utils.createMedicineImageObj(medicine, false);
        medicineImageRepository.save(medicineImage);

        mockMvc
                .perform(get("/medicines/image/"+medicine.getBarcode())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_JPEG_VALUE));
    }

    @Test
    void deleteImage() throws Exception {
        Medicine medicine = Utils.createMedicineObj();
        medicinesRepository.save(medicine);
        MedicineImage medicineImage = Utils.createMedicineImageObj(medicine, false);
        medicineImageRepository.save(medicineImage);

        mockMvc
                .perform(delete("/medicines/image/"+medicine.getBarcode())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<MedicineImage> found = medicineImageRepository.findAll();
        assertEquals(Collections.emptyList(), found);
    }

    @Test
    void testNotifications() throws Exception {
        mockMvc
                .perform(get("/medicines/notifications")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
