package com.prueba.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.security.entity.Ruta;

public interface RutaRepository extends JpaRepository<Ruta, Long>{

	public Ruta findByRutaAndMetodo(String ruta, String metodo);

}
