package com.prueba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.dto.ReporteVerificacionDTO;
import com.prueba.service.ProductoService;

@RestController
@RequestMapping("/reportes")
public class ReportesController {
	
	@Autowired
	private ProductoService productoService;
	
	@GetMapping("/verificacion")
	public ResponseEntity<ReporteVerificacionDTO> getVerificacion(
			@RequestParam String orden,
			@RequestParam(required=false, defaultValue = "todos") String filtro){
		System.out.println("Controller orden " + orden);
		System.out.println("Controller filtro " + filtro);
		ReporteVerificacionDTO reporte = productoService.getVerificacion(orden, filtro);
		
		return new ResponseEntity<ReporteVerificacionDTO>(reporte, HttpStatus.OK);
	}

}
