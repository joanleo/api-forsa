package com.prueba.security.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.exception.ResourceNotFoundException;
import com.prueba.security.dto.RolDTO;
import com.prueba.security.entity.Rol;
import com.prueba.security.repository.RolRepository;

@Service
public class RolServiceImpl implements RolService{
	
	@Autowired
	private RolRepository rolRepo;
	
	@Autowired
	private ModelMapper modelmapper;

	@Override
	public RolDTO create(RolDTO rolDTO) {
		Rol rol = mapearDTO(rolDTO);
		Rol exist = rolRepo.findByNombre(rol.getNombre());
		if(exist == null) {
			rolRepo.save(rol);
		}else {
			throw new IllegalAccessError("El Rol que desea crear ya existe: " + rol.getNombre() + "Descripcion :" + rol.getDescripcion());
		}
		
		return mapearEntidad(rol);
	}

	@Override
	public List<RolDTO> list() {
		List<Rol> roles = rolRepo.findAll();
		
		return roles.stream().map(rol -> mapearEntidad(rol)).collect(Collectors.toList());
	}

	@Override
	public RolDTO getRol(Long id) {
		Rol rol = rolRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ROL", "id", id));
		
		return mapearEntidad(rol);
	}

	@Override
	public RolDTO update(Long id, RolDTO rolDTO) {
		Rol rol = rolRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ROL", "id", id));
		
		rol.setNombre(rolDTO.getNombre());
		rol.setDescripcion(rolDTO.getDescripcion());
		
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

}