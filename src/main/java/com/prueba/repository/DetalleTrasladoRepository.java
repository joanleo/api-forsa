/**
 * 
 */
package com.prueba.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.DetalleTrasl;
import com.prueba.entity.Traslado;

/**
 * @author Joan Leon
 *
 */
public interface DetalleTrasladoRepository extends JpaRepository<DetalleTrasl, Long> {

	/**
	 * @param traslado
	 * @param page 
	 * @return
	 */
	Page<DetalleTrasl> findByTraslado(Traslado traslado, Pageable page);

}
