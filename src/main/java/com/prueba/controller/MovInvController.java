package com.prueba.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.json.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.DocumentException;
import com.prueba.dto.ApiResponse;
import com.prueba.dto.MovInventarioDTO;
import com.prueba.dto.UsuarioDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.MovInventario;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.service.MovInventarioService;
import com.prueba.util.ReporteInventarioPDF;
import com.prueba.util.UtilitiesApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;




@RestController
@RequestMapping("/inventarios")
@Tag(name = "Inventarios", description = "Operaciones referentes a los inventarios")
public class MovInvController {
	
	@Autowired
	private MovInventarioService movInvService;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private UtilitiesApi util;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping
	@Operation(summary = "Crea un inventario", description = "Crea un nuevo inventario")
	public ResponseEntity<MovInventario> create(@RequestBody MovInventarioDTO movInventarioDto,
													@RequestParam(required=false) Long nit){

		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuariop = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		UsuarioDTO usuario = modelMapper.map(usuariop, UsuarioDTO.class);
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuariop.getEmpresa();			
		}
		if(movInventarioDto.getRealizo() == null) {
			movInventarioDto.setRealizo(usuario);			
		}
		if(movInventarioDto.getEmpresa() == null) {
			movInventarioDto.setEmpresa(empresa);
		}
		movInventarioDto.setEmpresa(empresa);
		return new ResponseEntity<MovInventario>(movInvService.create(movInventarioDto), HttpStatus.CREATED);
	}
	
	@GetMapping
	@Operation(summary = "Pagina los inventarios existentes", description = "Retorna los inventarios que se encuentren en el rango de fechas dado (formato de fecha 'yyyy-MM-dd') o que en su nombre "
			+ "	contenga los valores  indicadas en la variable letras. Si no se incluye ningun valor retorna todos los inventarios existentes")
	public ApiResponse<Page<MovInventario>> list(@RequestParam(required=false, defaultValue = "0") Integer pagina, 
												 @RequestParam(required=false, defaultValue = "10") Integer items,
												 @RequestParam(required=false) String letras,
												 @RequestParam(required=false) Long nit,
												 @RequestParam(required=false) @DateTimeFormat(pattern = "yyyy-MM-dd")Date  desde,
												 @RequestParam(required=false) @DateTimeFormat(pattern = "yyyy-MM-dd")Date  hasta)throws ParseException{
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		
		if(letras != null && desde != null) {
			Page<MovInventario> inventarios = movInvService.searchInv(letras, empresa, pagina, items);
			return new ApiResponse<>(inventarios.getSize(), inventarios);
		}else {
			Page<MovInventario> inventarios = movInvService.list(empresa, pagina, items);
			return new ApiResponse<>(inventarios.getSize(), inventarios);
		}
		
	}
	
	@GetMapping("/detalle/{id}")
	@Operation(summary = "Encuentra un inventario", description = "Retorna un inventario con detalle segun el numero de inventario")
	public ResponseEntity<MovInventario> getInventario(@PathVariable Integer id){
		return ResponseEntity.ok(movInvService.getInventario(id));
	}
	
	@GetMapping("/detalle/{id}/descarga")
	@Operation(summary = "Crea un inventario en formato PDF", description = "Retorna un inventario con detalle segun el numero de inventario")
	public void exportToPDF(HttpServletResponse response,
							@PathVariable Integer id) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=reporteInventario_" + id + "_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);
		
		MovInventario inventario = movInvService.getInventario(id);
		
		ReporteInventarioPDF exportar = new ReporteInventarioPDF(inventario);
		exportar.export(response);
		
	}

}
