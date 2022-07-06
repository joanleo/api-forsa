package com.prueba.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.prueba.dto.FabricanteDTO;
import com.prueba.dto.TipoUbicacionDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.TipoUbicacion;


public interface TipoUbicacionService {
	
public TipoUbicacionDTO create(TipoUbicacionDTO tipoUbicacionDTO);
	
	//public List<TipoUbicacionDTO> list();
	
	//public TipoUbicacionDTO getTipoMov(Long id);
	
	public TipoUbicacionDTO update(Long id, TipoUbicacionDTO tipoUbicacionDTO);
	
	//public void delete(Long id);

	void unable(Long id, Empresa empresa);

	void delete(Long id, Empresa empresa);

	TipoUbicacionDTO getTipoMov(Long id, Empresa empresa);

	List<TipoUbicacionDTO> list(Empresa empresa);

	List<TipoUbicacionDTO> findByNombreAndEmpresaAndEstaActivoTrue(String letras, Empresa empresa);

	public Page<TipoUbicacion> searchTipoUbicacion(TipoUbicacionDTO tipoUbicacionDTO, Empresa empresa, Integer pagina,
			Integer items);

	public Page<TipoUbicacion> searchTipoUbicacion(Empresa empresa, Integer pagina, Integer items);

	public List<TipoUbicacionDTO> listTipoUbicacion(TipoUbicacionDTO tipoUbicacionDTO, Empresa empresa, boolean b);



}
