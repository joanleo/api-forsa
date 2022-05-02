package com.prueba.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.dto.UbicacionDTO;
import com.prueba.entity.Ubicacion;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.UbicacionRepository;

@Service
public class UbicacionServiceImpl implements UbicacionService {
	
	@Autowired
	private UbicacionRepository ubicacionRepo;

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public UbicacionDTO create(UbicacionDTO ubicacionDTO) {
		Ubicacion ubicacion = mapearDTO(ubicacionDTO);
		Ubicacion exist = ubicacionRepo.findByNombre(ubicacion.getNombre());
		if(exist != null) {
			ubicacionRepo.save(ubicacion);
		}else {
			throw new IllegalAccessError("La ubicacion que desea crear ya existe: "+ ubicacion.getNombre());
		}
		
		return mapearEntidad(ubicacion);
	}

	@Override
	public List<UbicacionDTO> list() {
		List<Ubicacion> ubicaciones = ubicacionRepo.findAll();
		
		return ubicaciones.stream().map(ubicacion -> mapearEntidad(ubicacion)).collect(Collectors.toList());
	}

	@Override
	public UbicacionDTO getUbicacion(Long id) {
		Ubicacion ubicacion = ubicacionRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Ubicacion", "id", id));
		
		return mapearEntidad(ubicacion);
	}

	@Override
	public UbicacionDTO update(Long id, UbicacionDTO ubicacionDTO) {
		Ubicacion ubicacion = ubicacionRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Ubicacion", "id", id));
		
		ubicacion.setCiudad(ubicacionDTO.getCiudad());
		ubicacion.setDireccion(ubicacionDTO.getDireccion());
		ubicacion.setNombre(ubicacionDTO.getnombre());
		ubicacion.setTipo(ubicacionDTO.getTipo());
		
		ubicacionRepo.save(ubicacion);
		
		return mapearEntidad(ubicacion);
	}

	@Override
	public void delete(Long id) {
		Ubicacion ubicacion = ubicacionRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Ubicacion", "id", id));
		
		ubicacionRepo.delete(ubicacion);

	}
	
	public UbicacionDTO mapearEntidad(Ubicacion ubicacion) {
		return modelMapper.map(ubicacion, UbicacionDTO.class);
	}
	
	public Ubicacion mapearDTO(UbicacionDTO ubicacionDTO) {
		return modelMapper.map(ubicacionDTO, Ubicacion.class);
	}

}
