package com.prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Ubicacion;

public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
	
	public Ubicacion findByNombre(String nombre);
}
