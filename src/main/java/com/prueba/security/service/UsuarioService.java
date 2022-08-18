package com.prueba.security.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;

import com.prueba.entity.Empresa;
import com.prueba.security.dto.RegistroDTO;
import com.prueba.security.entity.Usuario;

public interface UsuarioService {
	
	public Usuario update(Long id, RegistroDTO registroDTO) throws Exception;

	public List<Usuario> findByNombreAndEmpresaAndEstaActivo(String letras, Empresa empresa);

	public void delete(Long nitFabricante, Empresa empresa) throws Exception;

	public List<Usuario> list(Empresa empresa);

	/**
	 * @param id
	 * @param empresa
	 */
	public void deshabilitar(Long id, Empresa empresa);

	/**
	 * @param registroDTO
	 * @param empresa
	 * @return
	 */
	public List<Usuario> listUsuarios(RegistroDTO registroDTO, Empresa empresa);

	public Page<Usuario> buscarUsuarios(@Valid RegistroDTO registroDTO, Empresa empresa, Integer pagina, Integer items);

	public Page<Usuario> buscarUsuarios(Empresa empresa, Integer pagina, Integer items);
	
	

}
