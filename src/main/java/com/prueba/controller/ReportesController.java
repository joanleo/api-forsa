package com.prueba.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.DocumentException;
import com.prueba.dto.ComparativoInventarioDTO;
import com.prueba.dto.ComparativoUbicacionDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Producto;
import com.prueba.entity.Ubicacion;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.UbicacionRepository;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.service.ProductoService;
import com.prueba.util.CsvExportService;
import com.prueba.util.ReporteComparativo;
import com.prueba.util.ReporteDiferenciasPDF;
import com.prueba.util.ReporteVerificarPDF;
import com.prueba.util.UtilitiesApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;




@RestController
@RequestMapping("/reportes")
@Tag(name = "Reportes", description = "Crecion y descarga de reportes en pdf")
public class ReportesController {
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private UtilitiesApi util;
	
	@Autowired
	private UbicacionRepository ubicacionRepo;
	
	@Autowired
	private CsvExportService csvService;
	
	@GetMapping("/verificacion")
	@Operation(summary = "Crea un reporte de verificacion", description = "Retorna un listado de los activos de una orden dada "
			+ "segun el filtro indicado con paginacion. Los filtros podran ser 'faltantes', 'sobrantes', 'ok', 'todos'")
	public Page<Producto> getVerificacion(
			@RequestParam(required=false, defaultValue = "0") Integer pagina, 
			@RequestParam(required=false, defaultValue = "0") Integer items,
			@RequestParam(required=false) String orden,
			@RequestParam(required=false, defaultValue = "todos") String filtro,
			@RequestParam(required=false) Long nit){
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
				
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		
		System.out.println("Controller orden " + orden);
		System.out.println("Controller filtro " + filtro);
		Page<Producto> reporte = productoService.getVerificacion(orden, filtro, empresa, pagina, items);
		
		return reporte; 
	}
	
	@GetMapping("/verificacion/descarga/csv")
	@Operation(summary = "Crea un reporte de verificacion", description = "Retorna un listado de los activos de una orden dada "
			+ "segun el filtro indicado en formato csv.'")
	public void getCsvVerificacion(HttpServletResponse response,
			@RequestParam String orden,
			@RequestParam(defaultValue = "todos") String filtro,
			@RequestParam(required=false) Long nit) throws IOException{
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
				
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		
		response.setContentType("application/x-download");
		response.addHeader("Content-Disposition", "attachment; filename=reporteVerificacion_" + orden + "_" + currentDateTime + ".csv");
		
		List<Producto> productos =  productoService.getVerificacion(orden,filtro, empresa);
		csvService.writeReporteVerificarToCsv(response.getWriter(), productos, filtro, orden, usuario);
		
	}
	
	@GetMapping("/verificacion/descarga/pdf")
	@Operation(summary = "Crea un reporte de verificacion en formato PDF", description = "Retorna un PDF con el listado de los activos de una orden dada "
			+ "segun el filtro indicado. Los filtros podran ser 'faltantes', 'sobrantes', 'ok', 'todos'")
	public void exportToPDF(HttpServletResponse response,
			@RequestParam String orden,
			@RequestParam(defaultValue = "todos") String filtro,
			@RequestParam(required=false) Long nit) throws DocumentException, IOException {
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
				
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=reporteVerificacion_" + orden + "_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);
		
		List<Producto> productos =  productoService.getVerificacion(orden,filtro, empresa);
		
		ReporteVerificarPDF exporter = new ReporteVerificarPDF(productos, filtro, orden, usuario);
        exporter.export(response);
		
	}
	
	@GetMapping("/compararinventarios/descarga/pdf")
	@Operation(summary = "Crea un reporte de comparacion de 2 inventarios", description = "Retorna un PDF con el listado de comparacion de 2 inventario")
	public void compararInventariosPDF(HttpServletResponse response,
			@RequestParam Long inventario1,
			@RequestParam Long inventario2,
			@RequestParam(required=false) Long nit) throws DocumentException, IOException {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=comparativo_" + inventario1 + "_" + inventario2 +"  "+ currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);
		
		List<ComparativoInventarioDTO> comparativo = util.compararInventarios(inventario1, inventario2);
		
		ReporteComparativo exporter = new ReporteComparativo(comparativo, usuario);
        exporter.export(response);
	}
	
	@GetMapping("/compararinventarios/descarga/csv")
	@Operation(summary = "Crea un reporte de comparacion de 2 inventarios", description = "Retorna un reporte de comparacion de 2 inventarios en formato csv")
	public void compararInventariosCSV(HttpServletResponse response,
			@RequestParam Long inventario1,
			@RequestParam Long inventario2,
			@RequestParam(required=false) Long nit) throws IOException {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
	
		response.setContentType("application/x-download");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=diferencias" + inventario1 + "_INV-" + inventario2 +"  "+ currentDateTime + ".csv";
		response.setHeader(headerKey, headerValue);
		
		List<ComparativoInventarioDTO> comparativo = util.compararInventarios(inventario1, inventario2);
		System.out.println("Enviando lista para generar pdf");
		csvService.compararInventariosToCsv(response.getWriter(),comparativo);
	}
	
	@GetMapping("/compararinventarios")
	@Operation(summary = "Crea un reporte de comparacion de 2 inventarios", description = "Retorna un listado de comparacion de 2 inventarios paginado")
	public Page<ComparativoInventarioDTO> compararInventarios(
			@RequestParam Long inventario1,
			@RequestParam Long inventario2,
			@RequestParam(required=false, defaultValue = "0") Integer pagina, 
			@RequestParam(required=false, defaultValue = "0") Integer items,
			@RequestParam(required=false) Long nit) {
		
		Pageable pageable = null;
		if(items == 0) {
			pageable = PageRequest.of(pagina, 10);
		}else {
			pageable = PageRequest.of(pagina, items); 			
		}
		List<ComparativoInventarioDTO> comparativo = util.compararInventarios(inventario1, inventario2);
		
		int start = (int) pageable.getOffset();
		
		int end = Math.min((start + pageable.getPageSize()), comparativo.size());

		Page<ComparativoInventarioDTO> pages = new PageImpl<ComparativoInventarioDTO> (comparativo.subList(start, end), pageable, comparativo.size());

		return pages;
	}
	
	@GetMapping("/diferenciainventario/descarga/pdf")
	@Operation(summary = "Crea un reporte de diferencias de ubicacion vs inventario", description = "Retorna un pdf con las diferencias de ubicacion vs inventario")
	public void diferenciainventarioPDF(HttpServletResponse response,
			@RequestParam Long idubicacion,
			@RequestParam Long idinventario,
			@RequestParam(required=false) Long nit) throws DocumentException, IOException {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		Ubicacion ubicacion = ubicacionRepo.findById(idubicacion).orElseThrow(()->new ResourceNotFoundException("Ubicacion", "id", idubicacion));
	
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=diferencias" + ubicacion.getNombre() + "_INV-" + idinventario +"  "+ currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);
		
		List<ComparativoUbicacionDTO> comparativo = util.analisisDiferencias(idubicacion, idinventario);
		System.out.println("Enviando lista para generar pdf");
		ReporteDiferenciasPDF exporter = new ReporteDiferenciasPDF(comparativo, usuario, ubicacion); 
        exporter.export(response);
	}
	
	@GetMapping("/diferenciainventario")
	@Operation(summary = "Crea un reporte de diferencias de ubicacion vs inventario", description = "Retorna un reporte con las diferencias de ubicacion vs inventario, paginado")
	public Page<ComparativoUbicacionDTO> diferenciainventario(
			@RequestParam Long idubicacion,
			@RequestParam Long idinventario,
			@RequestParam(required=false, defaultValue = "0") Integer pagina, 
			@RequestParam(required=false, defaultValue = "0") Integer items,
			@RequestParam(required=false) Long nit) {
		
		List<ComparativoUbicacionDTO> comparativo = util.analisisDiferencias(idubicacion, idinventario);
		Pageable pageable = null;
		if(items == 0) {
			pageable = PageRequest.of(pagina, 10);
		}else {		
			pageable = PageRequest.of(pagina, items); 			
		}
		
		int start = (int) pageable.getOffset();
		System.out.println(start);
		int end = Math.min((start + pageable.getPageSize()), comparativo.size());
		System.out.println(comparativo.size());
		System.out.println(pagina + pageable.getPageSize());
		Page<ComparativoUbicacionDTO> pages = new PageImpl<ComparativoUbicacionDTO> (comparativo.subList(start, end), pageable, comparativo.size());

		return pages;
	}
	
	@GetMapping("/diferenciainventario/descarga/csv")
	@Operation(summary = "Crea un reporte de diferencias de ubicacion vs inventario", description = "Retorna un reporte con las diferencias de ubicacion vs inventario en formato csv")
	public void diferenciainventarioCSV(HttpServletResponse response,
			@RequestParam Long idubicacion,
			@RequestParam Long idinventario,
			@RequestParam(required=false) Long nit) throws IOException {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		Ubicacion ubicacion = ubicacionRepo.findById(idubicacion).orElseThrow(()->new ResourceNotFoundException("Ubicacion", "id", idubicacion));
	
		response.setContentType("application/x-download");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=diferencias" + ubicacion.getNombre() + "_INV-" + idinventario +"  "+ currentDateTime + ".csv";
		response.setHeader(headerKey, headerValue);
		
		List<ComparativoUbicacionDTO> comparativo = util.analisisDiferencias(idubicacion, idinventario);
		System.out.println("Enviando lista para generar pdf");
		csvService.writeDiferenciaInventarioToCsv(response.getWriter(),comparativo, ubicacion);
	}

}
