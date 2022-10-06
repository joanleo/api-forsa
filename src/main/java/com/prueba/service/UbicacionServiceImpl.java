package com.prueba.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.prueba.dto.UbicacionDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Ubicacion;
import com.prueba.exception.ResourceAlreadyExistsException;
import com.prueba.exception.ResourceCannotBeAccessException;
import com.prueba.exception.ResourceCannotBeDeleted;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.UbicacionRepository;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.specifications.UbicacionSpecifications;
import com.prueba.util.UtilitiesApi;


@Service
public class UbicacionServiceImpl implements UbicacionService {
	
	@Autowired
	private UbicacionRepository ubicacionRepo;
	
	@Autowired
	private UsuarioRepository usuarioRepo;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UbicacionSpecifications ubicacionSpec;
	
	@Autowired
	private UtilitiesApi util;
	
	@Override
	public UbicacionDTO create(UbicacionDTO ubicacionDTO) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		Empresa empresa; 
		
		if(ubicacionDTO.getEmpresa() != null) {
			empresa = util.obtenerEmpresa(ubicacionDTO.getEmpresa().getNit());
		}else {
			empresa = usuario.getEmpresa();			
		}
		
		ubicacionDTO.setEmpresa(empresa);
		
		Ubicacion ubicacion = mapearDTO(ubicacionDTO);
		Ubicacion exist = ubicacionRepo.findByNombreAndEmpresa(ubicacionDTO.getNombre(), ubicacionDTO.getEmpresa());
		String verNombre = ubicacionDTO.getNombre().trim();
		if(verNombre.length() > 0) {
			if(exist == null) {
				ubicacionRepo.save(ubicacion);
			}else {
				throw new ResourceAlreadyExistsException("Ubicacion", "nombre", ubicacion.getNombre());
			}			
		}else {
			throw new ResourceCannotBeAccessException("El nombre debe ser un nombre valildo, no solo espacios");
		}
		
		return mapearEntidad(ubicacion);
	}

	@Override
	public List<UbicacionDTO> list(Empresa empresa) {
		List<Ubicacion> ubicaciones = ubicacionRepo.findByEmpresaAndEstaActivoTrue(empresa);
		return ubicaciones.stream().map(ubicacion -> mapearEntidad(ubicacion)).collect(Collectors.toList());
	}

	@Override
	public UbicacionDTO getUbicacion(Long id, Empresa empresa) {
		Ubicacion ubicacion = ubicacionRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Ubicacion", "id", id));
		
		return mapearEntidad(ubicacion);
	}

	@Override
	public UbicacionDTO update(Long id, UbicacionDTO ubicacionDTO) {
		Ubicacion ubicacion = ubicacionRepo.findByIdAndEmpresa(id, ubicacionDTO.getEmpresa())
				.orElseThrow(() -> new ResourceNotFoundException("Ubicacion", "id", id));
		Ubicacion ubicacionNombre = ubicacionRepo.findByNombreAndEmpresa(ubicacionDTO.getNombre(), ubicacionDTO.getEmpresa());
		if(!Objects.isNull(ubicacionNombre)) {
			throw new ResourceAlreadyExistsException("Ubicacion", "nombre", ubicacionDTO.getNombre());
		}
		if(ubicacionDTO.getNombre() != null) {
			ubicacion.setNombre(ubicacionDTO.getNombre());			
		}
		if(ubicacionDTO.getCiudad() != null) {
			ubicacion.setCiudad(ubicacionDTO.getCiudad());			
		}
		if(ubicacionDTO.getDireccion() != null) {
			ubicacion.setDireccion(ubicacionDTO.getDireccion());			
		}
		if(ubicacionDTO.getTipo() != null) {
			ubicacion.setTipo(ubicacionDTO.getTipo());			
		}
		
		ubicacionRepo.save(ubicacion);
		
		return mapearEntidad(ubicacion);
	}

	@Override
	public void delete(Long id, Empresa empresa) {
		Ubicacion ubicacion = ubicacionRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Ubicacion", "id", id));
		
		if(ubicacion.getProductos().size() > 0) {
			throw new ResourceCannotBeDeleted("Empresa");
		}
		
		ubicacionRepo.delete(ubicacion);

	}
	
	@Override
	public void unable(Long id, Empresa empresa) {
		Ubicacion ubicacion  = ubicacionRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Ubicacion", "id", id));
		
		Boolean estado = ubicacion.getEstaActivo();
		if(estado) {
			ubicacion.setEstaActivo(false);			
		}else {
			ubicacion.setEstaActivo(true);
		}

		ubicacionRepo.save(ubicacion);
		
	}
	
	public UbicacionDTO mapearEntidad(Ubicacion ubicacion) {
		return modelMapper.map(ubicacion, UbicacionDTO.class);
	}
	
	public Ubicacion mapearDTO(UbicacionDTO ubicacionDTO) {
		return modelMapper.map(ubicacionDTO, Ubicacion.class);
	}

	@Override
	public List<UbicacionDTO> findByName(String letras, Empresa empresa) {
		List<Ubicacion> ubicaciones = ubicacionRepo.findByNombreContainsAndEmpresaAndEstaActivoTrue(letras, empresa);
		return ubicaciones.stream().map(ubicacion -> mapearEntidad(ubicacion)).collect(Collectors.toList());
	}

	@Override
	public Page<Ubicacion> searchUbicaciones(UbicacionDTO ubicacionDTO, Empresa empresa, Integer pagina,
			Integer items) {
		if(items == 0) {
			Page<Ubicacion> ubicaciones = ubicacionRepo.findAll(ubicacionSpec.getUbicacion(ubicacionDTO, empresa), PageRequest.of(0, 10));
			return ubicaciones;
		}
		Page<Ubicacion> ubicaciones = ubicacionRepo.findAll(ubicacionSpec.getUbicacion(ubicacionDTO, empresa), PageRequest.of(pagina, items));		
		return ubicaciones;
	}

	@Override
	public Page<Ubicacion> searchUbicaciones(Empresa empresa, Integer pagina, Integer items) {
		if(items == 0) {
			Page<Ubicacion> ubicaciones = ubicacionRepo.findByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(0, 10));
			return ubicaciones;
		}
		Page<Ubicacion> ubicaciones = ubicacionRepo.findByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(pagina, items));		
		return ubicaciones;
	}

	@Override
	public List<UbicacionDTO> listUbicaciones(UbicacionDTO ubicacionDTO, Empresa empresa) {
		List<Ubicacion> fabricantes = ubicacionRepo.findAll(ubicacionSpec.getUbicacion(ubicacionDTO, empresa));
		return fabricantes.stream().map(ubicacion -> mapearEntidad(ubicacion)).collect(Collectors.toList());
	}

}
