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
		
		empresaRepo.delete(empresa);

	}

	public EmpresaDTO mapearEntidad(Empresa Empresa) {
		return modelMapper.map(Empresa, EmpresaDTO.class);
	}

	public Empresa mapearDto(EmpresaDTO EmpresaDTO) {
		return modelMapper.map(EmpresaDTO, Empresa.class);
	}

	@Override
	public List<EmpresaDTO> findByName(String name) {
		List<Empresa> listEmpresas = empresaRepo.findByNombreContains(name);
		
		return listEmpresas.stream().map(empresa -> mapearEntidad(empresa)).collect(Collectors.toList());
	}
}
