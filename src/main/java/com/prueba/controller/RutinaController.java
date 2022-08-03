package com.prueba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.entity.Rutina;
import com.prueba.service.RutinaServices;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;

@Hidden
@RestController
@RequestMapping("/rutinas")
@Tag(name = "Rutinas", description = "Rutinas o modulos de la aplicacion")
public class RutinaController {
	
	@Autowired
	private RutinaServices rutinaServices;
	
	@GetMapping
	public List<Rutina> listRutinas(){
		return rutinaServices.listRutinas();
	}

}
