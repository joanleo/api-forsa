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

@RestController
@RequestMapping("/tiposubicaciones")
@Api(tags = "Tipos ubicaciones")
public class TipoUbicacionController {
	
	@Autowired
	private TipoUbicacionService tipoUbicService;
	
	@PostMapping
	public ResponseEntity<TipoUbicacionDTO> create(@Valid @RequestBody TipoUbicacionDTO tipoUbicacionDTO){
		return new ResponseEntity<TipoUbicacionDTO>(tipoUbicService.create(tipoUbicacionDTO), HttpStatus.CREATED); 
	}
	
	@GetMapping
	public List<TipoUbicacionDTO> getTiposUbic(){
		return tipoUbicService.list();
	}

	@GetMapping("/{id}")
	public ResponseEntity<TipoUbicacionDTO> getTipoUbic(@PathVariable(name="id") Long id){
		return ResponseEntity.ok(tipoUbicService.getTipoMov(id));
	}
}
