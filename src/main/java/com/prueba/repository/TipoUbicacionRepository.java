package com.prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.TipoUbicacion;

public interface TipoUbicacionRepository extends JpaRepository<TipoUbicacion, Long> {

	public TipoUbicacion findByNombre(String nombre);
}
