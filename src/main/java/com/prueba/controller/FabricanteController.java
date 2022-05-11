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
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/fabricantes")
@Api(tags = "Fabricantes", description = "Operaciones referentes a los fabricantes")
public class FabricanteController {

	@Autowired
	private FabricanteService fabricanteService;
	
	@PostMapping
	@ApiOperation(value = "Crea un fabricante", notes = "Crea un nuevo fabricante")
	public ResponseEntity<FabricanteDTO> create(@Valid @RequestBody FabricanteDTO fabricanteDTO){
		return new ResponseEntity<FabricanteDTO>(fabricanteService.create(fabricanteDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	@ApiOperation(value = "Encuenta los fabricantes", notes = "Retorna los fabricantes que en su nombre contengan las letrtas indicadas, retorna todos los fabricantes si no se indica ninguna letra")
	public List<FabricanteDTO> list(@RequestParam(required = false) String letters){
		if(letters != null) {
			return fabricanteService.findByName(letters);			
		}else {
			return fabricanteService.list();
		}
	}
	
	@GetMapping("/{id}")
	@ApiOperation(value = "Encuentra un fabricante", notes = "Retorna un fabricante por el id")
	public FabricanteDTO get(@PathVariable(name = "id") Long id) {
		return fabricanteService.getFabricante(id);
	}
}
