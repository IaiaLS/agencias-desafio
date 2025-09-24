package com.santander.desafio.agencia_api.application.usecase;

import com.santander.desafio.agencia_api.domain.model.Agency;
import com.santander.desafio.agencia_api.domain.model.Coordinate;
import com.santander.desafio.agencia_api.domain.port.out.AgencyRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterAgencyServiceTest {


    @Mock
    AgencyRepositoryPort repository;
    @InjectMocks
    RegisterAgencyService service;


    @Test
    void register_persists_and_returns_id() {
        var pos = new Coordinate(10, -5);
        when(repository.save(any())).thenAnswer(inv -> {
            var a = inv.getArgument(0, Agency.class);
            return new Agency(1L, a.getPosition(), a.getName());
        });


        Agency created = service.register(pos);


        assertNotNull(created);
        assertEquals(1L, created.getId());
        assertEquals(pos.x(), created.getPosition().x(), 1e-9);
        verify(repository).save(any(Agency.class));
    }
}
