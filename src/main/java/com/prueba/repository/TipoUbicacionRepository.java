package com.prueba.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Empresa;
import com.prueba.entity.TipoUbicacion;

public interface TipoUbicacionRepository extends JpaRepository<TipoUbicacion, Long> {

	//public TipoUbicacion findByNombre(String nombre);

	public Optional<TipoUbicacion> findByIdAndEmpresa(Long id, Empresa empresa);

	public List<TipoUbicacion> findByEmpresa(Empresa empresa);

	public TipoUbicacion findByNombreAndEmpresa(String nombre, Empresa empresa);

	public List<TipoUbicacion> findByNombreContainsAndEmpresaAndEstaActivo(String letras, Empresa empresa,
			Boolean estaActivo);
}
