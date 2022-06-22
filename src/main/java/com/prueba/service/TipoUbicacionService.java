package com.prueba.service;

import java.util.List;

import com.prueba.dto.TipoUbicacionDTO;
import com.prueba.entity.Empresa;


public interface TipoUbicacionService {
	
public TipoUbicacionDTO create(TipoUbicacionDTO tipoUbicacionDTO);
	
	//public List<TipoUbicacionDTO> list();
	
	//public TipoUbicacionDTO getTipoMov(Long id);
	
	public TipoUbicacionDTO update(Long id, TipoUbicacionDTO tipoUbicacionDTO);
	
	//public void delete(Long id);

	void unable(Long id, Empresa empresa);

	void delete(Long id, Empresa empresa);

	TipoUbicacionDTO getTipoMov(Long id, Empresa empresa);

	List<TipoUbicacionDTO> list(Empresa empresa);

	List<TipoUbicacionDTO> findByNombreAndEmpresaAndEstaActivo(String letras, Empresa empresa, Boolean estaActivo);

}
