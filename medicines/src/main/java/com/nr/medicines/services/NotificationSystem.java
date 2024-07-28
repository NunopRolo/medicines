package com.nr.medicines.services;

import com.nr.medicines.models.dto.NotificationDto;

public interface NotificationSystem {
    void sendNotification(NotificationDto notificationDto);
}
