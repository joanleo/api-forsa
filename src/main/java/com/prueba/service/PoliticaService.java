/**
 * 
 */
package com.prueba.service;

import org.springframework.data.domain.Page;

import com.prueba.entity.Empresa;
import com.prueba.security.entity.Politica;
import com.prueba.security.entity.Rol;

/**
 * @author Joan Leon
 *
 */
public interface PoliticaService {

	/**
	 * @param empresa
	 * @param pagina
	 * @param items
	 * @return
	 */
	Page<Politica> buscarPoliticas(Rol rol, Empresa empresa, Integer pagina, Integer items);

	/**
	 * @param nit
	 * @param pagina
	 * @param items
	 * @return
	 */
	Page<Politica> buscarPoliticas(Long nit, Integer pagina, Integer items);


}
