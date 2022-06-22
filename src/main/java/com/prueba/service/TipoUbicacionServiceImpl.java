package com.prueba.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.dto.TipoMovDTO;
import com.prueba.dto.TipoUbicacionDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.TipoMov;
import com.prueba.entity.TipoUbicacion;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.TipoUbicacionRepository;

@Service
public class TipoUbicacionServiceImpl implements TipoUbicacionService {
	
	@Autowired
	private TipoUbicacionRepository tipoUbicRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public TipoUbicacionDTO create(TipoUbicacionDTO tipoUbicacionDTO) {
		TipoUbicacion tipoUbicacion = mapearDTO(tipoUbicacionDTO);
		TipoUbicacion exist = tipoUbicRepo.findByNombreAndEmpresa(tipoUbicacion.getNombre(), tipoUbicacionDTO.getEmpresa());
		
		if(exist != null) {
			tipoUbicRepo.save(tipoUbicacion);
		}else {
			throw new IllegalAccessError("El tipo de movimiento que desea crear ya existe");
		}
		
		return mapearEntidad(tipoUbicacion);
	}

	@Override
	public List<TipoUbicacionDTO> list(Empresa empresa) {
		List<TipoUbicacion> lista = tipoUbicRepo.findByEmpresa(empresa);
		
		return lista.stream().map(tipo -> mapearEntidad(tipo)).collect(Collectors.toList());
	}

	@Override
	public TipoUbicacionDTO getTipoMov(Long id, Empresa empresa) {
		TipoUbicacion tipoUbicacion = tipoUbicRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de ubicacion", "id", id));
		
		return mapearEntidad(tipoUbicacion);
	}

	@Override
	public TipoUbicacionDTO update(Long id, TipoUbicacionDTO tipoUbicacionDTO) {
		TipoUbicacion tipoUbicacion = tipoUbicRepo.findByIdAndEmpresa(id, tipoUbicacionDTO.getEmpresa())
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de ubicacion", "id", id));
		
		tipoUbicacion.setNombre(tipoUbicacionDTO.getNombre());
		
		tipoUbicRepo.save(tipoUbicacion);
		
		return mapearEntidad(tipoUbicacion);
	}

	@Override
	public void delete(Long id, Empresa empresa) {
		TipoUbicacion tipoUbicacion = tipoUbicRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de ubicacion", "id", id));
		
		if(tipoUbicacion.getUbicaciones().size() > 0) {
			throw new IllegalAccessError("El tipo de empresa no se puede eliminar, tiene ubicaciones asociadas");
		}
		
		tipoUbicRepo.delete(tipoUbicacion);

	}
	
	@Override
	public void unable(Long id, Empresa empresa) {
		TipoUbicacion tipoUbicacion = tipoUbicRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de ubicacion", "id", id));
		
		tipoUbicacion.setEstaActivo(false);
		tipoUbicRepo.save(tipoUbicacion);
		
	}
	
	public TipoUbicacionDTO mapearEntidad(TipoUbicacion tipoUbicacion) {
		return modelMapper.map(tipoUbicacion, TipoUbicacionDTO.class);
	}
	
	public TipoUbicacion mapearDTO(TipoUbicacionDTO tipoUbicacionDTO) {
		return modelMapper.map(tipoUbicacionDTO, TipoUbicacion.class);
	}
	
	@Override
	public List<TipoUbicacionDTO> findByNombreAndEmpresaAndEstaActivo(String letras, Empresa empresa, Boolean estaActivo) {
		List<TipoUbicacion> tiposMov = tipoUbicRepo.findByNombreContainsAndEmpresaAndEstaActivo(letras, empresa, estaActivo);
		return tiposMov.stream().map(tipo -> mapearEntidad(tipo)).collect(Collectors.toList());
	}

}
