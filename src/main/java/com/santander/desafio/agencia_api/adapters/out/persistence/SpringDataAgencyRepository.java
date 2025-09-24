package com.santander.desafio.agencia_api.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAgencyRepository extends JpaRepository<AgencyJpaEntity, Long> { }