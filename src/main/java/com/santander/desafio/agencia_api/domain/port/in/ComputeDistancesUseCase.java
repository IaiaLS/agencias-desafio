package com.santander.desafio.agencia_api.domain.port.in;

import com.santander.desafio.agencia_api.domain.model.AgencyDistance;
import com.santander.desafio.agencia_api.domain.model.Coordinate;

import java.util.List;

public interface ComputeDistancesUseCase {
    List<AgencyDistance> compute(Coordinate userPosition);
}
