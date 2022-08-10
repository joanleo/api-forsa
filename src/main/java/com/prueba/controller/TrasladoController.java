package com.prueba.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.json.ParseException;
import org.modelmapper.ModelMapper;
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

import com.lowagie.text.DocumentException;
import com.prueba.dto.ApiResponse;
import com.prueba.dto.DetalleTrasladoDTO;
import com.prueba.dto.TrasladoDTO;
import com.prueba.entity.DetalleTrasl;
import com.prueba.entity.Traslado;
import com.prueba.security.dto.ResDTO;
import com.prueba.service.TrasladoService;
import com.prueba.util.ReporteTrasladoPDF;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;





@RestController
@RequestMapping("/traslados")
@Tag(name = "Traslados", description = "Operaciones referentes a los traslados")
public class TrasladoController {
	
	@Autowired
	private TrasladoService trasladoService;
	
	@Autowired
	private ModelMapper modelMapper;
		
	@PostMapping
	@Operation(summary = "Crear un traslado", description = "Crea un nuevo traslado")
	public ResponseEntity<TrasladoDTO> create(@RequestBody TrasladoDTO trasladoDTO){
		return new ResponseEntity<>(trasladoService.create(trasladoDTO), HttpStatus.CREATED);
	}
	
	@PostMapping("/indexados")
	@Operation(summary = "Encuentra los traslados", description = "Retorna paginacion de los traslados que coincidan con el filtro enviado en trasladoDTO")
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
	
	@DeleteMapping("eliminar/{idtraslado}")
	@Operation(summary = "Elimina todas las piezas de un traslado")
	public ResponseEntity<?> eliminarPieza(@PathVariable Long idtraslado,
										   @RequestParam(required=false) Long nit){
		trasladoService.eliminarTodo(idtraslado);
		return new ResponseEntity<ResDTO>(new ResDTO("Activos eliminados del traslado con exito"), HttpStatus.OK);
	}
	
	@DeleteMapping("/{idtraslado}")
	@Operation(summary = "Elimina un traslado", description = "Elimina un traslado por su id")
	public ResponseEntity<?> eliminarTraslado(@PathVariable Long idtraslado,
			   @RequestParam(required=false) Long nit){
		trasladoService.eliminarTraslado(idtraslado, nit);
		return new ResponseEntity<ResDTO>(new ResDTO("Traslado eliminado con exito"), HttpStatus.OK);
	}
	
	@DeleteMapping("/{idtraslado}/eliminar/{codigopieza}")
	public ResponseEntity<?> eliminarActivo(@PathVariable Long idtraslado,
			@PathVariable String codigopieza){
		trasladoService.eliminarPieza(idtraslado, codigopieza);
		return new ResponseEntity<ResDTO>(new ResDTO("Activo eliminada con exito"), HttpStatus.OK);
	}
	
	@GetMapping("/detalle/{idtraslado}/descarga")
	@Operation(summary = "Retorna el detalle de traslado en formato PDF", description = "Retorna un traslado con detalle segun el numero del traslado")
	public void exportToPdfSlida(HttpServletResponse response,
			@PathVariable Long idtraslado) throws DocumentException, IOException{
		
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=reporteTraslado_" + idtraslado + "_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);
		
		Traslado traslado = trasladoService.getTraslado(idtraslado);
		ReporteTrasladoPDF exportar = new ReporteTrasladoPDF(traslado);
		exportar.export(response);
	}
	
	@GetMapping("/detalle/{idtraslado}")
	@Operation(summary = "Obtiene el detalle de un traslado")
	public ApiResponse<Page<DetalleTrasl>> obtenerDetalleTraslado(
			@RequestParam(required=false, defaultValue = "0") Integer pagina, 
			@RequestParam(required=false, defaultValue = "0") Integer items,
			@PathVariable Long idtraslado){
		
		Page<DetalleTrasl> detalles = trasladoService.obtieneDetalleTraslado(idtraslado, pagina, items);
		
		/*for(DetalleTrasl detalle: detalles) {
			DetalleTrasladoDTO detalleTrasladoDTO = modelMapper.map(detalles, DetalleTrasladoDTO.class);
			
		}
		System.out.println(detalleTrasladoDTO.getUsuarioconfirma().getNombre());*/
		return new ApiResponse<>(detalles.getSize(), detalles);
	}
	
}
