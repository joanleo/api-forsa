package com.prueba.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.dto.UbicacionDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Estado;
import com.prueba.entity.Ubicacion;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.UbicacionRepository;

@Service
public class UbicacionServiceImpl implements UbicacionService {
	
	@Autowired
	private UbicacionRepository ubicacionRepo;

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public UbicacionDTO create(UbicacionDTO ubicacionDTO) {
		Ubicacion ubicacion = mapearDTO(ubicacionDTO);
		Ubicacion exist = ubicacionRepo.findByNombreAndEmpresa(ubicacion.getNombre(), ubicacionDTO.getEmpresa());
		if(exist != null) {
			ubicacionRepo.save(ubicacion);
		}else {
			throw new IllegalAccessError("La ubicacion que desea crear ya existe: "+ ubicacion.getNombre());
		}
		
		return mapearEntidad(ubicacion);
	}

	@Override
	public List<UbicacionDTO> list(Empresa empresa) {
		List<Ubicacion> ubicaciones = ubicacionRepo.findByEmpresaAndEstaActivo(empresa, true);
		return ubicaciones.stream().map(ubicacion -> mapearEntidad(ubicacion)).collect(Collectors.toList());
	}

	@Override
	public UbicacionDTO getUbicacion(Long id, Empresa empresa) {
		Ubicacion ubicacion = ubicacionRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Ubicacion", "id", id));
		
		return mapearEntidad(ubicacion);
	}

	@Override
	public UbicacionDTO update(Long id, UbicacionDTO ubicacionDTO) {
		Ubicacion ubicacion = ubicacionRepo.findByIdAndEmpresa(id, ubicacionDTO.getEmpresa())
				.orElseThrow(() -> new ResourceNotFoundException("Ubicacion", "id", id));
		
		ubicacion.setCiudad(ubicacionDTO.getCiudad());
		ubicacion.setDireccion(ubicacionDTO.getDireccion());
		ubicacion.setNombre(ubicacionDTO.getNombre());
		ubicacion.setTipo(ubicacionDTO.getTipo());
		
		ubicacionRepo.save(ubicacion);
		
		return mapearEntidad(ubicacion);
	}

	@Override
	public void delete(Long id, Empresa empresa) {
		Ubicacion ubicacion = ubicacionRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Ubicacion", "id", id));
		
		if(ubicacion.getProductos().size() > 0) {
			throw new IllegalAccessError("No se pude eliminar la ubicacion tiene productos asociados");
		}
		
		ubicacionRepo.delete(ubicacion);

	}
	
	@Override
	public void unable(Long id, Empresa empresa) {
		Ubicacion ubicacion  = ubicacionRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Ubicacion", "id", id));
		
		ubicacion.setEstaActivo(false);
		ubicacionRepo.save(ubicacion);
		
	}
	
	public UbicacionDTO mapearEntidad(Ubicacion ubicacion) {
		return modelMapper.map(ubicacion, UbicacionDTO.class);
	}
	
	public Ubicacion mapearDTO(UbicacionDTO ubicacionDTO) {
		return modelMapper.map(ubicacionDTO, Ubicacion.class);
	}

	@Override
	public List<UbicacionDTO> findByName(String name, Empresa empresa, Boolean estaActivo) {
		List<Ubicacion> ubicaciones = ubicacionRepo.findByNombreContainsAndEmpresaAndEstaActivo(name, empresa, estaActivo);
		return ubicaciones.stream().map(ubicacion -> mapearEntidad(ubicacion)).collect(Collectors.toList());
	}

}
