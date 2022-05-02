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

import com.prueba.dto.UbicacionDTO;
import com.prueba.service.UbicacionService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/ubicaciones")
@Api(tags = "Ubicaciones")
public class UbicacionController {

	@Autowired
	private UbicacionService ubicacionService;
	
	@PostMapping
	public ResponseEntity<UbicacionDTO> create(@Valid @RequestBody UbicacionDTO ubicacionDTO){
		return new ResponseEntity<UbicacionDTO>(ubicacionService.create(ubicacionDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	public List<UbicacionDTO> list(){
		return ubicacionService.list();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UbicacionDTO> getUbicacion(@PathVariable(name="id") Long id){
		return ResponseEntity.ok(ubicacionService.getUbicacion(id));
	}
	
}
