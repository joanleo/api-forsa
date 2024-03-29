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

import com.prueba.dto.EstadoDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Estado;
import com.prueba.exception.ResourceAlreadyExistsException;
import com.prueba.exception.ResourceCannotBeAccessException;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.EstadoRepository;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.specifications.EstadoSpecifications;
import com.prueba.util.UtilitiesApi;

@Service
public class EstadoServiceImpl implements EstadoService {
	
	@Autowired
	private EstadoRepository estadoRepo;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private ModelMapper modelmapper;
	
	@Autowired
	private EstadoSpecifications estadoSpec;
	
	@Autowired
	private UtilitiesApi util;

	@Override
	public EstadoDTO create(EstadoDTO estadoDTO) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
				
		Empresa empresa;
		if(estadoDTO.getEmpresa() != null) {
			empresa = util.obtenerEmpresa(estadoDTO.getEmpresa().getNit());
		}else {
			empresa = usuario.getEmpresa();			
		}
		estadoDTO.setEmpresa(empresa);
		Estado estado = new Estado();
		Estado exist = estadoRepo.findByTipoAndEmpresa(estadoDTO.getTipo(), estadoDTO.getEmpresa());
		String verNombre = estadoDTO.getTipo().trim();
		if(verNombre.length() > 0) {
			if(exist == null) {
				estado.setEmpresa(empresa);
				estado.setTipo(estadoDTO.getTipo());
				exist = estadoRepo.save(estado);
			}else {
				throw new ResourceAlreadyExistsException("Estado", "nombre", estado.getTipo());
			}
		}else {
			throw new ResourceCannotBeAccessException("El nombre debe ser un nombre valildo, no solo espacios");
		}
		
		return mapearEntidad(exist);
	}
	
	@Override
	public Page<Estado> searchEstado(String letras, Empresa empresa, Integer pagina, Integer items) {
		if(items == 0) {
			Page<Estado> estados = estadoRepo.findByTipoContainsAndEmpresaAndEstaActivoTrue(letras, empresa, PageRequest.of(0, 10));
			return estados;
		}
		Page<Estado> estados = estadoRepo.findByTipoContainsAndEmpresaAndEstaActivoTrue(letras, empresa, PageRequest.of(pagina, items));		
		return estados;
	}
	
	@Override
	public Page<Estado> searchEstado(Empresa empresa, Integer pagina, Integer items) {
		if(items == 0) {
			Page<Estado> estados = estadoRepo.findByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(0, 10));
			return estados;
		}
		Page<Estado> estados = estadoRepo.findByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(pagina, items));		
		return estados;
	}
	

	@Override
	public List<EstadoDTO> list(Empresa empresa) {
		List<Estado> estados = estadoRepo.findByEmpresaAndEstaActivo(empresa, true);
		
		return estados.stream().map(estado -> mapearEntidad(estado)).collect(Collectors.toList());
	}

	@Override
	public EstadoDTO getEstado(Long id, Empresa empresa) {
		Estado estado = estadoRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Estado", "id", id));
		
		return mapearEntidad(estado);
	}

	@Override
	public EstadoDTO update(Long id, EstadoDTO estadoDTO) {
		Estado estado = estadoRepo.findByIdAndEmpresa(id, estadoDTO.getEmpresa())
				.orElseThrow(() -> new ResourceNotFoundException("Estado", "id", id));
		String verNombre = estadoDTO.getTipo().trim();
		if(verNombre.length() > 0) {
			if(estadoDTO.getTipo() != null) {
				Estado estadoUpdae = estadoRepo.findByTipoAndEmpresa(estadoDTO.getTipo(), estadoDTO.getEmpresa());
				if(estadoUpdae == null) {
					estado.setTipo(estadoDTO.getTipo());
				}else {
					throw new ResourceAlreadyExistsException("Estado", "nombre", estadoDTO.getTipo());
				}
			}			
		}else {
			throw new ResourceCannotBeAccessException("El nombre debe ser un nombre valildo, no solo espacios");
		}
		estadoRepo.save(estado);
		
		return mapearEntidad(estado);
	}

	@Override
	public void delete(Long id, Empresa empresa) {
		Estado estado = estadoRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Estado", "id", id));
		
		estadoRepo.delete(estado);
	}
	
	@Override
	public void unable(Long id, Empresa empresa) {
		Estado estado = estadoRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Estado", "id", id));
		
		Boolean activo = estado.getEstaActivo();
		if(activo) {
			estado.setEstaActivo(false);			
		}else {
			estado.setEstaActivo(true);
		}
		
		estadoRepo.save(estado);
		
	}
	
	
	public EstadoDTO mapearEntidad(Estado estado) {
		return modelmapper.map(estado, EstadoDTO.class);
	}
	
	public Estado mapearDTO(EstadoDTO estadoDTO) {
		return modelmapper.map(estadoDTO, Estado.class);
	}

	@Override
	public List<EstadoDTO> findByTipoAndEmpresaAndEstaActivo(String tipo, Empresa empresa) {
		List<Estado> listEstados = estadoRepo.findByTipoContainsAndEmpresaAndEstaActivoTrue(tipo, empresa);
		
		return listEstados.stream().map(estado -> mapearEntidad(estado)).collect(Collectors.toList());
	}

	@Override
	public Page<Estado> searchEstados(EstadoDTO estadoDTO, Empresa empresa, Integer pagina, Integer items) {
		if(items == 0) {
			Page<Estado> estados = estadoRepo.findAll(estadoSpec.getEstado(estadoDTO, empresa), PageRequest.of(0, 10));
			return estados;
		}
		Page<Estado> estados = estadoRepo.findAll(estadoSpec.getEstado(estadoDTO, empresa), PageRequest.of(pagina, items));		
		return estados;
	}

	@Override
	public Page<Estado> searchEstados(Empresa empresa, Integer pagina, Integer items) {
		if(items == 0) {
			Page<Estado> estados = estadoRepo.findByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(0, 10));
			return estados;
		}
		Page<Estado> estados = estadoRepo.findByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(pagina, items));		
		return estados;
	}

	@Override
	public List<EstadoDTO> listEstados(EstadoDTO estadoDTO, Empresa empresa) {
		List<Estado> estados = estadoRepo.findAll(estadoSpec.getEstado(estadoDTO, empresa));
		return estados.stream().map(estado -> mapearEntidad(estado)).collect(Collectors.toList());
	}

	@Override
	public List<EstadoDTO> findByNameAndEmpresaAndEstaActivo(String letras, Empresa empresa) {
		List<Estado> listEstados = estadoRepo.findByTipoContainsAndEmpresaAndEstaActivoTrue(letras, empresa);
		return listEstados.stream().map(estado -> mapearEntidad(estado)).collect(Collectors.toList());
	}



}
