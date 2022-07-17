package com.prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Ruta;

public interface RutaRepository extends JpaRepository<Ruta, Long> {

	Ruta findByUrlAndNombre(String ruta, String nombre);

}
