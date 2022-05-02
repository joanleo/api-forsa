package com.prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.TipoEmpresa;

public interface TipoEmpresaRepository extends JpaRepository<TipoEmpresa, Long> {

	public TipoEmpresa findByTipo(String tipo);
}
