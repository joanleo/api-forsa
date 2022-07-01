package com.prueba.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.prueba.entity.Empresa;
import com.prueba.entity.Fabricante;

public interface FabricanteRepository extends JpaRepository<Fabricante, Long>, JpaSpecificationExecutor<Fabricante>{
	
	//List<Fabricante> findByEmpresaAndEstaActivo(Empresa empresa, boolean b);

	//List<Fabricante> findByNombreContains(String nombre);
	
	//Fabricante findByNit(Long nit);

	//List<Fabricante> findByNombreContainsAndEstaActivo(String nombre, Boolean estaActivo);

	List<Fabricante> findByNombreContainsAndEmpresaAndEstaActivoTrue(String letras, Empresa empresa);

	List<Fabricante> findByEmpresaAndEstaActivoTrue(Empresa empresa);

	Optional<Fabricante> findByNitAndEmpresa(Long nit, Empresa empresa);
	
	public Fabricante  findByNitAndEmpresaAndEstaActivoTrue(Long nit, Empresa empresa);

	Page<Fabricante> findByNombreContainsAndEmpresaAndEstaActivoTrue(String letras, Empresa empresa, Pageable page);

	Page<Fabricante> findByEmpresaAndEstaActivoTrue(Empresa empresa, Pageable page);

}
