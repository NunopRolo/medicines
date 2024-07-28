package com.nr.medicines.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "medicine_image")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MedicineImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "medicine_image_uuid")
    private UUID medicineImageUuid;

    @ManyToOne
    @JoinColumn(name = "medicine_barcode")
    private Medicine medicine;

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    @Column(name = "created")
    private Date created;
}
