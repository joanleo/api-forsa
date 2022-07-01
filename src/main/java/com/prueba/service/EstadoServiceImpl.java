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
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.EstadoRepository;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.specifications.EstadoSpecifications;

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

	@Override
	public EstadoDTO create(EstadoDTO estadoDTO) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByUsernameOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(estadoDTO.getEmpresa() == null) {
			estadoDTO.setEmpresa(usuario.getEmpresa());
		}
		Estado estado = mapearDTO(estadoDTO);
		Estado exist = estadoRepo.findByTipoAndEmpresa(estadoDTO.getTipo(), estadoDTO.getEmpresa());
		if(exist == null) {
			estadoRepo.save(estado);
		}else {
			throw new IllegalAccessError("El estado que esta tratando de crear ya existe" + estado.getTipo());
		}
		
		return mapearEntidad(estado);
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
		
		//estado.setDescripcion(estadoDTO.getDescripcion());
		estado.setTipo(estadoDTO.getTipo());
		estadoRepo.save(estado);
		
		return mapearEntidad(estado);
	}

	@Override
	public void delete(Long id, Empresa empresa) {
		Estado estado = estadoRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("EStado", "id", id));
		
		estadoRepo.delete(estado);
	}
	
	@Override
	public void unable(Long id, Empresa empresa) {
		Estado estado = estadoRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Estado", "id", id));
		
		estado.setEstaActivo(false);
		estadoRepo.save(estado);
		
	}
	
	
	public EstadoDTO mapearEntidad(Estado estado) {
		return modelmapper.map(estado, EstadoDTO.class);
	}
	
	public Estado mapearDTO(EstadoDTO estadoDTO) {
		return modelmapper.map(estadoDTO, Estado.class);
	}

	@Override
	public List<EstadoDTO> findByTipoAndEmpresaAndEstaActivo(String tipo, Empresa empresa, Boolean estaActivo) {
		List<Estado> listEstados = estadoRepo.findByTipoContainsAndEmpresaAndEstaActivo(tipo, empresa, estaActivo);
		
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



}
