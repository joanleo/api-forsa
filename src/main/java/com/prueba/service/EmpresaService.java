package com.prueba.service;

import java.util.List;

import com.prueba.dto.EmpresaDTO;

public interface EmpresaService {

	public EmpresaDTO create(EmpresaDTO empresaDTO);
	
	public List<EmpresaDTO> list();
	
	public EmpresaDTO getEmpresa(Long id);
	
	public EmpresaDTO update(Long id, EmpresaDTO empresaDTO);
	
	public void delete(Long id);
	
	public List<EmpresaDTO> findByName(String name);
}
