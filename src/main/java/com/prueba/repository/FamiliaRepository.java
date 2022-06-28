package com.prueba.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Empresa;
import com.prueba.entity.Familia;

public interface FamiliaRepository extends JpaRepository<Familia, Long>{
	
	public Familia findBySiglaAndEmpresa(String sigla, Empresa empresa);

	//public Familia findByNombre(String nombre);
	
	//public List<Familia> findByNombreContains(String nombre);

	public List<Familia> findByNombreContainsAndEmpresaAndEstaActiva(String letras, Empresa empresa, Boolean estaActiva);

	public List<Familia> findByNombreContainsAndEmpresa(String name, Empresa empresa);

	public List<Familia> findByEmpresa(Empresa empresa);

	public Familia findByNombreAndEmpresa(String nombre, Empresa empresa);

	public Optional<Familia> findByIdAndEmpresa(Long id, Empresa empresa);
}
