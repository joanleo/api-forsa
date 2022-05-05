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

import com.prueba.dto.FamiliaDTO;
import com.prueba.service.FamiliaService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/familias")
@Api(tags = "Familias")
public class FamiliaController {

	@Autowired
	private FamiliaService familiaService;
	
	@PostMapping
	public ResponseEntity<FamiliaDTO> create(@Valid @RequestBody FamiliaDTO familiaDTO){
		return new ResponseEntity<FamiliaDTO>(familiaService.create(familiaDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	public List<FamiliaDTO> list(@RequestParam(required=false) String letters){
		if(letters != null) {
			return familiaService.findByName(letters);
		}else {
			return familiaService.list();			
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<FamiliaDTO> get(@PathVariable(name = "id") Long id) {
		return ResponseEntity.ok(familiaService.getFamilia(id));
	}
}

