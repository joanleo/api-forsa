package com.prueba.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Traslado;

public interface TrasladoRepository extends JpaRepository<Traslado, Long> {
	
	Page<Traslado> findByFechaSalidaBetween(LocalDate desde, LocalDate hasta, Pageable pageable);

}
