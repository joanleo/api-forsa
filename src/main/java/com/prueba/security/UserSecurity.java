package com.prueba.security;

import java.util.List;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.prueba.security.dto.PoliticaDTO;
import com.prueba.security.dto.RutinaDTO;
import com.prueba.security.entity.Politica;
import com.prueba.security.entity.Rol;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.RolRepository;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.security.service.RolService;
import com.prueba.service.PoliticaService;

@Component("userSecurity")
public class UserSecurity {
	
	@Autowired
	private UsuarioRepository UsuarioRepo;
	
	@Autowired
	private RolRepository rolRepo;
	
	@Autowired
	private RolService rolService;
	
	@Autowired
	private PoliticaService politicaService;
	
	public boolean hasPrivilege(Authentication authentication, ServletRequest  servletRequest) {
		
		var request = (HttpServletRequest) servletRequest;
		String rutaRequest = request.getServletPath();
		System.out.println("Metodo: "+rutaRequest);
		//System.out.println(authentication.getName());
		Usuario usuario = UsuarioRepo.findByEmail(authentication.getName());
		
		//SecurityContextHolder.getContext().setAuthentication(authentication);
		
		System.out.println("Obteniendo lista de politicas");
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session= attr.getRequest().getSession(true);
		Set<Politica> politicas = (Set<Politica>) session.getAttribute("politicas");
		//System.out.println("politicas"+session.getAttribute("politicas").toString());
		
		for(Politica politica: politicas) {
			System.out.println(politica.getDetalle().getRutina().getNombre());
		}
		
		if(usuario == null) return false;
		//Rol rol = usuario.getRol();
		
		//List<Politica> politicas = rolService.listarPoliticas(rol.getIdRol());
		//String politicas = session.getAttribute("politicas").toString();

		/*System.out.println("Verificando cada una de las politicas");
		boolean salir=false;
		for(RutinaDTO politica: politicas) {
			for(PoliticaDTO detalle: politica.getPoliticas()) {
				List<String> urls = detalle.getUrl();
				if(urls.contains(rutaRequest)) {
					System.out.println("Url: "+ detalle.getUrl());
					System.out.println("Permitido: "+ detalle.getPermiso());
					salir=true;
					break;
				}
			}
			if(salir)break;
		}*/
		
        /*for (RutinaDTO politica : politicas) {
        		System.out.println("Obteniendo url de cada una de las politicas");
        		List<String> urls = politica.getDetalle().getRuta().getUrl();
        		if(urls.contains(rutaRequest)) {
            		if(politica.getPermiso()) {
            			System.out.println("Esta autorizado: "+politica.getDetalle().getRutina().getNombre());
            		}
            		//break;
            	}

        }*/
        		
        //System.out.println(permiso);
        
		return true;
	}

}
