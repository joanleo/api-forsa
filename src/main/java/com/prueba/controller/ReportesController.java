package com.prueba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.dto.ProductoDTO;
import com.prueba.dto.ReporteVerificacionDTO;
import com.prueba.entity.Producto;
import com.prueba.service.ProductoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/reportes")
@Api(tags = "Reportes", description = "Reportes")
public class ReportesController {
	
	@Autowired
	private ProductoService productoService;
	
	@GetMapping("/verificacion")
	@ApiOperation(value = "Crea un reporte de verificacion", notes = "Retorna un listado de los activos de una orden dada "
			+ "segun el filtro indicado. Los filtros podran ser 'faltantes', 'sobrantes', 'ok', 'todos'")
	public Page<Producto> getVerificacion(
			@RequestParam(required=false, defaultValue = "0") Integer pagina, 
			@RequestParam(required=false, defaultValue = "0") Integer items,
			@RequestParam(required=false) String orden,
			@RequestParam(required=false, defaultValue = "todos") String filtro){
		System.out.println("Controller orden " + orden);
		System.out.println("Controller filtro " + filtro);
		Page<Producto> reporte = productoService.getVerificacion(orden, filtro, pagina, items);
		
		return reporte;//new ResponseEntity<ReporteVerificacionDTO>(reporte, HttpStatus.OK);
	}

}
