package com.santander.desafio.agencia_api.application.usecase;


import com.santander.desafio.agencia_api.domain.model.AgencyDistance;
import com.santander.desafio.agencia_api.domain.model.Coordinate;
import com.santander.desafio.agencia_api.domain.port.in.ComputeDistancesUseCase;
import com.santander.desafio.agencia_api.domain.port.out.AgencyRepositoryPort;
import com.santander.desafio.agencia_api.domain.service.DistanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ComputeDistancesService implements ComputeDistancesUseCase {
    private final AgencyRepositoryPort repository;
    private final DistanceService distanceService;

    public ComputeDistancesService(AgencyRepositoryPort repository, DistanceService distanceService) {
        this.repository = repository; this.distanceService = distanceService;
    }

    @Override
    public List<AgencyDistance> compute(Coordinate userPosition) {
        Objects.requireNonNull(userPosition, "userPosition must not be null");
        long t0 = System.nanoTime();
        log.debug("ComputeDistancesService.start user=({}, {})", userPosition.x(), userPosition.y());

        var agencies = repository.findAll();
        log.debug("ComputeDistancesService.loaded agencies={}", agencies.size());

        var result = agencies.stream()
                .map(a -> new AgencyDistance(a.getId(), distanceService.euclidean(userPosition, a.getPosition())))
                .sorted(Comparator.comparingDouble(AgencyDistance::distance)
                        .thenComparing(AgencyDistance::agencyId)) // desempate estável
                .toList();

        if (log.isDebugEnabled()) {
            var top = result.stream().limit(3)
                    .map(d -> "%d:%.2f".formatted(d.agencyId(), d.distance()))
                    .collect(Collectors.joining(", "));
            log.debug("ComputeDistancesService.top3=[{}]", top);
        }

        log.info("ComputeDistancesService.done user=({}, {}) count={} elapsedMs={}",
                userPosition.x(), userPosition.y(), result.size(), (System.nanoTime()-t0)/1_000_000);
        return result;
    }
}