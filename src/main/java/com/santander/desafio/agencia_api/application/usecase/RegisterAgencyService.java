package com.santander.desafio.agencia_api.application.usecase;

import com.santander.desafio.agencia_api.domain.model.Agency;
import com.santander.desafio.agencia_api.domain.model.Coordinate;
import com.santander.desafio.agencia_api.domain.port.in.RegisterAgencyUseCase;
import com.santander.desafio.agencia_api.domain.port.out.AgencyRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class RegisterAgencyService implements RegisterAgencyUseCase {
    private final AgencyRepositoryPort repository;
    public RegisterAgencyService(AgencyRepositoryPort repository) { this.repository = repository; }
    @Override
    public Agency register(Coordinate position) {
        return repository.save(new Agency(null, position , "Agência em (" + position.x() + ", " + position.y() + ")"));
    }
}
