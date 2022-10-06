/**
 * 
 */
package com.prueba.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prueba.entity.DetalleSalida;
import com.prueba.entity.Salida;

/**
 * @author Joan Leon
 *
 */
public interface DetalleSalidaRepository extends JpaRepository<DetalleSalida, Long> {

	/**
	 * @param salida
	 * @param page 
	 * @return
	 */
	Page<DetalleSalida> findBySalida(Salida salida, Pageable page);
	
	@Query(value="SELECT * FROM mov_det_salidas "
			+ "WHERE mov_det_salidas.salida_nidsalida=?1", nativeQuery = true)
	List<DetalleSalida> findBySalida(Salida salida);

}
