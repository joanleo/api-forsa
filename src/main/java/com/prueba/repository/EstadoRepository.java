package com.prueba.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.prueba.entity.Empresa;
import com.prueba.entity.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Long>, JpaSpecificationExecutor<Estado>{
	
	public Estado findByTipoAndEmpresa(String tipo, Empresa empresa);
	
	public List<Estado> findByEmpresaAndEstaActivo(Empresa empresa, Boolean estaActivo);
	
	public List<Estado> findByTipoContainsAndEmpresa(String tipo, Empresa empresa);

	public List<Estado> findByTipoContainsAndEmpresaAndEstaActivo(String tipo, Empresa empresa, Boolean estaActivo);

	public Optional<Estado> findByIdAndEmpresa(Long id, Empresa empresa);

	public Page<Estado> findByTipoContainsAndEmpresaAndEstaActivoTrue(String letras, Empresa empresa, Pageable page);

	public Page<Estado> findByEmpresaAndEstaActivoTrue(Empresa empresa, Pageable page);

}
