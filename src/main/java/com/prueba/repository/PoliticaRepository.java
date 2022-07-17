package com.prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Politica;

public interface PoliticaRepository extends JpaRepository<Politica, Long> {

}
