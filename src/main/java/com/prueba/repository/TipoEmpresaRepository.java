package com.prueba.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.prueba.entity.TipoEmpresa;

public interface TipoEmpresaRepository extends JpaRepository<TipoEmpresa, Long>,JpaSpecificationExecutor<TipoEmpresa> {

	public TipoEmpresa findByTipo(String tipo);

	public Page<TipoEmpresa> findByTipoContainsAndEstaActivoTrue(String letras, Pageable page);

	public Page<TipoEmpresa> findByEstaActivoTrue(Pageable page);

	public List<TipoEmpresa> findByEstaActivoTrue();

	public List<TipoEmpresa> findByTipoContainsAndEstaActivoTrue(String letras);
}
