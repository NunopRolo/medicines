package com.nr.medicines.services;

import com.nr.medicines.mappers.MedicationMapper;
import com.nr.medicines.models.*;
import com.nr.medicines.models.dto.*;
import com.nr.medicines.repositories.DayPeriodsRepository;
import com.nr.medicines.repositories.MedicineImageRepository;
import com.nr.medicines.repositories.MedicationRepository;
import com.nr.medicines.repositories.TemporaryMedicationRepository;
import com.nr.medicines.utils.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.util.*;
import java.util.List;

@Service
public class MedicationService {
    private final MedicationRepository medicationRepository;
    private final DayPeriodsRepository dayPeriodsRepository;
    private final MedicineService medicineService;
    private final PdfService pdfService;
    private final MedicineImageRepository medicineImageRepository;
    private final MedicationMapper medicationMapper;
    private final PersonsService personsService;
    private final TemporaryMedicationRepository temporaryMedicationRepository;
    private final DateTime dateTime;

    public MedicationService(
            MedicationRepository medicationRepository,
            MedicineService medicineService,
            DayPeriodsRepository dayPeriodsRepository,
            PdfService pdfService,
            MedicineImageRepository medicineImageRepository,
            MedicationMapper medicationMapper,
            PersonsService personsService,
            TemporaryMedicationRepository temporaryMedicationRepository,
            DateTime dateTime
    ) {
        this.medicationRepository = medicationRepository;
        this.medicineService = medicineService;
        this.dayPeriodsRepository = dayPeriodsRepository;
        this.pdfService = pdfService;
        this.medicineImageRepository = medicineImageRepository;
        this.medicationMapper = medicationMapper;
        this.personsService = personsService;
        this.temporaryMedicationRepository = temporaryMedicationRepository;
        this.dateTime = dateTime;
    }

    public Medication addMedication(MedicationRequest medicationRequest){
        Optional<DayPeriod> dayPeriod = this.dayPeriodsRepository.findById(medicationRequest.getDayPeriodId());
        if(dayPeriod.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Day Period Not Found");
        }
        Medicine medicine = this.medicineService.getMedicineByBarcode(medicationRequest.getBarcode());

        Person person = this.personsService.getPersonByUuid(UUID.fromString(medicationRequest.getPersonUuid()));

        Medication medication = Medication.builder()
                .medicine(medicine)
                .dayPeriod(dayPeriod.get())
                .created(dateTime.getDate())
                .observations(medicationRequest.getObservations())
                .person(person)
                .build();

        return this.medicationRepository.save(medication);
    }

    @Transactional
    public Medication addTemporaryMedication(TemporaryMedicationRequest temporaryMedicationRequest){
        Medication medication = addMedication(temporaryMedicationRequest.getMedication());

        TemporaryMedication temporaryMedication = TemporaryMedication.builder()
                .medication(medication)
                .days(temporaryMedicationRequest.getDays())
                .startDate(temporaryMedicationRequest.getStartDate())
                .build();
        this.temporaryMedicationRepository.save(temporaryMedication);

        return medication;
    }

    public List<MedicationResult> getAllMedications(UUID personUuid){
        List<DayPeriod> dayPeriods = this.dayPeriodsRepository.findByOrderByOrderAsc();
        List<MedicationResult> medicationResults = new ArrayList<>();

        Person person = this.personsService.getPersonByUuid(personUuid);

        for(DayPeriod dayPeriod : dayPeriods){
            List<Medication> medicationList = this.medicationRepository.findByPersonAndDayPeriodOrderByCreated(person, dayPeriod);
            List<MedicationDto> medicationDtoList = medicationMapper
                    .medicationListToMedicationDtoList(medicationList, this.medicineImageRepository);

            List<TemporaryMedication> findTemporaryMedications = this.temporaryMedicationRepository.findTemporaryMedications(person, dayPeriod);
            List<TemporaryMedicationDto> temporaryMedicationDtoList = medicationMapper
                    .temporaryMedicationListToTemporaryMedicationDtoList(findTemporaryMedications, this.medicineImageRepository);

            MedicationResult medicationResult = MedicationResult.builder()
                    .dayPeriod(dayPeriod)
                    .medications(medicationDtoList)
                    .temporaryMedications(temporaryMedicationDtoList)
                    .build();

            medicationResults.add(medicationResult);
        }

        return medicationResults;
    }

    public void updateMedication(MedicationRequest medicationRequest, UUID medicationUuid){
        Medication medication = getMedication(medicationUuid);
        medication.setObservations(medicationRequest.getObservations());

        this.medicationRepository.save(medication);
    }

    public void deleteMedication(UUID medicationUuid){
        Medication medication = getMedication(medicationUuid);
        this.medicationRepository.delete(medication);
    }

    @Transactional
    public void deleteTemporaryMedication(UUID temporaryMedicationUuid){
        Optional<TemporaryMedication> temporaryMedication = this.temporaryMedicationRepository.findById(temporaryMedicationUuid);
        if (temporaryMedication.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Temporary Medication Not Found");
        }

        this.temporaryMedicationRepository.delete(temporaryMedication.get());
        this.medicationRepository.delete(temporaryMedication.get().getMedication());
    }

    public ByteArrayOutputStream pdf(UUID personUuid, PdfContentEnum pdfContent){
        try {
            Person person = this.personsService.getPersonByUuid(personUuid);
            return this.pdfService.generateHtmlToPdf(person, pdfContent);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating PDF");
        }
    }

    public List<DayPeriod> getDayPeriods(){
        return this.dayPeriodsRepository.findByOrderByOrderAsc();
    }

    public void addDayPeriod(DayPeriodRequest dayPeriodRequest) {
        DayPeriod dayPeriod = DayPeriod.builder()
                .description(dayPeriodRequest.getName())
                .hour(dayPeriodRequest.getHour())
                .build();

        this.dayPeriodsRepository.save(dayPeriod);
    }

    private Medication getMedication(UUID medicationUuid){
        Optional<Medication> medicationOptional = this.medicationRepository.findById(medicationUuid);
        if(medicationOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Medication Not Found");
        }

        return medicationOptional.get();
    }

    public void deleteDayPeriod(Integer dayPeriodId){
        Optional<DayPeriod> dayPeriodOptional = this.dayPeriodsRepository.findById(dayPeriodId);
        if(dayPeriodOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Day Period not found");
        }

        this.dayPeriodsRepository.delete(dayPeriodOptional.get());
    }
}
