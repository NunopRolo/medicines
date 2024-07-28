package com.nr.medicines.services;

import com.nr.medicines.Utils;
import com.nr.medicines.mappers.MedicineMapperImpl;
import com.nr.medicines.models.Medicine;
import com.nr.medicines.models.MedicineImage;
import com.nr.medicines.models.dto.MedicineDto;
import com.nr.medicines.repositories.MedicineImageRepository;
import com.nr.medicines.repositories.MedicinesRepository;
import com.nr.medicines.utils.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicineServiceTest {
    @Mock
    private MedicinesRepository medicinesRepository;

    @Mock
    private MedicineImageRepository medicineImageRepository;

    @Mock
    private HassNotificationService hassNotificationsService;

    @Mock
    private DateTime dateTime;

    private final MedicineMapperImpl medicineMapper = new MedicineMapperImpl();

    private MedicineService medicineService;

    @BeforeEach
    void setUp(){
        medicineService = new MedicineService(medicinesRepository, medicineImageRepository, medicineMapper, hassNotificationsService, dateTime);
    }

    // getMedicineByBarcode
    @Test
    void getMedicineByBarcode(){
        Medicine medicine = Utils.createMedicineObj();

        when(medicinesRepository.findByBarcode(medicine.getBarcode()))
                .thenReturn(Optional.of(medicine));

        Medicine result = this.medicineService.getMedicineByBarcode(1);
        assertEquals(medicine, result);
        verify(medicinesRepository).findByBarcode(medicine.getBarcode());
    }

    @Test
    void getMedicineByBarcode_NotFound(){
        Medicine medicine = Utils.createMedicineObj();

        when(medicinesRepository.findByBarcode(medicine.getBarcode()))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> this.medicineService.getMedicineByBarcode(1));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(medicinesRepository).findByBarcode(medicine.getBarcode());
    }

    // getMedicineDtoByBarcode
    @Test
    void getMedicineDtoByBarcode(){
        Medicine medicine = Utils.createMedicineObj();
        MedicineDto medicineDto = medicineMapper.medicineToMedicineDto(medicine, this.medicineImageRepository);

        when(medicinesRepository.findByBarcode(medicine.getBarcode()))
                .thenReturn(Optional.of(medicine));

        MedicineDto result = this.medicineService.getMedicineDtoByBarcode(1);
        assertEquals(medicineDto, result);
        verify(medicinesRepository).findByBarcode(medicine.getBarcode());
    }

    @Test
    void getMedicineDtoByBarcode_WithImage() throws IOException {
        Medicine medicine = Utils.createMedicineObj();
        MedicineImage medicineImage = Utils.createMedicineImageObj(medicine, true);

        when(medicinesRepository.findByBarcode(medicine.getBarcode()))
                .thenReturn(Optional.of(medicine));

        when(medicineImageRepository.findByMedicineBarcode(medicine.getBarcode()))
                .thenReturn(Optional.of(medicineImage));

        MedicineDto medicineDto = medicineMapper.medicineToMedicineDto(medicine, this.medicineImageRepository);

        MedicineDto result = this.medicineService.getMedicineDtoByBarcode(1);
        assertEquals(medicineDto, result);

        verify(medicinesRepository).findByBarcode(medicine.getBarcode());
        verify(medicineImageRepository, times(2)).findByMedicineBarcode(medicine.getBarcode());
    }

    // getImage
    @Test
    void getImage() throws IOException {
        Medicine medicine = Utils.createMedicineObj();
        MedicineImage medicineImage = Utils.createMedicineImageObj(medicine, true);

        when(medicineImageRepository.findByMedicineBarcode(medicine.getBarcode()))
                .thenReturn(Optional.of(medicineImage));

        byte[] result = this.medicineService.getImage(1);
        assertArrayEquals(Base64.getDecoder().decode(medicineImage.getImage()), result);
        verify(medicineImageRepository).findByMedicineBarcode(medicine.getBarcode());
    }

    @Test
    void getImage_WhenThereIsNoImage(){
        Medicine medicine = Utils.createMedicineObj();

        when(medicineImageRepository.findByMedicineBarcode(medicine.getBarcode()))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> this.medicineService.getImage(1));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(medicineImageRepository).findByMedicineBarcode(medicine.getBarcode());
    }

    // getAllMedicines
    @Test
    void getAllMedicines(){
        Medicine medicine = Utils.createMedicineObj();
        MedicineDto medicineDto = medicineMapper.medicineToMedicineDto(medicine, this.medicineImageRepository);
        Page<Medicine> page = new PageImpl<>( List.of(medicine) );

        PageRequest pageRequest = PageRequest.of(0, 10);

        when(medicinesRepository.findAll(pageRequest))
                .thenReturn(page);

        Page<MedicineDto> result = this.medicineService.getAllMedicines(pageRequest);
        assertEquals(medicineDto, result.getContent().get(0));
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        verify(medicinesRepository).findAll(pageRequest);
    }

    @Test
    void getAllMedicines_WithEmptyResults(){
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(medicinesRepository.findAll(pageRequest))
                .thenReturn( Page.empty() );

        Page<MedicineDto> result = this.medicineService.getAllMedicines(pageRequest);
        assertEquals(0, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        verify(medicinesRepository).findAll(pageRequest);
    }

    // search
    @Test
    void search(){
        Medicine medicine = Utils.createMedicineObj();
        MedicineDto medicineDto = medicineMapper.medicineToMedicineDto(medicine, this.medicineImageRepository);

        Page<Medicine> page = new PageImpl<>( List.of(medicine) );
        PageRequest pageRequest = PageRequest.of(0, 10);

        String searchTerm = "dumyrox";

        when(medicinesRepository.findByNameContainingIgnoreCaseOrDosageContainingIgnoreCaseOrInternationalCommonNameContainingIgnoreCaseOrFormContainingIgnoreCaseOrPackagingSizeContainingIgnoreCase(pageRequest, searchTerm, searchTerm, searchTerm, searchTerm, searchTerm))
                .thenReturn(page);

        Page<MedicineDto> result = this.medicineService.search(pageRequest, searchTerm);
        assertEquals(medicineDto, result.getContent().get(0));
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        verify(medicinesRepository).findByNameContainingIgnoreCaseOrDosageContainingIgnoreCaseOrInternationalCommonNameContainingIgnoreCaseOrFormContainingIgnoreCaseOrPackagingSizeContainingIgnoreCase(pageRequest, searchTerm, searchTerm, searchTerm, searchTerm, searchTerm);
    }

    @Test
    void search_WithIntForBarcode(){
        Medicine medicine = Utils.createMedicineObj();
        MedicineDto medicineDto = medicineMapper.medicineToMedicineDto(medicine, this.medicineImageRepository);

        Page<Medicine> page = new PageImpl<>( List.of(medicine) );
        PageRequest pageRequest = PageRequest.of(0, 10);

        String searchTerm = "1";
        int barcode = Integer.parseInt(searchTerm);

        when(medicinesRepository.findByNameContainingIgnoreCaseOrDosageContainingIgnoreCaseOrInternationalCommonNameContainingIgnoreCaseOrFormContainingIgnoreCaseOrPackagingSizeContainingIgnoreCaseOrBarcode(pageRequest, searchTerm, searchTerm, searchTerm, searchTerm, searchTerm, barcode))
                .thenReturn(page);

        Page<MedicineDto> result = this.medicineService.search(pageRequest, searchTerm);
        assertEquals(medicineDto, result.getContent().get(0));
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        verify(medicinesRepository).findByNameContainingIgnoreCaseOrDosageContainingIgnoreCaseOrInternationalCommonNameContainingIgnoreCaseOrFormContainingIgnoreCaseOrPackagingSizeContainingIgnoreCaseOrBarcode(pageRequest, searchTerm, searchTerm, searchTerm, searchTerm, searchTerm, barcode);
    }

    @Test
    void search_WhenDoesntFindResults(){
        PageRequest pageRequest = PageRequest.of(0, 10);

        String searchTerm = "nuno";

        when(medicinesRepository.findByNameContainingIgnoreCaseOrDosageContainingIgnoreCaseOrInternationalCommonNameContainingIgnoreCaseOrFormContainingIgnoreCaseOrPackagingSizeContainingIgnoreCase(pageRequest, searchTerm, searchTerm, searchTerm, searchTerm, searchTerm))
                .thenReturn(Page.empty());

        Page<MedicineDto> result = this.medicineService.search(pageRequest, searchTerm);
        assertEquals(0, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        verify(medicinesRepository).findByNameContainingIgnoreCaseOrDosageContainingIgnoreCaseOrInternationalCommonNameContainingIgnoreCaseOrFormContainingIgnoreCaseOrPackagingSizeContainingIgnoreCase(pageRequest, searchTerm, searchTerm, searchTerm, searchTerm, searchTerm);
    }

    // addImage
    @Test
    void addImage() throws IOException {
        Medicine medicine = Utils.createMedicineObj();
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "image",
                "image.png",
                MediaType.TEXT_PLAIN_VALUE,
                "image".getBytes()
            );

        MedicineImage medicineImage = Utils.createMedicineImageObj(medicine, true);

        when(this.medicinesRepository.findByBarcode( medicine.getBarcode() ))
                .thenReturn(Optional.of(medicine));

        when(dateTime.getDate())
                .thenReturn(medicineImage.getCreated());

        this.medicineService.addImage(medicine.getBarcode(), mockMultipartFile);

        ArgumentCaptor<MedicineImage> medicineImageArgumentCaptor = ArgumentCaptor.forClass(MedicineImage.class);
        verify(medicineImageRepository).save(medicineImageArgumentCaptor.capture());

        MedicineImage result = medicineImageArgumentCaptor.getValue();
        assertEquals(medicineImage.getMedicine(), result.getMedicine());
        assertEquals(medicineImage.getImage(), result.getImage());
    }

    // deleteImage
    @Test
    void deleteMedicineImage() throws IOException {
        Medicine medicine = Utils.createMedicineObj();
        MedicineImage medicineImage = Utils.createMedicineImageObj(medicine, true);

        when(this.medicineImageRepository.findByMedicineBarcode(medicine.getBarcode()))
                .thenReturn(Optional.of(medicineImage));

        this.medicineService.deleteImage(medicine.getBarcode());
        verify(medicineImageRepository).delete(medicineImage);
    }

    @Test
    void deleteMedicineImage_NotFound(){
        Integer barcode = 1;

        when(this.medicineImageRepository.findByMedicineBarcode(barcode))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> this.medicineService.deleteImage(barcode));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(medicineImageRepository, never()).save(any(MedicineImage.class));
    }

}
