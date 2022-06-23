package com.prueba.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.DocumentException;
import com.prueba.entity.Producto;
import com.prueba.service.ProductoService;
import com.prueba.util.ReporteVerificarPDF;

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
		
		return reporte;
	}
	
	@GetMapping("/verificacion/descarga")
	@ApiOperation(value = "Crea un reporte de verificacion en formato PDF", notes = "Retorna un PDF con el listado de los activos de una orden dada "
			+ "segun el filtro indicado. Los filtros podran ser 'faltantes', 'sobrantes', 'ok', 'todos'")
	public void exportToPDF(HttpServletResponse response,
			@RequestParam String orden,
			@RequestParam(defaultValue = "todos") String filtro) throws DocumentException, IOException {
		
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=reporteVerificacion_" + orden + "_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);
		
		List<Producto> productos =  productoService.getVerificacion(orden,filtro);
		
		ReporteVerificarPDF exporter = new ReporteVerificarPDF(productos, filtro, orden);
        exporter.export(response);
		
	}

}
