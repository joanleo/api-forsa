package com.prueba.security.service;

import java.util.List;

import com.prueba.security.dto.RolDTO;


public interface RolService {
	
	public RolDTO create(RolDTO rolDTO);
	
	public List<RolDTO> list();
	
	public RolDTO getRol(Long id);
	
	public RolDTO update(Long id, RolDTO rolDTO);
	
	public void delete(Long id);

}
