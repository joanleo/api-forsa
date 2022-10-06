 package com.prueba.security;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.prueba.entity.DetalleRutina;
import com.prueba.entity.Permiso;
import com.prueba.repository.DetalleRutinaRepository;
import com.prueba.repository.PermisoRepository;
import com.prueba.security.entity.Politica;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.PoliticaRepository;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.specifications.PermisoSpecifications;


@Component("userSecurity")
public class UserSecurity {
	
	@Autowired
	private UsuarioRepository UsuarioRepo;
	
	@Autowired
	private PoliticaRepository politicaRepo;
		
	@Autowired
	private DetalleRutinaRepository detalleRutinaRepo;
	
	@Autowired
	private PermisoRepository permisoRepo;
	
	@Autowired
	private PermisoSpecifications permisoSpec;
		
	public boolean hasPrivilege(Authentication authentication, ServletRequest  servletRequest) {
		
		var request = (HttpServletRequest) servletRequest;
		String rutaRequest = request.getServletPath();
		String metodo = request.getMethod();
		System.out.println("Ruta: "+rutaRequest);
		Usuario usuario = UsuarioRepo.findByEmail(authentication.getName());
		
		//SecurityContextHolder.getContext().setAuthentication(authentication);
		
		System.out.println("Obteniendo lista de politicas");

		if(usuario == null) return false;
		List<Permiso> permiso = permisoRepo.findAll(permisoSpec.getPermiso(rutaRequest, metodo));
		System.out.println("Id ruta: "+permiso.get(0).getIdRuta());
		DetalleRutina detalleRutina = detalleRutinaRepo.findByRuta(permiso.get(0));
		System.out.println("Pk detalle rutina: "+detalleRutina.getPkDetalle());
		Politica politica = politicaRepo.findByDetalle(detalleRutina);
		System.out.println("Permiso: "+politica.getPermiso());
		        
		return politica.getPermiso();
	}

}
