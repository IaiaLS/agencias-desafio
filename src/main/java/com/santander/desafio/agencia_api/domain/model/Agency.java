package com.santander.desafio.agencia_api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Agency {
    private Long id;
    private Coordinate position;
    private String name;
}
