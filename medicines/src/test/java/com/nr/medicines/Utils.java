package com.nr.medicines;

import com.nr.medicines.models.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Utils {

    public static Medicine createMedicineObj(){
        Medicine medicine = new Medicine();
        medicine.setBarcode(1);
        medicine.setName("Dumyrox");
        medicine.setDosage("50 mg");
        medicine.setGeneric(false);
        medicine.setInternationalCommonName("Fluvoxamina");
        medicine.setForm("Comprimido revestido por película");
        medicine.setPackagingSize("Blister - 60 unidade(s)");

        return medicine;
    }

    public static MedicineImage createMedicineImageObj(Medicine medicine, boolean mocked) throws IOException {
        MedicineImage medicineImage = new MedicineImage();
        medicineImage.setMedicineImageUuid(UUID.randomUUID());
        medicineImage.setMedicine(medicine);
        medicineImage.setCreated(new Date());

        if(mocked){
            medicineImage.setImage(Base64.getEncoder().encodeToString("image".getBytes()));
        }else{
            File imgPath = new File("src/main/resources/logo.png");
            BufferedImage bufferedImage = ImageIO.read(imgPath);
            WritableRaster raster = bufferedImage .getRaster();
            DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

            medicineImage.setImage(Base64.getEncoder().encodeToString(data.getData()));
        }

        return medicineImage;
    }

    public static Person createPersonObj(){
        Person person = new Person();
        person.setPersonUuid(UUID.randomUUID());
        person.setName("name");
        return person;
    }

    public static Stock createStockObj(){
        Stock stock = new Stock();
        stock.setUuid(UUID.randomUUID());
        stock.setCreated(new Date());
        stock.setMedicine( createMedicineObj() );
        stock.setPerson( createPersonObj() );
        stock.setValidityEndDate( new Date() );
        return stock;
    }

    public static Medication createMedicationObj(){
        Medication medication = new Medication();
        medication.setUuid(UUID.randomUUID());
        medication.setCreated(new Date());
        medication.setObservations("obs");
        medication.setPerson( createPersonObj() );
        medication.setMedicine( createMedicineObj() );
        medication.setDayPeriod( createDayPeriodObj() );

        return medication;
    }

    public static DayPeriod createDayPeriodObj(){
        DayPeriod dayPeriod = new DayPeriod();
        dayPeriod.setId(1);
        dayPeriod.setDescription("desc");
        dayPeriod.setOrder(1);

        return dayPeriod;
    }

    public static TemporaryMedication createTemporaryMedicationObj(){
        TemporaryMedication temporaryMedication = new TemporaryMedication();
        temporaryMedication.setUuid(UUID.randomUUID());
        temporaryMedication.setMedication( createMedicationObj() );
        temporaryMedication.setDays(5);
        temporaryMedication.setStartDate( new Date() );

        return temporaryMedication;
    }

    public static Stock createStockInValidityObj(){
        Stock stock = createStockObj();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, +2);
        Date dateMinus1Month = calendar.getTime();
        stock.setValidityEndDate(dateMinus1Month);

        return stock;
    }

    public static Stock createStockAlmostNoValidityObj(){
        Stock stock = createStockObj();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, +15);
        Date dateMinus1Month = calendar.getTime();
        stock.setValidityEndDate(dateMinus1Month);

        return stock;
    }

    public static Stock createStockWithoutValidityObj(){
        Stock stock = createStockObj();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -2);
        Date datePlus1Month = calendar.getTime();
        stock.setValidityEndDate(datePlus1Month);

        return stock;
    }
}
