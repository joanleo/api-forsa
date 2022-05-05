package com.prueba.service;

import java.util.List;

import com.prueba.dto.FamiliaDTO;

public interface FamiliaService {

	public FamiliaDTO create(FamiliaDTO familiaDto);
	
	public List<FamiliaDTO> list();
	
	public FamiliaDTO getFamilia(Long id);
	
	public FamiliaDTO update(Long id, FamiliaDTO familiaDTO);
	
	public void delete(Long id);
	
	public List<FamiliaDTO> findByName(String name);

}
