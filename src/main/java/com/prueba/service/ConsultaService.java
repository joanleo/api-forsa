package com.prueba.service;

import java.util.List;

import com.prueba.entity.Consulta;

public interface ConsultaService {

	List<Consulta> encontrarDatosPorEmpresa(Long nitempresa);

}
