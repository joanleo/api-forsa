package com.prueba.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.dto.FamiliaDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Familia;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.FamiliaRepository;


@Service
public class FamiliaServiceImpl implements FamiliaService {
	
	@Autowired
	private FamiliaRepository familiaRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public FamiliaDTO create(FamiliaDTO familiaDto, Empresa empresa) {
		Familia familia = mapearDTO(familiaDto);
		Familia exist = familiaRepo.findByNombreAndEmpresa(familia.getNombre(), empresa);
		if(exist == null) {
			familiaRepo.save(familia);
		}else {
			throw new IllegalAccessError("La familia que esta tratando de crear ya existe " + familia.getNombre() + "Descripcio: " + familia.getNombre());
		}
		return mapearEntidad(familia);
	}

	@Override
	public List<FamiliaDTO> list(Empresa empresa) {
		List<Familia> listaFamilias = familiaRepo.findByEmpresa(empresa);
		return listaFamilias.stream().map(familia -> mapearEntidad(familia)).collect(Collectors.toList());
	}

	@Override
	public FamiliaDTO getFamilia(Long id) {
		Familia familia = familiaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Familia", "id", id));
		
		return mapearEntidad(familia);
	}

	@Override
	public FamiliaDTO update(Long id, FamiliaDTO familiaDTO) {
		Familia familia = familiaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Familia", "id", id));

		if(familiaDTO.getNombre() != null) {
			familia.setNombre(familiaDTO.getNombre());			
		}
		if(familiaDTO.getId() != null) {
			familia.setId(familiaDTO.getId());			
		}
		if(familiaDTO.getEmpresa() != null) {
			familia.setEmpresa(familiaDTO.getEmpresa());			
		}
		familiaRepo.save(familia);
		return mapearEntidad(familia);
	}

	@Override
	public void delete(Long id) {
		Familia familia = familiaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Familia", "id", id));
		if(familia.getProductos().size() > 0) {
			throw new IllegalAccessError("No se pude eliminar la empresa tiene usuarios y/o productos asociados");
		}
		familiaRepo.delete(familia);

	}
	
	@Override
	public void unable(Long id) {
		Familia familia = familiaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Familia", "id", id));
		familia.setEstaActiva(false);
		familiaRepo.save(familia);
		
	}
	
	public FamiliaDTO mapearEntidad(Familia familia) {
		return modelMapper.map(familia, FamiliaDTO.class);
	}
	
	public Familia mapearDTO(FamiliaDTO familiaDTO) {
		return modelMapper.map(familiaDTO, Familia.class);
	}

	@Override
	public List<FamiliaDTO> findByNameAndEmpresa(String name, Empresa empresa) {
		List<Familia> listFamilias = familiaRepo.findByNombreContainsAndEmpresa(name, empresa);
		return listFamilias.stream().map(familia -> mapearEntidad(familia)).collect(Collectors.toList());
	}


	@Override
	public List<FamiliaDTO> findByNameAndEmpreaAndEstaActiva(String letters, Empresa empresa, Boolean estaActiva) {
		List<Familia> listFamilias = familiaRepo.findByNombreContainsAndEmpresaAndEstaActiva(letters, empresa, estaActiva);
		return listFamilias.stream().map(familia -> mapearEntidad(familia)).collect(Collectors.toList());
	}

	

}
