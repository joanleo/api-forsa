package com.prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.TipoMov;

public interface TipoMovRepository extends JpaRepository<TipoMov, Long> {
	
	public TipoMov findByNombre(String nombre);

}
