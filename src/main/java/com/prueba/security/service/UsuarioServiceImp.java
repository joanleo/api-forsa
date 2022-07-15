package com.prueba.security.service;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.prueba.entity.Empresa;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.EmpresaRepository;
import com.prueba.security.dto.RegistroDTO;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.specifications.UsuarioSpecifications;

@Service
public class UsuarioServiceImp implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private EmpresaRepository empresaRepo;
	
	@Autowired
	private UsuarioSpecifications usuarioSpec;
	
	@Override
	public Usuario update(Long id, RegistroDTO registroDTO) throws Exception {
		Empresa empresa = empresaRepo.findByNit(registroDTO.getEmpresa().getNit());
		if(Objects.isNull(empresa)) {
			throw new Exception("Empresa no existe");
		}
		Usuario usuario = usuarioRepo.findByNombreAndEmpresa(registroDTO.getNombre(), registroDTO.getEmpresa());
		usuarioRepo.save(usuario);		
		return usuario;
	}

	@Override
	public Page<Usuario> searchFabricantes(Empresa empresa, Integer pagina, Integer items) {
		if(items == 0) {
			Page<Usuario> usuarios = usuarioRepo.findByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(0, 10));
			return usuarios;
		}
		Page<Usuario> usuarios = usuarioRepo.findByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(pagina, items));		
		return usuarios;
	}

	@Override
	public Page<Usuario> searchFabricantes(@Valid RegistroDTO registroDTO, Empresa empresa, Integer pagina,
			Integer items) {
		if(items == 0) {
			Page<Usuario> usuarios = usuarioRepo.findAll(usuarioSpec.getUsuarios(registroDTO, empresa), PageRequest.of(0, 10));
			return usuarios;
		}
		Page<Usuario> usuarios = usuarioRepo.findAll(usuarioSpec.getUsuarios(registroDTO, empresa), PageRequest.of(pagina, items));		
		return usuarios;
	}

	@Override
	public List<Usuario> findByNombreAndEmpresaAndEstaActivo(String letras, Empresa empresa) {
		List<Usuario> usuarios = usuarioRepo.findByNombreContainsAndEmpresaAndEstaActivoTrue(letras, empresa);
		return usuarios;
	}

	@Override
	public void delete(Long id, Empresa empresa) throws Exception{
		Usuario usuario = usuarioRepo.findByIdAndEmpresa(id, empresa);
		if(Objects.isNull(usuario)) {
			throw new ResourceNotFoundException("Usuario", "id", id);
		}
		usuario.setEstaActivo(false);
		usuario = usuarioRepo.save(usuario);
	}

	@Override
	public List<Usuario> list(Empresa empresa) {
		// TODO Auto-generated method stub
		return usuarioRepo.findByEmpresaAndEstaActivoTrue(empresa);
	}

}
