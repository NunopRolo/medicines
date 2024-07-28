package com.nr.medicines.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "stock")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid")
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "medicine_barcode")
    private Medicine medicine;

    @Column(name = "validity_end_date")
    private Date validityEndDate;

    @ManyToOne
    @JoinColumn(name = "person_uuid")
    private Person person;

    @Column(name = "created")
    private Date created;
}
