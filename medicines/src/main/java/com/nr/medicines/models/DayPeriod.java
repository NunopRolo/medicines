package com.nr.medicines.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "day_periods")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DayPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "description")
    private String description;

    @Column(name = "display_order")
    private Integer order;

    @Column(name = "day_period_hour")
    private LocalTime hour;
}
