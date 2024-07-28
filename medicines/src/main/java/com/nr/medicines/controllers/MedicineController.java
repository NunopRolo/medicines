package com.nr.medicines.controllers;

import com.nr.medicines.models.dto.MedicineDto;
import com.nr.medicines.services.MedicineService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/medicines")
public class MedicineController {
    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService){
        this.medicineService = medicineService;
    }

    @GetMapping(value = "/{barcode}")
    public MedicineDto getMedicineByBarcode(@PathVariable Integer barcode){
        return medicineService.getMedicineDtoByBarcode(barcode);
    }

    @GetMapping(value = {"/all", "/search/"})
    public Page<MedicineDto> getAllMedicines(@PageableDefault Pageable pageable){
        return medicineService.getAllMedicines(pageable);
    }

    @GetMapping(value = "/search/{term}")
    public Page<MedicineDto> search(
            @PageableDefault Pageable pageable,
            @PathVariable String term
    ){
        return medicineService.search(pageable, term);
    }

    @PostMapping(value = "/image/{medicineBarcode}")
    public String uploadImage(
            @PathVariable Integer medicineBarcode,
            @RequestParam("image") MultipartFile file
    ) throws IOException {
        return this.medicineService.addImage(medicineBarcode, file);
    }

    @GetMapping(value = "/image/{medicineBarcode}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getMedicationImage(
            @PathVariable Integer medicineBarcode
    ) {
        return this.medicineService.getImage(medicineBarcode);
    }

    @DeleteMapping(value = "/image/{medicineBarcode}")
    public void deleteImage(
            @PathVariable Integer medicineBarcode
    ){
        this.medicineService.deleteImage(medicineBarcode);
    }

    @GetMapping(value = "/notifications")
    public void testNotification(){
        this.medicineService.testNotifications();
    }
}
