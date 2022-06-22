package com.prueba.service;

import java.util.List;

import com.prueba.dto.EstadoDTO;
import com.prueba.entity.Empresa;


public interface EstadoService {
	
	public EstadoDTO create(EstadoDTO estadoDTO);
	
	public List<EstadoDTO> list(Empresa empresa);
	
	public EstadoDTO getEstado(Long id, Empresa empresa);
	
	public EstadoDTO update(Long id, EstadoDTO estadoDTO);
	
	public void delete(Long id, Empresa empresa);
	
	public List<EstadoDTO> findByTipoAndEmpresaAndEstaActivo(String tipo, Empresa empresa, Boolean estaActivo);

	public void unable(Long id, Empresa empresa);

}
