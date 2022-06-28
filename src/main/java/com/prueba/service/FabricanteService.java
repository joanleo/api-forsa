package com.prueba.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.prueba.dto.FabricanteDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Fabricante;

public interface FabricanteService {
	
	public FabricanteDTO create(FabricanteDTO fabricanteDTO);
	
	public List<FabricanteDTO> list(Empresa empresa);
	
	public FabricanteDTO getFabricante(Long id, Empresa empresa);
	
	public FabricanteDTO update(Long id, FabricanteDTO fabricanteDTO);
	
	public void delete(Long id, Empresa empresa);
	
	//List<FabricanteDTO> findByNameAndEstaActivo(String nombre);

	public void unable(Long id, Empresa empresa);

	public List<FabricanteDTO> findByNameAndEmpresaAndEstaActivo(String letras, Empresa empresa, Boolean estaActivo);

	public Page<Fabricante> searchFabricantes(String letras, Empresa empresa, Integer pagina, Integer items);

	public Page<Fabricante> searchFabricantes(Empresa empresa, Integer pagina, Integer items);


}
