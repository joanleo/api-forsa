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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.prueba.dto.ProductoDTO;
import com.prueba.dto.SearchDTO;
import com.prueba.entity.Eror;
import com.prueba.entity.Producto;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.ErorRepository;
import com.prueba.repository.ProductoRepository;
import com.prueba.specifications.ProductSpecifications;

@Service
public class ProductoServiceImpl implements ProductoService {
	
	@Autowired
	private ProductoRepository productoRepo;
	
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
		
		if(exist == null) {
			productoRepo.save(producto);
		}else {
			throw new IllegalAccessError("El producto con id "+ producto.getCodigoPieza() + " que trata de crear ya existe");
		}
		
		return mapearEntidad(producto);
	}

	@Override
	public Page<Producto> list(Integer offset, Integer pageSize) {
		if(pageSize == 0) {
			System.out.println("Sin parametros"); 
			Page<Producto> productos = productoRepo.findAll(PageRequest.of(0, 10));
			return productos;
		}
		Page<Producto> productos = productoRepo.findAll(PageRequest.of(offset, pageSize));
		return productos;
	}

	@Override
	public Page<Producto> searchProducts(SearchDTO searchDTO, int offset, int pageSize) {
		if(pageSize == 0) {
			System.out.println("Sin parametros"); 
			Page<Producto> productos = productoRepo.findAll(productSpec.getProductos(searchDTO),PageRequest.of(0, 10));
			return productos;
		}
		Page<Producto> listProducts = productoRepo.findAll(productSpec.getProductos(searchDTO), PageRequest.of(offset, pageSize));		
		return listProducts;//listProducts.stream().map(producto -> mapearEntidad(producto)).collect(Collectors.toList());
	}

	@Override
	public ProductoDTO getProducto(Long id) {
		Producto producto = productoRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Producto", "Codigo de pieza", id));
		
		return mapearEntidad(producto);
	}

	@Override
	public ProductoDTO update(Long id, ProductoDTO productoDTO) {
		Producto producto = productoRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Producto", "Codigo de pieza", id));
		
		producto.setArea(productoDTO.getArea());
		producto.setOrden(productoDTO.getOrden());
		producto.setDescripcion(productoDTO.getDescripcion());
		
		return mapearEntidad(producto);
	}

	@Override
	public void delete(String id) {
		Producto producto = productoRepo.findByCodigoPieza(id);
		
		if(producto == null) {
			throw new ResourceNotFoundException("Producto", "No existe", id);
		}
		
		if(producto.getVerificado() == false) {
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
	public ProductoDTO receive(Long id/*, String ubicacion, String estado*/) {
		Producto producto = productoRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Producto", "Codigo de pieza", id));
		
		/*producto.setUbicacion(ubicacion);
		producto.setEstado(estado);*/
		producto.setVerificado(true);	
		productoRepo.save(producto);
		
		return mapearEntidad(producto);
	}
	

	@Override
	public Page<Producto> searchProducts(String letra) {
		
		 Page<Producto> listProducts = productoRepo.findByDescripcionContains(letra, PageRequest.of(0, 10));				
		return listProducts;
	}
	

	@Override
	public void load(List<ProductoDTO> listProductoDTO) {
		List<Producto> listProducts = listProductoDTO.stream().map(productoDTO -> mapearDTO(productoDTO)).collect(Collectors.toList());
		productoRepo.saveAll(listProducts);
		
	}

	@Override
	public String loadFile(MultipartFile file) {
		try {
			int count = 1;
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
					
					while(it.hasNext()) {
						
						String line = it.nextLine();
						String[] producto = line.split("\\|");
						System.out.println("********************************************");
						lineError = "El tamaño del arreglo esta errado linea " + count;
						System.out.println("Tamaño del arreglo  " + (producto.length));
						if(producto.length > 7) {
							error = true;
							System.out.println(lineError);
							errores.add(lineError);
						}
						System.out.println("********************************************");
						System.out.println("Codigopieza " + producto[0] + " Tipo " + ((Object)producto[0]).getClass().getSimpleName());
						Matcher matcher = special.matcher(producto[0]);
						boolean CodigopiezaconstainsSymbols = matcher.find();
						if(CodigopiezaconstainsSymbols) {
							error = true;
							codigoPiezaError = "Codigopieza Contiene caracteres especiales linea " + count;
							System.out.println(codigoPiezaError);
							errores.add(codigoPiezaError);
						}						
						
						System.out.println("Nombre " + producto[1] + " Tipo " + ((Object)producto[1]).getClass().getSimpleName());
						matcher = special.matcher(producto[1]);
						boolean nombreconstainsSymbols = matcher.find();
						if(nombreconstainsSymbols) {
							error = true;
							nombreError = "Nombre Contiene caracteres especiales linea " + count;
							System.out.println(nombreError);
							errores.add(nombreError);
						}						
						
						try {
							System.out.println("Area " + producto[2] + " Tipo " + ((Object)Float.parseFloat(producto[2])).getClass().getSimpleName());							
						} catch (Exception e) {
							error = true;
							areaError = "Error en el campo Area en la linea " + count + " " + e;
							System.out.println(e);
							errores.add(areaError);
						}
						
						
						System.out.println("Orden " + producto[3] + " Tipo " + ((Object)producto[3]).getClass().getSimpleName());
						matcher = special.matcher(producto[1]);
						boolean ordenconstainsSymbols = matcher.find();
						if(ordenconstainsSymbols) {
							error = true;
							ordenError = "Orden Contiene caracteres especiales linea " + count;
							System.out.println(ordenError);
							errores.add(ordenError);
						}						
						
						try {
							System.out.println("Familia " + producto[4] + " Tipo " + ((Object)Integer.parseInt(producto[4])).getClass().getSimpleName());							
						} catch (Exception e) {
							error = true;
							familiaError = "Error en el campo Familia en la linea " + count + " " + e;
							System.out.println(familiaError);
							errores.add(familiaError);
						}
						
						
						try {
							System.out.println("Fabricante " + producto[5] + "  Tipo " + ((Object)Integer.parseInt(producto[5])).getClass().getSimpleName());							
						} catch (Exception e) {
							error = true;
							fabricanteError = "Error en el campo Fabricante en la linea " + count + " " + e;
							System.out.println(e);
							errores.add(fabricanteError);
						}
						
						
						try {
							System.out.println("Empresa " + producto[6] + " Tipo " + ((Object)Integer.parseInt(producto[6])).getClass().getSimpleName());							
						} catch (Exception e) {
							error = true;
							empresaError = "Error en el campo Empresa en la linea " + count + " " + e;
							System.out.println(e);
							errores.add(empresaError);
						}
						
						count++;
					}
				} catch (Exception e) {
					System.out.println(e);
				}
				
				if(error) {
					Eror errorr = erorRepo.findTopByOrderByIdErrorDesc();
					if(errorr == null) {
						System.out.println("Primer registro");
					}
					System.out.println("los errores son:");
					System.out.println(errores);
				}
			}
			
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return "Archivo cargado con exito";
	}
	
}
