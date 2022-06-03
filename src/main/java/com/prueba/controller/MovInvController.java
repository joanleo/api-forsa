package com.prueba.controller;

import java.util.Date;

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
import com.prueba.dto.MovInventarioDTO;
import com.prueba.entity.MovInventario;
import com.prueba.service.MovInventarioService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/inventarios")
@Api(tags = "Inventarios", description = "Operaciones referentes a los inventarios")
public class MovInvController {
	
	@Autowired
	private MovInventarioService movInvService;
	
	@PostMapping
	@ApiOperation(value = "Crea un inventario", notes = "Crea un nuevo inventario")
	public ResponseEntity<MovInventarioDTO> create(@RequestBody MovInventarioDTO movInventarioDto){
		System.out.println(movInventarioDto.getUbicacion().getId());
		return new ResponseEntity<MovInventarioDTO>(movInvService.create(movInventarioDto), HttpStatus.CREATED);
	}
	
	@GetMapping
	@ApiOperation(value = "Lista los inventarios existentes", notes = "Retorna los inventarios que se encuentren en el rango de fechas dado (formato de fecha 'dd-MM-yyyy') o que en su nombre "
			+ "	contenga los valores  indicadas en la variable letras. Si no se incluye ningun valor retorna todos los inventarios existentes")
	public ApiResponse<Page<MovInventario>> list(@RequestParam(required=false, defaultValue = "0") Integer pagina, 
												 @RequestParam(required=false, defaultValue = "10") Integer items,
												 @RequestParam(required=false) String letras,
												 @RequestParam(required=false) @DateTimeFormat(pattern = "dd-MM-yyyy")Date  desde,
												 @RequestParam(required=false) @DateTimeFormat(pattern = "dd-MM-yyyy")Date  hasta)throws ParseException{
		
				
		if(letras != null) {
			System.out.println("letras "+letras);
			Page<MovInventario> inventarios = movInvService.searchInv(letras, pagina, items);
			return new ApiResponse<>(inventarios.getSize(), inventarios);
		}else {
			Page<MovInventario> inventarios = movInvService.list(pagina, items);
			return new ApiResponse<>(inventarios.getSize(), inventarios);
		}
		
	}
	
	@GetMapping("/detalle/{id}")
	@ApiOperation(value = "Encuentra un inventario", notes = "Retorna un inventario con detalle segun el numero de inventario")
	public ResponseEntity<MovInventario> getInventario(@PathVariable Long id){
		return ResponseEntity.ok(movInvService.getInventario(id));
	}

}
