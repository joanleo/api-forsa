/**
 * 
 */
package com.prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.DetalleRutina;
import com.prueba.entity.Permiso;

/**
 * @author Joan Leon
 *
 */
public interface DetalleRutinaRepository extends JpaRepository<DetalleRutina, Long> {

	/**
	 * @param permiso
	 * @return
	 */
	DetalleRutina findByRuta(Permiso permiso);

}
