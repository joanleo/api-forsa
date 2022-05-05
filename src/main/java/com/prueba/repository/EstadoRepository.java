package com.prueba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Long>{
	
	public Estado findByTipo(String tipo);
	
	public List<Estado> findByTipoContains(String tipo);

}
