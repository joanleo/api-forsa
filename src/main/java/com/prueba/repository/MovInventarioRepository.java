package com.prueba.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

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
	
	/**
	 * @param empresa
	 * @param desde
	 * @param hasta
	 * @param page
	 * @return
	 */
	@Query(value = "SELECT * FROM mov_inventarios "
			+ "WHERE mov_inventarios.vcnitempresa=?1 "
			+ "AND mov_inventarios.dfecha>=?2 "
			+ "AND mov_inventarios.dfecha<=?3 ", nativeQuery = true)
	Page<MovInventario> findBetweenDay(Long empresa, String desde, String hasta, Pageable page);
	
}
