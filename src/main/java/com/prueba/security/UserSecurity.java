package com.prueba.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.prueba.entity.Rutina;
import com.prueba.security.entity.Rol;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;

@Component("userSecurity")
public class UserSecurity {
	
	@Autowired
	private UsuarioRepository UsuarioRepo;
	
	public boolean hasPrivilege(Authentication authentication, ServletRequest  servletRequest) {
		
		var request = (HttpServletRequest) servletRequest;
		System.out.println("Metodo: "+request.getMethod());
		System.out.println("Metodo: "+request.getServletPath());
		System.out.println(authentication.getName());
		Usuario usuario = UsuarioRepo.findByEmail(authentication.getName());
		if(usuario == null) return false;
		Rol roles = usuario.getRoles();
		System.out.println(Arrays.asList(usuario.getRoles()));
		
		List<String> privileges = new ArrayList<>();
        List<Rutina> collection = new ArrayList<>();

        	System.out.println("Rol "+ roles.getNombre());
            privileges.add(roles.getNombre());
            //collection.addAll(rol.getPoliticas());

        for (Rutina item : collection) {
        	System.out.println("PoliRol "+ item.getNombre());
            privileges.add(item.getNombre());
        }
        
        boolean permiso = privileges.contains("READ_PRIVILEGE");
		
        System.out.println(permiso);
        
		return true;
	}

}
