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

import com.prueba.dto.TipoMovDTO;
import com.prueba.service.TipoMovService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/tiposmovimientos")
@Api(tags = "Tipos Movimiento")
public class TipoMovController {
	
	@Autowired
	private TipoMovService tipoMovService;
	
	@PostMapping
	public ResponseEntity<TipoMovDTO> create(@Valid @RequestBody TipoMovDTO tipoMovDTO){
		return new ResponseEntity<TipoMovDTO>(tipoMovService.create(tipoMovDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	public List<TipoMovDTO> getTiposMov(){
		return tipoMovService.list();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TipoMovDTO> getTipoMov(@PathVariable(name="id") Long id){
		return ResponseEntity.ok(tipoMovService.getTipoMov(id));
	}

}
