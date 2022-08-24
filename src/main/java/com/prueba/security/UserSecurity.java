package com.prueba.security;

import java.util.List;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.prueba.security.entity.Politica;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;

@Component("userSecurity")
public class UserSecurity {
	
	@Autowired
	private UsuarioRepository UsuarioRepo;
	
	public boolean hasPrivilege(Authentication authentication, ServletRequest  servletRequest) {
		
		var request = (HttpServletRequest) servletRequest;
		String rutaRequest = request.getServletPath();
		System.out.println("Metodo: "+rutaRequest);
		//System.out.println(authentication.getName());
		Usuario usuario = UsuarioRepo.findByEmail(authentication.getName());
		if(usuario == null) return false;
		Set<Politica> politicas = usuario.getRol().getPoliticas();
		//System.out.println(Arrays.asList(usuario.getRol()));
		
		//List<String> privileges = new ArrayList<>();
        //List<Rutina> collection = new ArrayList<>();
     	
        //collection.addAll(rol.getPoliticas());

        for (Politica item : politicas) {
        	List<String> urls = item.getDetalle().getRuta().getUrl();
        	if(urls.contains(rutaRequest)) {
        		System.out.println("Ruta: " + item.getDetalle().getRuta().getUrl());
        		System.out.println("Permiso: "+ item.getPermiso());
        		if(item.getPermiso()) return item.getPermiso();
        		break;
        	}
        }
        
       // boolean permiso = privileges.contains("READ_PRIVILEGE");
		
        //System.out.println(permiso);
        
		return true;
	}

}
