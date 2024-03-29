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
import com.prueba.entity.Familia;
import com.prueba.exception.ResourceAlreadyExistsException;
import com.prueba.exception.ResourceCannotBeAccessException;
import com.prueba.exception.ResourceCannotBeDeleted;
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
		Familia familia = new Familia();
		Familia exist = familiaRepo.findByNombreAndEmpresa(familia.getNombre(), empresa);
		String verNombre = familiaDto.getNombre().trim();
		if(verNombre.length() > 0) {
			if(exist == null) {
				familia.setEmpresa(empresa);
				familia.setNombre(familiaDto.getNombre());
				familia.setSigla(familiaDto.getSigla());
				exist = familiaRepo.saveAndFlush(familia);
			}else {
				throw new ResourceAlreadyExistsException("Ubicacion", "nombre", familiaDto.getNombre());
			}			
		}else {
			throw new ResourceCannotBeAccessException("El nombre debe ser un nombre valildo, no solo espacios");
		}
		
		return mapearEntidad(exist);
	}

	@Override
	public List<FamiliaDTO> list(Empresa empresa) {
		List<Familia> listaFamilias = familiaRepo.findByEmpresaAndEstaActivoTrue(empresa);
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

		Familia familia = familiaRepo.findByIdAndEmpresa(id, familiaDTO.getEmpresa())
				.orElseThrow(() -> new ResourceNotFoundException("Familia", "id", id));

		if(familiaDTO.getNombre() != null) {
			Familia familiaUpdate = familiaRepo.findByNombreAndEmpresa(familiaDTO.getNombre(), empresa);
			if(familiaUpdate == null) {
				familia.setNombre(familiaDTO.getNombre());							
			}else {
				throw new ResourceAlreadyExistsException("Familia", "nombre", familiaDTO.getNombre());
			}
		}
		if(familiaDTO.getSigla() != null) {
			familia.setSigla(familiaDTO.getSigla());			
		}

		familia = familiaRepo.saveAndFlush(familia);
		return mapearEntidad(familia);
	}

	@Override
	public void delete(Long id, Empresa empresa) {
		Familia familia = familiaRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Familia", "id", id));
		if(familia.getProductos().size() > 0) {
			throw new ResourceCannotBeDeleted("Familia");
		}
		familiaRepo.delete(familia);

	}
	
	@Override
	public void unable(Long id, Empresa empresa) {
		Familia familia = familiaRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Familia", "id", id));
		Boolean estado = familia.getEstaActivo();
		if(estado) {
			familia.setEstaActivo(false);			
		}else {
			familia.setEstaActivo(true);
		}
		
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
	public List<FamiliaDTO> findByNameAndEmpresaAndEstaActivo(String letras, Empresa empresa) {
		List<Familia> listFamilias = familiaRepo.findByNombreContainsAndEmpresaAndEstaActivoTrue(letras, empresa);
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
			Page<Familia> familias = familiaRepo.findByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(0, 10));
			return familias;
		}
		Page<Familia> familias = familiaRepo.findByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(pagina, items));		
		return familias;
	}

	@Override
	public List<FamiliaDTO> listFamilias(FamiliaDTO familiaDTO, Empresa empresa) {
		List<Familia> fabricantes = familiaRepo.findAll(familiaSpec.getFamilia(familiaDTO, empresa));
		return fabricantes.stream().map(fabricante -> mapearEntidad(fabricante)).collect(Collectors.toList());
	}

	

}
