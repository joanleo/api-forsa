package com.prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Rutina;

public interface RutinaRepository extends JpaRepository<Rutina, Long> {

	Rutina findByNombre(String rut);

}
