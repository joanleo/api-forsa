package com.prueba.service;

import java.util.List;

import com.prueba.dto.TipoMovDTO;
import com.prueba.entity.Empresa;


public interface TipoMovService {
	
	public TipoMovDTO create(TipoMovDTO tipoMovDTO);
	
	public List<TipoMovDTO> list(Empresa empresa);
	
	public TipoMovDTO getTipoMov(Long id, Empresa empresa);
	
	public TipoMovDTO update(Long id, TipoMovDTO tipoMovDTO);
	
	public void delete(Long id, Empresa empresa);
	
	public void unable(Long id, Empresa empresa);

	public List<TipoMovDTO> findByNombreAndEmpresaAndEstaActivo(String letras, Empresa empresa, Boolean estaActivo);

}
