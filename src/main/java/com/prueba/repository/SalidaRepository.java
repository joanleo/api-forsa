/**
 * 
 */
package com.prueba.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.prueba.entity.Empresa;
import com.prueba.entity.Salida;

/**
 * @author Joan Leon
 *
 */
public interface SalidaRepository extends JpaRepository<Salida, Integer>, JpaSpecificationExecutor<Salida> {

	/**
	 * @param empresa
	 * @param page 
	 * @param pageRequest 
	 * @return
	 */
	public Page<Salida> findByEmpresa(Empresa empresa, Pageable page);

}
