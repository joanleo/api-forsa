package com.prueba.security.service;

import java.util.List;

import com.prueba.security.dto.RutaDTO;


public interface RutaService {

	public RutaDTO create(RutaDTO rutaDTO);
	
	public List<RutaDTO> list();
	
	public RutaDTO getRuta(Long id);
	
	public RutaDTO updateRuta(Long id, RutaDTO rutaDTO);
	
	public void delete(Long id);
}
