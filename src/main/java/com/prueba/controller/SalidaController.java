/**
 * 
 */
package com.prueba.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.prueba.entity.DetalleSalida;
import com.prueba.entity.Empresa;
import com.prueba.entity.Salida;
import com.prueba.security.dto.ResDTO;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.service.SalidaService;
import com.prueba.util.ReporteSalidaPDF;
import com.prueba.util.UtilitiesApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author Joan Leon
 *
 */
@RestController
@RequestMapping("/salidas")
@Tag(name = "Salidas", description = "Operaciones referentes a los documentos de salida")
public class SalidaController {
	
	@Autowired
	private SalidaService salidaService;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private UtilitiesApi util;
	
	@PostMapping
	@Operation(summary = "Crea una salida")
	public ResponseEntity<?> crearSalida(@RequestBody Salida salida){
		return new ResponseEntity<Salida>(salidaService.crearSalida(salida), HttpStatus.CREATED);
	}
	
	@GetMapping
	@Operation(summary = "Obtiene una lista de salidas")
	public List<Salida> obtenerSalidas(
			@RequestParam(required=false)String letras,
			@RequestParam(required=false) Long nit){
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit == null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();
		}
		
		return salidaService.buscarSalidas(letras, empresa);
	}
	
	@GetMapping("/{idsalida}")
	@Operation(summary = "Obtiene una salida")
	public ResponseEntity<Salida> obtenerSalida(@PathVariable Integer idsalida){
		
		Salida salida = salidaService.obtieneSalida(idsalida);
		
		return ResponseEntity.ok(salida);
	}
	
	@GetMapping("/indexados")
	@Operation(summary = "Pagina las salidas existentes")
	public ApiResponse<Page<Salida>> obtenerSalidasPaginado(
			@RequestParam(required=false, defaultValue = "0") Integer pagina, 
			@RequestParam(required=false, defaultValue = "0") Integer items,
			@RequestBody(required=false) Salida salida,
			@RequestParam(required=false) Long nit){
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		
		if(Objects.isNull(salida)) {
			Page<Salida> salidas = salidaService.buscarSalidas(empresa, pagina, items);
			return new ApiResponse<>(salidas.getSize(), salidas);
		}else {
			Page<Salida> salidas = salidaService.buscarSalidas(salida, empresa, pagina, items);
			return new ApiResponse<>(salidas.getSize(), salidas);
		}

	}
	
	@PatchMapping("/{idsalida}/{codigopieza}")
	@Operation(summary = "Confirma un activo de la salida")
	public ResponseEntity<?> confirmarActivoSalida(
			@PathVariable Integer idsalida,
			@PathVariable String codigopieza){
		
		Salida salida = salidaService.confirmarActivoSalida(idsalida, codigopieza);
		
		return new ResponseEntity<Salida>(salida, HttpStatus.OK);
	}
	
	@PutMapping("/{idsalida}")
	@Operation(summary = "Confirma todos los activos de la salida")
	public ResponseEntity<?> confirmarSalida(@PathVariable Integer idsalida){
		
		Salida salida = salidaService.confirmarSalida(idsalida);
		
		return new ResponseEntity<Salida>(salida, HttpStatus.OK);
	}
	
	@GetMapping("/detalle/{idsalida}")
	@Operation(summary = "Obtiene el detalle de una salida")
	public ApiResponse<Page<DetalleSalida>> getInventario(@PathVariable Integer idsalida){
		
		Page<DetalleSalida> detalles = salidaService.obtieneDetalleSalida(idsalida);
		return new ApiResponse<>(detalles.getSize(), detalles);
		//return ResponseEntity.ok(salidaService.obtieneSalida(idsalida));
	}
	
	@DeleteMapping("/{idsalida}/eliminar/{codigopieza}")
	@Operation(summary = "Elimina un activo de una salida")
	public ResponseEntity<?> eliminarActivo(@PathVariable Integer idsalida,
			@PathVariable String codigopieza){
		salidaService.eliminarActivo(idsalida, codigopieza);
		return new ResponseEntity<ResDTO>(new ResDTO("Activo eliminada con exito"), HttpStatus.OK);
	}
	
	@GetMapping("/detalle/{idsalida}/descarga")
	@Operation(summary = "Retorna una salida en formato PDF", description = "Retorna una salida con detalle segun el numero de salida")
	public void exportToPdfSlida(HttpServletResponse response,
			@PathVariable Integer idsalida) throws DocumentException, IOException{
		
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=reporteSalida_" + idsalida + "_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);
		
		Salida salida = salidaService.obtieneSalida(idsalida);
		System.out.println(salida);
		ReporteSalidaPDF exportar = new ReporteSalidaPDF(salida);
		exportar.export(response);
	}

}
