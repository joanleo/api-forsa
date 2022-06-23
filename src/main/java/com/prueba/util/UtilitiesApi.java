package com.prueba.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.entity.Empresa;
import com.prueba.repository.EmpresaRepository;

@Service
public class UtilitiesApi {
	
	@Autowired
	private EmpresaRepository empresaRepo;

	public Empresa obtenerEmpresa(Long nit) {
		Empresa exist = empresaRepo.findByNit(nit);
		if (exist != null) {
			return exist;
		} else {
			throw new IllegalAccessError("La empresa " + nit + " no existe en la base de datos");
		}
	}
}
