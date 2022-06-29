package com.prueba.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.prueba.dto.FamiliaDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Fabricante;
import com.prueba.entity.Familia;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.FamiliaRepository;
import com.prueba.specifications.FamiliaSpecifications;


@Service
public class FamiliaServiceImpl implements FamiliaService {
	
	@Autowired
	private FamiliaRepository familiaRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private FamiliaSpecifications familiaSpec;

	@Override
	public FamiliaDTO create(FamiliaDTO familiaDto, Empresa empresa) {
		Familia familia = mapearDTO(familiaDto);
		Familia exist = familiaRepo.findByNombreAndEmpresa(familia.getNombre(), empresa);
		if(exist == null) {
			familiaRepo.save(familia);
		}else {
			throw new IllegalAccessError("La familia "+ familia.getNombre() +" que esta tratando de crear ya existe en empresa " + empresa);
		}
		return mapearEntidad(familia);
	}

	@Override
	public List<FamiliaDTO> list(Empresa empresa) {
		List<Familia> listaFamilias = familiaRepo.findByEmpresaAndEstaActivaTrue(empresa);
		return listaFamilias.stream().map(familia -> mapearEntidad(familia)).collect(Collectors.toList());
	}

	@Override
	public FamiliaDTO getFamilia(Long id, Empresa empresa) {
		Familia familia = familiaRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Familia", "id", id));
		
		return mapearEntidad(familia);
	}

	@Override
	public FamiliaDTO update(Long id, FamiliaDTO familiaDTO, Empresa empresa) {
		if(familiaDTO.getEmpresa() == null) {
			familiaDTO.setEmpresa(empresa);
		}
		Familia familia = familiaRepo.findByIdAndEmpresa(id, familiaDTO.getEmpresa())
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
	public void delete(Long id, Empresa empresa) {
		Familia familia = familiaRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Familia", "id", id));
		if(familia.getProductos().size() > 0) {
			throw new IllegalAccessError("No se pude eliminar la familia tiene activoss asociados");
		}
		familiaRepo.delete(familia);

	}
	
	@Override
	public void unable(Long id, Empresa empresa) {
		Familia familia = familiaRepo.findByIdAndEmpresa(id, empresa)
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
	public List<FamiliaDTO> findByNameAndEmpreaAndEstaActiva(String letras, Empresa empresa, Boolean estaActiva) {
		List<Familia> listFamilias = familiaRepo.findByNombreContainsAndEmpresaAndEstaActiva(letras, empresa, estaActiva);
		return listFamilias.stream().map(familia -> mapearEntidad(familia)).collect(Collectors.toList());
	}

	@Override
	public Page<Familia> searchFabricantes(FamiliaDTO familiaDTO, Empresa empresa, Integer pagina, Integer items) {
		if(items == 0) {
			Page<Familia> familias = familiaRepo.findAll(familiaSpec.getFamilia(familiaDTO, empresa), PageRequest.of(0, 10));
			return familias;
		}
		Page<Familia> familias = familiaRepo.findAll(familiaSpec.getFamilia(familiaDTO, empresa), PageRequest.of(pagina, items));		
		return familias;
	}

	@Override
	public Page<Familia> searchFabricantes(Empresa empresa, Integer pagina, Integer items) {
		if(items == 0) {
			Page<Familia> familias = familiaRepo.findByEmpresaAndEstaActivaTrue(empresa, PageRequest.of(0, 10));
			return familias;
		}
		Page<Familia> familias = familiaRepo.findByEmpresaAndEstaActivaTrue(empresa, PageRequest.of(pagina, items));		
		return familias;
	}

	@Override
	public List<FamiliaDTO> listFamilias(FamiliaDTO familiaDTO, Empresa empresa) {
		List<Familia> fabricantes = familiaRepo.findAll(familiaSpec.getFamilia(familiaDTO, empresa));
		return fabricantes.stream().map(fabricante -> mapearEntidad(fabricante)).collect(Collectors.toList());
	}

	

}
