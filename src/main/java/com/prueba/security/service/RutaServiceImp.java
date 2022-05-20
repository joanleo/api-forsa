package com.prueba.security.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.exception.ResourceNotFoundException;
import com.prueba.security.dto.RutaDTO;
import com.prueba.security.entity.Ruta;
import com.prueba.security.repository.RutaRepository;


@Service
public class RutaServiceImp implements RutaService {
	
	@Autowired
	private RutaRepository rutaRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public RutaDTO create(RutaDTO rutaDTO) {
		Ruta ruta = mapearDTO(rutaDTO);
		Ruta exist = rutaRepo.findByRuta(ruta.getRuta());
		if(exist == null) {
			rutaRepo.save(ruta);
		}else {
			throw new IllegalAccessError("La ruta que desea crear ya existe" + " " + ruta.getRuta() );
		}
		
		return mapearEntidad(ruta);
	}

	@Override
	public List<RutaDTO> list() {
		List<Ruta> rutas = rutaRepo.findAll();
		
		return rutas.stream().map(ruta -> mapearEntidad(ruta)).collect(Collectors.toList());
	}

	@Override
	public RutaDTO getRuta(Long id) {
		Ruta ruta = rutaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Ruta", "id", id));
		
		return mapearEntidad(ruta);
	}

	@Override
	public RutaDTO updateRuta(Long id, RutaDTO rutaDTO) {
		Ruta ruta = rutaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Ruta", "id", id));
		
		ruta.setRuta(rutaDTO.getRuta());
		
		return mapearEntidad(ruta);
	}

	@Override
	public void delete(Long id) {
		Ruta ruta = rutaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Ruta", "id", id));
		
		rutaRepo.delete(ruta);

	}

	public RutaDTO mapearEntidad(Ruta ruta) {
		return modelMapper.map(ruta, RutaDTO.class);
	}
	
	public Ruta mapearDTO(RutaDTO rutaDTO) {
		return modelMapper.map(rutaDTO, Ruta.class);
	}
}
