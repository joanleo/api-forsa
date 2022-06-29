package com.prueba.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.prueba.dto.FamiliaDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Familia;

public interface FamiliaService {

	public FamiliaDTO create(FamiliaDTO familiaDto, Empresa empresa);
	
	public List<FamiliaDTO> list(Empresa empresa);
	
	public FamiliaDTO getFamilia(Long id, Empresa empresa);
	
	public FamiliaDTO update(Long id, FamiliaDTO familiaDTO, Empresa empresa);
	
	public void delete(Long id, Empresa empresa);
	
	public List<FamiliaDTO> findByNameAndEmpresa(String name, Empresa empresa);

	public void unable(Long id, Empresa empresa);

	public List<FamiliaDTO> findByNameAndEmpreaAndEstaActiva(String letras, Empresa empresa, Boolean estaActiva);

	public Page<Familia> searchFabricantes(FamiliaDTO familiaDTO, Empresa empresa, Integer pagina, Integer items);

	public Page<Familia> searchFabricantes(Empresa empresa, Integer pagina, Integer items);

	public List<FamiliaDTO> listFamilias(FamiliaDTO familiaDTO, Empresa empresa);

}
