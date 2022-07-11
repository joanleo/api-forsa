package com.prueba.security.controller;

import java.util.Arrays;
//import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.entity.Empresa;
import com.prueba.security.dto.JWTAuthResonseDTO;
import com.prueba.security.dto.LoginDTO;
import com.prueba.security.dto.RegistroDTO;
import com.prueba.security.entity.Rol;
import com.prueba.security.entity.Usuario;
import com.prueba.security.jwt.JwtTokenProvider;
import com.prueba.security.repository.RolRepository;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.util.UtilitiesApi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/auth")
@Api(tags = "Autenticacion", description = "Operaciones de autenticacion y registro de usuarios")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationMnager;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private RolRepository rolRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UtilitiesApi util;
	
	@PostMapping("/login")
	@ApiOperation(value = "Autenticacion de usuarios")
	public ResponseEntity<JWTAuthResonseDTO> authenticateUser(@RequestBody LoginDTO loginDTO){
		
		Authentication autentication = authenticationMnager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(autentication);
		
		Usuario usuario = usuarioRepo.findByUsernameOrEmail(loginDTO.getUsernameOrEmail(), loginDTO.getUsernameOrEmail()).get();
		//System.out.println(usuario.getEmpresa());
		//System.out.println(autentication);
		
		//Obtenemos el token
		String token = jwtTokenProvider.generarToken(autentication, usuario.getNombre());
		
		UserDetails user = (UserDetails)autentication.getPrincipal();
		
		return ResponseEntity.ok(new JWTAuthResonseDTO(token, user.getAuthorities()));
		
	}
	
	@PostMapping("/register")
	@ApiOperation(value = "Registro de usuarios")
	public ResponseEntity<?> registrarUsuario(@RequestBody RegistroDTO registroDTO){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication.getName() == "anonymousUser") {
			throw new IllegalAccessError("Debe estar logueado para realizar el registro");
		}
		Usuario usuarioActual = usuarioRepo.findByUsernameOrEmail(authentication.getName(), authentication.getName()).get();
		System.out.println(usuarioActual);
		if(usuarioActual == null) {
			System.out.println("Debe estar logueado para realizar el registro");
		}
		
		if(registroDTO.getNitEmpresa() != null) {
			empresa = util.obtenerEmpresa(registroDTO.getNitEmpresa());
		}else {
			empresa = usuarioActual.getEmpresa();			
		}
		if(usuarioRepo.existsByUsername(registroDTO.getUsername())) {
			return new ResponseEntity<>("Ese nombre de usuario ya existe",HttpStatus.BAD_REQUEST);
		}
		
		if(usuarioRepo.existsByEmail(registroDTO.getEmail())) {
			return new ResponseEntity<>("Ese email de usuario ya existe",HttpStatus.BAD_REQUEST);
		}
		
		Usuario usuario = new Usuario();
		usuario.setEmpresa(empresa);
		usuario.setNombre(registroDTO.getNombre());
		usuario.setUsername(registroDTO.getUsername());
		usuario.setEmail(registroDTO.getEmail());
		usuario.setPassword(passwordEncoder.encode(registroDTO.getPassword()));
		
		Rol roles = rolRepo.findByNombre("ROLE_USER");
		usuario.setRoles(Arrays.asList(roles));
		
		usuarioRepo.save(usuario);
		return new ResponseEntity<>("Usuario registrado exitosamente",HttpStatus.OK);
	}
}
