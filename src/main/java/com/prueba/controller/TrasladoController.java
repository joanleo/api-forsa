package com.prueba.controller;

import java.time.LocalDate;

import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.dto.ApiResponse;
import com.prueba.dto.TrasladoDTO;
import com.prueba.entity.Traslado;
import com.prueba.service.TrasladoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;





@RestController
@RequestMapping("/traslados")
@Tag(name = "Traslados", description = "Operaciones referentes a los traslados")
public class TrasladoController {
	
	@Autowired
	private TrasladoService trasladoService;
		
	@PostMapping
	@Operation(summary = "Crear un traslado", description = "Crea un nuevo traslado")
	public ApiResponse<TrasladoDTO> create(@RequestBody TrasladoDTO trasladoDTO){
		System.out.println(trasladoDTO.getCantProductos());
		return new ApiResponse<>(trasladoService.create(trasladoDTO), HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Encuentra un traslado", description = "Retorna el traslado segun el id indicado")
	public ResponseEntity<Traslado> get(@PathVariable(name = "id")Long id){
		
		return ResponseEntity.ok(trasladoService.getTraslado(id));
	}
	
	@GetMapping("/fecha")
	@Operation(summary = "Encuentra traslados entre fechas dadas", description = "Retorna listado de traslado entre dos fechas dadas")
	public ApiResponse<Page<Traslado>> list(@RequestParam(required=false, defaultValue = "0") Integer pagina, 
											@RequestParam(required=false, defaultValue = "0") Integer items,
											@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate desde,
											@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate hasta)throws ParseException{
		Page<Traslado> traslados = trasladoService.findBetweenDates(desde, hasta, pagina, items);
		
		return new ApiResponse<>(traslados.getSize(), traslados);
	}

}
