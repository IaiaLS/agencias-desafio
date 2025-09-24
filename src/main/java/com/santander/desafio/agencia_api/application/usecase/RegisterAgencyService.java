package com.santander.desafio.agencia_api.application.usecase;

import com.santander.desafio.agencia_api.domain.model.Agency;
import com.santander.desafio.agencia_api.domain.model.Coordinate;
import com.santander.desafio.agencia_api.domain.port.in.RegisterAgencyUseCase;
import com.santander.desafio.agencia_api.domain.port.out.AgencyRepositoryPort;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class RegisterAgencyService implements RegisterAgencyUseCase {
    private final AgencyRepositoryPort repository;
    public RegisterAgencyService(AgencyRepositoryPort repository) { this.repository = repository; }
    @Override
    public Agency register(Coordinate position) {
        long t0 = System.nanoTime();
        var saved = repository.save(new Agency(null, position , "Agência em (" + position.x() + ", " + position.y() + ")"));
        log.info("RegisterAgencyService.done id={} elapsedMs={}", saved.getId(), (System.nanoTime()-t0)/1_000_000);
        return saved;
    }
}
