package com.prueba.service;

import java.util.List;


import org.springframework.data.domain.Page;

import com.prueba.dto.EmpresaDTO;
import com.prueba.entity.Empresa;

public interface EmpresaService {

	public EmpresaDTO create(EmpresaDTO empresaDTO);
	
	public List<EmpresaDTO> list(String letras);
	
	public List<EmpresaDTO> list();

	public EmpresaDTO getEmpresa(Long id);
	
	public EmpresaDTO update(Long id, EmpresaDTO empresaDTO);
	
	public void delete(Long id);
	
	public Page<Empresa> searchEmpresas(EmpresaDTO empresaDTO, Integer pagina, Integer items);

	public Page<Empresa> searchEmpresas(Integer pagina, Integer items);

	public void unable(Long id);

	public List<EmpresaDTO> findByNameAndEstaActivo(String name);

	public List<EmpresaDTO> list(EmpresaDTO empresaDTO);


}
