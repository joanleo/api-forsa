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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.dto.EstadoDTO;
import com.prueba.service.EstadoService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/estados")
@Api(tags = "Estado", description="Operaciones referentes al estado del producto")
public class EstadoController {

	@Autowired
	private EstadoService estadoService;
	
	@PostMapping
	public ResponseEntity<EstadoDTO> create(@Valid @RequestBody EstadoDTO estadoDTO){
		return new ResponseEntity<EstadoDTO>(estadoService.create(estadoDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	public List<EstadoDTO> list(@RequestParam(required=false) String letter){
		if(letter != null) {
			return estadoService.findByTipo(letter);
		}else {
			return estadoService.list();			
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<EstadoDTO> get(@PathVariable(name = "id") Long id){
		return ResponseEntity.ok(estadoService.getEstado(id));
	}
}

