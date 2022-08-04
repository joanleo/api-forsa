package com.prueba.security.controller;

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

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



@Hidden
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticacion", description = "Operaciones de autenticacion y registro de usuarios")
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
	@Operation(summary = "Autenticacion de usuarios")
	public ResponseEntity<JWTAuthResonseDTO> authenticateUser(@RequestBody LoginDTO loginDTO){
		Authentication autentication = authenticationMnager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(autentication);
		
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(loginDTO.getUsernameOrEmail(), loginDTO.getUsernameOrEmail()).get();

		//Obtenemos el token
		String token = jwtTokenProvider.generarToken(autentication, usuario.getNombre());
		
		UserDetails user = (UserDetails)autentication.getPrincipal();
		
		return ResponseEntity.ok(new JWTAuthResonseDTO(token, user.getAuthorities()));
		
	}
	
	@PostMapping("/register")
	@Operation(summary = "Registro de usuarios")
	public ResponseEntity<?> registrarUsuario(@RequestBody RegistroDTO registroDTO){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication.getName() == "anonymousUser") {
			throw new IllegalAccessError("Debe estar logueado para realizar el registro");
		}
		Usuario usuarioActual = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		if(usuarioActual == null) {
			System.out.println("Debe estar logueado para realizar el registro");
		}
		
		if(registroDTO.getEmpresa() != null) {
			empresa = util.obtenerEmpresa(registroDTO.getEmpresa().getNit());
		}else {
			empresa = usuarioActual.getEmpresa();			
		}
		if(usuarioRepo.existsByNombreUsuario(registroDTO.getNombreUsuario())) {
			return new ResponseEntity<>("Ese nombre de usuario ya existe",HttpStatus.BAD_REQUEST);
		}
		
		if(usuarioRepo.existsByEmail(registroDTO.getEmail())) {
			return new ResponseEntity<>("Ese email de usuario ya existe",HttpStatus.BAD_REQUEST);
		}
		
		Usuario usuario = new Usuario();
		usuario.setEmpresa(empresa);
		usuario.setNombre(registroDTO.getNombre());
		usuario.setNombreUsuario(registroDTO.getNombreUsuario());
		usuario.setEmail(registroDTO.getEmail());
		usuario.setContrasena(passwordEncoder.encode(registroDTO.getContrasena()));
		
		Rol roles = rolRepo.findByNombre("ROLE_USER");
		usuario.setRol(roles);
		
		usuarioRepo.save(usuario);
		return new ResponseEntity<>("Usuario registrado exitosamente",HttpStatus.OK);
	}
}
