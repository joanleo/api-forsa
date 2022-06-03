package com.prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.prueba.entity.MovInventario;

public interface MovInventarioRepository extends JpaRepository<MovInventario, Long>, JpaSpecificationExecutor<MovInventario> {
	
}
