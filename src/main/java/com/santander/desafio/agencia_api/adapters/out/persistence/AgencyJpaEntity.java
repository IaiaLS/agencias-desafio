package com.santander.desafio.agencia_api.adapters.out.persistence;

import com.santander.desafio.agencia_api.domain.model.Coordinate;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "agencies")
public class AgencyJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "pos_x", nullable = false)),
            @AttributeOverride(name = "y", column = @Column(name = "pos_y", nullable = false))
    })
    private Coordinate position;

    @Column(name = "name", nullable = true, length = 120)
    private String name;

   }
