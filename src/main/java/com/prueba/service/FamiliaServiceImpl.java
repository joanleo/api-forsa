package com.prueba.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.dto.FamiliaDTO;
import com.prueba.entity.Familia;
import com.prueba.repository.FamiliaRepository;


@Service
public class FamiliaServiceImpl implements FamiliaService {
	
	@Autowired
	private FamiliaRepository familiaRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public FamiliaDTO create(FamiliaDTO familiaDto) {
		Familia familia = mapearDTO(familiaDto);
		Familia exist = familiaRepo.findByNombre(familia.getNombre());
		if(exist == null) {
			familiaRepo.save(familia);
		}else {
			throw new IllegalAccessError("La familia que esta tratando de crear ya existe " + familia.getNombre() + "Descripcio: " + familia.getNombre());
		}
		return mapearEntidad(familia);
	}

	@Override
	public List<FamiliaDTO> list() {
		List<Familia> listaFamilias = familiaRepo.findAll();
		return listaFamilias.stream().map(familia -> mapearEntidad(familia)).collect(Collectors.toList());
	}

	@Override
	public FamiliaDTO getFamilia(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FamiliaDTO update(Long id, FamiliaDTO familiaDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub

	}
	
	public FamiliaDTO mapearEntidad(Familia familia) {
		return modelMapper.map(familia, FamiliaDTO.class);
	}
	
	public Familia mapearDTO(FamiliaDTO familiaDTO) {
		return modelMapper.map(familiaDTO, Familia.class);
	}

	@Override
	public List<FamiliaDTO> findByName(String name) {
		List<Familia> listFamilias = familiaRepo.findByNombreContains(name);
		return listFamilias.stream().map(familia -> mapearEntidad(familia)).collect(Collectors.toList());
	}

}
