package com.santander.desafio.agencia_api.config;

import com.santander.desafio.agencia_api.domain.service.DistanceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {
    @Bean
    public DistanceService distanceService() { return new DistanceService(); }
}
