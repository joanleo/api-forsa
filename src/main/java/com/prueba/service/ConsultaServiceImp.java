package com.prueba.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.entity.Consulta;
import com.prueba.repository.ConsultaRepository;

@Service
public class ConsultaServiceImp implements ConsultaService {

	@Autowired
	private ConsultaRepository consultaRepo;
	
	@Override
	public List<Consulta> encontrarDatosPorEmpresa(Long nitempresa) {
		
		List<Consulta> consultas = consultaRepo.findByNitEmpresa(nitempresa);
		return consultas;
	}

}
