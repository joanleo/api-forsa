package com.prueba.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.security.entity.PoliRol;

public interface PoliRolRepository extends JpaRepository<PoliRol, Long> {
	
	public PoliRol findByNombre(String nombre);

}
