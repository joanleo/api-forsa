package com.prueba.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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
import com.prueba.entity.Producto;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.EmpresaRepository;
import com.prueba.repository.ErorRepository;
import com.prueba.repository.ProductoRepository;
import com.prueba.specifications.ProductSpecifications;

@Service
public class ProductoServiceImpl implements ProductoService {
	
	@Autowired
	private ProductoRepository productoRepo;
	
	@Autowired
	private EmpresaRepository empresaRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ProductSpecifications productSpec;
	
	@Autowired
	private ErorRepository erorRepo;

	
	@Override
	public ProductoDTO create(ProductoDTO productoDTO) {
		Producto producto = mapearDTO(productoDTO);
		Producto exist = productoRepo.findByCodigoPieza(producto.getCodigoPieza());
		Empresa empresa = empresaRepo.findByNitOrderByFecha(productoDTO.getEmpresa().getNit());
		if(empresa != null) {
			System.out.println(empresa.getNconfimacion());			
		}else {
			System.out.println("error en la consulta de empresa");
		}
		if(exist == null) {
			System.out.println(producto.toString());
			productoRepo.save(producto);
		}else {
			throw new IllegalAccessError("El producto con id "+ producto.getCodigoPieza() + " que trata de crear ya existe");
		}
		
		return mapearEntidad(producto);
	}

	
	@Override
	public Page<Producto> list(Integer offset, Integer pageSize) {
		if(pageSize == 0) {
			Page<Producto> productos = productoRepo.findAllByEstaActivoTrue(PageRequest.of(0, 10));
			return productos;
		}
		Page<Producto> productos = productoRepo.findAllByEstaActivoTrue(PageRequest.of(offset, pageSize));
		return productos;
	}

	
	@Override
	public Page<Producto> searchProducts(SearchDTO searchDTO, int offset, int pageSize) {
		if(pageSize == 0) {
			Page<Producto> productos = productoRepo.findAll(productSpec.getProductos(searchDTO),PageRequest.of(0, 10));
			return productos;
		}
		Page<Producto> listProducts = productoRepo.findAll(productSpec.getProductos(searchDTO), PageRequest.of(offset, pageSize));		
		return listProducts;//listProducts.stream().map(producto -> mapearEntidad(producto)).collect(Collectors.toList());
	}

	
	@Override
	public ProductoDTO getProducto(String codigoPieza) {
				
		Producto exist = productoRepo.findByCodigoPieza(codigoPieza);
		
		if(exist == null) {
			throw new IllegalAccessError("El producto con codigo de pieza "+ codigoPieza + " no existe");
		}
		
		return mapearEntidad(exist);
	}

	
	@Override
	public ProductoDTO update(String codigoPieza, ProductoDTO productoDTO) {
		Producto exist = productoRepo.findByCodigoPieza(codigoPieza);
		
		if(exist == null) {
			throw new IllegalAccessError("El producto con codigo de pieza "+ codigoPieza + " no existe");
		}
		
		exist.setArea(productoDTO.getArea());
		exist.setOrden(productoDTO.getOrden());
		exist.setDescripcion(productoDTO.getDescripcion());
		
		return mapearEntidad(exist);
	}

	
	@Override
	public void delete(String id) {
		Producto producto = productoRepo.findByCodigoPieza(id);
		
		if(producto == null) {
			throw new ResourceNotFoundException("Producto", "No existe", id);
		}
		
		if(!producto.getVerificado() && producto.getEstaActivo()) {
			producto.setEstaActivo(false);
		}else {
			throw new IllegalAccessError("No es posible realizar la accion solicitada");
		}
		
		productoRepo.save(producto);

	}

	
	public ProductoDTO mapearEntidad(Producto producto) {
		return modelMapper.map(producto, ProductoDTO.class);
	}

	
	public Producto mapearDTO(ProductoDTO productoDTO) {
		return modelMapper.map(productoDTO, Producto.class);
	}

	
	@Override
	public ProductoDTO receive(String id/*, String ubicacion, String estado*/) {
		
		Producto producto = productoRepo.findByCodigoPieza(id);		
		if(producto == null) {
			throw new ResourceNotFoundException("Producto", "No existe", id);
		}
		
		if(!producto.getVerificado() && producto.getEstaActivo()) {
			//Capturar datos de la empresa del usuario		
			Empresa empresa = empresaRepo.findByNitOrderByFecha(producto.getEmpresa().getNit());
			
			String[] nconfirmacion = new String[2];
			Integer numero = 0;
			if(empresa != null && empresa.getNconfimacion() != null) {
				System.out.println(empresa.getNombre());
				System.out.println(empresa.getNconfimacion());
				nconfirmacion = empresa.getNconfimacion().split("-");			
				numero =  Integer.parseInt(nconfirmacion[1]) + 1;
			}else {
				nconfirmacion[0] = "D";
				nconfirmacion[1] = "0";
				numero++;
				System.out.println(numero);
			}
			System.out.println("afuera"+numero);
			String nconf = nconfirmacion[0] + "-" + numero.toString();
			producto.setNconfirmacion(nconf);
			System.out.println(nconf);
			
			/*producto.setUbicacion(ubicacion);
			producto.setEstado(estado);*/
			producto.setVerificado(true);
			producto.setEstaActivo(true);
			productoRepo.save(producto);
			
			empresa.setNconfimacion(nconf);
			empresaRepo.save(empresa);
			
			
			
		}else if(producto.getVerificado()){
			throw new IllegalAccessError("No se puede realizar esta accion el activo ya fue verificado");
		}else if(!producto.getEstaActivo()) {
			throw new IllegalAccessError("No se puede realizar esta accion el activo no esta activo");
		}
		
		return mapearEntidad(producto);
	}
	

	@Override
	public Page<Producto> searchProducts(String letra, int offset, int pageSize) {
		
		if(pageSize == 0) {
			Page<Producto> productos = productoRepo.findAll(/*productSpec.getProductosActivos(letra),*/PageRequest.of(0, 10));
			return productos;
		}
		Page<Producto> listProducts = productoRepo.findAll(/*productSpec.getProductosActivos(letra),*/ PageRequest.of(offset, pageSize));		
		return listProducts;
	}
	

	@Override
	public void load(List<ProductoDTO> listProductoDTO) {
		List<Producto> listProducts = listProductoDTO.stream().map(productoDTO -> mapearDTO(productoDTO)).collect(Collectors.toList());
		productoRepo.saveAll(listProducts);
		
	}

	@Override
	public String loadFile(MultipartFile file, WebRequest webRequest) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String currentUserName = "";
			if ((authentication != null)) {
			    currentUserName = authentication.getName();
			}
			String ruta = webRequest.getDescription(false);
			Eror errorr = erorRepo.findTopByOrderByIdErrorDesc();
			int idError = errorr.getIdError();
			int count = 0;
			if(!file.isEmpty()) {
				File newFile = new File("src/main/resources/targetFile.tmp");
				try (OutputStream os = new FileOutputStream(newFile)) {
				    os.write(file.getBytes());
				}
				LineIterator it = FileUtils.lineIterator(newFile);
				
				boolean error = false;
				List<String> errores = new ArrayList<String>(); 
				
				try {
					Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\\\[\\\\]~-]", Pattern.CASE_INSENSITIVE);
					String lineError, codigoPiezaError, nombreError, areaError, ordenError, familiaError, fabricanteError, empresaError = "";
					Long start = System.currentTimeMillis();
					while(it.hasNext()) {
						count++;
						String line = it.nextLine();
						String[] producto = line.split("\\|");
						lineError = "El tamaño del arreglo esta errado linea " + count;
						if(producto.length > 7) {
							error = true;
							Eror nuevoError = new Eror(idError, ruta, lineError, currentUserName);
							erorRepo.save(nuevoError);
							errores.add(lineError);
						}
						//System.out.println("Codigopieza " + producto[0] + " Tipo " + ((Object)producto[0]).getClass().getSimpleName());
						String codigoPieza = producto[0];
						Matcher matcher = special.matcher(producto[0]);
						boolean CodigopiezaconstainsSymbols = matcher.find();
						if(CodigopiezaconstainsSymbols) {
							error = true;
							codigoPiezaError = "Codigopieza Contiene caracteres especiales linea " + count;
							Eror nuevoError = new Eror(idError, ruta, codigoPiezaError, currentUserName);
							erorRepo.save(nuevoError);
							errores.add(codigoPiezaError);
						}						
						
						//System.out.println("Nombre " + producto[1] + " Tipo " + ((Object)producto[1]).getClass().getSimpleName());
						String nombre = producto[1];
						matcher = special.matcher(producto[1]);
						boolean nombreconstainsSymbols = matcher.find();
						if(nombreconstainsSymbols) {
							error = true;
							nombreError = "Nombre Contiene caracteres especiales linea " + count;
							Eror nuevoError = new Eror(idError, ruta, nombreError, currentUserName);
							erorRepo.save(nuevoError);
							errores.add(nombreError);
						}						
						
						try {
							//System.out.println("Area " + producto[2] + " Tipo " + ((Object)Float.parseFloat(producto[2])).getClass().getSimpleName());
							Float area = Float.parseFloat(producto[2]);
						} catch (Exception e) {
							error = true;
							areaError = "Error en el campo Area en la linea " + count + " " + e;
							Eror nuevoError = new Eror(idError, ruta, areaError, currentUserName);
							erorRepo.save(nuevoError);
							errores.add(areaError);
						}
						
						//System.out.println("Orden " + producto[3] + " Tipo " + ((Object)producto[3]).getClass().getSimpleName());
						String orden = producto[3];
						matcher = special.matcher(producto[1]);
						boolean ordenconstainsSymbols = matcher.find();
						if(ordenconstainsSymbols) {
							error = true;
							ordenError = "Orden Contiene caracteres especiales linea " + count;
							Eror nuevoError = new Eror(idError, ruta, ordenError, currentUserName);
							erorRepo.save(nuevoError);
							errores.add(ordenError);
						}						
						
						try {
							//System.out.println("Familia " + producto[4] + " Tipo " + ((Object)Integer.parseInt(producto[4])).getClass().getSimpleName());
							Integer familia = Integer.parseInt(producto[4]);
						} catch (Exception e) {
							error = true;
							familiaError = "Error en el campo Familia en la linea " + count + " " + e;
							Eror nuevoError = new Eror(idError, ruta, familiaError, currentUserName);
							erorRepo.save(nuevoError);
							errores.add(familiaError);
						}
											
						try {
							//System.out.println("Fabricante " + producto[5] + "  Tipo " + ((Object)Integer.parseInt(producto[5])).getClass().getSimpleName());
							Integer fabricante = Integer.parseInt(producto[5]);
						} catch (Exception e) {
							error = true;
							fabricanteError = "Error en el campo Fabricante en la linea " + count + " " + e;
							Eror nuevoError = new Eror(idError, ruta, fabricanteError, currentUserName);
							erorRepo.save(nuevoError);
							System.out.println(e);
							errores.add(fabricanteError);
						}
						
						
						try {
							//System.out.println("Empresa " + producto[6] + " Tipo " + ((Object)Integer.parseInt(producto[6])).getClass().getSimpleName());
							Integer empresa = Integer.parseInt(producto[6]);
						} catch (Exception e) {
							error = true;
							empresaError = "Error en el campo Empresa en la linea " + count + " " + e;
							Eror nuevoError = new Eror(idError, ruta, empresaError, currentUserName);
							erorRepo.save(nuevoError);
							System.out.println(e);
							errores.add(empresaError);
						}
						
						
					}
					Long end = System.currentTimeMillis();
					System.out.println("Duracion de carga de errores linea por linea "+count+" lineas: "+(end-start)/1000+" segundos");
				} catch (Exception e) {
					System.out.println(e);
				}
				
				if(error) {
					//Eror errorr = erorRepo.findTopByOrderByIdErrorDesc();
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
						//int idError = errorr.getIdError();
						System.out.println("Ya existen registros: "+ idError);
						idError+=1;
						System.out.println("Siguiente registro: "+ idError);
						File filename = new File("src/main/resources/errorFile.dat");
					 	RandomAccessFile stream = new RandomAccessFile(filename, "rw");
					 	FileChannel channel = stream.getChannel(); 
						for(String er: errores) {
							
							Eror nuevoError = new Eror(idError, ruta, er, currentUserName);
							//System.out.println(nuevoError);
						 	erroresCarga.add(nuevoError);
						 	
						    
						    String linea = nuevoError.toString()+"|";
						    byte[] strBytes = linea.getBytes();
						    ByteBuffer buffer = ByteBuffer.allocate(strBytes.length);
						    buffer.put(strBytes);
						    buffer.flip();
						    channel.write(buffer);
						    
						}
						stream.close();
					    channel.close();
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
	public List<Producto> searchProducts(SearchDTO searchDTO) {
		
		List<Producto> listProducts = productoRepo.findAll(productSpec.getProductos(searchDTO));		
		return listProducts;
	}


	@Override
	public List<Producto> searchProducts(String letra) {
		List<Producto> listProducts = productoRepo.findAll(productSpec.getProductosActivos(letra));		
		return listProducts;
	}
	
}
