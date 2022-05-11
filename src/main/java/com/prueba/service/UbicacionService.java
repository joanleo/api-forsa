package com.prueba.service;

import java.util.List;

import com.prueba.dto.UbicacionDTO;

public interface UbicacionService {
	
	public UbicacionDTO create(UbicacionDTO ubicacionDTO);
	
	public List<UbicacionDTO> list();
	
	public UbicacionDTO getUbicacion(Long id);
	
	public UbicacionDTO update(Long id, UbicacionDTO ubicacionDTO);
	
	public void delete(Long id);
	
	public List<UbicacionDTO> findByName(String name);

}
