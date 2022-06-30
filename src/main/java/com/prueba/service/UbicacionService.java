package com.prueba.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.prueba.dto.UbicacionDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Ubicacion;

public interface UbicacionService {
	
	public UbicacionDTO create(UbicacionDTO ubicacionDTO);
	
	//public List<UbicacionDTO> list();
	
	//public UbicacionDTO getUbicacion(Long id);
	
	public UbicacionDTO update(Long id, UbicacionDTO ubicacionDTO);
	
	//public void delete(Long id);
	
	//public List<UbicacionDTO> findByName(String name);

	List<UbicacionDTO> list(Empresa empresa);

	UbicacionDTO getUbicacion(Long id, Empresa empresa);

	void delete(Long id, Empresa empresa);

	void unable(Long id, Empresa empresa);

	List<UbicacionDTO> findByName(String name, Empresa empresa, Boolean estaActivo);

	public Page<Ubicacion> searchUbicaciones(UbicacionDTO ubicacionDTO, Empresa empresa, Integer pagina, Integer items);

	public Page<Ubicacion> searchUbicaciones(Empresa empresa, Integer pagina, Integer items);

	public List<UbicacionDTO> listUbicaciones(UbicacionDTO ubicacionDTO, Empresa empresa);

}
