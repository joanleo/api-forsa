/**
 * 
 */
package com.prueba.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.DetalleSalida;
import com.prueba.entity.Salida;

/**
 * @author Joan Leon
 *
 */
public interface DetalleSalidaRepository extends JpaRepository<DetalleSalida, Integer> {

	/**
	 * @param salida
	 * @param page 
	 * @return
	 */
	Page<DetalleSalida> findBySalida(Salida salida, Pageable page);

}
