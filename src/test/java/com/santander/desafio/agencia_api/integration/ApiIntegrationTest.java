package com.santander.desafio.agencia_api.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiIntegrationTest {


    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper om;


    @Test
    void full_flow_register_then_distance_sorted_and_formatted() throws Exception {
        mvc.perform(post("/desafio/cadastrar").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"posX\":10,\"posY\":0}"))
                .andExpect(status().isCreated());
        mvc.perform(post("/desafio/cadastrar").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"posX\":1,\"posY\":1}"))
                .andExpect(status().isCreated());
        mvc.perform(post("/desafio/cadastrar").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"posX\":-5,\"posY\":0}"))
                .andExpect(status().isCreated());


        var resp = mvc.perform(get("/desafio/distancia").param("posX", "0").param("posY", "0"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();


        JsonNode arr = om.readTree(resp);
        assertThat(arr).hasSize(3);
        assertThat(arr.get(0).get("distance").asText()).isEqualTo("1.41");
        assertThat(arr.get(1).get("distance").asText()).isEqualTo("5.00");
        assertThat(arr.get(2).get("distance").asText()).isEqualTo("10.00");
    }


    @Test
    void cadastrar_without_required_fields_returns_400() throws Exception {
        mvc.perform(post("/desafio/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"posY\":-5}"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void distancia_missing_params_returns_400() throws Exception {
        mvc.perform(get("/desafio/distancia"))
                .andExpect(status().isBadRequest());
    }
}
