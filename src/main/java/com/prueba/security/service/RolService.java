package com.prueba.security.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.prueba.entity.Empresa;
import com.prueba.security.dto.RolDTO;
import com.prueba.security.entity.Politica;
import com.prueba.security.entity.Rol;


public interface RolService {
	
	public RolDTO create(RolDTO rolDTO);
	
	public List<Rol> list(String letras, Empresa empresa);
	
	public RolDTO getRol(Long id, Empresa empresa);
	
	public RolDTO update(Long id, RolDTO rolDTO);
	
	public void delete(Long id);

	public List<Rol> list(Empresa empresa);

	public Page<Rol> searchRoles(Empresa empresa, Integer pagina, Integer items);

	public Page<Rol> serachRoles(RolDTO rolDTO, Empresa empresa, Integer pagina, Integer items);

	public List<Politica> listarPoliticas(Long role);

	public Politica actualizarPoliticar(Long idPolitica, Politica politica);

	/**
	 * @param id
	 * @param empresa
	 */
	public void deshabilitar(Long id, Empresa empresa);

	/**
	 * @param rolDTO
	 * @param empresa
	 * @param pagina
	 * @param items
	 * @return
	 */
	public List<Rol> buscarRoles(RolDTO rolDTO, Empresa empresa);


}
