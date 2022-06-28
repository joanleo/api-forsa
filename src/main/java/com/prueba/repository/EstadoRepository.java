package com.prueba.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Empresa;
import com.prueba.entity.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Long>{
	
	public Estado findByTipoAndEmpresa(String tipo, Empresa empresa);
	
	public List<Estado> findByEmpresaAndEstaActivo(Empresa empresa, Boolean estaActivo);
	
	public List<Estado> findByTipoContainsAndEmpresa(String tipo, Empresa empresa);

	public List<Estado> findByTipoContainsAndEmpresaAndEstaActivo(String tipo, Empresa empresa, Boolean estaActivo);

	public Optional<Estado> findByIdAndEmpresa(Long id, Empresa empresa);

}
