package com.prueba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Politica;
import com.prueba.security.entity.Rol;

public interface PoliticaRepository extends JpaRepository<Politica, Long> {

	Politica findByIdPolitica(Long id);

	List<Politica> findByRol(Rol rol);

}
