package com.prueba.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Empresa;
import com.prueba.entity.Fabricante;

public interface FabricanteRepository extends JpaRepository<Fabricante, Long>{
	
	//List<Fabricante> findByNombreContains(String nombre);
	
	//Fabricante findByNit(Long nit);

	//List<Fabricante> findByNombreContainsAndEstaActivo(String nombre, Boolean estaActivo);

	List<Fabricante> findByNombreContainsAndEmpresaAndEstaActivo(String letras, Empresa empresa, Boolean estaActivo);

	List<Fabricante> findByEmpresaAndEstaActivo(Empresa empresa, boolean b);

	Optional<Fabricante> findByNitAndEmpresa(Long nit, Empresa empresa);

}
