package com.santander.desafio.agencia_api.domain.port.out;

import com.santander.desafio.agencia_api.domain.model.Agency;

import java.util.List;

public interface AgencyRepositoryPort {
    Agency save(Agency agency);
    List<Agency> findAll();
}