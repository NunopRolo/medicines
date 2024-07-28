package com.nr.medicines.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "stock_notifications")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StockNotifications {
    @Id
    @Column(name = "\"uuid\"")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "stock_uuid")
    private Stock stock;

    @Column(name = "title")
    private String title;

    @Column(name = "body")
    private String body;

    @Column(name = "created")
    private Date created;
}
