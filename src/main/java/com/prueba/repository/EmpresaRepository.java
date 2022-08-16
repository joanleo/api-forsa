package com.prueba.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.prueba.entity.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer>, JpaSpecificationExecutor<Empresa> {
	
	public Empresa findByNit(Long nit);
	
	public List<Empresa> findByNombreContainsAndEstaActivoTrue(String nombre);
		
	public Empresa findByNitOrderByFecha(Long nit);
	
	//public Empresa findTopByOrderByFechaDesc(Long nit);
	
	//public Empresa findByNitOrderByFecha(Long nit);

	public Page<Empresa> findByNombreContainsAndEstaActivoTrue(String letras, Pageable page);

	public Page<Empresa> findByEstaActivoTrue(Pageable page);

	public List<Empresa> findByEstaActivoTrue();

}
