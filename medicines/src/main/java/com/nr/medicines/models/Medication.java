package com.nr.medicines.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "regular_medication")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid")
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "medicine_barcode")
    private Medicine medicine;

    @ManyToOne
    @JoinColumn(name = "day_period_id")
    private DayPeriod dayPeriod;

    @Column(name = "observations")
    private String observations;

    @ManyToOne
    @JoinColumn(name = "person_uuid")
    private Person person;

    @Column(name = "created")
    private Date created;
}
