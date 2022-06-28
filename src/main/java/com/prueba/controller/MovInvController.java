package com.prueba.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.json.ParseException;
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
import com.prueba.entity.Empresa;
import com.prueba.entity.MovInventario;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.service.MovInventarioService;
import com.prueba.util.ReporteInventarioPDF;
import com.prueba.util.UtilitiesApi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/inventarios")
@Api(tags = "Inventarios", description = "Operaciones referentes a los inventarios")
public class MovInvController {
	
	@Autowired
	private MovInventarioService movInvService;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private UtilitiesApi util;
	
	@PostMapping
	@ApiOperation(value = "Crea un inventario", notes = "Crea un nuevo inventario")
	public ResponseEntity<MovInventarioDTO> create(@RequestBody MovInventarioDTO movInventarioDto){
		System.out.println(movInventarioDto.getUbicacion().getId());

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		Usuario usuario = usuarioRepo.findByUsernameOrEmail(authentication.getName(), authentication.getName()).get();
		System.out.println(usuario);
		movInventarioDto.setRealizo(usuario);
		return new ResponseEntity<MovInventarioDTO>(movInvService.create(movInventarioDto), HttpStatus.CREATED);
	}
	
	@GetMapping
	@ApiOperation(value = "Lista los inventarios existentes", notes = "Retorna los inventarios que se encuentren en el rango de fechas dado (formato de fecha 'yyyy-MM-dd') o que en su nombre "
			+ "	contenga los valores  indicadas en la variable letras. Si no se incluye ningun valor retorna todos los inventarios existentes")
	public ApiResponse<Page<MovInventario>> list(@RequestParam(required=false, defaultValue = "0") Integer pagina, 
												 @RequestParam(required=false, defaultValue = "10") Integer items,
												 @RequestParam(required=false) String letras,
												 @RequestParam(required=false) Long nit,
												 @RequestParam(required=false) @DateTimeFormat(pattern = "yyyy-MM-dd")Date  desde,
												 @RequestParam(required=false) @DateTimeFormat(pattern = "yyyy-MM-dd")Date  hasta)throws ParseException{
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByUsernameOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		
		if(letras != null && desde != null) {
			System.out.println("letras "+letras);
			Page<MovInventario> inventarios = movInvService.searchInv(letras, empresa, pagina, items);
			return new ApiResponse<>(inventarios.getSize(), inventarios);
		}else {
			System.out.println("entra");
			Page<MovInventario> inventarios = movInvService.list(empresa, pagina, items);
			return new ApiResponse<>(inventarios.getSize(), inventarios);
		}
		
	}
	
	@GetMapping("/detalle/{id}")
	@ApiOperation(value = "Encuentra un inventario", notes = "Retorna un inventario con detalle segun el numero de inventario")
	public ResponseEntity<MovInventario> getInventario(@PathVariable Long id){
		return ResponseEntity.ok(movInvService.getInventario(id));
	}
	
	@GetMapping("/detalle/{id}/descarga")
	@ApiOperation(value = "Crea un inventario en formato PDF", notes = "Retorna un inventario con detalle segun el numero de inventario")
	public void exportToPDF(HttpServletResponse response,
							@PathVariable Long id) throws DocumentException, IOException {
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
