package com.nr.medicines.controllers;

import com.nr.medicines.models.DayPeriod;
import com.nr.medicines.models.Medication;
import com.nr.medicines.models.PdfContentEnum;
import com.nr.medicines.models.dto.DayPeriodRequest;
import com.nr.medicines.models.dto.MedicationRequest;
import com.nr.medicines.models.dto.MedicationResult;
import com.nr.medicines.models.dto.TemporaryMedicationRequest;
import com.nr.medicines.services.MedicationService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/medication")
public class MedicationController {
    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @PostMapping(value = "/add")
    public Medication addMedication(
            @RequestBody MedicationRequest medicationRequest
    ){
        return this.medicationService.addMedication(medicationRequest);
    }

    @PostMapping(value = "/temporary-medication/add")
    public Medication addTemporaryMedication(
            @RequestBody TemporaryMedicationRequest temporaryMedicationRequest
    ){
        return this.medicationService.addTemporaryMedication(temporaryMedicationRequest);
    }

    @PutMapping(value = "/update/{medicationUuid}")
    public void updateMedication(
            @PathVariable String medicationUuid,
            @RequestBody MedicationRequest medicationRequest
    ){
        this.medicationService.updateMedication(medicationRequest, UUID.fromString(medicationUuid));
    }

    @GetMapping(value = "/get/{personUuid}")
    public List<MedicationResult> getMedication(
            @PathVariable String personUuid
    ){
        return this.medicationService.getAllMedications(UUID.fromString(personUuid));
    }

    @DeleteMapping(value = "/delete/{medicationUuid}")
    public void deleteMedication(
            @PathVariable String medicationUuid
    ){
        this.medicationService.deleteMedication(UUID.fromString(medicationUuid));
    }

    @DeleteMapping(value = "/temporary-medication/delete/{temporaryMedicationUuid}")
    public void deleteTemporaryMedication(
            @PathVariable String temporaryMedicationUuid
    ){
        this.medicationService.deleteTemporaryMedication(UUID.fromString(temporaryMedicationUuid));
    }

    @GetMapping(value = "/pdf/{pdfContent}/{personUuid}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody Resource pdf(
            @PathVariable String personUuid,
            @PathVariable PdfContentEnum pdfContent
    ){
        return new ByteArrayResource(this.medicationService.pdf(UUID.fromString(personUuid), pdfContent).toByteArray());
    }

    @GetMapping(value = "/day-periods")
    public List<DayPeriod> getMedicationDayPeriods(){
        return this.medicationService.getDayPeriods();
    }

    @PostMapping(value = "/day-period")
    public void addDayPeriod(@RequestBody DayPeriodRequest dayPeriodRequest){
        this.medicationService.addDayPeriod(dayPeriodRequest);
    }

    @DeleteMapping(value = "/day-period/{dayPeriodId}")
    public void deleteDayPeriod(@PathVariable Integer dayPeriodId){
        this.medicationService.deleteDayPeriod(dayPeriodId);
    }

}
