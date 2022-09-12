package com.prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.prueba.entity.Permiso;

public interface PermisoRepository extends JpaRepository<Permiso, Long>, JpaSpecificationExecutor<Permiso> {

	Permiso findByUrlAndNombre(String ruta, String nombre);

	/**
	 * @param string
	 * @return
	 */
	Permiso findByNombre(String string);

}
