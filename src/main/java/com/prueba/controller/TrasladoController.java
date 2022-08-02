package com.prueba.controller;

import java.time.LocalDate;
import java.util.Objects;

import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.dto.ApiResponse;
import com.prueba.dto.TrasladoDTO;
import com.prueba.entity.Traslado;
import com.prueba.security.dto.ResDTO;
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
	public ResponseEntity<TrasladoDTO> create(@RequestBody TrasladoDTO trasladoDTO){
		return new ResponseEntity<>(trasladoService.create(trasladoDTO), HttpStatus.CREATED);
	}
	
	@PostMapping("/indexados")
	@Operation(summary = "Encuentra los traslados")
	public ApiResponse<Page<Traslado>> listaPaginada(
			@RequestParam(required=false, defaultValue = "0") Integer pagina, 
			@RequestParam(required=false, defaultValue = "0") Integer items,
			@RequestBody(required=false) TrasladoDTO trasladoDTO){
		
		if(Objects.isNull(trasladoDTO)) {
			Page<Traslado> traslados = trasladoService.buscarTraslados(pagina, items);
			return new ApiResponse<>(traslados.getSize(), traslados);			
		}else {
			Page<Traslado> traslados = trasladoService.buscarTraslados(trasladoDTO, pagina, items);
			return new ApiResponse<>(traslados.getSize(), traslados);
		}
	
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
	
	@PatchMapping("/{idtraslado}/{codigopieza}")
	@Operation(summary = "Confirma una pieza de un traslado")
	public ResponseEntity<?> confirmarPieza(@PathVariable Long idtraslado,
											@PathVariable String codigopieza,
											@RequestParam(required=false) Long nit){
		
		Traslado traslado;
		try {
			traslado = trasladoService.confirmarPieza(idtraslado, codigopieza);
			return new ResponseEntity<Traslado>(traslado, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ResDTO>(new ResDTO("Hubo un error"), HttpStatus.CONFLICT);
		}
		
		
	}
	
	@PatchMapping("/{idtraslado}")
	@Operation(summary = "Confirma todas las piezas de un traslado")
	public ResponseEntity<?> confirmarTodo(@PathVariable Long idtraslado,
											@RequestParam(required=false) Long nit){
		
		Traslado traslado = trasladoService.confirmarTodo(idtraslado);
		
		return new ResponseEntity<Traslado>(traslado, HttpStatus.OK);
	}
	
	@PutMapping("/{idtraslado}/{codigopieza}")
	@Operation(summary = "Recibe una pieza de un traslado")
	public ResponseEntity<?> recibirPieza(@PathVariable Long idtraslado,
											@PathVariable String codigopieza,
											@RequestParam(required=false) Long nit){
		
		Traslado traslado;
		try {
			traslado = trasladoService.recibirPieza(idtraslado, codigopieza);
			return new ResponseEntity<Traslado>(traslado, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Hubo un error", HttpStatus.CONFLICT);
		}
		
		
	}
	
	@PutMapping("/{idtraslado}")
	@Operation(summary = "Recibe todas las pieza de un traslado")
	public ResponseEntity<?> recibirTodo(@PathVariable Long idtraslado,
											@RequestParam(required=false) Long nit){
								
		Traslado traslado = trasladoService.recibirTodo(idtraslado);
		
		return new ResponseEntity<Traslado>(traslado, HttpStatus.OK);
	}
	
	@PatchMapping("eliminar/{idtraslado}")
	@Operation(summary = "Eliminar piezas de un traslado", description = "Elimina una pieza de un traslado o elimina todas las piezas de un traslado")
	public ResponseEntity<?> eliminarPieza(@PathVariable Long idtraslado,
										   @RequestParam String codigopieza,
										   @RequestParam(required=false) Long nit){
		if(codigopieza != null) {
			trasladoService.eliminarPieza(idtraslado, codigopieza);			
			return new ResponseEntity<ResDTO>(new ResDTO("Activo eliminado del traslado con exito"), HttpStatus.OK);
		}
		trasladoService.eliminarTodo(idtraslado);
		return new ResponseEntity<ResDTO>(new ResDTO("Activos eliminados del traslado con exito"), HttpStatus.OK);
	}
	
	/*@PatchMapping("/{idtraslado}/verificar aun no se confirma este endpoint")
	public ResponseEntity<?> eliminarTodo(@PathVariable Long idtraslado,
										   @RequestParam(required=false) Long nit){
		}*/

	@DeleteMapping("/{idtraslado}")
	public ResponseEntity<?> eliminarTraslado(@PathVariable Long idtraslado,
			   @RequestParam(required=false) Long nit){
		trasladoService.eliminarTraslado(idtraslado, nit);
		return new ResponseEntity<ResDTO>(new ResDTO("Traslado eliminado con exito"), HttpStatus.OK);
	}
	
}
