package com.prueba.controller;

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

import com.prueba.dto.TipoUbicacionDTO;
import com.prueba.service.TipoUbicacionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tiposubicaciones")
@Api(tags = "Tipos ubicaciones", description = "Operaciones referentes a los tipos de ubicaciones")
public class TipoUbicacionController {
	
	@Autowired
	private TipoUbicacionService tipoUbicService;
	
	@PostMapping
	@ApiOperation(value = "Crea un tipo de ubicacion", notes = "Crea un nuevo tipo de ubicacion")
	public ResponseEntity<TipoUbicacionDTO> create(@Valid @RequestBody TipoUbicacionDTO tipoUbicacionDTO){
		return new ResponseEntity<TipoUbicacionDTO>(tipoUbicService.create(tipoUbicacionDTO), HttpStatus.CREATED); 
	}
	
	@GetMapping
	@ApiOperation(value = "Encuentra los tipos de ubicacion", notes = "Retorna todos los tipos de ubicacion")
	public List<TipoUbicacionDTO> getTiposUbic(){
		return tipoUbicService.list();
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Encuentra un tipo de ubicacion", notes = "Retorna un tipo de ubicacion segun su id")
	public ResponseEntity<TipoUbicacionDTO> getTipoUbic(@PathVariable(name="id") Long id){
		return ResponseEntity.ok(tipoUbicService.getTipoMov(id));
	}
}
