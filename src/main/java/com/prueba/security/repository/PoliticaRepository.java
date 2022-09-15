package com.prueba.security.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.prueba.entity.DetalleRutina;
import com.prueba.security.entity.Politica;
import com.prueba.security.entity.Rol;

public interface PoliticaRepository extends JpaRepository<Politica, Long>, JpaSpecificationExecutor<Politica> {

	Politica findByIdPolitica(Long id);

	List<Politica> findByRol(Rol rol);

	/**
	 * 
	 * @param rol
	 * @param page
	 * @return
	 */
	Page<Politica> findByRol(Rol rol, Pageable page);

	/**
	 * @param nit
	 * @param page 
	 * @return
	 */
	Page<Politica> findByRol_Empresa_Nit_ClassName(Long nit, Pageable page);

	/**
	 * @param detalleRutina
	 * @return
	 */
	Politica findByDetalle(DetalleRutina detalleRutina);

}
