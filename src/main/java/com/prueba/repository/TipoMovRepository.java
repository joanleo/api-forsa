package com.prueba.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Empresa;
import com.prueba.entity.TipoMov;

public interface TipoMovRepository extends JpaRepository<TipoMov, Long> {
	
	//public TipoMov findByNombre(String nombre);

	public TipoMov findByNombreAndEmpresa(String nombre, Empresa empresa);

	public List<TipoMov> findByEmpresa(Empresa empresa);

	public Optional<TipoMov> findByIdAndEmpresa(Long id, Empresa empresa);

	public List<TipoMov> findByNombreContainsAndEmpresaAndEstaActivo(String letras, Empresa empresa,
			Boolean estaActivo);

}
