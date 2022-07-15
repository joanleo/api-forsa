package com.prueba.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prueba.entity.Rutina;
import com.prueba.security.entity.Rol;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;



@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UsuarioRepository usuarioRepositorio;
	
	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepositorio.findByNombreUsuarioOrEmail(usernameOrEmail, usernameOrEmail)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con ese username o email : " + usernameOrEmail));
	
		return new User(usuario.getEmail(), usuario.getContrasena(), mapearRoles(usuario.getRol()));
	}

	private Collection<? extends GrantedAuthority> mapearRoles(Rol collection){
		return getGrantedAuthorities(getPrivileges(collection));//collection.stream().map(rol -> new SimpleGrantedAuthority(rol.getNombre())).collect(Collectors.toList());
	}
	
	private List<String> getPrivileges(Rol rol) {
		 
        List<String> privileges = new ArrayList<>();
        List<Rutina> collection = new ArrayList<>();

            privileges.add(rol.getNombre());
            //collection.addAll(rol.getPoliticas());

        for (Rutina item : collection) {
            privileges.add(item.getNombre());
        }
        return privileges;
    }
	
	private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
