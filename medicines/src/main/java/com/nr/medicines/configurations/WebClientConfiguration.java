package com.nr.medicines.configurations;

import com.nr.medicines.utils.DateTime;
import com.nr.medicines.utils.DateTimeImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {
    @Value("${notifications.webclient-url}")
    private String url;

    @Bean()
    public WebClient hassWebClient(
            @Value("${notifications.hass-token}") String token
    ) {
        return WebClient.builder()
                .baseUrl(url)
                .defaultHeader("Authorization", "Bearer "+ token)
                .build();
    }

    @Bean()
    public DateTime dateBean(){
        return new DateTimeImpl();
    }
}
