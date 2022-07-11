package com.prueba.security.service;

import java.util.List;

import com.prueba.entity.Empresa;
import com.prueba.security.dto.RolDTO;


public interface RolService {
	
	public RolDTO create(RolDTO rolDTO);
	
	public List<RolDTO> list(String letras, Empresa empresa);
	
	public RolDTO getRol(Long id, Empresa empresa);
	
	public RolDTO update(Long id, RolDTO rolDTO);
	
	public void delete(Long id);

	public List<RolDTO> list(Empresa empresa);


}
