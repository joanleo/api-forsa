package com.prueba.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.prueba.dto.EmpresaDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Trazabilidad;
import com.prueba.exception.ResourceAlreadyExistsException;
import com.prueba.exception.ResourceCannotBeDeleted;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.EmpresaRepository;
import com.prueba.repository.TrazabilidadRepository;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.specifications.EmpresaSpecifications;

@Service
public class EmpresaServiceImpl implements EmpresaService {

	@Autowired
	private EmpresaRepository empresaRepo;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private EmpresaSpecifications empresaSpec;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private TrazabilidadRepository trazaRepo;

	@Override
	public EmpresaDTO create(EmpresaDTO empresaDTO) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		Empresa empresa = mapearDto(empresaDTO);
		Empresa exist = empresaRepo.findByNit(empresa.getNit());
		if (exist == null) {
			empresaRepo.save(empresa);
		} else {
			throw new ResourceAlreadyExistsException("Empresa", "nombre", empresa.getNombre());
		}
		
		ServletUriComponentsBuilder ruta = ServletUriComponentsBuilder.fromCurrentRequest();
		String operacion = "El usuario "+usuario.getNombre()+" creo la empresa "+empresa.getNombre()+" con nit "+empresa.getNit();
		Trazabilidad trazaCrearEmpresa = new Trazabilidad(operacion,usuario,ruta.toUriString());
		trazaRepo.save(trazaCrearEmpresa);
		return mapearEntidad(empresa);
	}
	
	@Override
	public Page<Empresa> searchEmpresas(EmpresaDTO empresaDTO, Integer pagina, Integer items) {
		if(items == 0) {
			Page<Empresa> empresas = empresaRepo.findAll(empresaSpec.getEmpresa(empresaDTO),PageRequest.of(0, 10));
			return empresas;
		}
		Page<Empresa> empresas = empresaRepo.findAll(empresaSpec.getEmpresa(empresaDTO), PageRequest.of(pagina, items));		
		return empresas;
	}
	
	@Override
	public Page<Empresa> searchEmpresas(Integer pagina, Integer items) {
		if(items == 0) {
			Page<Empresa> empresas = empresaRepo.findByEstaActivoTrue(PageRequest.of(0, 10));
			return empresas;
		}
		Page<Empresa> empresas = empresaRepo.findByEstaActivoTrue(PageRequest.of(pagina, items));		
		return empresas;
	}

	@Override
	public List<EmpresaDTO> list(String letras) {
		List<Empresa> empresas = empresaRepo.findByNombreContainsAndEstaActivoTrue(letras);
		
		return empresas.stream().map(empresa -> mapearEntidad(empresa)).collect(Collectors.toList());
	}
	
	@Override
	public List<EmpresaDTO> list() {
		
		List<Empresa> empresas = empresaRepo.findByEstaActivoTrue();		
		return empresas.stream().map(empresa -> mapearEntidad(empresa)).collect(Collectors.toList());
	}

	@Override
	public EmpresaDTO getEmpresa(Long id) {
		Empresa empresa = empresaRepo.findByNit(id);
		if(Objects.isNull(empresa)) {
			throw new ResourceNotFoundException("Empresa", "id", id);			
		}
		
		return mapearEntidad(empresa);
	}

	@Override
	public EmpresaDTO update(Long id, EmpresaDTO empresaDTO) {
		Empresa empresa = empresaRepo.findByNit(id);
		if(Objects.isNull(empresa)) {
			throw new ResourceNotFoundException("Empresa", "id", id);			
		}
		
		empresa.setNit(empresaDTO.getNit());
		empresa.setNombre(empresaDTO.getNombre());
		
		empresaRepo.save(empresa);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		ServletUriComponentsBuilder ruta = ServletUriComponentsBuilder.fromCurrentRequest();
		String operacion = "El usuario "+usuario.getNombre()+" actualizo la empresa "+empresa.getNombre();
		Trazabilidad trazaCrearEmpresa = new Trazabilidad(operacion,usuario,ruta.toUriString());
		trazaRepo.save(trazaCrearEmpresa);
		
		return mapearEntidad(empresa);
	}

	@Override
	public void delete(Long id) {
		Empresa empresa = empresaRepo.findByNit(id);
		if(Objects.isNull(empresa)) {
			throw new ResourceNotFoundException("Empresa", "id", id);			
		}
		
		if(empresa.getUsuarios().size() > 0 || empresa.getProductos().size() > 0) {
			throw new ResourceCannotBeDeleted("Empresa");
		}
		
		empresaRepo.delete(empresa);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		ServletUriComponentsBuilder ruta = ServletUriComponentsBuilder.fromCurrentRequest();
		String operacion = "El usuario "+usuario.getNombre()+" elimino la empresa "+empresa.getNombre();
		Trazabilidad trazaCrearEmpresa = new Trazabilidad(operacion,usuario,ruta.toUriString());
		trazaRepo.save(trazaCrearEmpresa);

	}
	
	@Override
	public void unable(Long id) {
		Empresa empresa = empresaRepo.findByNit(id);
		if(Objects.isNull(empresa)) {
			throw new ResourceNotFoundException("Empresa", "id", id);			
		}
		
		empresa.setEstaActivo(false);
		empresaRepo.save(empresa);
		
	}

	public EmpresaDTO mapearEntidad(Empresa Empresa) {
		return modelMapper.map(Empresa, EmpresaDTO.class);
	}

	public Empresa mapearDto(EmpresaDTO EmpresaDTO) {
		return modelMapper.map(EmpresaDTO, Empresa.class);
	}

	
	@Override
	public List<EmpresaDTO> findByNameAndEstaActivo(String name) {
		List<Empresa> listEmpresas = empresaRepo.findByNombreContainsAndEstaActivoTrue(name);
		
		return listEmpresas.stream().map(empresa -> mapearEntidad(empresa)).collect(Collectors.toList());
	}

	@Override
	public List<EmpresaDTO> list(EmpresaDTO empresaDTO) {
		List<Empresa> empresas = empresaRepo.findAll(empresaSpec.getEmpresa(empresaDTO));
		return empresas.stream().map(empresa -> mapearEntidad(empresa)).collect(Collectors.toList());
	}

}
