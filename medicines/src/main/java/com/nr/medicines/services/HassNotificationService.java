package com.nr.medicines.services;

import com.nr.medicines.models.dto.HassRequest;
import com.nr.medicines.models.dto.NotificationDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.*;

@Service
public class HassNotificationService implements NotificationSystem{
    private final WebClient webClient;

    public HassNotificationService(
            WebClient webClient
    ){
        this.webClient = webClient;
    }

    public void sendNotification(NotificationDto notificationDto) {
        HassRequest hassRequest = new HassRequest();
        hassRequest.setTitle( notificationDto.getTitle() );
        hassRequest.setMessage( notificationDto.getMessage() );
        hassRequest.setNotification_id(UUID.randomUUID() );

        webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(hassRequest)
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }

}
