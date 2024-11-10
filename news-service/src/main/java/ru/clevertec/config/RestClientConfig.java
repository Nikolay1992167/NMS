package ru.clevertec.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${urls.userservice_url}")
    private String baseUrl;

    @Bean
    public RestClient webClient() {
        return RestClient.builder().baseUrl(baseUrl).build();
    }
}