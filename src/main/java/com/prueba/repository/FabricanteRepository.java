package com.prueba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Fabricante;

public interface FabricanteRepository extends JpaRepository<Fabricante, Long>{
	
	List<Fabricante> findByNombreContains(String nombre);

}
