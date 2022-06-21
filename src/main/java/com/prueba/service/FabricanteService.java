package com.prueba.service;

import java.util.List;

import com.prueba.dto.FabricanteDTO;
import com.prueba.entity.Empresa;

public interface FabricanteService {
	
	public FabricanteDTO create(FabricanteDTO fabricanteDTO);
	
	public List<FabricanteDTO> list(Empresa empresa);
	
	public FabricanteDTO getFabricante(Long id);
	
	public FabricanteDTO update(Long id, FabricanteDTO fabricanteDTO);
	
	public void delete(Long id);
	
	//List<FabricanteDTO> findByNameAndEstaActivo(String nombre);

	public void unable(Long id);

	public List<FabricanteDTO> findByNameAndEmpresaAndEstaActivo(String letras, Empresa empresa, Boolean estaActivo);


}
