package com.prueba.service;

import java.util.List;

import com.prueba.entity.Empresa;
import com.prueba.entity.TipoActivo;

public interface TipoActivoService {

	TipoActivo create(TipoActivo tipoActivo, Empresa empresa);

	List<TipoActivo> findByNameAndEmpreaAndEstaActivo(String letras, Empresa empresa, Boolean estaActivo);

	List<TipoActivo> list(Empresa empresa);

	TipoActivo getFamilia(Long id, Empresa empresa);

	void delete(Long id, Empresa empresa);

	void unable(Long id, Empresa empresa);

	TipoActivo update(Long id, TipoActivo tipoActivo, Empresa empresa);

}
