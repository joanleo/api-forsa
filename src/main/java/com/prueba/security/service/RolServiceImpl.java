package com.prueba.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.prueba.entity.DetalleRutina;
import com.prueba.entity.Empresa;
import com.prueba.entity.Rutina;
import com.prueba.exception.ResourceAlreadyExistsException;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.RutinaRepository;
import com.prueba.security.dto.RolDTO;
import com.prueba.security.entity.Politica;
import com.prueba.security.entity.Rol;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.PoliticaRepository;
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
	
	@Autowired
	private PoliticaRepository politicaRepo;
	
	@Autowired
	private RutinaRepository rutinaRepo;
	
	@Override
	public RolDTO create(RolDTO rolDTO) throws IllegalArgumentException {
		Pattern special = Pattern.compile("[!@#$%&*()+=|<>?:;{}/./,\\\\[\\\\^'\"]~]", Pattern.CASE_INSENSITIVE);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		Matcher matcher = special.matcher(rolDTO.getNombre());
		boolean errorNombre = matcher.find();
		
		if(errorNombre) {
			throw new IllegalArgumentException("El nombre no debe contener caracteres espaciales []!@#$%&*()+=|<>?{},.:;");
		}
		
		if(rolDTO.getEmpresa() == null) {
			rolDTO.setEmpresa(usuario.getEmpresa());
		}
		
		//Rol rol = mapearDTO(rolDTO);
		Rol exist = rolRepo.findByNombreAndEmpresaAndEstaActivoTrue(rolDTO.getNombre(), rolDTO.getEmpresa());
		if(exist == null) {
			exist = new Rol(rolDTO.getNombre());
			exist.setEmpresa(rolDTO.getEmpresa());
			List<Rutina> listRutinas = rutinaRepo.findAll();
			List<DetalleRutina> listaDetalle = new ArrayList<>();
			for(Rutina rutina: listRutinas) {
            	for(DetalleRutina detalle: rutina.getDetalles()) {
            		listaDetalle.add(detalle);
            	}
            }
			for(DetalleRutina detalle: listaDetalle) {
            	Politica politica = new Politica(exist, detalle, false);
            	politica = politicaRepo.save(politica);
            }
			rolRepo.save(exist);
		}else {
			throw new ResourceAlreadyExistsException("Rol", "Nombre", rolDTO.getNombre());
		}
		
		return mapearEntidad(exist);
	}

	@Override
	public List<Rol> list(String letras, Empresa empresa) {
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
		Rol rol = rolRepo.findByIdRol(id);
		if(Objects.isNull(rol)) {
			throw new ResourceNotFoundException("Rol", "id", id);
		}
		rol.setEstaActivo(false);
		rol = rolRepo.save(rol);
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

	@Override
	public List<Politica> listarPoliticas(Long idRole) {
		Rol rol = rolRepo.findByIdRol(idRole);
		if(Objects.isNull(rol)) {
			throw new ResourceNotFoundException("Rol", "Id", idRole);
		}
		List<Politica> politicas = politicaRepo.findByRol(rol);
		return politicas;
	}

	@Override
	public Politica actualizarPoliticar(Long idPolitica, Politica politica) {
		Politica exist = politicaRepo.findByIdPolitica(idPolitica);
		if(!Objects.isNull(exist)) {
			exist.setPermiso(politica.getPermiso());
			politicaRepo.save(exist);
			return exist;
		}else {
			throw new ResourceNotFoundException("Politica", "id", idPolitica);
		}
		
	}

}