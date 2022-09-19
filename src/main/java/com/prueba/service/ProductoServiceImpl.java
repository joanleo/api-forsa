package com.prueba.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.prueba.dto.ProductoDTO;
import com.prueba.dto.ReconversionDTO;
import com.prueba.dto.SearchDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Eror;
import com.prueba.entity.Estado;
import com.prueba.entity.Fabricante;
import com.prueba.entity.Familia;
import com.prueba.entity.Producto;
import com.prueba.entity.TipoActivo;
import com.prueba.entity.Ubicacion;
import com.prueba.exception.ResourceAlreadyExistsException;
import com.prueba.exception.ResourceCannotBeAccessException;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.EmpresaRepository;
import com.prueba.repository.ErorRepository;
import com.prueba.repository.EstadoRepository;
import com.prueba.repository.FabricanteRepository;
import com.prueba.repository.FamiliaRepository;
import com.prueba.repository.ProductoRepository;
import com.prueba.repository.TipoActivoRepository;
import com.prueba.repository.UbicacionRepository;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.specifications.ProductSpecifications;

@Service
public class ProductoServiceImpl implements ProductoService {
	
	@Autowired
	private ProductoRepository productoRepo;
	
	@Autowired
	private FabricanteRepository fabricanteRepo;
	
	@Autowired
	private EmpresaRepository empresaRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ProductSpecifications productSpec;
	
	@Autowired
	private ErorRepository erorRepo;
	
	@Autowired
	private FamiliaRepository familiaRepo;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private TipoActivoRepository tipoActivoRepo;
	
	@Autowired
	private EstadoRepository estadoRepo;
	
	@Autowired
	private UbicacionRepository ubicacionRepo;
	
	
	@Override
	public ProductoDTO create(ProductoDTO productoDTO) {
		Producto producto = mapearDTO(productoDTO);
		Producto exist = productoRepo.findByCodigoPieza(productoDTO.getCodigoPieza());
		String verCodigo = productoDTO.getCodigoPieza().trim();
		if(verCodigo.length() > 0) {
			
		}else {
			throw new ResourceCannotBeAccessException("El QR debe ser un QR valildo, no solo espacios");
		}
		String verNombre = productoDTO.getDescripcion().trim();
		if(verNombre.length() > 0) {
			
		}else {
			throw new ResourceCannotBeAccessException("El nombre debe ser un nombre valildo, no solo espacios");
		}
		 
		Float verArea = productoDTO.getArea();
		if(verArea > 0) {
			
		}else {
			throw new ResourceCannotBeAccessException("El area debe ser un valor valido, mayor a cero");
		}

		if(exist == null) {
			Empresa empresa = empresaRepo.findByNit(productoDTO.getEmpresa().getNit());
			if(Objects.isNull(empresa)) {
				throw new ResourceNotFoundException("Empresa", "nit", productoDTO.getEmpresa().getNit());
			}
			Fabricante fabricante = fabricanteRepo.findByNitAndEmpresaAndEstaActivoTrue(productoDTO.getFabricante().getNit(), empresa);
			if(Objects.isNull(fabricante)) {
				throw new ResourceNotFoundException("Fabricante", "nit", productoDTO.getFabricante().getNit());
			}
			Familia familia = familiaRepo.findById(productoDTO.getFamilia().getId())
					.orElseThrow(()-> new ResourceNotFoundException("Familia", "id", productoDTO.getFamilia().getId()));
			Ubicacion ubicacion = ubicacionRepo.findById(productoDTO.getUbicacion().getId())
					.orElseThrow(()->new ResourceNotFoundException("Ubicacion", "id", productoDTO.getUbicacion().getId()));
			Estado estado = estadoRepo.findById(productoDTO.getEstado().getId())
					.orElseThrow(()->new ResourceNotFoundException("Estado", "id", productoDTO.getEstado().getId()));
			producto.setUbicacion(ubicacion);
			producto.setEstado(estado);
			producto.setEmpresa(empresa);
			producto.setFabricante(fabricante);
			producto.setFamilia(familia);
			productoRepo.save(producto);
		}else {
			throw new ResourceAlreadyExistsException("Activo", "codigo de pieza", productoDTO.getCodigoPieza());
		}
		
		return mapearEntidad(producto);
	}

	
	@Override
	public Page<Producto> list(Empresa empresa, Integer offset, Integer pageSize) {

		if(pageSize == 0) {
			Page<Producto> productos = productoRepo.findAllByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(0, 10));
			return productos;
		}
		Page<Producto> productos = productoRepo.findAllByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(offset, pageSize));
		return productos;
	}
	
	@Override
	public Page<Producto> searchProducts(Empresa empresa, SearchDTO searchDTO, int offset, int pageSize) {

		if(pageSize == 0) {
			Page<Producto> productos = productoRepo.findAll(productSpec.getProductos(searchDTO, empresa),PageRequest.of(0, 10));
			return productos;
		}
		Page<Producto> listProducts = productoRepo.findAll(productSpec.getProductos(searchDTO, empresa), PageRequest.of(offset, pageSize));		
		return listProducts;
	}

	
	@Override
	public Producto getProducto(String codigoPieza) {
				
		Producto exist = productoRepo.findByCodigoPieza(codigoPieza);
		
		
		if(exist == null) {
			throw new ResourceNotFoundException("Activo", "codigo de pieza ", codigoPieza);
		}
		
		
		return exist;
	}

	
	@Override
	public Producto update(String codigoPieza, ProductoDTO productoDTO) {
		Producto exist = productoRepo.findByCodigoPieza(productoDTO.getCodigoPieza());
		
		if(exist == null) {
			throw new ResourceNotFoundException("Activo", "codigo de pieza ", codigoPieza);
		}
		exist.setFechaActualizacion(new Date());
		exist.setArea(productoDTO.getArea());
		exist.setOrden(productoDTO.getOrden());
		exist.setDescripcion(productoDTO.getDescripcion());
		
		return exist;
	}

	
	@Override
	public String delete(String codigoPieza) {
		Producto producto = productoRepo.findByCodigoPieza(codigoPieza);
		boolean error = false;
		if(producto == null) {
			throw new ResourceNotFoundException("Producto", "No existe", codigoPieza);
		}
		
		if(!producto.getVerificado() && producto.getEstaActivo()) {
			producto.setEstaActivo(false);
		}else {
			error = true;
			throw new ResourceCannotBeAccessException("No es posible realizar la accion solicitada, el producto ha sido verificado verificado");
			
		}
		
		productoRepo.save(producto);
		if(error) return "No es posible realizar la accion solicitada, el producto no esta verificado";
		
		return "Item eliminado con exito";

	}

	
	public ProductoDTO mapearEntidad(Producto producto) {
		return modelMapper.map(producto, ProductoDTO.class);
	}

	
	public Producto mapearDTO(ProductoDTO productoDTO) {
		return modelMapper.map(productoDTO, Producto.class);
	}

	
	@Override
	public Producto receive(String codigoPieza, ProductoDTO productoDTO) {
		
		Producto producto = productoRepo.findByCodigoPieza(codigoPieza);		
		if(producto == null) {
			throw new ResourceNotFoundException("Producto", "No existe", codigoPieza);
		}
		if(!producto.getVerificado() && producto.getEstaActivo()) {
			Usuario usuario = null;
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if(authentication != null) {
			    usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
			}else {
				throw new ResourceCannotBeAccessException("Debe estar logueado para realizar esta accion");
			}

			//Capturar datos de la empresa del usuario		
			Empresa empresa = empresaRepo.findByNit(producto.getEmpresa().getNit());
			String[] nconfirmacion = new String[2];
			Integer numero = 0;
			if(empresa != null && empresa.getNconfimacion() != null) {
				nconfirmacion = empresa.getNconfimacion().split("-");			
				numero =  Integer.parseInt(nconfirmacion[1]) + 1;
			}else {
				nconfirmacion[0] = "D";
				nconfirmacion[1] = "0";
				numero++;
			}
			String nconf = nconfirmacion[0] + "-" + numero.toString();
			producto.setNconfirmacion(nconf);
			Ubicacion nuevaUbicacion = new Ubicacion(productoDTO.getUbicacion().getId());
			Estado nuevoEstado = new Estado(productoDTO.getEstado().getId());
			producto.setUbicacion(nuevaUbicacion);
			producto.setEstado(nuevoEstado);
			producto.setVerificado(true);//añadir fecha de verificacion
			producto.setFechaConfirmacion(new Date());
			producto.setEstaActivo(true);
			producto.setReviso(usuario);
			productoRepo.save(producto);
			
			empresa.setNconfimacion(nconf);
			empresaRepo.save(empresa);
			
			
			
		}else if(producto.getVerificado()){
			throw new ResourceCannotBeAccessException("No se puede realizar esta accion el activo ya fue verificado");
		}else if(!producto.getEstaActivo()) {
			throw new ResourceCannotBeAccessException("No se puede realizar esta accion el activo no esta activo");
		}
		
		return producto;
	}
	
	@Override
	public void load(List<ProductoDTO> listProductoDTO) {
		List<Producto> listProducts = listProductoDTO.stream().map(productoDTO -> mapearDTO(productoDTO)).collect(Collectors.toList());
		productoRepo.saveAll(listProducts);
		
	}

	@Override
	public String loadFile(MultipartFile file, WebRequest webRequest) {
		
		Float area = 0.0f;
		String orden = "", familia = "", tipo = "", nombre ="", medidas ="", codigoPieza = "";
		Integer pallet = null;
		Long nitfabricante = null;
		Long nitempresa = null;
		
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String currentUserName = "";
			if (authentication != null) {
			    currentUserName = authentication.getName();
			}else {
				throw new ResourceCannotBeAccessException("Debe estar logueado para realizar esta accion");
			}
			Usuario usuario = usuarioRepo.findByEmail(currentUserName);
			String extArchivo = file.getOriginalFilename().split("\\.")[1];
			if(!extArchivo.equals("txt")) {
				throw new ResourceCannotBeAccessException("El tipo de archivo no es compatible");
			}
			String ruta = webRequest.getDescription(false);

			int count = 0;
			if(!file.isEmpty()) {
				File newFile = new File("src/main/resources/targetFile.tmp");
				try (OutputStream os = new FileOutputStream(newFile)) {
				    os.write(file.getBytes());
				}
				LineIterator it = FileUtils.lineIterator(newFile);
				
				boolean error = false;
				List<String> errores = new ArrayList<String>();
				Set<Producto> listProductos = new HashSet<Producto>();
				
				
				
				try {
					Pattern special = Pattern.compile("[!@$%&*()_=|<>?{}\\\\[\\\\]~]");
					String lineError, errorDescripcion= "";
					Long start = System.currentTimeMillis();
					System.out.println("Inicio verificacion");
					
					while(it.hasNext()) {
						boolean erroresCiclo = false;
						count++;
						String line = it.nextLine();
						String[] producto = line.split("\\|");
						lineError = "El tamaño del arreglo esta errado linea " + count;
						
						
						
						if(producto.length > 10) {
							error = true;
							erroresCiclo = true;
							errores.add(lineError);
						}
						
						orden = producto[0];
						Matcher matcher = special.matcher(orden);
						boolean ordenconstainsSymbols = matcher.find();
						if(ordenconstainsSymbols) {
							error = true;
							erroresCiclo = true;
							errorDescripcion = "Orden Contiene caracteres especiales linea " + count;
							errores.add(errorDescripcion);
							System.out.println(errorDescripcion);
						}
						
						familia = producto[1];
						matcher = special.matcher(familia);
						boolean familiaconstainsSymbols = matcher.find();
						if(familiaconstainsSymbols) {
							error = true;
							erroresCiclo = true;
							errorDescripcion = "Familia Contiene caracteres especiales "+familia+" linea " + count;
							errores.add(errorDescripcion);
							System.out.println(errorDescripcion);
						}
						
						tipo = producto[2];
						matcher = special.matcher(tipo);
						boolean tipoconstainsSymbols = matcher.find();
						if(tipoconstainsSymbols) {
							error = true;
							erroresCiclo = true;
							errorDescripcion = "Tipo Contiene caracteres especiales linea " + count;
							errores.add(errorDescripcion);
							System.out.println(errorDescripcion);
						}
						
						nombre = producto[3];
						matcher = special.matcher(nombre);
						boolean nombreconstainsSymbols = matcher.find();
						if(nombreconstainsSymbols) {
							error = true;
							erroresCiclo = true;
							errorDescripcion = "Nombre Contiene caracteres especiales "+nombre+" linea " + count;
							errores.add(errorDescripcion);
							System.out.println(errorDescripcion);
						}
						
						medidas = producto[4];
						matcher = special.matcher(medidas);
						boolean medidasconstainsSymbols = matcher.find();
						if(medidasconstainsSymbols) {
							error = true;
							erroresCiclo = true;
							errorDescripcion = "Medidas Contiene caracteres especiales linea " + count;
							errores.add(errorDescripcion);
							System.out.println(errorDescripcion);
						}
						
						try {
							area = Float.parseFloat(producto[5]);
						} catch (Exception e) {
							error = true;
							erroresCiclo = true;
							errorDescripcion = "Error en el campo Area en la linea " + count + " " + e;
							errores.add(errorDescripcion);
							System.out.println(errorDescripcion);
						}
						
						codigoPieza = producto[6];
						matcher = special.matcher(codigoPieza);
						boolean CodigopiezaconstainsSymbols = matcher.find();
						if(CodigopiezaconstainsSymbols) {
							error = true;
							erroresCiclo = true;
							errorDescripcion = "Codigopieza Contiene caracteres especiales linea " + count;
							errores.add(errorDescripcion);
							System.out.println(errorDescripcion);
						}						
																	
						try {
							nitfabricante = Long.valueOf(producto[7]);
						} catch (Exception e) {
							error = true;
							erroresCiclo = true;
							errorDescripcion = "Error en el campo Fabricante en la linea " + count + " " + e;
							errores.add(errorDescripcion);
							System.out.println(errorDescripcion);
						}
						
						
						try {
							nitempresa = Long.valueOf(producto[8]);
						} catch (Exception e) {
							error = true;
							erroresCiclo = true;
							errorDescripcion = "Error en el campo Empresa en la linea " + count + " " + e;
							errores.add(errorDescripcion);
							System.out.println(errorDescripcion);
						}
						
						try {
							pallet = Integer.valueOf(producto[9]);							
						}catch (Exception e) {
							error = true;
							erroresCiclo = true;
							errorDescripcion = "Codigopieza Contiene caracteres especiales linea " + count;
							errores.add(errorDescripcion);
							System.out.println(errorDescripcion);							
						}
						
						if(!erroresCiclo) {
							Empresa empresaAdd = empresaRepo.findByNit(nitempresa);
							Familia familiaAdd = familiaRepo.findBySiglaAndEmpresa(familia, empresaAdd);
							Long localNitFabricante = nitfabricante;
							Fabricante fabricanteAdd = fabricanteRepo.findByNitAndEmpresa(localNitFabricante, empresaAdd)
									.orElseThrow(() -> new ResourceNotFoundException("Fabricante", "nit", localNitFabricante));
							TipoActivo tipoactivo = tipoActivoRepo.findByNombreAndEmpresa(tipo, empresaAdd);
							if(tipoactivo == null) {
								tipoactivo = new TipoActivo(tipo, empresaAdd, familiaAdd);
								tipoactivo = tipoActivoRepo.save(tipoactivo);
							}
							
							Producto product = new Producto(codigoPieza, nombre, area, orden, familiaAdd, tipoactivo,fabricanteAdd,empresaAdd,true,medidas,usuario,pallet);
							listProductos.add(product);
						}else {
							System.out.println("Error");
						}
					}
					Long end = System.currentTimeMillis();
					System.out.println("Duracion de le verificacion "+count+" lineas: "+(end-start)/1000+" segundos");
				} catch (Exception e) {
					System.out.println(e);
				}
				
				System.out.println("Inicio de guardado");
				Long startProducts = System.currentTimeMillis();
				List<Producto> productosGuardar = new ArrayList<>();
				productosGuardar.addAll(listProductos);
				productoRepo.saveAllAndFlush(productosGuardar);
				
				Long endProducts = System.currentTimeMillis();
				System.out.println("Duracion de carga de " + listProductos.size() + " productos: "+(endProducts-startProducts)/1000+" segundos");
				if(error) {
					Eror errorr = erorRepo.findTopByOrderByIdErrorDesc();
					
					List<Eror> erroresCarga = new ArrayList<Eror>();
					if(errorr == null) {
						System.out.println("Primer registro");
						for(String er: errores) {
							Eror nuevoError = new Eror(1, ruta, er, currentUserName);
						 	erroresCarga.add(nuevoError);
						}
						erorRepo.saveAll(erroresCarga);
					}else {
						int idError = errorr.getIdError();
						idError+=1;
						for(String er: errores) {
							Eror nuevoError = new Eror(idError, ruta, er, currentUserName);
						 	erroresCarga.add(nuevoError);
						}

						Long start = System.currentTimeMillis();
						erorRepo.saveAll(erroresCarga);
						Long end = System.currentTimeMillis();
						System.out.println("Duracion de carga de errores con "+count+" lineas: "+(end-start)/1000+" segundos");

					}
				}
				
			}else {
				System.out.println("Archivo vacio");
			}
			
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return "Archivo cargado con exito";
	}


	@Override
	public List<Producto> searchProducts(SearchDTO searchDTO, Empresa empresa) {
		List<Producto> listProducts = productoRepo.findAll(productSpec.getProductos(searchDTO, empresa));		
		return listProducts;
	}


	@Override
	public List<Producto> searchProducts(String letras, Empresa empresa) {
		List<Producto> listProducts = productoRepo.findAll(productSpec.getProductosActivos(letras, empresa));		
		return listProducts;
	}
	


	@Override
	public Page<Producto> getVerificacion(String orden, String filtro, Empresa empresa, int offset, int pageSize) {
		
		if(pageSize == 0) {
			Page<Producto> productos = productoRepo.findAll(productSpec.getVerificacion(orden, filtro, empresa),PageRequest.of(0, 10));
			return productos;
		}
		Page<Producto> productos = productoRepo.findAll(productSpec.getVerificacion(orden, filtro, empresa), PageRequest.of(offset, pageSize));		
		return productos;
	}


	@Override
	public List<Producto> getVerificacion(String orden, String filtro, Empresa empresa){
		List<Producto> productos = productoRepo.findAll(productSpec.getVerificacion(orden, filtro, empresa));
		productos.sort(Comparator.comparing(Producto :: getPallet));
		return productos;
	}


	@Override
	public List<Producto> list(Empresa empresa) {
		List<Producto> productos = productoRepo.findAllByEmpresaAndEstaActivoTrue(empresa);		
		return productos;
	}


	@Override
	public List<ProductoDTO> reconversionPieza(ReconversionDTO reconversion ) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		if(reconversion.getCodigoPadre() == null) {
			throw new ResourceCannotBeAccessException("Debe incluir el codigo padre");
		}
		Producto padre = productoRepo.findByCodigoPieza(reconversion.getCodigoPadre());
		if(padre == null) {
			throw new ResourceNotFoundException("Activo", "QR", reconversion.getCodigoPadre());
		}
		List<ProductoDTO> hijos = reconversion.getPiezasHijas();
		List<ProductoDTO> creados = new ArrayList<>();
		for(ProductoDTO item: hijos) {
			Producto hijo = productoRepo.findByCodigoPieza(item.getCodigoPieza());
			if(hijo != null) {
				throw new ResourceAlreadyExistsException("Activo", "codigo pieza", item.getCodigoPieza());
			}
			
			hijo = new Producto();
			hijo.setUsuarioCrea(usuario);
			hijo.setReviso(usuario);
			hijo.setFechaCreacion(new Date());
			hijo.setFechaConfirmacion(new Date());
			hijo.setPadre(padre);
			Empresa empresa = padre.getEmpresa();

			if(item.getUbicacion() == null) {
				hijo.setUbicacion(padre.getUbicacion());				
			}else {
				Ubicacion ubicacion = ubicacionRepo.findByIdAndEmpresa(item.getUbicacion().getId(), empresa)
						.orElseThrow(()-> new ResourceNotFoundException("Ubicacion", "id", item.getUbicacion().getId()));
				hijo.setUbicacion(ubicacion);
			}
			hijo.setCodigoPieza(item.getCodigoPieza());
			hijo.setDescripcion(item.getDescripcion());
			hijo.setMedidas(item.getMedidas());
			hijo.setArea(item.getArea());
			if(item.getOrden() == null) {
				hijo.setOrden("99999");
			}else {
				hijo.setOrden(item.getOrden());
			}
			hijo.setVerificado(true);
			
			Estado estado = null;
			if(item.getEstado() == null) {
				estado = estadoRepo.findByIdAndEmpresa(Long.valueOf(1), empresa)
						.orElseThrow(()-> new ResourceNotFoundException("Estado", "id", Long.valueOf(1)));
			}else {
				estado = estadoRepo.findByIdAndEmpresa(item.getEstado().getId(), empresa)
						.orElseThrow(()-> new ResourceNotFoundException("Estado", "id", Long.valueOf(1)));
			}
			Fabricante fabricante = null;
			if(item.getFabricante() != null) {
				fabricante = fabricanteRepo.findByNitAndEmpresaAndEstaActivoTrue(item.getFabricante().getNit(), empresa);				
			}else {
				fabricante = fabricanteRepo.findByNombre("Forsa SAS");
			}
			Familia familia = familiaRepo.findByIdAndEmpresa(item.getFamilia().getId(), empresa)
					.orElseThrow(() -> new ResourceNotFoundException("Familia", "id", item.getFamilia().getId()));
			TipoActivo tipo = tipoActivoRepo.findById(item.getTipo().getId())
					.orElseThrow(()-> new ResourceNotFoundException("Tipo", "id", item.getTipo().getId()));
			hijo.setEmpresa(empresa);
			hijo.setEstado(estado);
			hijo.setFabricante(fabricante);
			hijo.setFamilia(familia);
			hijo.setTipo(tipo);
			
			hijo = productoRepo.saveAndFlush(hijo);
			
			ProductoDTO creado = mapearEntidad(hijo);
			
			creados.add(creado);
			
		}
		
		padre.setEstaActivo(false);
		productoRepo.save(padre);
		return creados;
	}
	
}
