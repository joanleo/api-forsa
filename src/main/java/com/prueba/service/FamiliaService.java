package com.prueba.service;

import java.util.List;

import com.prueba.dto.FamiliaDTO;
import com.prueba.entity.Empresa;

public interface FamiliaService {

	public FamiliaDTO create(FamiliaDTO familiaDto, Empresa empresa);
	
	public List<FamiliaDTO> list(Empresa empresa);
	
	public FamiliaDTO getFamilia(Long id, Empresa empresa);
	
	public FamiliaDTO update(Long id, FamiliaDTO familiaDTO, Empresa empresa);
	
	public void delete(Long id, Empresa empresa);
	
	public List<FamiliaDTO> findByNameAndEmpresa(String name, Empresa empresa);

	public void unable(Long id, Empresa empresa);

	public List<FamiliaDTO> findByNameAndEmpreaAndEstaActiva(String letras, Empresa empresa, Boolean estaActiva);

}
