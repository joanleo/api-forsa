package com.prueba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Familia;

public interface FamiliaRepository extends JpaRepository<Familia, Long>{

	public Familia findByNombre(String nombre);
	
	public List<Familia> findByNombreContains(String nombre);
}
