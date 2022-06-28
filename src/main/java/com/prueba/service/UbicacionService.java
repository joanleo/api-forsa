package com.prueba.service;

import java.util.List;

import com.prueba.dto.UbicacionDTO;
import com.prueba.entity.Empresa;

public interface UbicacionService {
	
	public UbicacionDTO create(UbicacionDTO ubicacionDTO);
	
	//public List<UbicacionDTO> list();
	
	//public UbicacionDTO getUbicacion(Long id);
	
	public UbicacionDTO update(Long id, UbicacionDTO ubicacionDTO);
	
	//public void delete(Long id);
	
	//public List<UbicacionDTO> findByName(String name);

	List<UbicacionDTO> list(Empresa empresa);

	UbicacionDTO getUbicacion(Long id, Empresa empresa);

	void delete(Long id, Empresa empresa);

	void unable(Long id, Empresa empresa);

	List<UbicacionDTO> findByName(String name, Empresa empresa, Boolean estaActivo);

}
