package com.prueba.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.prueba.dto.TipoEmpresaDTO;
import com.prueba.entity.TipoEmpresa;

public interface TipoEmpresaService {

	public TipoEmpresaDTO create(TipoEmpresaDTO tipoEmpresaDTO);

	public List<TipoEmpresaDTO> list();
	
	public List<TipoEmpresaDTO> list(String letras);

	public TipoEmpresaDTO getTipoEmpresa(Long id);

	public TipoEmpresaDTO update(Long id, TipoEmpresaDTO tipoEmpresaDTO);

	public void delete(Long id);

	public Page<TipoEmpresa> searchTiposEmpresa(String letras, Integer pagina, Integer items);

	public Page<TipoEmpresa> searchTiposEmpresa(Integer pagina, Integer items);

	public void unable(Long id);
}
