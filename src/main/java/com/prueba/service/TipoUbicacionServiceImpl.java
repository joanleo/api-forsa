package com.prueba.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.prueba.dto.TipoUbicacionDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.TipoUbicacion;
import com.prueba.exception.ResourceAlreadyExistsException;
import com.prueba.exception.ResourceCannotBeDeleted;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.TipoUbicacionRepository;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.specifications.TipoUbicacionSpecifications;
import com.prueba.util.UtilitiesApi;

@Service
public class TipoUbicacionServiceImpl implements TipoUbicacionService {
	
	@Autowired
	private TipoUbicacionRepository tipoUbicRepo;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private TipoUbicacionSpecifications tipoUbicacionSpec;
	
	@Autowired
	private UtilitiesApi util;

	@Override
	public TipoUbicacionDTO create(TipoUbicacionDTO tipoUbicacionDTO) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		Empresa empresa; 
		
		if(tipoUbicacionDTO.getEmpresa() != null) {
			empresa = util.obtenerEmpresa(tipoUbicacionDTO.getEmpresa().getNit());
		}else {
			empresa = usuario.getEmpresa();			
		}
		
		tipoUbicacionDTO.setEmpresa(empresa);
		
		TipoUbicacion tipoUbicacion = new TipoUbicacion();
		TipoUbicacion exist = tipoUbicRepo.findByNombreAndEmpresaAndEstaActivoTrue(tipoUbicacion.getNombre(), tipoUbicacionDTO.getEmpresa());
		if(exist == null) {
			tipoUbicacion.setEmpresa(empresa);
			tipoUbicacion.setNombre(tipoUbicacionDTO.getNombre());
			exist = tipoUbicRepo.save(tipoUbicacion);
		}else {
			throw new ResourceAlreadyExistsException("Tipo de ubicacion", "nombre", tipoUbicacionDTO.getNombre());
		}
		
		return mapearEntidad(exist);
	}

	@Override
	public List<TipoUbicacionDTO> list(Empresa empresa) {
		List<TipoUbicacion> lista = tipoUbicRepo.findByEmpresaAndEstaActivoTrue(empresa);
		
		return lista.stream().map(tipo -> mapearEntidad(tipo)).collect(Collectors.toList());
	}

	@Override
	public TipoUbicacionDTO getTipoMov(Long id, Empresa empresa) {
		TipoUbicacion tipoUbicacion = tipoUbicRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de ubicacion", "id", id));
		
		return mapearEntidad(tipoUbicacion);
	}

	@Override
	public TipoUbicacionDTO update(Long id, TipoUbicacionDTO tipoUbicacionDTO) {
		TipoUbicacion tipoUbicacion = tipoUbicRepo.findByIdAndEmpresa(id, tipoUbicacionDTO.getEmpresa())
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de ubicacion", "id", id));
		
		tipoUbicacion.setNombre(tipoUbicacionDTO.getNombre());
		
		tipoUbicRepo.save(tipoUbicacion);
		
		return mapearEntidad(tipoUbicacion);
	}

	@Override
	public void delete(Long id, Empresa empresa) {
		TipoUbicacion tipoUbicacion = tipoUbicRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de ubicacion", "id", id));
		
		if(tipoUbicacion.getUbicaciones().size() > 0) {
			throw new ResourceCannotBeDeleted("Tipo de empresa");
		}
		
		tipoUbicRepo.delete(tipoUbicacion);

	}
	
	@Override
	public void unable(Long id, Empresa empresa) {
		TipoUbicacion tipoUbicacion = tipoUbicRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de ubicacion", "id", id));
		
		tipoUbicacion.setEstaActivo(false);
		tipoUbicRepo.save(tipoUbicacion);
		
	}
	
	public TipoUbicacionDTO mapearEntidad(TipoUbicacion tipoUbicacion) {
		return modelMapper.map(tipoUbicacion, TipoUbicacionDTO.class);
	}
	
	public TipoUbicacion mapearDTO(TipoUbicacionDTO tipoUbicacionDTO) {
		return modelMapper.map(tipoUbicacionDTO, TipoUbicacion.class);
	}
	
	@Override
	public List<TipoUbicacionDTO> findByNombreAndEmpresaAndEstaActivoTrue(String letras, Empresa empresa) {
		List<TipoUbicacion> tiposMov = tipoUbicRepo.findByNombreContainsAndEmpresaAndEstaActivoTrue(letras, empresa);
		return tiposMov.stream().map(tipo -> mapearEntidad(tipo)).collect(Collectors.toList());
	}

	@Override
	public Page<TipoUbicacion> searchTipoUbicacion(TipoUbicacionDTO tipoUbicacionDTO, Empresa empresa, Integer pagina,
			Integer items) {
		if(items == 0) {
			Page<TipoUbicacion> tiposUbicacion = tipoUbicRepo.findAll(tipoUbicacionSpec.getTipoUbibcacion(tipoUbicacionDTO, empresa), PageRequest.of(0, 10));
			return tiposUbicacion;
		}
		Page<TipoUbicacion> tiposUbicacion = tipoUbicRepo.findAll(tipoUbicacionSpec.getTipoUbibcacion(tipoUbicacionDTO, empresa), PageRequest.of(pagina, items));		
		return tiposUbicacion;
	}

	@Override
	public Page<TipoUbicacion> searchTipoUbicacion(Empresa empresa, Integer pagina, Integer items) {
		if(items == 0) {
			Page<TipoUbicacion> tiposUbicacion = tipoUbicRepo.findByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(0, 10));
			return tiposUbicacion;
		}
		Page<TipoUbicacion> tiposUbicacion = tipoUbicRepo.findByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(pagina, items));		
		return tiposUbicacion;
	}

	@Override
	public List<TipoUbicacionDTO> listTipoUbicacion(TipoUbicacionDTO tipoUbicacionDTO, Empresa empresa, boolean b) {
		List<TipoUbicacion> fabricantes = tipoUbicRepo.findAll(tipoUbicacionSpec.getTipoUbibcacion(tipoUbicacionDTO, empresa));
		return fabricantes.stream().map(fabricante -> mapearEntidad(fabricante)).collect(Collectors.toList());
	}



}
