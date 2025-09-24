package com.santander.desafio.agencia_api.domain.service;

import com.santander.desafio.agencia_api.domain.model.Coordinate;

public class DistanceService {
    public double euclidean(Coordinate a, Coordinate b) {
        double dx = b.x() - a.x();
        double dy = b.y() - a.y();
        return Math.sqrt(dx*dx + dy*dy);
    }
}
