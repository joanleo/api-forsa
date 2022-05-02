package com.prueba.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.prueba.security.entity.PoliRol;
import com.prueba.security.entity.Rol;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;

@Component("userSecurity")
public class UserSecurity {
	
	@Autowired
	private UsuarioRepository UsuarioRepo;
	
	public boolean hasPrivilege(Authentication authentication, String ruta) {
		
		System.out.println(authentication.getName());
		Usuario usuario = UsuarioRepo.findByEmail(authentication.getName());
		if(usuario == null) return false;
		Collection<Rol> roles = usuario.getRoles();
		//Arrays.asList(usuario.getRoles());
		
		List<String> privileges = new ArrayList<>();
        List<PoliRol> collection = new ArrayList<>();
        for (Rol rol : roles) {
        	System.out.println("Rol "+ rol.getNombre());
            privileges.add(rol.getNombre());
            collection.addAll(rol.getPoliRoles());
        }
        for (PoliRol item : collection) {
        	System.out.println("PoliRol "+ item.getNombre());
            privileges.add(item.getNombre());
        }
        
        boolean permiso = privileges.contains("READ_PRIVILEGE");
		
        System.out.println(permiso);
        
		return true;
	}

}
