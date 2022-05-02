package com.prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Long>{
	
	Estado findByTipo(String tipo);

}
