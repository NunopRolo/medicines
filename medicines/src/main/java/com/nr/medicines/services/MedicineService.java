package com.nr.medicines.services;

import com.nr.medicines.mappers.MedicineMapper;
import com.nr.medicines.models.Medicine;
import com.nr.medicines.models.MedicineImage;
import com.nr.medicines.models.dto.NotificationDto;
import com.nr.medicines.models.dto.MedicineDto;
import com.nr.medicines.repositories.MedicineImageRepository;
import com.nr.medicines.repositories.MedicinesRepository;
import com.nr.medicines.utils.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;

@Service
public class MedicineService {
    private final MedicinesRepository medicinesRepository;
    private final MedicineImageRepository medicineImageRepository;
    private final MedicineMapper medicineMapper;
    private final NotificationSystem notificationSystem;
    private final DateTime dateTime;
    private static final String MEDICINES_NOT_FOUND = "Medicine Not Found";


    public MedicineService(
            MedicinesRepository medicinesRepository,
            MedicineImageRepository medicineImageRepository,
            MedicineMapper mapper,
            NotificationSystem notificationSystem,
            DateTime dateTime
    ){
        this.medicinesRepository = medicinesRepository;
        this.medicineImageRepository = medicineImageRepository;
        this.medicineMapper = mapper;
        this.notificationSystem = notificationSystem;
        this.dateTime = dateTime;
    }

    public MedicineDto getMedicineDtoByBarcode(Integer barcode){
        Medicine medicine = getMedicineByBarcode(barcode);

        return medicineMapper.medicineToMedicineDto(medicine, this.medicineImageRepository);
    }

    public Medicine getMedicineByBarcode(Integer barcode){
        Optional<Medicine> medicineOptional = this.medicinesRepository.findByBarcode(barcode);
        if(medicineOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MEDICINES_NOT_FOUND);
        }

        return medicineOptional.get();
    }

    public Page<MedicineDto> getAllMedicines(Pageable pageable){
        return this.medicinesRepository.findAll(pageable).map(
                medicine -> medicineMapper.medicineToMedicineDto(medicine, this.medicineImageRepository)
        );
    }

    public Page<MedicineDto> search(Pageable pageable, String term){
        term = term.toLowerCase();
        try{
            int barcode = Integer.parseInt(term);
            return this.medicinesRepository
                    .findByNameContainingIgnoreCaseOrDosageContainingIgnoreCaseOrInternationalCommonNameContainingIgnoreCaseOrFormContainingIgnoreCaseOrPackagingSizeContainingIgnoreCaseOrBarcode(pageable, term, term, term, term, term, barcode)
                    .map(medicine -> medicineMapper.medicineToMedicineDto(medicine, this.medicineImageRepository));
        } catch (NumberFormatException numberFormatException){
            return this.medicinesRepository
                    .findByNameContainingIgnoreCaseOrDosageContainingIgnoreCaseOrInternationalCommonNameContainingIgnoreCaseOrFormContainingIgnoreCaseOrPackagingSizeContainingIgnoreCase(pageable, term, term, term, term, term)
                    .map(medicine -> medicineMapper.medicineToMedicineDto(medicine, this.medicineImageRepository));
        }

    }

    public String addImage(Integer medicineBarcode, MultipartFile file) throws IOException {
        Medicine medicine = this.getMedicineByBarcode(medicineBarcode);

        byte[] image = Base64.getEncoder().encode(file.getBytes());
        String base64Image = new String(image);

        MedicineImage medicineImage = MedicineImage.builder()
                .medicine(medicine)
                .image(base64Image)
                .created(dateTime.getDate())
                .build();

        this.medicineImageRepository.save(medicineImage);
        return base64Image;
    }

    public byte[] getImage(Integer medicineBarcode){
        Optional<MedicineImage> medicineImageOptional
                = this.medicineImageRepository.findByMedicineBarcode(medicineBarcode);
        if(medicineImageOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Medication Image Not Found");
        }

        MedicineImage medicineImage = medicineImageOptional.get();

        return Base64.getDecoder().decode( medicineImage.getImage().getBytes() );
    }

    public void deleteImage(Integer medicineBarcode){
        Optional<MedicineImage> medicineImageOptional
                = this.medicineImageRepository.findByMedicineBarcode(medicineBarcode);
        if(medicineImageOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Medication Image Not Found");
        }

        this.medicineImageRepository.delete(medicineImageOptional.get());
    }

    public void testNotifications(){
        NotificationDto notificationDto = NotificationDto.builder()
                .title("Test")
                .message("Body Test")
                .build();
        this.notificationSystem.sendNotification(notificationDto);
    }
}
