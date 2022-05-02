package com.prueba.service;

import java.util.List;

import com.prueba.dto.EstadoDTO;


public interface EstadoService {
	
	public EstadoDTO create(EstadoDTO estadoDTO);
	
	public List<EstadoDTO> list();
	
	public EstadoDTO getEstado(Long id);
	
	public EstadoDTO update(Long id, EstadoDTO estadoDTO);
	
	public void delete(Long id);

}
