package com.prueba.service;

import java.util.List;

import com.prueba.dto.TipoUbicacionDTO;


public interface TipoUbicacionService {
	
public TipoUbicacionDTO create(TipoUbicacionDTO tipoUbicacionDTO);
	
	public List<TipoUbicacionDTO> list();
	
	public TipoUbicacionDTO getTipoMov(Long id);
	
	public TipoUbicacionDTO update(Long id, TipoUbicacionDTO tipoUbicacionDTO);
	
	public void delete(Long id);

}
