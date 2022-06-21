package com.prueba.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.prueba.entity.Empresa;
import com.prueba.entity.MovInventario;

public interface MovInventarioRepository extends JpaRepository<MovInventario, Long>, JpaSpecificationExecutor<MovInventario> {

	Page<MovInventario> findByEmpresa(Empresa empresa, Pageable page);
	
}
