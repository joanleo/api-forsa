/**
 * 
 */
package com.prueba.service;

import org.springframework.data.domain.Page;

import com.prueba.entity.Empresa;
import com.prueba.entity.Salida;

/**
 * @author Joan Leon
 *
 */
public interface SalidaService {

	/**
	 * @param salida
	 * @return
	 */
	public Salida crearSalida(Salida salida);

	/**
	 * @param empresa
	 * @param pagina
	 * @param items
	 * @return
	 */
	public Page<Salida> buscarSalidas(Empresa empresa, Integer pagina, Integer items);

	/**
	 * @param salida
	 * @param empresa
	 * @param pagina
	 * @param items
	 * @return
	 */
	public Page<Salida> buscarSalidas(Salida salida, Empresa empresa, Integer pagina, Integer items);

}
