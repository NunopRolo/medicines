package com.nr.medicines.services;

import com.nr.medicines.mappers.MedicationMapper;
import com.nr.medicines.mappers.MedicationMapperImpl;
import com.nr.medicines.mappers.MedicineMapper;
import com.nr.medicines.mappers.MedicineMapperImpl;
import com.nr.medicines.models.DayPeriod;
import com.nr.medicines.models.Medication;
import com.nr.medicines.models.PdfContentEnum;
import com.nr.medicines.models.TemporaryMedication;
import com.nr.medicines.models.dto.*;
import com.nr.medicines.repositories.DayPeriodsRepository;
import com.nr.medicines.repositories.MedicationRepository;
import com.nr.medicines.repositories.MedicineImageRepository;
import com.nr.medicines.repositories.TemporaryMedicationRepository;
import com.nr.medicines.utils.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static com.nr.medicines.Utils.*;

import java.io.ByteArrayOutputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicationServiceTest {
    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private DayPeriodsRepository dayPeriodsRepository;

    @Mock
    private MedicineService medicineService;

    @Mock
    private PdfService pdfService;

    @Mock
    private MedicineImageRepository medicineImageRepository;

    private final MedicineMapper medicineMapper = new MedicineMapperImpl();
    private final MedicationMapper medicationMapper = new MedicationMapperImpl(medicineMapper);

    @Mock
    private PersonsService personsService;

    @Mock
    private TemporaryMedicationRepository temporaryMedicationRepository;

    @Mock
    private DateTime dateTime;

    @InjectMocks
    private MedicationService medicationService;

    @BeforeEach
    void setUp(){
        this.medicationService = new MedicationService(medicationRepository, medicineService, dayPeriodsRepository, pdfService, medicineImageRepository, medicationMapper, personsService, temporaryMedicationRepository, dateTime);
    }

    // addMedication
    @Test
    void addMedication(){
        Medication medication = createMedicationObj();

        MedicationRequest medicationRequest = new MedicationRequest();
        medicationRequest.setBarcode(medication.getMedicine().getBarcode());
        medicationRequest.setObservations(medication.getObservations());
        medicationRequest.setPersonUuid(medication.getPerson().getPersonUuid().toString());
        medicationRequest.setDayPeriodId(medication.getDayPeriod().getId());

        when(medicineService.getMedicineByBarcode(medicationRequest.getBarcode()))
                .thenReturn(medication.getMedicine());

        when(dayPeriodsRepository.findById(medicationRequest.getDayPeriodId()))
                .thenReturn(Optional.of(medication.getDayPeriod()));

        when(personsService.getPersonByUuid(UUID.fromString(medicationRequest.getPersonUuid())))
                .thenReturn(medication.getPerson());

        medicationService.addMedication(medicationRequest);

        ArgumentCaptor<Medication> medicationArgumentCaptor = ArgumentCaptor.forClass(Medication.class);
        verify(medicationRepository).save(medicationArgumentCaptor.capture());

        Medication result = medicationArgumentCaptor.getValue();
        assertEquals(medication.getMedicine(), result.getMedicine());
        assertEquals(medication.getDayPeriod(), result.getDayPeriod());
        assertEquals(medication.getPerson(), result.getPerson());
    }

    @Test
    void addMedication_DayPeriodNotFound(){
        Medication medication = createMedicationObj();

        MedicationRequest medicationRequest = new MedicationRequest();
        medicationRequest.setBarcode(medication.getMedicine().getBarcode());
        medicationRequest.setObservations(medication.getObservations());
        medicationRequest.setPersonUuid(medication.getPerson().getPersonUuid().toString());
        medicationRequest.setDayPeriodId(medication.getDayPeriod().getId());

        when(dayPeriodsRepository.findById(medicationRequest.getDayPeriodId()))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> medicationService.addMedication(medicationRequest));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(dayPeriodsRepository).findById(medicationRequest.getDayPeriodId());
        verify(medicationRepository, never()).save(any(Medication.class));
    }

    // addTemporaryMedication
    @Test
    void addTemporaryMedication(){
        Medication medication = createMedicationObj();

        Date date = new Date();

        MedicationRequest medicationRequest = new MedicationRequest();
        medicationRequest.setBarcode(medication.getMedicine().getBarcode());
        medicationRequest.setObservations("obs");
        medicationRequest.setPersonUuid(String.valueOf(medication.getPerson().getPersonUuid()));
        medicationRequest.setDayPeriodId(1);

        TemporaryMedicationRequest temporaryMedicationRequest = new TemporaryMedicationRequest();
        temporaryMedicationRequest.setMedication(medicationRequest);
        temporaryMedicationRequest.setDays(5);
        temporaryMedicationRequest.setStartDate(date);

        when(medicineService.getMedicineByBarcode(medicationRequest.getBarcode()))
                .thenReturn(medication.getMedicine());

        when(dayPeriodsRepository.findById(medicationRequest.getDayPeriodId()))
                .thenReturn(Optional.of(medication.getDayPeriod()));

        when(personsService.getPersonByUuid(UUID.fromString(medicationRequest.getPersonUuid())))
                .thenReturn(medication.getPerson());

        when(dateTime.getDate())
                .thenReturn(date);

        doReturn(medication).when(medicationRepository).save(any(Medication.class));

        medicationService.addTemporaryMedication(temporaryMedicationRequest);

        ArgumentCaptor<TemporaryMedication> temporaryMedicationArgumentCaptor = ArgumentCaptor.forClass(TemporaryMedication.class);
        verify(temporaryMedicationRepository).save(temporaryMedicationArgumentCaptor.capture());

        TemporaryMedication result = temporaryMedicationArgumentCaptor.getValue();
        assertEquals(medicationRequest.getBarcode(), result.getMedication().getMedicine().getBarcode());
    }

    // getAllMedications
    @Test
    void getAllMedications(){
        Medication medication = createMedicationObj();
        List<MedicationDto> medicationResult = medicationMapper.medicationListToMedicationDtoList(List.of(medication), this.medicineImageRepository);

        TemporaryMedication temporaryMedication = createTemporaryMedicationObj();
        List<TemporaryMedicationDto> temporaryMedicationDtoResult = medicationMapper.temporaryMedicationListToTemporaryMedicationDtoList(List.of(temporaryMedication), this.medicineImageRepository);

        when(dayPeriodsRepository.findByOrderByOrderAsc())
                .thenReturn(List.of(medication.getDayPeriod()));

        when(personsService.getPersonByUuid(medication.getPerson().getPersonUuid()))
                .thenReturn(medication.getPerson());

        when(medicationRepository.findByPersonAndDayPeriodOrderByCreated(medication.getPerson(), medication.getDayPeriod()))
                .thenReturn(List.of(medication));

        when(temporaryMedicationRepository.findTemporaryMedications(medication.getPerson(), medication.getDayPeriod()))
                .thenReturn(List.of(temporaryMedication));

        List<MedicationResult> result = medicationService.getAllMedications(medication.getPerson().getPersonUuid());
        assertEquals(result.get(0).getMedications().get(0), medicationResult.get(0));
        assertEquals(result.get(0).getTemporaryMedications().get(0), temporaryMedicationDtoResult.get(0));
        verify(dayPeriodsRepository).findByOrderByOrderAsc();
        verify(medicationRepository).findByPersonAndDayPeriodOrderByCreated(medication.getPerson(), medication.getDayPeriod());
        verify(temporaryMedicationRepository).findTemporaryMedications(medication.getPerson(), medication.getDayPeriod());
    }

    @Test
    void getAllMedications_NullResponse(){
        Medication medication = createMedicationObj();

        when(dayPeriodsRepository.findByOrderByOrderAsc())
                .thenReturn(List.of(medication.getDayPeriod()));

        when(personsService.getPersonByUuid(medication.getPerson().getPersonUuid()))
                .thenReturn(medication.getPerson());

        when(medicationRepository.findByPersonAndDayPeriodOrderByCreated(medication.getPerson(), medication.getDayPeriod()))
                .thenReturn(null);

        when(temporaryMedicationRepository.findTemporaryMedications(medication.getPerson(), medication.getDayPeriod()))
                .thenReturn(null);

        List<MedicationResult> result = medicationService.getAllMedications(medication.getPerson().getPersonUuid());
        assertNull(result.get(0).getMedications());
        assertNull(result.get(0).getTemporaryMedications());
        verify(dayPeriodsRepository).findByOrderByOrderAsc();
        verify(medicationRepository).findByPersonAndDayPeriodOrderByCreated(medication.getPerson(), medication.getDayPeriod());
        verify(temporaryMedicationRepository).findTemporaryMedications(medication.getPerson(), medication.getDayPeriod());
    }

    @Test
    void getAllMedications_NullListElements(){
        Medication medication = createMedicationObj();

        ArrayList<Medication> medicationArrayList = new ArrayList<>();
        medicationArrayList.add(null);

        ArrayList<TemporaryMedication> temporaryMedicationArrayList = new ArrayList<>();
        temporaryMedicationArrayList.add(null);

        when(dayPeriodsRepository.findByOrderByOrderAsc())
                .thenReturn(List.of(medication.getDayPeriod()));

        when(personsService.getPersonByUuid(medication.getPerson().getPersonUuid()))
                .thenReturn(medication.getPerson());

        when(medicationRepository.findByPersonAndDayPeriodOrderByCreated(medication.getPerson(), medication.getDayPeriod()))
                .thenReturn(medicationArrayList);

        when(temporaryMedicationRepository.findTemporaryMedications(medication.getPerson(), medication.getDayPeriod()))
                .thenReturn(temporaryMedicationArrayList);

        List<MedicationResult> result = medicationService.getAllMedications(medication.getPerson().getPersonUuid());
        assertNull(result.get(0).getMedications().get(0));
        assertNull(result.get(0).getTemporaryMedications().get(0));
        verify(dayPeriodsRepository).findByOrderByOrderAsc();
        verify(medicationRepository).findByPersonAndDayPeriodOrderByCreated(medication.getPerson(), medication.getDayPeriod());
        verify(temporaryMedicationRepository).findTemporaryMedications(medication.getPerson(), medication.getDayPeriod());
    }

    // updateMedication
    @Test
    void updateMedication(){
        Medication medication = createMedicationObj();
        MedicationRequest medicationRequest = new MedicationRequest();
        medicationRequest.setDayPeriodId(medication.getDayPeriod().getId());
        medicationRequest.setBarcode(medication.getMedicine().getBarcode());
        medicationRequest.setPersonUuid(String.valueOf(medication.getPerson().getPersonUuid()));
        medicationRequest.setObservations(medication.getObservations()+"changed");

        when(medicationRepository.findById(medication.getUuid()))
                .thenReturn(Optional.of(medication));

        medicationService.updateMedication(medicationRequest, medication.getUuid());
        ArgumentCaptor<Medication> medicationArgumentCaptor = ArgumentCaptor.forClass(Medication.class);
        verify(medicationRepository).save(medicationArgumentCaptor.capture());

        Medication result = medicationArgumentCaptor.getValue();
        assertEquals(medicationRequest.getObservations(), result.getObservations());
    }

    // deleteMedication
    @Test
    void deleteMedication(){
        Medication medication = createMedicationObj();

        when(medicationRepository.findById(medication.getUuid()))
                .thenReturn(Optional.of(medication));

        medicationService.deleteMedication(medication.getUuid());
        verify(medicationRepository).delete(medication);
        verify(medicationRepository).findById(medication.getUuid());
    }

    @Test
    void deleteMedication_NotFound(){
        UUID uuid = UUID.randomUUID();

        when(medicationRepository.findById(uuid))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> medicationService.deleteMedication(uuid));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(medicationRepository, never()).delete(any(Medication.class));
        verify(medicationRepository).findById(uuid);
    }

    // deleteTemporaryMedication
    @Test
    void deleteTemporaryMedication(){
        TemporaryMedication temporaryMedication = createTemporaryMedicationObj();

        when(temporaryMedicationRepository.findById(temporaryMedication.getUuid()))
                .thenReturn(Optional.of(temporaryMedication));

        medicationService.deleteTemporaryMedication(temporaryMedication.getUuid());
        verify(medicationRepository).delete(temporaryMedication.getMedication());
        verify(temporaryMedicationRepository).delete(temporaryMedication);
        verify(temporaryMedicationRepository).findById(temporaryMedication.getUuid());
    }

    @Test
    void deleteTemporaryMedication_NotFound(){
        UUID uuid = UUID.randomUUID();

        when(temporaryMedicationRepository.findById(uuid))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> medicationService.deleteTemporaryMedication(uuid));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(temporaryMedicationRepository, never()).delete(any(TemporaryMedication.class));
        verify(temporaryMedicationRepository).findById(uuid);
    }

    // getDayPeriods
    @Test
    void getDayPeriods(){
        DayPeriod dayPeriod = createDayPeriodObj();

        when(dayPeriodsRepository.findByOrderByOrderAsc())
                .thenReturn(List.of(dayPeriod));

        List<DayPeriod> result = medicationService.getDayPeriods();
        assertEquals(dayPeriod, result.get(0));
        verify(dayPeriodsRepository).findByOrderByOrderAsc();
    }

    // addDayPeriod
    @Test
    void addDayPeriod(){
        DayPeriod dayPeriod = createDayPeriodObj();

        DayPeriodRequest dayPeriodRequest = new DayPeriodRequest();
        dayPeriodRequest.setName(dayPeriod.getDescription());
        dayPeriodRequest.setHour(dayPeriod.getHour());

        medicationService.addDayPeriod(dayPeriodRequest);

        ArgumentCaptor<DayPeriod> argumentCaptor = ArgumentCaptor.forClass(DayPeriod.class);
        verify(dayPeriodsRepository).save(argumentCaptor.capture());

        DayPeriod result = argumentCaptor.getValue();
        assertEquals(dayPeriod.getHour(), result.getHour());
        assertEquals(dayPeriod.getDescription(), result.getDescription());
    }

    // deleteDayPeriod
    @Test
    void deleteDayPeriod(){
        DayPeriod dayPeriod = createDayPeriodObj();

        when(dayPeriodsRepository.findById(dayPeriod.getId()))
                .thenReturn(Optional.of(dayPeriod));

        medicationService.deleteDayPeriod(dayPeriod.getId());
        verify(dayPeriodsRepository).delete(dayPeriod);
        verify(dayPeriodsRepository).findById(dayPeriod.getId());
    }

    @Test
    void deleteDayPeriod_NotFound(){
        Integer id = 1;

        when(dayPeriodsRepository.findById(id))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> medicationService.deleteDayPeriod(id));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(dayPeriodsRepository, never()).delete(any(DayPeriod.class));
    }

    // pdf
    @Test
    void pdf() throws Exception {
        Medication medication = createMedicationObj();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        when(personsService.getPersonByUuid(medication.getPerson().getPersonUuid()))
                .thenReturn(medication.getPerson());

        when(pdfService.generateHtmlToPdf(medication.getPerson(), PdfContentEnum.INFO))
                .thenReturn(byteArrayOutputStream);

        ByteArrayOutputStream result = medicationService.pdf(medication.getPerson().getPersonUuid(), PdfContentEnum.INFO);
        assertEquals(byteArrayOutputStream, result);
    }

    @Test
    void pdf_Error() throws Exception {
        Medication medication = createMedicationObj();

        when(personsService.getPersonByUuid(medication.getPerson().getPersonUuid()))
                .thenReturn(medication.getPerson());

        when(pdfService.generateHtmlToPdf(medication.getPerson(), PdfContentEnum.INFO))
                .thenThrow(Exception.class);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> medicationService.pdf(medication.getPerson().getPersonUuid(), PdfContentEnum.INFO));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }
}
