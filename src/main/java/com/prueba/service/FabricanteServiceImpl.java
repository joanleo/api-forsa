package com.prueba.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.dto.FabricanteDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Fabricante;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.FabricanteRepository;

@Service
public class FabricanteServiceImpl implements FabricanteService {
	
	@Autowired
	private FabricanteRepository fabricanteRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public FabricanteDTO create(FabricanteDTO fabricanteDTO) {
		Fabricante fabricante = mapearDTO(fabricanteDTO);
		Optional<Fabricante> exist = fabricanteRepo.findById(fabricante.getNit());
		if(exist == null) {
			fabricanteRepo.save(fabricante);
		}else {
			throw new IllegalAccessError("El fabricante que esta tratando de crear ya existe"
					+ fabricante.getNit() + " " + fabricante.getNombre() );
		}
		
		return mapearEntidad(fabricante);
	}

	@Override
	public List<FabricanteDTO> list(Empresa empresa) {
		List<Fabricante> fabricantes = fabricanteRepo.findByEmpresaAndEstaActivo(empresa, true);
		
		return fabricantes.stream().map(fabricante -> mapearEntidad(fabricante)).collect(Collectors.toList());
		
	}

	@Override
	public FabricanteDTO getFabricante(Long id) {
		Fabricante fabricante = fabricanteRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Fabricante", "id", id));
		
		return mapearEntidad(fabricante);
	}

	@Override
	public FabricanteDTO update(Long id, FabricanteDTO fabricanteDTO) {
		Fabricante fabricante = fabricanteRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Fabricante", "id", id));
		
		//fabricante.setDescripcion(fabricanteDTO.getDescripcion());
		fabricante.setNit(fabricanteDTO.getNit());
		fabricante.setNombre(fabricanteDTO.getNombre());
		
		return mapearEntidad(fabricante);
	}

	@Override
	public void delete(Long id) {
		Fabricante fabricante = fabricanteRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Fabricante", "id", id));
		
		if(fabricante.getProductos().size() > 0) {
			throw new IllegalAccessError("El fabricante no se puede elliminar, tiene productos asociados");
		}
		
		fabricanteRepo.delete(fabricante);
	}
	
	@Override
	public void unable(Long id) {
		Fabricante fabricante = fabricanteRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Fabricante", "id", id));
		
		fabricante.setEstaActivo(false);
		fabricanteRepo.save(fabricante);		
	}
	
	public FabricanteDTO mapearEntidad(Fabricante fabricante) {
		return modelMapper.map(fabricante, FabricanteDTO.class);
	}
	
	public Fabricante mapearDTO(FabricanteDTO fabricanteDTO) {
		return modelMapper.map(fabricanteRepo, Fabricante.class);
	}

	@Override
	public List<FabricanteDTO> findByNameAndEmpresaAndEstaActivo(String letras, Empresa empresa, Boolean estaActivo) {
		List<Fabricante> listFabricante = fabricanteRepo.findByNombreContainsAndEmpresaAndEstaActivo(letras, empresa, true);
		
		return listFabricante.stream().map(fabricante -> mapearEntidad(fabricante)).collect(Collectors.toList());
	}

	

}
