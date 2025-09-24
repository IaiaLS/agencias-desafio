package com.santander.desafio.agencia_api.adapters.out.persistence;

import com.santander.desafio.agencia_api.domain.model.Agency;
import com.santander.desafio.agencia_api.domain.port.out.AgencyRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AgencyRepositoryAdapter implements AgencyRepositoryPort {
    private final SpringDataAgencyRepository jpa;
    public AgencyRepositoryAdapter(SpringDataAgencyRepository jpa) { this.jpa = jpa; }


    @Override
    public Agency save(Agency agency) {
        var entity = new AgencyJpaEntity();
        entity.setPosition(agency.getPosition());
        var saved = jpa.save(entity);
        return new Agency(saved.getId(), saved.getPosition(),saved.getName());
    }


    @Override
    public List<Agency> findAll() {
        return jpa.findAll().stream()
                .map(e -> new Agency(e.getId(), e.getPosition(), e.getName()))
                .toList();
    }
}
