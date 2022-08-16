package com.prueba.repository;

import java.util.List;

import com.prueba.entity.Consulta;

public interface ConsultaRepository extends ReadOnlyRepository<Consulta, Integer> {

	List<Consulta> findByNitEmpresa(Long nitempresa);

}
