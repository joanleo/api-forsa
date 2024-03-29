package com.prueba.security.service;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prueba.entity.Empresa;
import com.prueba.entity.MovInventario;
import com.prueba.entity.Producto;
import com.prueba.entity.Traslado;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.EmpresaRepository;
import com.prueba.repository.MovInventarioRepository;
import com.prueba.repository.ProductoRepository;
import com.prueba.repository.TrasladoRepository;
import com.prueba.security.dto.RegistroDTO;
import com.prueba.security.entity.Rol;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.RolRepository;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.specifications.UsuarioSpecifications;

@Service
public class UsuarioServiceImp implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private EmpresaRepository empresaRepo;
	
	@Autowired
	private UsuarioSpecifications usuarioSpec;
	
	@Autowired
	private MovInventarioRepository movInventarioRepo;
	
	@Autowired
	private ProductoRepository productoRepo;
	
	@Autowired
	private TrasladoRepository trasladoRepo;
	
	@Autowired
	private RolRepository rolRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public Usuario update(Long id, RegistroDTO registroDTO) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioLogueado = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		if(registroDTO.getEmpresa() == null) {
			registroDTO.setEmpresa(usuarioLogueado.getEmpresa());
		}
		Empresa empresa = empresaRepo.findByNit(registroDTO.getEmpresa().getNit());
		if(Objects.isNull(empresa)) {
			throw new Exception("Empresa no existe");
		}
		Usuario usuario = usuarioRepo.findByIdAndEmpresa(id, registroDTO.getEmpresa());
		
		if(Objects.isNull(usuario)) {
			throw new ResourceNotFoundException("Usuario", "Nombre de usuario", registroDTO.getNombreUsuario());
		}
		
		if(registroDTO.getNombre() != null) {
			usuario.setNombre(registroDTO.getNombre());
		}
		if(registroDTO.getNombreUsuario() != null) {
			usuario.setNombreUsuario(registroDTO.getNombreUsuario().trim());
		}
		if(registroDTO.getEmail() != null) {
			usuario.setEmail(registroDTO.getEmail());
		}
		if(registroDTO.getContrasena() != null) {
			usuario.setContrasena(passwordEncoder.encode(registroDTO.getContrasena()));
		}
		if(registroDTO.getRol() != null) {
			Rol nuevoRol = rolRepo.findByIdRol(registroDTO.getRol().getIdRol());
			if(Objects.isNull(nuevoRol)) {
				throw new ResourceNotFoundException("Rol", "id", registroDTO.getRol().getIdRol());
			}
			usuario.setRol(nuevoRol);
		}
		usuario = usuarioRepo.save(usuario);		
		return usuario;
	}

	@Override
	public Page<Usuario> buscarUsuarios(Empresa empresa, Integer pagina, Integer items) {
		if(items == 0) {
			Page<Usuario> usuarios = usuarioRepo.findByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(0, 10));
			return usuarios;
		}
		Page<Usuario> usuarios = usuarioRepo.findByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(pagina, items));		
		return usuarios;
	}

	@Override
	public Page<Usuario> buscarUsuarios(@Valid RegistroDTO registroDTO, Empresa empresa, Integer pagina,
			Integer items) {
		if(items == 0) {
			Page<Usuario> usuarios = usuarioRepo.findAll(usuarioSpec.getUsuarios(registroDTO, empresa), PageRequest.of(0, 10));
			return usuarios;
		}
		Page<Usuario> usuarios = usuarioRepo.findAll(usuarioSpec.getUsuarios(registroDTO, empresa), PageRequest.of(pagina, items));		
		return usuarios;
	}

	@Override
	public List<Usuario> findByNombreAndEmpresaAndEstaActivo(String letras, Empresa empresa) {
		List<Usuario> usuarios = usuarioRepo.findByNombreContainsAndEmpresaAndEstaActivoTrue(letras, empresa);
		return usuarios;
	}

	@Override
	public void delete(Long id, Empresa empresa) throws Exception{
		Usuario usuario = usuarioRepo.findByIdAndEmpresa(id, empresa);
		if(Objects.isNull(usuario)) {
			throw new ResourceNotFoundException("Usuario", "id", id);
		}
		
		MovInventario existInven = movInventarioRepo.findByRealizo(usuario);
		Producto existProducto = productoRepo.findByReviso(usuario);
		Traslado existTraslado = trasladoRepo.findByUsuarioEnvioOrUsuarioRecibe(usuario, usuario);
		
		if(!Objects.isNull(existInven) || !Objects.isNull(existProducto) || !Objects.isNull(existTraslado)){
			throw new Exception("No es posible eliminar este usuario, se deberia inhabilitar");
		}
	
		usuario = usuarioRepo.save(usuario);
	}

	@Override
	public List<Usuario> list(Empresa empresa) {
		return usuarioRepo.findByEmpresaAndEstaActivoTrue(empresa);
	}

	@Override
	public void deshabilitar(Long id, Empresa empresa) {
		Usuario usuario = usuarioRepo.findByIdAndEmpresa(id, empresa);
		if(Objects.isNull(usuario)) {
			throw new ResourceNotFoundException("Usuario", "id", id);
		}
		
		Boolean estado = usuario.getEstaActivo();
		if(estado) {
			usuario.setEstaActivo(false);			
		}else {
			usuario.setEstaActivo(true);
		}

		usuarioRepo.save(usuario);
		
	}

	@Override
	public List<Usuario> listUsuarios(RegistroDTO registroDTO, Empresa empresa) {
		List<Usuario> usuarios = usuarioRepo.findAll(usuarioSpec.getUsuarios(registroDTO, empresa));
		return usuarios;
	}

}
