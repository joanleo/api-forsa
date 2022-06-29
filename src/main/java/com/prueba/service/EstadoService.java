package com.prueba.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.prueba.dto.EstadoDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Estado;


public interface EstadoService {
	
	public EstadoDTO create(EstadoDTO estadoDTO);
	
	public List<EstadoDTO> list(Empresa empresa);
	
	public EstadoDTO getEstado(Long id, Empresa empresa);
	
	public EstadoDTO update(Long id, EstadoDTO estadoDTO);
	
	public void delete(Long id, Empresa empresa);
	
	public List<EstadoDTO> findByTipoAndEmpresaAndEstaActivo(String tipo, Empresa empresa, Boolean estaActivo);

	public void unable(Long id, Empresa empresa);

	Page<Estado> searchEstado(String letras, Empresa empresa, Integer pagina, Integer items);

	Page<Estado> searchEstado(Empresa empresa, Integer pagina, Integer items);

}
