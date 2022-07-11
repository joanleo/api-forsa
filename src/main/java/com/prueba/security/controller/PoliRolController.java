package com.prueba.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.security.entity.PoliRol;
import com.prueba.security.service.PoliRolService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/polirol")
@Api(tags = "Politicas de Rol",description = "Operaciones referentes a la politicas de los roles")
public class PoliRolController {

	@Autowired
	private PoliRolService poliRolService;
	
	@PostMapping
	@ApiOperation(value = "Crear politicas de rol", notes = "Crea las politicas de un rol")
	public ResponseEntity<PoliRol> create(@RequestBody PoliRol poliRol){
		System.out.println(poliRol.getNombre());
		System.out.println(poliRol.getRuta().getRuta());
		return new ResponseEntity<PoliRol>(poliRolService.create(poliRol), HttpStatus.CREATED);
	}
	
	@GetMapping
	@ApiOperation(value = "Encuentra una politica de rol")
	public List<PoliRol> list(){
		return poliRolService.list();
	}
}
