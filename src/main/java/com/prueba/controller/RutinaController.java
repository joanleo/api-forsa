package com.prueba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.entity.Rutina;
import com.prueba.service.RutinaServices;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/rutinas")
@Tag(name = "Rutinas", description = "")
public class RutinaController {
	
	@Autowired
	private RutinaServices rutinaServices;
	
	@GetMapping
	@Tag(name = "Encuentra las rutinas de la aplicacion", description = "Retorna todas las rutinas de la aplicacion")
	public List<Rutina> listRutinas(){
		return rutinaServices.listRutinas();
	}

}
