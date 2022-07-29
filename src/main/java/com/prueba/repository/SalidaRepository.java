/**
 * 
 */
package com.prueba.repository;

import java.util.List;

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

	/**
	 * @param empresa
	 * @return
	 */
	public List<Salida> findByEmpresa(Empresa empresa);

	/**
	 * @param letras
	 * @param empresa
	 * @return
	 */
	public List<Salida> findByNumDocumentoContainsAndEmpresa(String letras, Empresa empresa);

	/**
	 * @param idsalida
	 * @return
	 */
	public Salida findByIdSalida(Integer idsalida);

}
