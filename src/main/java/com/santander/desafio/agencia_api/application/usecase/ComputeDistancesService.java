package com.santander.desafio.agencia_api.application.usecase;


import com.santander.desafio.agencia_api.domain.model.AgencyDistance;
import com.santander.desafio.agencia_api.domain.model.Coordinate;
import com.santander.desafio.agencia_api.domain.port.in.ComputeDistancesUseCase;
import com.santander.desafio.agencia_api.domain.port.out.AgencyRepositoryPort;
import com.santander.desafio.agencia_api.domain.service.DistanceService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ComputeDistancesService implements ComputeDistancesUseCase {
    private final AgencyRepositoryPort repository;
    private final DistanceService distanceService;


    public ComputeDistancesService(AgencyRepositoryPort repository, DistanceService distanceService) {
        this.repository = repository; this.distanceService = distanceService;
    }


    @Override
    public List<AgencyDistance> compute(Coordinate userPosition) {
        return repository.findAll().stream()
                .map(a -> new AgencyDistance(a.getId(), distanceService.euclidean(userPosition, a.getPosition())))
                .sorted(Comparator.comparingDouble(AgencyDistance::distance))
                .toList();
    }
}