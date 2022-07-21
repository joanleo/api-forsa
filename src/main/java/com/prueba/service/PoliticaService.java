/**
 * 
 */
package com.prueba.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.prueba.entity.Empresa;
import com.prueba.security.dto.RutinaDTO;
import com.prueba.security.entity.Politica;
import com.prueba.security.entity.Rol;

/**
 * @author Joan Leon
 *
 */
public interface PoliticaService {

	/**
	 * 
	 * @param rol
	 * @param empresa
	 * @param pagina
	 * @param items
	 * @return
	 */
	//Page<Politica> buscarPoliticas(Rol rol, Empresa empresa, Integer pagina, Integer items);

	/**
	 * @param nit
	 * @param pagina
	 * @param items
	 * @return
	 */
	Page<Politica> buscarPoliticas(Long nit, Integer pagina, Integer items);
	
	public Set<RutinaDTO> buscarPoliticas(Rol rol, Empresa empresa);


}
