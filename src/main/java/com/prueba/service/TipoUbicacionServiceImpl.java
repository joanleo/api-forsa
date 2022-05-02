package com.prueba.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.dto.TipoUbicacionDTO;
import com.prueba.entity.TipoUbicacion;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.TipoUbicacionRepository;

@Service
public class TipoUbicacionServiceImpl implements TipoUbicacionService {
	
	@Autowired
	private TipoUbicacionRepository tipoUbicRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public TipoUbicacionDTO create(TipoUbicacionDTO tipoUbicacionDTO) {
		TipoUbicacion tipoUbicacion = mapearDTO(tipoUbicacionDTO);
		TipoUbicacion exist = tipoUbicRepo.findByNombre(tipoUbicacion.getnombre());
		
		if(exist != null) {
			tipoUbicRepo.save(tipoUbicacion);
		}else {
			throw new IllegalAccessError("El tipo de movimiento que desea crear ya existe");
		}
		
		return mapearEntidad(tipoUbicacion);
	}

	@Override
	public List<TipoUbicacionDTO> list() {
		List<TipoUbicacion> lista = tipoUbicRepo.findAll();
		
		return lista.stream().map(tipo -> mapearEntidad(tipo)).collect(Collectors.toList());
	}

	@Override
	public TipoUbicacionDTO getTipoMov(Long id) {
		TipoUbicacion tipoUbicacion = tipoUbicRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de ubicacion", "id", id));
		
		return mapearEntidad(tipoUbicacion);
	}

	@Override
	public TipoUbicacionDTO update(Long id, TipoUbicacionDTO tipoUbicacionDTO) {
		TipoUbicacion tipoUbicacion = tipoUbicRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de ubicacion", "id", id));
		
		tipoUbicacion.setnombre(tipoUbicacionDTO.getNombre());
		
		tipoUbicRepo.save(tipoUbicacion);
		
		return mapearEntidad(tipoUbicacion);
	}

	@Override
	public void delete(Long id) {
		TipoUbicacion tipoUbicacion = tipoUbicRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de ubicacion", "id", id));
		
		tipoUbicRepo.delete(tipoUbicacion);

	}
	
	public TipoUbicacionDTO mapearEntidad(TipoUbicacion tipoUbicacion) {
		return modelMapper.map(tipoUbicacion, TipoUbicacionDTO.class);
	}
	
	public TipoUbicacion mapearDTO(TipoUbicacionDTO tipoUbicacionDTO) {
		return modelMapper.map(tipoUbicacionDTO, TipoUbicacion.class);
	}

}
