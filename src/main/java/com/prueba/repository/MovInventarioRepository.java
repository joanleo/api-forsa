package com.prueba.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.prueba.entity.Empresa;
import com.prueba.entity.MovInventario;
import com.prueba.security.entity.Usuario;

public interface MovInventarioRepository extends JpaRepository<MovInventario, Integer>, JpaSpecificationExecutor<MovInventario> {

	Page<MovInventario> findByEmpresa(Empresa empresa, Pageable page);

	/**
	 * @param usuario
	 * @return
	 */
	MovInventario findByRealizo(Usuario usuario);

	/**
	 * @param idMov
	 * @return
	 */
	MovInventario findByidMov(Long idMov);

	/**
	 * @param empresa
	 * @return
	 */
	List<MovInventario> findByEmpresa(Empresa empresa);
	
}
