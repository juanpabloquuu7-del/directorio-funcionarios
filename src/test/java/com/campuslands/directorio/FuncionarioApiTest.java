package com.campuslands.directorio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FuncionarioApiTest {

    @Autowired MockMvc mvc;

    static final String BASE = "/api/v1/funcionarios";

    String json(String nombre, String cargo, String depto) {
        return "{\"nombre\":\"%s\",\"cargo\":\"%s\",\"departamento\":\"%s\"}".formatted(nombre, cargo, depto);
    }

    MockHttpServletRequestBuilder autenticado(MockHttpServletRequestBuilder request) {
        String token = Base64.getEncoder().encodeToString("testuser:testpassword".getBytes(StandardCharsets.UTF_8));
        return request.header("Authorization", "Basic " + token);
    }

    @Test
    void flujoCompleto() throws Exception {
        mvc.perform(autenticado(post(BASE).contentType(MediaType.APPLICATION_JSON).content(json("Ana Pérez", "Contadora", "Contabilidad"))))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").exists());
        mvc.perform(autenticado(get(BASE + "?nombre=ANA"))).andExpect(status().isOk());
        mvc.perform(autenticado(get(BASE + "?departamento=contabilidad"))).andExpect(status().isOk());
        mvc.perform(autenticado(get(BASE + "?nombre=zzz"))).andExpect(status().isNoContent());
        mvc.perform(autenticado(get(BASE + "?departamento=Conta"))).andExpect(status().isNoContent());
        mvc.perform(autenticado(get(BASE + "/99999"))).andExpect(status().isNotFound());
        mvc.perform(autenticado(put(BASE + "/99999").contentType(MediaType.APPLICATION_JSON).content(json("a", "b", "c"))))
                .andExpect(status().isNotFound());
        mvc.perform(autenticado(delete(BASE + "/99999"))).andExpect(status().isNotFound());
    }

    @Test
    void validaciones() throws Exception {
        mvc.perform(autenticado(post(BASE).contentType(MediaType.APPLICATION_JSON).content(json("  ", "x", "y"))))
                .andExpect(status().isBadRequest());
        mvc.perform(autenticado(post(BASE).contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"a\",\"cargo\":\"b\"}")))
                .andExpect(status().isBadRequest());
        mvc.perform(autenticado(post(BASE).contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":5,\"nombre\":\"a\",\"cargo\":\"b\",\"departamento\":\"c\"}")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarYEliminar() throws Exception {
        String res = mvc.perform(autenticado(post(BASE).contentType(MediaType.APPLICATION_JSON).content(json("Luis", "Jefe", "TI"))))
                .andReturn().getResponse().getContentAsString();
        String id = res.replaceAll(".*\"id\":(\\d+).*", "$1");
        mvc.perform(autenticado(put(BASE + "/" + id).contentType(MediaType.APPLICATION_JSON).content(json("Luis R", "Gerente", "TI"))))
                .andExpect(status().isOk()).andExpect(jsonPath("$.cargo").value("Gerente"));
        mvc.perform(autenticado(delete(BASE + "/" + id))).andExpect(status().isNoContent());
        mvc.perform(autenticado(get(BASE + "/" + id))).andExpect(status().isNotFound());
    }
}
