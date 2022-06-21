package com.prueba.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.dto.EmpresaDTO;
import com.prueba.entity.Empresa;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.EmpresaRepository;

@Service
public class EmpresaServiceImpl implements EmpresaService {

	@Autowired
	private EmpresaRepository empresaRepo;

	@Autowired
	private ModelMapper modelMapper;

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
	public List<EmpresaDTO> list() {
		List<Empresa> empresas = empresaRepo.findAll();
		
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
		
		return mapearEntidad(empresa);
	}

	@Override
	public void delete(Long id) {
		Empresa empresa = empresaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Empresa", "id", id));
		if(empresa.getProductos().size() > 0 || empresa.getUsuarios().size() > 0) {
			throw new IllegalAccessError("No se pude eliminar la empresa tiene usuarios y/o productos asociados");
		}
		empresaRepo.delete(empresa);

	}
	
	@Override
	public void unable(Long id) {
		Empresa empresa = empresaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Empresa", "id", id));
		
		empresa.setEstaActiva(false);
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
		List<Empresa> listEmpresas = empresaRepo.findByNombreContainsAndEstaActiva(name, estaActiva);
		
		return listEmpresas.stream().map(empresa -> mapearEntidad(empresa)).collect(Collectors.toList());
	}


}
