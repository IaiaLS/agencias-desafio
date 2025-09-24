package com.santander.desafio.agencia_api.adapters.in.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterAgencyRequest(@NotNull Double posX, @NotNull Double posY, @Size(max = 120) String name) { }
