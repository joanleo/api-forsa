package com.prueba.service;

import java.util.List;

import com.prueba.dto.TipoEmpresaDTO;

public interface TipoEmpresaService {

	public TipoEmpresaDTO create(TipoEmpresaDTO tipoEmpresaDTO);

	public List<TipoEmpresaDTO> list();

	public TipoEmpresaDTO getTipoEmpresa(Long id);

	public TipoEmpresaDTO update(Long id, TipoEmpresaDTO tipoEmpresaDTO);

	public void delete(Long id);
}
