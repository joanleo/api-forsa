/**
 * 
 */
package com.prueba.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.prueba.dto.SalidaDTO;
import com.prueba.entity.DetalleSalida;
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

	/**
	 * @param letras
	 * @param empresa
	 * @return
	 */
	public List<Salida> buscarSalidas(String letras, Empresa empresa);

	/**
	 * @param idsalida
	 * @param codigopieza
	 * @return
	 */
	public Salida confirmarActivoSalida(Long idsalida, String codigopieza);

	/**
	 * @param idsalida
	 * @return
	 */
	public Salida confirmarSalida(Long idsalida);

	/**
	 * @param idsalida
	 * @param nit
	 */
	public void eliminarSalida(Long idsalida, Long nit);

	/**
	 * @param id
	 * @return
	 */
	public Salida obtieneSalida(Long id);

	/**
	 * @param idsalida
	 * @param items 
	 * @param pagina 
	 * @return
	 */
	public Page<DetalleSalida> obtieneDetalleSalida(Long idsalida, Integer pagina, Integer items);

	/**
	 * @param idsalida
	 * @param codigopieza
	 */
	public void eliminarActivo(Long idsalida, String codigopieza);

	/**
	 * @param idsalida 
	 * 
	 */
	public void eliminarTodosActivos(Long idsalida);

	public Salida crearSalida(SalidaDTO salida);


}
