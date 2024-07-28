package com.nr.medicines.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "persons")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "person_uuid")
    private UUID personUuid;

    @Column(name = "name")
    private String name;
}
