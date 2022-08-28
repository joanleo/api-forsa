package com.prueba.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.entity.Empresa;
import com.prueba.entity.TipoActivo;
import com.prueba.exception.ResourceAlreadyExistsException;
import com.prueba.exception.ResourceCannotBeDeleted;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.TipoActivoRepository;

@Service
public class TipoActivoServiceImp implements TipoActivoService {
	
	@Autowired
	private TipoActivoRepository tipoActivoRepo;

	@Override
	public TipoActivo create(TipoActivo tipoActivo, Empresa empresa) {
		TipoActivo exist = tipoActivoRepo.findByNombreAndEmpresa(tipoActivo.getNombre(), empresa);
		if(exist == null) {
			tipoActivoRepo.save(tipoActivo);
		}else {
			throw new ResourceAlreadyExistsException("Tipo de activo", "nombre", tipoActivo.getNombre());
		}
		return null;
	}

	@Override
	public List<TipoActivo> findByNameAndEmpreaAndEstaActivo(String letras, Empresa empresa) {
		List<TipoActivo> tipos = tipoActivoRepo.findByNombreContainsAndEmpresaAndEstaActivoTrue(letras, empresa);
		return tipos;
	}

	@Override
	public List<TipoActivo> list(Empresa empresa) {
		List<TipoActivo> listaTipos = tipoActivoRepo.findByEmpresa(empresa);
		return listaTipos;
	}

	@Override
	public TipoActivo getFamilia(Long id, Empresa empresa) {
		TipoActivo tipoActivo = tipoActivoRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de activo", "id", id));
		
		return tipoActivo;
	}

	@Override
	public void delete(Long id, Empresa empresa) {
		TipoActivo tipoActivo = tipoActivoRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de activo", "id", id));
		if(tipoActivo.getProductos().size() > 0) {
			throw new ResourceCannotBeDeleted("Tipo de activo");
		}
		tipoActivoRepo.delete(tipoActivo);
		
	}

	@Override
	public void unable(Long id, Empresa empresa) {
		TipoActivo tipoActivo = tipoActivoRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de activo", "id", id));
		tipoActivo.setEstaActivo(false);
		tipoActivoRepo.save(tipoActivo);
		
	}

	@Override
	public TipoActivo update(Long id, TipoActivo tipoActivo, Empresa empresa) {
		if(tipoActivo.getEmpresa() == null) {
			tipoActivo.setEmpresa(empresa);
		}
		TipoActivo exist = tipoActivoRepo.findByIdAndEmpresa(id, empresa)
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de activo", "id", id));
		
		exist.setNombre(tipoActivo.getNombre());
		tipoActivoRepo.save(exist);
		
		return exist;
	}

}
