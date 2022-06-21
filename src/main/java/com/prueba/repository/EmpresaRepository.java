package com.prueba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
	
	public Empresa findByNit(Long nit);
	
	public List<Empresa> findByNombreContainsAndEstaActiva(String nombre, Boolean estaActiva);
	
	//public Empresa findTopByOrderByFechaDesc(Long nit);
	
	public Empresa findByNitOrderByFecha(Long nit);

}
