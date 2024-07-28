package com.nr.medicines.models.dto;

import lombok.Data;
import java.time.LocalTime;

@Data
public class DayPeriodRequest {
    private String name;
    private LocalTime hour;
}
