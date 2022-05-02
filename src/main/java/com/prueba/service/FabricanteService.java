package com.prueba.service;

import java.util.List;

import com.prueba.dto.FabricanteDTO;

public interface FabricanteService {
	
	public FabricanteDTO create(FabricanteDTO fabricanteDTO);
	
	public List<FabricanteDTO> list();
	
	public FabricanteDTO getFabricante(Long id);
	
	public FabricanteDTO update(Long id, FabricanteDTO fabricanteDTO);
	
	public void delete(Long id);
	
	List<FabricanteDTO> findByName(String nombre);

}
