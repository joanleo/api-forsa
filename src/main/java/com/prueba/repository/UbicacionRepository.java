package com.prueba.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.prueba.entity.Empresa;
import com.prueba.entity.Ubicacion;

public interface UbicacionRepository extends JpaRepository<Ubicacion, Long>, JpaSpecificationExecutor<Ubicacion> {
	
	public Ubicacion findByNombre(String nombre);
	
	public List<Ubicacion> findByNombreContains(String nombre);

	public Ubicacion findByNombreAndEmpresa(String nombre, Empresa empresa);

	public List<Ubicacion> findByEmpresaAndEstaActivoTrue(Empresa empresa);

	public List<Ubicacion> findByNombreContainsAndEmpresaAndEstaActivoTrue(String name, Empresa empresa);

	public Optional<Ubicacion> findByIdAndEmpresa(Long id, Empresa empresa);

	public Page<Ubicacion> findByEmpresaAndEstaActivoTrue(Empresa empresa, Pageable page);

}
