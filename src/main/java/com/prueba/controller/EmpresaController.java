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

import com.prueba.dto.EmpresaDTO;
import com.prueba.service.EmpresaService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/empresas")
@Api(tags = "Empresas")
public class EmpresaController {
	
	@Autowired
	private EmpresaService empresaService;
	
	@PostMapping
	public ResponseEntity<EmpresaDTO> create(@Valid @RequestBody EmpresaDTO empresaDTO){
		return new ResponseEntity<EmpresaDTO>(empresaService.create(empresaDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	public List<EmpresaDTO> list(@RequestParam(required=false) String letters){
		if(letters != null) {
			return empresaService.findByName(letters);
		}else {
			return empresaService.list();			
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<EmpresaDTO> get(@PathVariable(name = "id") Long id){
		return ResponseEntity.ok(empresaService.getEmpresa(id));
	}
	

}

