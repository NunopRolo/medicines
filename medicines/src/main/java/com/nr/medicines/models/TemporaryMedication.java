package com.nr.medicines.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "temporary_medication")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TemporaryMedication {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid")
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "medication_uuid")
    private Medication medication;

    @Column(name = "days")
    private Integer days;

    @Column(name = "start_date")
    private Date startDate;
}
