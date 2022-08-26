package com.prueba.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Empresa;
import com.prueba.entity.TipoActivo;

public interface TipoActivoRepository extends JpaRepository<TipoActivo, Long> {

	TipoActivo findByNombreAndEmpresa(String nombre, Empresa empresa);

	List<TipoActivo> findByEmpresa(Empresa empresa);

	Optional<TipoActivo> findByIdAndEmpresa(Long id, Empresa empresa);

	/**
	 * @param nombre
	 * @return
	 */
	TipoActivo findByNombre(String nombre);

	/**
	 * @param letras
	 * @param empresa
	 * @return
	 */
	List<TipoActivo> findByNombreContainsAndEmpresaAndEstaActivoTrue(String letras, Empresa empresa);

}
