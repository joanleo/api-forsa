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

import com.prueba.dto.TipoEmpresaDTO;
import com.prueba.service.TipoEmpresaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tipoempresas")
@Api(tags = "Tipos empresa", description = "Opereciones referentes a los tipos de empresa")
public class TipoEmpresaController {
	
	@Autowired
	private TipoEmpresaService tipoEmpresaService;
	
	@PostMapping
	@ApiOperation(value = "Crea un tipo de empresa", notes = "Crea un nuevo tipo de empresa")
	public ResponseEntity<TipoEmpresaDTO> create(@Valid @RequestBody TipoEmpresaDTO tipoEmpresaDTO){
		return new ResponseEntity<TipoEmpresaDTO>(tipoEmpresaService.create(tipoEmpresaDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	@ApiOperation(value = "Encuentra los tipos de empresa", notes = "Retorna todos los tipos de empresas")
	public List<TipoEmpresaDTO> getEmpresas(){
		return tipoEmpresaService.list();
	}
	
	@GetMapping("/{id}")
	@ApiOperation(value = "Encuentra un tipo de empresa", notes = "Retorna un tipo de empresa segun su id")
	public ResponseEntity<TipoEmpresaDTO> getEmpresa(@PathVariable(name = "id") Long id){
		return ResponseEntity.ok(tipoEmpresaService.getTipoEmpresa(id));
	}
	

}
