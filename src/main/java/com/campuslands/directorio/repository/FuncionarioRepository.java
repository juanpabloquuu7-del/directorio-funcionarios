package com.campuslands.directorio.repository;

import com.campuslands.directorio.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    List<Funcionario> findByNombreContainingIgnoreCase(String nombre);

    List<Funcionario> findByDepartamentoIgnoreCase(String departamento);
}
