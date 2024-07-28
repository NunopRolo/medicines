package com.nr.medicines.services;

import com.nr.medicines.Utils;
import com.nr.medicines.models.*;
import com.nr.medicines.repositories.DayPeriodsRepository;
import com.nr.medicines.repositories.MedicationRepository;
import com.nr.medicines.repositories.TemporaryMedicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class PdfServiceTest {
    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private DayPeriodsRepository dayPeriodsRepository;

    @Mock
    private TemporaryMedicationRepository temporaryMedicationRepository;

    @InjectMocks
    private PdfService pdfService;

    @Test
    void generateHtmlToPdf() throws Exception {
        TemporaryMedication temporaryMedication = Utils.createTemporaryMedicationObj();
        Medication medication = Utils.createMedicationObj();
        Person person = medication.getPerson();
        DayPeriod dayPeriod = medication.getDayPeriod();

        when(dayPeriodsRepository.findAll())
                .thenReturn(List.of(dayPeriod));

        when(medicationRepository.findByPersonAndDayPeriodOrderByCreated(person, dayPeriod))
                .thenReturn(List.of(medication));

        when(temporaryMedicationRepository.findTemporaryMedications(person, dayPeriod))
                .thenReturn(List.of(temporaryMedication));

        pdfService.generateHtmlToPdf(person, PdfContentEnum.INFO);
        verify(dayPeriodsRepository).findAll();
        verify(medicationRepository).findByPersonAndDayPeriodOrderByCreated(person, dayPeriod);
        verify(temporaryMedicationRepository).findTemporaryMedications(person, dayPeriod);
    }

    @Test
    void generateHtmlToPdf_WithoutObservations() throws Exception {
        TemporaryMedication temporaryMedication = Utils.createTemporaryMedicationObj();
        temporaryMedication.getMedication().setObservations(null);
        Medication medication = Utils.createMedicationObj();
        medication.setObservations(null);
        Person person = medication.getPerson();
        DayPeriod dayPeriod = medication.getDayPeriod();

        when(dayPeriodsRepository.findAll())
                .thenReturn(List.of(dayPeriod));

        when(medicationRepository.findByPersonAndDayPeriodOrderByCreated(person, dayPeriod))
                .thenReturn(List.of(medication));

        when(temporaryMedicationRepository.findTemporaryMedications(person, dayPeriod))
                .thenReturn(List.of(temporaryMedication));

        pdfService.generateHtmlToPdf(person, PdfContentEnum.INFO);
        verify(dayPeriodsRepository).findAll();
        verify(medicationRepository).findByPersonAndDayPeriodOrderByCreated(person, dayPeriod);
        verify(temporaryMedicationRepository).findTemporaryMedications(person, dayPeriod);
    }

    @Test
    void generateHtmlToPdf_WithImages() throws Exception {
        TemporaryMedication temporaryMedication = Utils.createTemporaryMedicationObj();
        Medication medication = Utils.createMedicationObj();
        Person person = medication.getPerson();
        DayPeriod dayPeriod = medication.getDayPeriod();

        when(dayPeriodsRepository.findAll())
                .thenReturn(List.of(dayPeriod));

        when(medicationRepository.findByPersonAndDayPeriodOrderByCreated(person, dayPeriod))
                .thenReturn(List.of(medication));

        when(temporaryMedicationRepository.findTemporaryMedications(person, dayPeriod))
                .thenReturn(List.of(temporaryMedication));

        pdfService.generateHtmlToPdf(person, PdfContentEnum.IMAGE);
        verify(dayPeriodsRepository).findAll();
        verify(medicationRepository).findByPersonAndDayPeriodOrderByCreated(person, dayPeriod);
        verify(temporaryMedicationRepository).findTemporaryMedications(person, dayPeriod);
    }
}
