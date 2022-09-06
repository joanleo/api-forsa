package com.prueba.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.prueba.dto.TipoEmpresaDTO;
import com.prueba.entity.TipoEmpresa;
import com.prueba.exception.ResourceAlreadyExistsException;
import com.prueba.exception.ResourceCannotBeDeleted;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.TipoEmpresaRepository;
import com.prueba.specifications.TipoEmpresaSpecifications;

@Service
public class TipoEmpresaServiceImpl implements TipoEmpresaService {

	@Autowired
	private TipoEmpresaRepository tipoEmpresaRepo;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private TipoEmpresaSpecifications tipoEmpresaSpec;

	@Override
	public TipoEmpresaDTO create(TipoEmpresaDTO tipoEmpresaDTO) {
		TipoEmpresa tipoEmpresa = new TipoEmpresa();
		TipoEmpresa exist = tipoEmpresaRepo.findByTipo(tipoEmpresa.getTipo());
		if (exist == null) {
			tipoEmpresa.setTipo(tipoEmpresaDTO.getTipo());
			exist = tipoEmpresaRepo.save(tipoEmpresa);
		} else {
			throw new ResourceAlreadyExistsException("Tipo de empresa", "nombre", tipoEmpresa.getTipo());
		}

		return mapearEntidad(exist);
	}

	@Override
	public List<TipoEmpresaDTO> list() {
		List<TipoEmpresa> listTipos = tipoEmpresaRepo.findByEstaActivoTrue();
		
		return listTipos.stream().map(tipo -> mapearEntidad(tipo)).collect(Collectors.toList());
	}
	
	public List<TipoEmpresaDTO> list(String letras) {
		List<TipoEmpresa> listTipos = tipoEmpresaRepo.findByTipoContainsAndEstaActivoTrue(letras);
		
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
			throw new ResourceCannotBeDeleted("Tipo de empresa");
		}
		tipoEmpresaRepo.delete(tipoEmpresa);

	}
	
	@Override
	public void unable(Long id) {
		TipoEmpresa tipoEmpresa = tipoEmpresaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de empresa", "id", id));
		
		tipoEmpresa.setEstaActivo(false);
		tipoEmpresaRepo.save(tipoEmpresa);
		
	}

	public TipoEmpresaDTO mapearEntidad(TipoEmpresa tipoEmpresa) {
		return modelMapper.map(tipoEmpresa, TipoEmpresaDTO.class);
	}

	public TipoEmpresa mapearDto(TipoEmpresaDTO tipoEmpresaDTO) {
		return modelMapper.map(tipoEmpresaDTO, TipoEmpresa.class);
	}

	@Override
	public Page<TipoEmpresa> searchTiposEmpresa(TipoEmpresaDTO tipoEmpresaDTO, Integer pagina, Integer items) {
		if(items == 0) {
			Page<TipoEmpresa> tiposEmpresa = tipoEmpresaRepo.findAll(tipoEmpresaSpec.getTipoEmpresa(tipoEmpresaDTO),PageRequest.of(0, 10));
			return tiposEmpresa;
		}
		Page<TipoEmpresa> tiposEmpresa = tipoEmpresaRepo.findAll(tipoEmpresaSpec.getTipoEmpresa(tipoEmpresaDTO), PageRequest.of(pagina, items));		
		return tiposEmpresa;
	}

	@Override
	public Page<TipoEmpresa> searchTiposEmpresa(Integer pagina, Integer items) {
		if(items == 0) {
			Page<TipoEmpresa> tiposEmpresa = tipoEmpresaRepo.findByEstaActivoTrue(PageRequest.of(0, 10));
			return tiposEmpresa;
		}
		Page<TipoEmpresa> tiposEmpresa = tipoEmpresaRepo.findByEstaActivoTrue(PageRequest.of(pagina, items));		
		return tiposEmpresa;
	}

	@Override
	public List<TipoEmpresaDTO> list(TipoEmpresaDTO tipoEmpresaDTO) {
		List<TipoEmpresa> empresas = tipoEmpresaRepo.findAll(tipoEmpresaSpec.getTipoEmpresa(tipoEmpresaDTO));
		return empresas.stream().map(empresa -> mapearEntidad(empresa)).collect(Collectors.toList());
	}

	
}
