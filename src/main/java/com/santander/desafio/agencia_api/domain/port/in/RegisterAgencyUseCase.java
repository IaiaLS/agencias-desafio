package com.santander.desafio.agencia_api.domain.port.in;



import com.santander.desafio.agencia_api.domain.model.Agency;
import com.santander.desafio.agencia_api.domain.model.Coordinate;


public interface RegisterAgencyUseCase {
    Agency register(Coordinate position);
}
