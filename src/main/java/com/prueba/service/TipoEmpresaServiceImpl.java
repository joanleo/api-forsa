package com.prueba.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.dto.TipoEmpresaDTO;
import com.prueba.entity.TipoEmpresa;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.TipoEmpresaRepository;

@Service
public class TipoEmpresaServiceImpl implements TipoEmpresaService {

	@Autowired
	private TipoEmpresaRepository tipoEmpresaRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public TipoEmpresaDTO create(TipoEmpresaDTO tipoEmpresaDTO) {
		TipoEmpresa tipoEmpresa = mapearDto(tipoEmpresaDTO);
		TipoEmpresa exist = tipoEmpresaRepo.findByTipo(tipoEmpresa.getTipo());
		if (exist == null) {
			tipoEmpresaRepo.save(tipoEmpresa);
		} else {
			throw new IllegalAccessError("El tipo de empresa que desea crear ya existe: " + " Tipo: " + tipoEmpresa.getTipo());
		}

		return mapearEntidad(tipoEmpresa);
	}

	@Override
	public List<TipoEmpresaDTO> list() {
		List<TipoEmpresa> listTipos = tipoEmpresaRepo.findAll();
		
		return listTipos.stream().map(tipo -> mapearEntidad(tipo)).collect(Collectors.toList());
	}

	@Override
	public TipoEmpresaDTO getTipoEmpresa(Long id) {
		TipoEmpresa tipoEmpresa = tipoEmpresaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de Empresa", "id", id));
		
		return mapearEntidad(tipoEmpresa);
	}

	@Override
	public TipoEmpresaDTO update(Long id, TipoEmpresaDTO tipoEmpresaDTO) {
		TipoEmpresa tipoEmpresa = tipoEmpresaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de Empresa", "id", id));
		
		tipoEmpresa.setTipo(tipoEmpresaDTO.getTipo());
		
		tipoEmpresaRepo.save(tipoEmpresa);
		
		return mapearEntidad(tipoEmpresa);
	}

	@Override
	public void delete(Long id) {
		TipoEmpresa tipoEmpresa = tipoEmpresaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de empresa", "id", id));
		
		if(tipoEmpresa.getEmpresas().size() > 0) {
			throw new IllegalAccessError("El tipo de empresa no se puede eliminar, tiene empresas asociadas");
		}
		
		tipoEmpresaRepo.delete(tipoEmpresa);

	}

	public TipoEmpresaDTO mapearEntidad(TipoEmpresa tipoEmpresa) {
		return modelMapper.map(tipoEmpresa, TipoEmpresaDTO.class);
	}

	public TipoEmpresa mapearDto(TipoEmpresaDTO tipoEmpresaDTO) {
		return modelMapper.map(tipoEmpresaDTO, TipoEmpresa.class);
	}

}
