package com.prueba.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
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
import com.prueba.dto.SearchDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Eror;
import com.prueba.entity.Estado;
import com.prueba.entity.Fabricante;
import com.prueba.entity.Familia;
import com.prueba.entity.Producto;
import com.prueba.entity.Ubicacion;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.EmpresaRepository;
import com.prueba.repository.ErorRepository;
import com.prueba.repository.FabricanteRepository;
import com.prueba.repository.FamiliaRepository;
import com.prueba.repository.ProductoRepository;
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
	
	
	@Override
	public ProductoDTO create(ProductoDTO productoDTO) {
		Producto producto = mapearDTO(productoDTO);
		Producto exist = productoRepo.findByCodigoPieza(productoDTO.getCodigoPieza());
		if(exist == null) {
			Empresa empresa = empresaRepo.findByNit(productoDTO.getEmpresa().getNit());
			producto.setEmpresa(empresa);
			productoRepo.save(producto);
		}else {
			throw new IllegalAccessError("El producto con id "+ productoDTO.getCodigoPieza() + " que trata de crear ya existe en la empresa "+productoDTO.getEmpresa().getNit());
		}
		
		return mapearEntidad(producto);
	}

	
	@Override
	public Page<Producto> list(Empresa empresa, Integer offset, Integer pageSize) {

		if(pageSize == 0) {
			Page<Producto> productos = productoRepo.findAllByEmpresaAndEstaActivoTrue(empresa, PageRequest.of(0, 10));
			return productos;
		}
		Page<Producto> productos = productoRepo.findAllByEmpresaAndEstaActivoTrue(empresa,  PageRequest.of(offset, pageSize));
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
			throw new IllegalAccessError("El producto con codigo de pieza "+ codigoPieza + " no existe");
		}
		
		
		return exist;
	}

	
	@Override
	public Producto update(String codigoPieza, ProductoDTO productoDTO) {
		Producto exist = productoRepo.findByCodigoPieza(productoDTO.getCodigoPieza());
		
		if(exist == null) {
			throw new IllegalAccessError("El producto con codigo de pieza "+ codigoPieza + " no existe");
		}
		
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
			throw new IllegalAccessError("No es posible realizar la accion solicitada, el producto ha sido verificado verificado");
			
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
	public Producto receive(String codigoPieza, ProductoDTO productoDTO) throws IllegalAccessException {
		
		Producto producto = productoRepo.findByCodigoPieza(codigoPieza);		
		if(producto == null) {
			throw new ResourceNotFoundException("Producto", "No existe", codigoPieza);
		}
		if(!producto.getVerificado() && producto.getEstaActivo()) {
			Usuario usuario = null;
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if(authentication != null) {
			    usuario = usuarioRepo.findByUsernameOrEmail(authentication.getName(), authentication.getName()).get();
			}else {
				throw new IllegalAccessException("Debe estar logueado para realizar esta accion");
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
			producto.setVerificado(true);
			producto.setEstaActivo(true);
			producto.setReviso(usuario);
			productoRepo.save(producto);
			
			empresa.setNconfimacion(nconf);
			empresaRepo.save(empresa);
			
			
			
		}else if(producto.getVerificado()){
			throw new IllegalAccessError("No se puede realizar esta accion el activo ya fue verificado");
		}else if(!producto.getEstaActivo()) {
			throw new IllegalAccessError("No se puede realizar esta accion el activo no esta activo");
		}
		
		return producto;
	}
	

	@Override
	public Page<Producto> searchProducts(Empresa empresa, String letra, int offset, int pageSize) {
		
		if(pageSize == 0) {
			Page<Producto> productos = productoRepo.findAll(productSpec.getProductosActivos(letra, empresa),PageRequest.of(0, 10));
			return productos;
		}
		Page<Producto> listProducts = productoRepo.findAll(productSpec.getProductosActivos(letra, empresa), PageRequest.of(offset, pageSize));		
		return listProducts;
	}
	

	@Override
	public void load(List<ProductoDTO> listProductoDTO) {
		List<Producto> listProducts = listProductoDTO.stream().map(productoDTO -> mapearDTO(productoDTO)).collect(Collectors.toList());
		productoRepo.saveAll(listProducts);
		
	}

	@SuppressWarnings("removal")
	@Override
	public String loadFile(MultipartFile file, WebRequest webRequest) {
		
		Float area = 0.0f;
		//Integer familia = 0;
		String familia = "";
		Integer fabricante = 0;
		Integer empresa = 0;
		
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String currentUserName = "";
			if (authentication != null) {
			    currentUserName = authentication.getName();
			}else {
				throw new IllegalAccessException("Debe estar logueado para realizar esta accion");
			}
			String extArchivo = file.getOriginalFilename().split("\\.")[1];
			if(!extArchivo.equals("txt")) {
				throw new IllegalArgumentException("El tipo de archivo no es compatible");
			}
			String ruta = webRequest.getDescription(false);
			//Eror errorr = erorRepo.findTopByOrderByIdErrorDesc();
			//int idError = errorr.getIdError();
			int count = 0;
			if(!file.isEmpty()) {
				File newFile = new File("src/main/resources/targetFile.tmp");
				try (OutputStream os = new FileOutputStream(newFile)) {
				    os.write(file.getBytes());
				}
				LineIterator it = FileUtils.lineIterator(newFile);
				
				boolean error = false;
				List<String> errores = new ArrayList<String>();
				List<Producto> listProductos = new ArrayList<Producto>();
				
				
				
				try {
					Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\\\[\\\\]~]", Pattern.CASE_INSENSITIVE);
					String lineError, codigoPiezaError, nombreError, areaError, ordenError, familiaError, fabricanteError, empresaError = "";
					Long start = System.currentTimeMillis();
					System.out.println("Inicio verificacion");
					while(it.hasNext()) {
						boolean erroresCiclo = false;
						count++;
						String line = it.nextLine();
						String[] producto = line.split("\\|");
						lineError = "El tamaño del arreglo esta errado linea " + count;
						
						
						
						if(producto.length > 7) {
							error = true;
							erroresCiclo = true;
							//Eror nuevoError = new Eror(idError, ruta, lineError, currentUserName);
							errores.add(lineError);
						}
						//System.out.println("Codigopieza " + producto[0] + " Tipo " + ((Object)producto[0]).getClass().getSimpleName());
						String codigoPieza = producto[0];
						Matcher matcher = special.matcher(producto[0]);
						boolean CodigopiezaconstainsSymbols = matcher.find();
						if(CodigopiezaconstainsSymbols) {
							error = true;
							erroresCiclo = true;
							codigoPiezaError = "Codigopieza Contiene caracteres especiales linea " + count;
							//Eror nuevoError = new Eror(idError, ruta, codigoPiezaError, currentUserName);
							errores.add(codigoPiezaError);
						}						
						
						//System.out.println("Nombre " + producto[1] + " Tipo " + ((Object)producto[1]).getClass().getSimpleName());
						String nombre = producto[1];
						//System.out.println(nombre);
						matcher = special.matcher(producto[1]);
						boolean nombreconstainsSymbols = matcher.find();
						if(nombreconstainsSymbols) {
							error = true;
							erroresCiclo = true;
							nombreError = "Nombre Contiene caracteres especiales linea " + count;
							//Eror nuevoError = new Eror(idError, ruta, nombreError, currentUserName);
							errores.add(nombreError);
						}						
						
						try {
							//System.out.println("Area " + producto[2] + " Tipo " + ((Object)Float.parseFloat(producto[2])).getClass().getSimpleName());
							area = Float.parseFloat(producto[2]);
						} catch (Exception e) {
							error = true;
							erroresCiclo = true;
							areaError = "Error en el campo Area en la linea " + count + " " + e;
							//Eror nuevoError = new Eror(idError, ruta, areaError, currentUserName);
							errores.add(areaError);
						}
						
						//System.out.println("Orden " + producto[3] + " Tipo " + ((Object)producto[3]).getClass().getSimpleName());
						String orden = producto[3];
						matcher = special.matcher(producto[3]);
						boolean ordenconstainsSymbols = matcher.find();
						if(ordenconstainsSymbols) {
							error = true;
							erroresCiclo = true;
							ordenError = "Orden Contiene caracteres especiales linea " + count;
							//Eror nuevoError = new Eror(idError, ruta, ordenError, currentUserName);
							errores.add(ordenError);
						}						
						
						/*try {
							//System.out.println("Familia " + producto[4] + " Tipo " + ((Object)Integer.parseInt(producto[4])).getClass().getSimpleName());
							familia = Integer.parseInt(producto[4]);
						} catch (Exception e) {
							error = true;
							erroresCiclo = true;
							familiaError = "Error en el campo Familia en la linea " + count + " " + e;
							//Eror nuevoError = new Eror(idError, ruta, familiaError, currentUserName);
							errores.add(familiaError);
						}*/
						
						familia = producto[4];
						//System.out.println(familia);
						matcher = special.matcher(producto[4]);
						boolean familiaconstainsSymbols = matcher.find();
						if(familiaconstainsSymbols) {
							error = true;
							erroresCiclo = true;
							familiaError = "Familia Contiene caracteres especiales linea " + count;
							//Eror nuevoError = new Eror(idError, ruta, ordenError, currentUserName);
							errores.add(familiaError);
						}
											
						try {
							//System.out.println("Fabricante " + producto[5] + "  Tipo " + ((Object)Integer.parseInt(producto[5])).getClass().getSimpleName());
							fabricante = Integer.parseInt(producto[5]);
						} catch (Exception e) {
							error = true;
							erroresCiclo = true;
							fabricanteError = "Error en el campo Fabricante en la linea " + count + " " + e;
							//Eror nuevoError = new Eror(idError, ruta, fabricanteError, currentUserName);
							errores.add(fabricanteError);
						}
						
						
						try {
							//System.out.println("Empresa " + producto[6] + " Tipo " + ((Object)Integer.parseInt(producto[6])).getClass().getSimpleName());
							empresa = Integer.parseInt(producto[6]);
						} catch (Exception e) {
							error = true;
							erroresCiclo = true;
							empresaError = "Error en el campo Empresa en la linea " + count + " " + e;
							//Eror nuevoError = new Eror(idError, ruta, empresaError, currentUserName);
							errores.add(empresaError);
						}
						
						if(!erroresCiclo) {
							Empresa empresaAdd = new Empresa(new Long(empresa));
							
							Familia familiaAdd = familiaRepo.findBySiglaAndEmpresa(familia, empresaAdd);
							System.out.println(familiaAdd.getNombre());


							
							Long nuevoFabricante = new Long(fabricante);
							Fabricante fabricanteAdd = fabricanteRepo.findByNitAndEmpresa(nuevoFabricante, empresaAdd)
									.orElseThrow(() -> new ResourceNotFoundException("Fabricante", "nit", nuevoFabricante));
							System.out.println(fabricanteAdd.getNombre());
							
							
							Producto product = new Producto(codigoPieza,nombre,area,orden,familiaAdd,fabricanteAdd,empresaAdd);
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
				
				/*File filename = new File("src/main/resources/procts.txt");
			 	RandomAccessFile stream = new RandomAccessFile(filename, "rw");
			 	FileChannel channel = stream.getChannel(); 
				for(Producto producto: listProductos) {
									    
				    String linea = producto.toString()+"\r\n";
				    byte[] strBytes = linea.getBytes();
				    ByteBuffer buffer = ByteBuffer.allocate(strBytes.length);
				    buffer.put(strBytes);
				    buffer.flip();
				    channel.write(buffer);
				    
				}
				stream.close();
			    channel.close();*/
				
				productoRepo.saveAllAndFlush(listProductos);
				
				//productoRepo.bulkLoadData();
				Long endProducts = System.currentTimeMillis();
				System.out.println("Duracion de carga de " + listProductos.size() + " productos: "+(endProducts-startProducts)/1000+" segundos");
				if(error) {
					Eror errorr = erorRepo.findTopByOrderByIdErrorDesc();
					
					List<Eror> erroresCarga = new ArrayList<Eror>();
					if(errorr == null) {
						System.out.println("Primer registro");
						for(String er: errores) {
							Eror nuevoError = new Eror(1, ruta, er, currentUserName);
							//System.out.println(nuevoError);
						 	erroresCarga.add(nuevoError);
						}
						erorRepo.saveAll(erroresCarga);
					}else {
						int idError = errorr.getIdError();
						//System.out.println("Ya existen registros: "+ idError);
						idError+=1;
						//System.out.println("Siguiente registro: "+ idError);
						/*File filename = new File("src/main/resources/errorFile.dat");
					 	RandomAccessFile stream = new RandomAccessFile(filename, "rw");
					 	FileChannel channel = stream.getChannel();*/ 
						for(String er: errores) {
							
							Eror nuevoError = new Eror(idError, ruta, er, currentUserName);
							//System.out.println(nuevoError);
						 	erroresCarga.add(nuevoError);
						 	
						    
						    /*String linea = nuevoError.toString()+"\r\n";
						    byte[] strBytes = linea.getBytes();
						    ByteBuffer buffer = ByteBuffer.allocate(strBytes.length);
						    buffer.put(strBytes);
						    buffer.flip();
						    channel.write(buffer);*/
						    
						}
						/*stream.close();
					    channel.close();*/
						Long start = System.currentTimeMillis();
						erorRepo.saveAll(erroresCarga);
						Long end = System.currentTimeMillis();
						System.out.println("Duracion de carga de errores con "+count+" lineas: "+(end-start)/1000+" segundos");

					/*System.out.println("los errores son:");
					for(String er: errores) {
						System.out.println(er);						
					}
					//erorRepo.saveAll(errores);*/
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
		Page<Producto> listProducts = productoRepo.findAll(productSpec.getVerificacion(orden, filtro, empresa), PageRequest.of(offset, pageSize));		
		return listProducts;
	}


	@Override
	public List<Producto> getVerificacion(String orden, String filtro, Empresa empresa){
		List<Producto> productos = productoRepo.findAll(productSpec.getVerificacion(orden, filtro, empresa));
		return productos;
	}
	
}
