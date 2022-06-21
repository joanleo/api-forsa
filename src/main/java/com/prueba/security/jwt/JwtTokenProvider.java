package com.prueba.security.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.prueba.exception.ApiException;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.lang.Strings;


@Component
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String jwtSecret;
	
	@Value("${jwt.expiration-milliseconds}")
	private int jwtExpirationInMs;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	public String generarToken(Authentication authentication, String nombre) {
		Usuario usuario = usuarioRepo.findByUsernameOrEmail(authentication.getName(), authentication.getName()).get();
		String email = usuario.getEmail();
		String empresa = String.valueOf(usuario.getEmpresa().getNit());
		Date fechaActual = new Date();
		Date fechaExpiracion = new Date(fechaActual.getTime() + jwtExpirationInMs);

		
		String token = Jwts.builder().setHeaderParam("typ", "JWT")
									 .claim("nombre", nombre)
									 .claim("empresa", empresa)
									 .claim("email", email).setIssuedAt(new Date())
									 .setIssuer("Metrolink SAS")
									 .setNotBefore(fechaActual)
									 .setExpiration(fechaExpiracion)
									 .signWith(SignatureAlgorithm.HS512, 
											  jwtSecret)
									 .compact();
		
		return token;
	}
	
	public String obtenerUsernameDelJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		return (String) claims.get("email");
	}
	
	public boolean validarToken(String token) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
			return true;
		}catch (SignatureException ex) {
			throw new ApiException(HttpStatus.BAD_REQUEST,"Firma JWT no valida");
		}
		catch (MalformedJwtException ex) {
			throw new ApiException(HttpStatus.BAD_REQUEST,"Token JWT no valida");
		}
		catch (ExpiredJwtException ex) {
			throw new ApiException(HttpStatus.BAD_REQUEST,"Token JWT caducado");
		}
		catch (UnsupportedJwtException ex) {
			throw new ApiException(HttpStatus.BAD_REQUEST,"Token JWT no compatible");
		}
		catch (IllegalArgumentException ex) {
			throw new ApiException(HttpStatus.BAD_REQUEST,"La cadena claims JWT esta vacia");
		}
	}
	
/*
	private SigningKeyResolver signingKeyResolver = new SigningKeyResolverAdapter() {
	    @Override
	    public Key resolveSigningKey(JwsHeader header, Claims claims) {
	        String kid = header.getKeyId();
	        if (!Strings.hasText(kid)) {
	            throw new JwtException("Missing required 'kid' header param in JWT with claims: " + claims);
	        }
	        Key key = publicKeys.get(kid);
	        if (key == null) {
	            throw new JwtException("No public key registered for kid: " + kid + ". JWT claims: " + claims);
	        }
	        return key;
	    }
	};
	 
	public SigningKeyResolver getSigningKeyResolver() {
	    return signingKeyResolver;
	}*/
}

