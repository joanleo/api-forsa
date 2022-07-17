package com.prueba.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.entity.Rutina;
import com.prueba.repository.RutinaRepository;

@Service
public class RutinaServicesImpl implements RutinaServices {
	
	@Autowired
	private RutinaRepository rutinaRepo;

	@Override
	public List<Rutina> listRutinas() {
		// TODO Auto-generated method stub
		return rutinaRepo.findAll();
	}

}
