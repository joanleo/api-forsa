package com.prueba.security.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class JWTAuthResonseDTO {

	private String tokenDeAcceso;
	private String tipoDeToken = "Bearer";
	private Collection<? extends GrantedAuthority> authorities;

	public JWTAuthResonseDTO(String tokenDeAcceso) {
		super();
		this.tokenDeAcceso = tokenDeAcceso;
	}

	public JWTAuthResonseDTO(String tokenDeAcceso, String tipoDeToken) {
		super();
		this.tokenDeAcceso = tokenDeAcceso;
		this.tipoDeToken = tipoDeToken;
	}

	public JWTAuthResonseDTO(String tokenDeAcceso, Collection<? extends GrantedAuthority> autorities) {
		super();
		this.tokenDeAcceso = tokenDeAcceso;
		this.authorities = autorities;
	}

	public String getTokenDeAcceso() {
		return tokenDeAcceso;
	}

	public void setTokenDeAcceso(String tokenDeAcceso) {
		this.tokenDeAcceso = tokenDeAcceso;
	}

	public String getTipoDeToken() {
		return tipoDeToken;
	}

	public void setTipoDeToken(String tipoDeToken) {
		this.tipoDeToken = tipoDeToken;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

}

