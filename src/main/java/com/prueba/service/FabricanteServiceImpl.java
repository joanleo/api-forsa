package com.prueba.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.prueba.dto.FabricanteDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Fabricante;
import com.prueba.exception.ResourceAlreadyExistsException;
import com.prueba.exception.ResourceCannotBeDeleted;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.FabricanteRepository;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.specifications.FabricanteSpecificatios;
import com.prueba.util.UtilitiesApi;

@Service
public class FabricanteServiceImpl implements FabricanteService {
	
	@Autowired
	private FabricanteRepository fabricanteRepo;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private FabricanteSpecificatios fabricanteSpec;
	
	@Autowired
	private UtilitiesApi util;

	@Override
	public FabricanteDTO create(FabricanteDTO fabricanteDTO) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(fabricanteDTO.getEmpresa() == null) {
			fabricanteDTO.setEmpresa(usuario.getEmpresa());
		}
		Empresa empresa;
		if(fabricanteDTO.getEmpresa() != null) {
			empresa = util.obtenerEmpresa(fabricanteDTO.getEmpresa().getNit());
		}else {
			empresa = usuario.getEmpresa();			
		}
		fabricanteDTO.setEmpresa(empresa);
		Fabricante fabricante = mapearDTO(fabricanteDTO);
		Fabricante exist = fabricanteRepo.findByNitAndEmpresaAndEstaActivoTrue(fabricanteDTO.getNit(), fabricanteDTO.getEmpresa());
		
		if(exist == null) {
			fabricanteRepo.save(fabricante);
		}else {
			throw new ResourceAlreadyExistsException("Fabricante", "nit", fabricanteDTO.getNit());
		}
		
		return mapearEntidad(fabricante);
	}
	
	@Override
	public Page<Fabricante> searchFabricantes(FabricanteDTO fabricanteDTO, Empresa empresa, Integer pagina, Integer items) {
		if(items == 0) {
			Page<Fabricante> fabricantes = fabricanteRepo.findAll(fabricanteSpec.getFabricante(fabricanteDTO, empresa), PageRequest.of(0, 10));
			return fabricantes;
		}
		Page<Fabricante> fabricantes = fabricanteRepo.findAll(fabricanteSpec.getFabricante(fabricanteDTO, empresa), PageRequest.of(pagina, items));		
		return fabricantes;
	}	
	
	@Override
	public Page<Fabricante> searchFabricantes(Empresa empresa, Integer pagina, Integer items) {
		if(items == 0) {
			Page<Fabricante> fabricantes = fabricanteRepo.findByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(0, 10));
			return fabricantes;
		}
		Page<Fabricante> fabricantes = fabricanteRepo.findByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(pagina, items));		
		return fabricantes;
	}

	@Override
	public List<FabricanteDTO> list(Empresa empresa) {
		List<Fabricante> fabricantes = fabricanteRepo.findByEmpresaAndEstaActivoTrue(empresa);
		
		return fabricantes.stream().map(fabricante -> mapearEntidad(fabricante)).collect(Collectors.toList());
		
	}

	@Override
	public FabricanteDTO getFabricante(Long id, Empresa empresa) {
		Fabricante fabricante = fabricanteRepo.findByNitAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Fabricante", "id", id));
		
		return mapearEntidad(fabricante);
	}

	@Override
	public FabricanteDTO update(Long id, FabricanteDTO fabricanteDTO) {
		Fabricante fabricante = fabricanteRepo.findByNitAndEmpresa(id, fabricanteDTO.getEmpresa())
				.orElseThrow(() -> new ResourceNotFoundException("Fabricante", "id", id));
		
		//fabricante.setDescripcion(fabricanteDTO.getDescripcion());
		fabricante.setNit(fabricanteDTO.getNit());
		fabricante.setNombre(fabricanteDTO.getNombre());
		fabricanteRepo.save(fabricante);
		return mapearEntidad(fabricante);
	}

	@Override
	public void delete(Long id, Empresa empresa) {
		Fabricante fabricante = fabricanteRepo.findByNitAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Fabricante", "id", id));
		
		if(fabricante.getProductos().size() > 0) {
			throw new ResourceCannotBeDeleted("Fabricante");
		}
		
		fabricanteRepo.delete(fabricante);
	}
	
	@Override
	public void unable(Long id, Empresa empresa) {
		Fabricante fabricante = fabricanteRepo.findByNitAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Fabricante", "id", id));
		
		fabricante.setEstaActivo(false);
		fabricanteRepo.save(fabricante);		
	}
	
	public FabricanteDTO mapearEntidad(Fabricante fabricante) {
		return modelMapper.map(fabricante, FabricanteDTO.class);
	}
	
	public Fabricante mapearDTO(FabricanteDTO fabricanteDTO) {
		Fabricante fabricante = modelMapper.map(fabricanteDTO, Fabricante.class);
		return fabricante;
	}

	@Override
	public List<FabricanteDTO> findByNameAndEmpresaAndEstaActivo(String letras, Empresa empresa) {
		List<Fabricante> listFabricante = fabricanteRepo.findByNombreContainsAndEmpresaAndEstaActivoTrue(letras, empresa);
		
		return listFabricante.stream().map(fabricante -> mapearEntidad(fabricante)).collect(Collectors.toList());
	}

	@Override
	public List<FabricanteDTO> listFabricantes(FabricanteDTO fabricanteDTO, Empresa empresa) {
		List<Fabricante> fabricantes = fabricanteRepo.findAll(fabricanteSpec.getFabricante(fabricanteDTO, empresa));
		return fabricantes.stream().map(fabricante -> mapearEntidad(fabricante)).collect(Collectors.toList());
	}

	

	

	

}
