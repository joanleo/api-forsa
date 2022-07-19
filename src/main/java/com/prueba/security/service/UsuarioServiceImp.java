package com.prueba.security.service;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import com.prueba.security.entity.Usuario;
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
	
	@Override
	public Usuario update(Long id, RegistroDTO registroDTO) throws Exception {
		Empresa empresa = empresaRepo.findByNit(registroDTO.getEmpresa().getNit());
		if(Objects.isNull(empresa)) {
			throw new Exception("Empresa no existe");
		}
		Usuario usuario = usuarioRepo.findByNombreAndEmpresa(registroDTO.getNombre(), registroDTO.getEmpresa());
		usuarioRepo.save(usuario);		
		return usuario;
	}

	@Override
	public Page<Usuario> searchFabricantes(Empresa empresa, Integer pagina, Integer items) {
		if(items == 0) {
			Page<Usuario> usuarios = usuarioRepo.findByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(0, 10));
			return usuarios;
		}
		Page<Usuario> usuarios = usuarioRepo.findByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(pagina, items));		
		return usuarios;
	}

	@Override
	public Page<Usuario> searchFabricantes(@Valid RegistroDTO registroDTO, Empresa empresa, Integer pagina,
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
		usuario.setEstaActivo(false);
		usuarioRepo.save(usuario);
		
	}

	@Override
	public List<Usuario> listUsuarios(RegistroDTO registroDTO, Empresa empresa) {
		List<Usuario> usuarios = usuarioRepo.findAll(usuarioSpec.getUsuarios(registroDTO, empresa));
		return usuarios;
	}

}
