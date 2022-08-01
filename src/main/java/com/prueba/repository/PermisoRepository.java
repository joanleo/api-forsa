package com.prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Permiso;

public interface PermisoRepository extends JpaRepository<Permiso, Long> {

	Permiso findByUrlAndNombre(String ruta, String nombre);

	/**
	 * @param string
	 * @return
	 */
	Permiso findByNombre(String string);

}
