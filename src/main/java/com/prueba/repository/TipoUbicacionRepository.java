package com.prueba.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.prueba.entity.Empresa;
import com.prueba.entity.TipoUbicacion;

public interface TipoUbicacionRepository extends JpaRepository<TipoUbicacion, Long>, JpaSpecificationExecutor<TipoUbicacion> {

	//public TipoUbicacion findByNombre(String nombre);

	public Optional<TipoUbicacion> findByIdAndEmpresa(Long id, Empresa empresa);

	public List<TipoUbicacion> findByEmpresa(Empresa empresa);

	//public TipoUbicacion findByNombreAndEmpresa(String nombre, Empresa empresa);

	public Page<TipoUbicacion> findByEmpresaAndEstaActivoTrue(Empresa empresa, Pageable page);

	public List<TipoUbicacion> findByEmpresaAndEstaActivoTrue(Empresa empresa);

	public TipoUbicacion findByNombreAndEmpresaAndEstaActivoTrue(String nombre, Empresa empresa);

	public List<TipoUbicacion> findByNombreContainsAndEmpresaAndEstaActivoTrue(String letras, Empresa empresa);
}
