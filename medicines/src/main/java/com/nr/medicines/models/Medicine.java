package com.nr.medicines.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medicines")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Medicine {
    @Id
    @Column(name = "barcode")
    private Integer barcode;

    @Column(name = "name")
    private String name;

    @Column(name = "dosage")
    private String dosage;

    @Column(name = "generic")
    private boolean generic;

    @Column(name = "international_common_name")
    private String internationalCommonName;

    @Column(name = "form")
    private String form;

    @Column(name = "packaging_size")
    private String packagingSize;
}
