package com.prueba.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.prueba.dto.EmpresaDTO;
import com.prueba.entity.Empresa;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.EmpresaRepository;
import com.prueba.specifications.EmpresaSpecifications;

@Service
public class EmpresaServiceImpl implements EmpresaService {

	@Autowired
	private EmpresaRepository empresaRepo;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private EmpresaSpecifications empresaSpec;

	@Override
	public EmpresaDTO create(EmpresaDTO empresaDTO) {
		Empresa empresa = mapearDto(empresaDTO);
		Empresa exist = empresaRepo.findByNit(empresa.getNit());
		if (exist == null) {
			empresaRepo.save(empresa);
		} else {
			throw new IllegalAccessError("La empresa que intenta crear ya existe en la base de datos: " + " "
					+ empresa.getNombre() + "" + empresa.getNit());
		}
		return mapearEntidad(empresa);
	}
	
	@Override
	public Page<Empresa> searchEmpresas(EmpresaDTO empresaDTO, Integer pagina, Integer items) {
		if(items == 0) {
			Page<Empresa> empresas = empresaRepo.findAll(empresaSpec.getEmpresa(empresaDTO),PageRequest.of(0, 10));
			return empresas;
		}
		Page<Empresa> empresas = empresaRepo.findAll(empresaSpec.getEmpresa(empresaDTO), PageRequest.of(pagina, items));		
		return empresas;
	}
	
	@Override
	public Page<Empresa> searchEmpresas(Integer pagina, Integer items) {
		if(items == 0) {
			Page<Empresa> empresas = empresaRepo.findByEstaActivoTrue(PageRequest.of(0, 10));
			return empresas;
		}
		Page<Empresa> empresas = empresaRepo.findByEstaActivoTrue(PageRequest.of(pagina, items));		
		return empresas;
	}

	@Override
	public List<EmpresaDTO> list(String letras) {
		List<Empresa> empresas = empresaRepo.findByNombreContainsAndEstaActivoTrue(letras);
		
		return empresas.stream().map(empresa -> mapearEntidad(empresa)).collect(Collectors.toList());
	}
	
	@Override
	public List<EmpresaDTO> list() {
		List<Empresa> empresas = empresaRepo.findByEstaActivoTrue();
		
		return empresas.stream().map(empresa -> mapearEntidad(empresa)).collect(Collectors.toList());
	}

	@Override
	public EmpresaDTO getEmpresa(Long id) {
		Empresa empresa = empresaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Empresa", "id", id));
		
		return mapearEntidad(empresa);
	}

	@Override
	public EmpresaDTO update(Long id, EmpresaDTO empresaDTO) {
		Empresa empresa = empresaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Empresa", "id", id));
		
		empresa.setNit(empresaDTO.getNit());
		empresa.setNombre(empresaDTO.getNombre());
		
		empresaRepo.save(empresa);
		return mapearEntidad(empresa);
	}

	@Override
	public void delete(Long id) {
		Empresa empresa = empresaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Empresa", "id", id));
		
		if(empresa.getUsuarios().size() > 0 || empresa.getProductos().size() > 0) {
			throw new IllegalAccessError("no se puede eliminar la empresa, existen productos y/o usuarios asociados");
		}
		
		empresaRepo.delete(empresa);

	}
	
	@Override
	public void unable(Long id) {
		Empresa empresa = empresaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Empresa", "id", id));
		
		empresa.setEstaActivo(false);
		empresaRepo.save(empresa);
		
	}

	public EmpresaDTO mapearEntidad(Empresa Empresa) {
		return modelMapper.map(Empresa, EmpresaDTO.class);
	}

	public Empresa mapearDto(EmpresaDTO EmpresaDTO) {
		return modelMapper.map(EmpresaDTO, Empresa.class);
	}

	
	@Override
	public List<EmpresaDTO> findByNameAndEstaActiva(String name, Boolean estaActiva) {
		List<Empresa> listEmpresas = empresaRepo.findByNombreContainsAndEstaActivoTrue(name);
		
		return listEmpresas.stream().map(empresa -> mapearEntidad(empresa)).collect(Collectors.toList());
	}

	@Override
	public List<EmpresaDTO> list(EmpresaDTO empresaDTO) {
		List<Empresa> empresas = empresaRepo.findAll(empresaSpec.getEmpresa(empresaDTO));
		return empresas.stream().map(empresa -> mapearEntidad(empresa)).collect(Collectors.toList());
	}

}
