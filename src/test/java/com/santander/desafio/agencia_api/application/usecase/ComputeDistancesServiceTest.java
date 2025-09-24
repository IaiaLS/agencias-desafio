package com.santander.desafio.agencia_api.application.usecase;

import com.santander.desafio.agencia_api.domain.model.Agency;
import com.santander.desafio.agencia_api.domain.model.Coordinate;
import com.santander.desafio.agencia_api.domain.port.out.AgencyRepositoryPort;
import com.santander.desafio.agencia_api.domain.service.DistanceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComputeDistancesServiceTest {

    @Mock private AgencyRepositoryPort repository;

    @Spy
    private DistanceService distanceService = new DistanceService();

    @InjectMocks private ComputeDistancesService service;

    @Test
    void compute_orders_by_ascending_distance() {
        var user = new Coordinate(0, 0);
        var a1 = new Agency(1L, new Coordinate(10, 0), null);
        var a2 = new Agency(2L, new Coordinate(1, 1), null);
        var a3 = new Agency(3L, new Coordinate(-5, 0), null);

        when(repository.findAll()).thenReturn(List.of(a1, a2, a3));

        var out = service.compute(user);

        assertEquals(3, out.size());
        assertEquals(2L, out.get(0).agencyId()); // √2
        assertEquals(3L, out.get(1).agencyId()); // 5
        assertEquals(1L, out.get(2).agencyId()); // 10
    }
}

