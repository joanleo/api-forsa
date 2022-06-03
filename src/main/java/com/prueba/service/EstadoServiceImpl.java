package com.prueba.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.dto.EstadoDTO;
import com.prueba.entity.Estado;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.EstadoRepository;

@Service
public class EstadoServiceImpl implements EstadoService {
	
	@Autowired
	private EstadoRepository estadoRepo;
	
	@Autowired
	private ModelMapper modelmapper;

	@Override
	public EstadoDTO create(EstadoDTO estadoDTO) {
		Estado estado = mapearDTO(estadoDTO);
		Estado exist = estadoRepo.findByTipo(estado.getTipo());
		if(exist == null) {
			estadoRepo.save(estado);
		}else {
			throw new IllegalAccessError("El estado que esta tratando de crear ya existe" + estado.getTipo());
		}
		
		return mapearEntidad(estado);
	}

	@Override
	public List<EstadoDTO> list() {
		List<Estado> estados = estadoRepo.findAll();
		
		return estados.stream().map(estado -> mapearEntidad(estado)).collect(Collectors.toList());
	}

	@Override
	public EstadoDTO getEstado(Long id) {
		Estado estado = estadoRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Estado", "id", id));
		
		return mapearEntidad(estado);
	}

	@Override
	public EstadoDTO update(Long id, EstadoDTO estadoDTO) {
		Estado estado = estadoRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("EStado", "id", id));
		
		//estado.setDescripcion(estadoDTO.getDescripcion());
		estado.setTipo(estadoDTO.getTipo());
		
		
		return mapearEntidad(estado);
	}

	@Override
	public void delete(Long id) {
		Estado estado = estadoRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("EStado", "id", id));
		
		estadoRepo.delete(estado);
	}
	
	public EstadoDTO mapearEntidad(Estado estado) {
		return modelmapper.map(estado, EstadoDTO.class);
	}
	
	public Estado mapearDTO(EstadoDTO estadoDTO) {
		return modelmapper.map(estadoDTO, Estado.class);
	}

	@Override
	public List<EstadoDTO> findByTipo(String tipo) {
		List<Estado> listEstados = estadoRepo.findByTipoContains(tipo);
		
		return listEstados.stream().map(estado -> mapearEntidad(estado)).collect(Collectors.toList());
	}

}
