package com.prueba.security.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.security.entity.PoliRol;
import com.prueba.security.entity.Ruta;
import com.prueba.security.repository.PoliRolRepository;
import com.prueba.security.repository.RutaRepository;

@Service
public class PoliRolSerciceImp implements PoliRolService {
	
	@Autowired
	private PoliRolRepository poliRolRepo;
	
	@Autowired
	private RutaRepository rutaRepo;
;

	@Override
	public PoliRol create(PoliRol poliRol) {

		PoliRol exist = poliRolRepo.findByNombreAndMetodo(poliRol.getNombre(), poliRol.getMetodo());
		Ruta existRuta = rutaRepo.findByRutaAndMetodo(poliRol.getRuta().getRuta(), poliRol.getMetodo());

		if(existRuta == null) {
			throw new IllegalAccessError("La ruta a la que trata de crear la politica no existe");
		}

		poliRol.setRuta(existRuta);
		if(exist == null) {
			poliRolRepo.save(poliRol);
		}else{
			throw new IllegalAccessError("La politica que desea crear ya existe: " + poliRol.getNombre());
		}
		
		return poliRol;
	}

	@Override
	public List<PoliRol> list() {
		
		return  poliRolRepo.findAll();
	}
}


