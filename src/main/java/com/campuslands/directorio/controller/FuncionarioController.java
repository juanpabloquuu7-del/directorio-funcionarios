package com.campuslands.directorio.controller;

import com.campuslands.directorio.model.Funcionario;
import com.campuslands.directorio.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/funcionarios")
public class FuncionarioController {

    private final FuncionarioService service;

    public FuncionarioController(FuncionarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Funcionario>> listar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String departamento) {
        List<Funcionario> resultado = service.listar(nombre, departamento);
        return resultado.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> consultar(@PathVariable Long id) {
        return service.buscarPorId(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Funcionario> registrar(@Valid @RequestBody Funcionario funcionario) {
        if (funcionario.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        Funcionario creado = service.guardar(funcionario);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.getId()).toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> actualizar(@PathVariable Long id,
                                                  @Valid @RequestBody Funcionario funcionario) {
        return service.actualizar(id, funcionario).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return service.eliminar(id) ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
