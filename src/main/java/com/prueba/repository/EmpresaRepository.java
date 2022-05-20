package com.prueba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
	
	public Empresa findByNit(Long nit);
	
	public List<Empresa> findByNombreContains(String nombre);
	
	public Empresa findTopByOrderByFecha();

}
