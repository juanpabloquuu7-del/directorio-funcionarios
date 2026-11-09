package com.campuslands.directorio.service;

import com.campuslands.directorio.model.Funcionario;
import com.campuslands.directorio.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    private final FuncionarioRepository repository;

    public FuncionarioService(FuncionarioRepository repository) {
        this.repository = repository;
    }

    public List<Funcionario> listar(String nombre, String departamento) {
        if (nombre != null && !nombre.isBlank()) {
            return repository.findByNombreContainingIgnoreCase(nombre.trim());
        }
        if (departamento != null && !departamento.isBlank()) {
            return repository.findByDepartamentoIgnoreCase(departamento.trim());
        }
        return repository.findAll();
    }

    public Optional<Funcionario> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Funcionario guardar(Funcionario f) {
        return repository.save(f);
    }

    public Optional<Funcionario> actualizar(Long id, Funcionario datos) {
        return repository.findById(id).map(existente -> {
            existente.setNombre(datos.getNombre());
            existente.setCargo(datos.getCargo());
            existente.setDepartamento(datos.getDepartamento());
            existente.setTelefono(datos.getTelefono());
            return repository.save(existente);
        });
    }

    public boolean eliminar(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
