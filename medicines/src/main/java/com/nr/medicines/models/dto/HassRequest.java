package com.nr.medicines.models.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class HassRequest {
    private String title;
    private String message;
    private UUID notification_id;
}
