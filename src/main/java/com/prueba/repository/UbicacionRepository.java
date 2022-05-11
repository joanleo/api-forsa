package com.prueba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Ubicacion;

public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
	
	public Ubicacion findByNombre(String nombre);
	
	public List<Ubicacion> findByNombreContains(String nombre); 
}
