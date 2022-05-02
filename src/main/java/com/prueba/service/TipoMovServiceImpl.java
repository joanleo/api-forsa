package com.prueba.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.dto.TipoMovDTO;
import com.prueba.entity.TipoMov;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.TipoMovRepository;

@Service
public class TipoMovServiceImpl implements TipoMovService {
	
	@Autowired
	private TipoMovRepository tipoMovRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public TipoMovDTO create(TipoMovDTO tipoMovDTO) {
		TipoMov tipoMov = mapearDTO(tipoMovDTO);
		TipoMov exist = tipoMovRepo.findByNombre(tipoMov.getNombre());
		if(exist != null) {
			tipoMovRepo.save(tipoMov);
		}else {
			throw new IllegalAccessError("El tipo de mpvimiento que desea crear ya existe: " + tipoMov.getNombre());
		}

		return mapearEntidad(tipoMov);
	}

	@Override
	public List<TipoMovDTO> list() {
		List<TipoMov> listTipos = tipoMovRepo.findAll();
		
		return listTipos.stream().map(tipo -> mapearEntidad(tipo)).collect(Collectors.toList());
	}

	@Override
	public TipoMovDTO getTipoMov(Long id) {
		TipoMov tipoMov = tipoMovRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de movimiento", "id", id));
		return mapearEntidad(tipoMov);
	}

	@Override
	public TipoMovDTO update(Long id, TipoMovDTO tipoMovDTO) {
		TipoMov tipoMov = tipoMovRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de movimiento", "id", id));
		
		tipoMov.setNombre(tipoMovDTO.getNombre());
		
		tipoMovRepo.save(tipoMov);
		
		return mapearEntidad(tipoMov);
	}

	@Override
	public void delete(Long id) {
		TipoMov tipoMov = tipoMovRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de movimiento", "id", id));
		
		tipoMovRepo.delete(tipoMov);

	}
	
	public TipoMovDTO mapearEntidad(TipoMov tipoMov) {
		return modelMapper.map(tipoMov, TipoMovDTO.class);
	}
	
	public TipoMov mapearDTO(TipoMovDTO tipoMovDTO) {
		return modelMapper.map(tipoMovDTO, TipoMov.class);
	}
}
