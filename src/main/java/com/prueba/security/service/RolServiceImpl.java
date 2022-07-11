package com.prueba.security.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.prueba.entity.Empresa;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.security.dto.RolDTO;
import com.prueba.security.entity.Rol;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.RolRepository;
import com.prueba.security.repository.UsuarioRepository;

@Service
public class RolServiceImpl implements RolService{
	
	@Autowired
	private RolRepository rolRepo;
	
	@Autowired
	private ModelMapper modelmapper;
	
	@Autowired
	private UsuarioRepository usuarioRepo;

	@Override
	public RolDTO create(RolDTO rolDTO) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByUsernameOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(rolDTO.getEmpresa() == null) {
			rolDTO.setEmpresa(usuario.getEmpresa());
		}
		
		Rol rol = mapearDTO(rolDTO);
		Rol exist = rolRepo.findByNombreAndEmpresaAndEstaActivoTrue(rol.getNombre(), rol.getEmpresa());
		if(exist == null) {
			rolRepo.save(rol);
		}else {
			throw new IllegalAccessError("El Rol que desea crear ya existe: " + rol.getNombre());
		}
		
		return mapearEntidad(rol);
	}

	@Override
	public List<RolDTO> list(String letras, Empresa empresa) {
		System.out.println("Servicio: ");
		List<Rol> roles = rolRepo.findByNombreContainsAndEmpresaAndEstaActivoTrue(letras, empresa);
		
		return roles.stream().map(rol -> mapearEntidad(rol)).collect(Collectors.toList());
	}

	@Override
	public RolDTO getRol(Long id, Empresa empresa) {
		Rol rol = rolRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ROL", "id", id));
		
		return mapearEntidad(rol);
	}

	@Override
	public RolDTO update(Long id, RolDTO rolDTO) {
		Rol rol = rolRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ROL", "id", id));
		
		rol.setNombre(rolDTO.getNombre());
		
		return mapearEntidad(rol);
	}

	@Override
	public void delete(Long id) {
		Rol rol = rolRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ROL", "id", id));
		
		rolRepo.delete(rol);
	}
	
	public RolDTO mapearEntidad(Rol rol) {
		return modelmapper.map(rol, RolDTO.class);
	}
	
	public Rol mapearDTO(RolDTO rolDTO) {
		return modelmapper.map(rolDTO, Rol.class);
	}

	@Override
	public List<RolDTO> list(Empresa empresa) {
		List<Rol> roles = rolRepo.findByEmpresaAndEstaActivoTrue(empresa);
		return roles.stream().map(rol -> mapearEntidad(rol)).collect(Collectors.toList());
	}

}