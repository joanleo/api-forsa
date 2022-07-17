package com.prueba.security.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import com.prueba.specifications.RolSpecifications;

@Service
public class RolServiceImpl implements RolService{
	
	@Autowired
	private RolRepository rolRepo;
	
	@Autowired
	private ModelMapper modelmapper;
	
	@Autowired
	private UsuarioRepository usuarioRepo;

	@Autowired
	private RolSpecifications rolSpec;
	
	@Override
	public RolDTO create(RolDTO rolDTO) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
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
	public List<Rol> list(String letras, Empresa empresa) {
		System.out.println("Servicio: ");
		List<Rol> roles = rolRepo.findByNombreContainsAndEmpresaAndEstaActivoTrue(letras, empresa);
		
		return roles;//.stream().map(rol -> mapearEntidad(rol)).collect(Collectors.toList());
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
		
		rolRepo.save(rol);
		
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
	public List<Rol> list(Empresa empresa) {
		List<Rol> roles = rolRepo.findByEmpresaAndEstaActivoTrue(empresa);
		return roles;//.stream().map(rol -> mapearEntidad(rol)).collect(Collectors.toList());
	}

	@Override
	public Page<Rol> searchRoles(Empresa empresa, Integer pagina, Integer items) {
		if(items == 0) {
			Page<Rol> roles = rolRepo.findByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(0, 10));
			return roles;
		}
		Page<Rol> roles = rolRepo.findByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(pagina, items));
		return roles;
	}

	@Override
	public Page<Rol> serachRoles(RolDTO rolDTO, Empresa empresa, Integer pagina, Integer items) {
		if(items == 0) {
			Page<Rol> roles = rolRepo.findAll(rolSpec.getRoles(rolDTO, empresa), PageRequest.of(0, 10));
			return roles;
		}
		Page<Rol> roles = rolRepo.findAll(rolSpec.getRoles(rolDTO, empresa), PageRequest.of(pagina, items));
		return roles;
	}

}