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

import com.prueba.dto.FabricanteDTO;
import com.prueba.service.FabricanteService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/fabricantes")
@Api(tags = "Fabricantes")
public class FabricanteController {

	@Autowired
	private FabricanteService fabricanteService;
	
	@PostMapping
	public ResponseEntity<FabricanteDTO> create(@Valid @RequestBody FabricanteDTO fabricanteDTO){
		return new ResponseEntity<FabricanteDTO>(fabricanteService.create(fabricanteDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	public List<FabricanteDTO> list(@RequestParam(required = false) String letter){
		if(letter != null) {
			return fabricanteService.findByName(letter);			
		}else {
			return fabricanteService.list();
		}
	}
	
	@GetMapping("/{id}")
	public FabricanteDTO get(@PathVariable(name = "id") Long id) {
		return fabricanteService.getFabricante(id);
	}
}
