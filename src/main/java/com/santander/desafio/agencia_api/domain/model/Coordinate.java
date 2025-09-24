package com.santander.desafio.agencia_api.domain.model;

import jakarta.persistence.Embeddable;



@Embeddable
public record Coordinate(double x, double y) { }


