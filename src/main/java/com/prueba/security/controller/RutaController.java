package com.prueba.security.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.security.dto.RutaDTO;
import com.prueba.security.service.RutaService;


import io.swagger.annotations.Api;

@RestController
@RequestMapping("/rutas")
@Api(tags = "Rutas")
public class RutaController {

	@Autowired
	private RutaService rutaService;
	
	@PostMapping
	public ResponseEntity<RutaDTO> create(@Valid @RequestBody RutaDTO rutaDTO){
		return new ResponseEntity<RutaDTO>(rutaService.create(rutaDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	public List<RutaDTO> list(){
		return rutaService.list();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<RutaDTO> get(@PathVariable(name = "id") Long id){
		return ResponseEntity.ok(rutaService.getRuta(id));
	}
}

