package com.santander.desafio.agencia_api.adapters.in.web;

import com.santander.desafio.agencia_api.adapters.in.web.dto.AgencyDistanceView;
import com.santander.desafio.agencia_api.adapters.in.web.dto.RegisterAgencyRequest;
import com.santander.desafio.agencia_api.adapters.in.web.dto.RegisterAgencyResponse;
import com.santander.desafio.agencia_api.domain.model.Coordinate;
import com.santander.desafio.agencia_api.domain.port.in.ComputeDistancesUseCase;
import com.santander.desafio.agencia_api.domain.port.in.RegisterAgencyUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/desafio")
public class AgencyController {
    private final RegisterAgencyUseCase registerUC;
    private final ComputeDistancesUseCase computeUC;


    public AgencyController(RegisterAgencyUseCase registerUC, ComputeDistancesUseCase computeUC) {
        this.registerUC = registerUC; this.computeUC = computeUC;
    }


    @PostMapping("/cadastrar")
    public ResponseEntity<RegisterAgencyResponse> register(@Valid @RequestBody RegisterAgencyRequest req) {
        var agency = registerUC.register(new Coordinate(req.posX(), req.posY()));
        var resp = new RegisterAgencyResponse(
                agency.getId(),
                agency.getPosition().x(),
                agency.getPosition().y()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }


    @GetMapping("/distancia")
    public ResponseEntity<List<AgencyDistanceView>> distances(@RequestParam double posX, @RequestParam double posY) {
        var list = computeUC.compute(new Coordinate(posX, posY)).stream()
                .map(d -> new AgencyDistanceView(d.agencyId(), String.format(Locale.US, "%.2f", d.distance())))
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/distancia/v1")
    public ResponseEntity<Map<String, String>> distancesLegacyFormat(@RequestParam double posX, @RequestParam double posY) {
        var ordered = new LinkedHashMap<String, String>();
        var distances = computeUC.compute(new Coordinate(posX, posY));
        for (int i = 0; i < distances.size(); i++) {
            var d = distances.get(i);
            var key = "AGENCIA " + d.agencyId();
            var value = "distancia = " + String.format(Locale.US, "%.2f", d.distance());
            ordered.put(key, value);
        }
        return ResponseEntity.ok(ordered);
    }
}
