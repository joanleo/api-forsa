package com.prueba.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.prueba.entity.Empresa;
import com.prueba.entity.Familia;

public interface FamiliaRepository extends JpaRepository<Familia, Long>, JpaSpecificationExecutor<Familia>{
	
	public Familia findBySiglaAndEmpresa(String sigla, Empresa empresa);

	//public Familia findByNombre(String nombre);
	
	//public List<Familia> findByNombreContains(String nombre);

	public List<Familia> findByNombreContainsAndEmpresaAndEstaActivoTrue(String letras, Empresa empresa);

	public List<Familia> findByNombreContainsAndEmpresa(String name, Empresa empresa);

	public List<Familia> findByEmpresa(Empresa empresa);

	public Familia findByNombreAndEmpresa(String nombre, Empresa empresa);

	public Optional<Familia> findByIdAndEmpresa(Long id, Empresa empresa);

	public Page<Familia> findByEmpresaAndEstaActivoTrue(Empresa empresa, Pageable page);

	public List<Familia> findByEmpresaAndEstaActivoTrue(Empresa empresa);
}
