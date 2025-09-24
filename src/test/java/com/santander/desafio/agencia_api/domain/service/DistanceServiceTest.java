package com.santander.desafio.agencia_api.domain.service;

import com.santander.desafio.agencia_api.domain.model.Coordinate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DistanceServiceTest {
    private final DistanceService service = new DistanceService();


    @ParameterizedTest
    @CsvSource({
            "0,0,3,4,5",
            "10,-5,10,-7,2",
            "-1,-1,2,3,5"
    })
    void euclidean_basics(double x1, double y1, double x2, double y2, double expected) {
        double d = service.euclidean(new Coordinate(x1, y1), new Coordinate(x2, y2));
        assertEquals(expected, d, 1e-9);
    }


    @Test
    void euclidean_isSymmetric() {
        var a = new Coordinate(10, -5);
        var b = new Coordinate(2, 7);
        assertEquals(service.euclidean(a, b), service.euclidean(b, a), 1e-9);
    }
}