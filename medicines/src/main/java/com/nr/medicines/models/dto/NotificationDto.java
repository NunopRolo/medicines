package com.nr.medicines.models.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private String title;
    private String message;
}
