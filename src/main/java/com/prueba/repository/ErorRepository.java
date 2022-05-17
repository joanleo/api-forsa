package com.prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Eror;

public interface ErorRepository extends JpaRepository<Eror, Long> {

	public Eror findTopByOrderByIdErrorDesc();
}
