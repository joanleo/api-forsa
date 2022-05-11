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

import com.prueba.dto.UbicacionDTO;
import com.prueba.service.UbicacionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/ubicaciones")
@Api(tags = "Ubicaciones", description = "Operaciones referentes a las ubicaciones")
public class UbicacionController {

	@Autowired
	private UbicacionService ubicacionService;
	
	@PostMapping
	@ApiOperation(value = "Crea una ubicacion", notes = "Crea una nueva ubicacion")
	public ResponseEntity<UbicacionDTO> create(@Valid @RequestBody UbicacionDTO ubicacionDTO){
		return new ResponseEntity<UbicacionDTO>(ubicacionService.create(ubicacionDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	@ApiOperation(value = "Encuentra las ubicaciones", notes = "Retorna las ubicaciones que en su nombre contenga las letras indicadas, retorna todas las ubicaciones si no se especifica ninguna letra")
	public List<UbicacionDTO> list(@RequestParam(required=false) String letters){
		if(letters != null) {
			return ubicacionService.findByName(letters);
		}else {
			return ubicacionService.list();			
		}
		
	}
	
	@GetMapping("/{id}")
	@ApiOperation(value = "Encuentra una ubicacion", notes = "Retorna una ubicacion por su id")
	public ResponseEntity<UbicacionDTO> getUbicacion(@PathVariable(name="id") Long id){
		return ResponseEntity.ok(ubicacionService.getUbicacion(id));
	}
	
}
