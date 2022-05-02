package com.prueba.service;

import java.util.List;

import com.prueba.dto.TipoMovDTO;


public interface TipoMovService {
	
	public TipoMovDTO create(TipoMovDTO tipoMovDTO);
	
	public List<TipoMovDTO> list();
	
	public TipoMovDTO getTipoMov(Long id);
	
	public TipoMovDTO update(Long id, TipoMovDTO tipoMovDTO);
	
	public void delete(Long id);

}
