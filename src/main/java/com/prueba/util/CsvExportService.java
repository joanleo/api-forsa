package com.prueba.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.prueba.dto.ComparativoInventarioDTO;
import com.prueba.dto.EmpresaDTO;
import com.prueba.dto.EstadoDTO;
import com.prueba.dto.FabricanteDTO;
import com.prueba.dto.FamiliaDTO;
import com.prueba.dto.TipoEmpresaDTO;
import com.prueba.dto.TipoMovDTO;
import com.prueba.dto.TipoUbicacionDTO;
import com.prueba.dto.UbicacionDTO;
import com.prueba.entity.Producto;
import com.prueba.entity.TipoActivo;
import com.prueba.entity.Ubicacion;
import com.prueba.security.dto.PoliticaDTO;
import com.prueba.security.dto.RutinaDTO;
import com.prueba.security.entity.Usuario;


@Service
public class CsvExportService {

	private static final Logger log = LoggerFactory.getLogger(CsvExportService.class);

    public void writeProductsToCsv(Writer writer, List<Producto> productos) {

        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
        	
        	csvPrinter.printRecord(
        			"Codigo de pieza", "Descripcion", "Area", "Empresa", "Estado", 
        			"Fabricante", "Familia", "No Orden", "Ubicacion");
        	
            for (Producto producto : productos) {
                csvPrinter.printRecord(producto.getCodigoPieza(), producto.getDescripcion(), producto.getArea(),
                		producto.getEmpresa().getNombre(), producto.getEstado() == null ? "" : producto.getEstado().getTipo(), producto.getFabricante().getNombre(), 
                		producto.getFamilia().getNombre(), producto.getOrden(), producto.getUbicacion() == null ? "":producto.getUbicacion().getNombre());
            }
        } catch (IOException e) {
            log.error("Error en la generacion del CSV ", e);
        }
    }

	public void writeTiposEmpresaToCsv(PrintWriter writer, List<TipoEmpresaDTO> tiposEmpresa) {
		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
        	
        	csvPrinter.printRecord(
        			"Id", "Tipo");
        	
            for (TipoEmpresaDTO tipoEmpresa : tiposEmpresa) {
                csvPrinter.printRecord(
                		tipoEmpresa.getId(), tipoEmpresa.getTipo());
            }
        } catch (IOException e) {
            log.error("Error While writing CSV ", e);
        }
		
	}

	public void writeEmpresasToCsv(PrintWriter writer, List<EmpresaDTO> empresasDTO) {
		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
        	
        	csvPrinter.printRecord(
        			"Nit", "Nombre");
        	
            for (EmpresaDTO empresaDTO : empresasDTO) {
                csvPrinter.printRecord(
                		empresaDTO.getNit(), empresaDTO.getNombre());
            }
        } catch (IOException e) {
            log.error("Error While writing CSV ", e);
        }
	}
    
    public void writeFabricantesToCsv(Writer writer, List<FabricanteDTO> fabricantes) {
    	try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
    		csvPrinter.printRecord(
        			"Nit", "Nombre");
    		for(FabricanteDTO fabricante: fabricantes) {
    			csvPrinter.printRecord(fabricante.getNit(), fabricante.getNombre());    			
    		}
	    }catch (IOException e) {
	        log.error("Error en la generacion del CSV  ", e);
	    }
    }

	public void writeEstadosToCsv(PrintWriter writer, List<EstadoDTO> estados) {
		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
			csvPrinter.printRecord(
        			"Id", "Estado");
    		for(EstadoDTO estado: estados) {
    			csvPrinter.printRecord(estado.getId(), estado.getTipo());    			
    		}
	    }catch (IOException e) {
	        log.error("Error en la generacion del CSV  ", e);
	    }
	}
	
	public void writeFamiliasToCsv(PrintWriter writer, List<FamiliaDTO> familias) {
		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
			csvPrinter.printRecord(
        			"Id", "Familia");
    		for(FamiliaDTO familia: familias) {
    			csvPrinter.printRecord(familia.getId(), familia.getNombre());    			
    		}
	    }catch (IOException e) {
	        log.error("Error en la generacion del CSV  ", e);
	    }
	}

	public void writeTiposMovToCsv(PrintWriter writer, List<TipoMovDTO> tiposMov) {
		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
			csvPrinter.printRecord(
        			"Id", "Nombre");
    		for(TipoMovDTO tipoMov: tiposMov) {
    			csvPrinter.printRecord(tipoMov.getId(), tipoMov.getNombre());    			
    		}
	    }catch (IOException e) {
	        log.error("Error en la generacion del CSV  ", e);
	    }
		
	}

	public void writeTiposUbiToCsv(PrintWriter writer, List<TipoUbicacionDTO> tiposUbic) {
		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
			csvPrinter.printRecord(
        			"Id", "Nombre");
    		for(TipoUbicacionDTO tipoMov: tiposUbic) {
    			csvPrinter.printRecord(tipoMov.getId(), tipoMov.getNombre());    			
    		}
	    }catch (IOException e) {
	        log.error("Error en la generacion del CSV  ", e);
	    }
		
	}

	public void writeUbicacionesToCsv(PrintWriter writer, List<UbicacionDTO> ubicaciones) {
		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
			csvPrinter.printRecord(
        			"Id", "Nombre", "Ciudad", "Direccion", "Tipo");
    		for(UbicacionDTO ubicacion: ubicaciones) {
    			csvPrinter.printRecord(ubicacion.getId(), ubicacion.getNombre(), ubicacion.getCiudad(),
    								   ubicacion.getDireccion(), ubicacion.getTipo().getNombre());    			
    		}
	    }catch (IOException e) {
	        log.error("Error en la generacion del CSV  ", e);
	    }
		
	}

	public void writeTiposActivoToCsv(PrintWriter writer, List<TipoActivo> tiposActivos) {
		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
    		for(TipoActivo tipoActivo: tiposActivos) {
    			csvPrinter.printRecord(tipoActivo.getId(), tipoActivo.getNombre());    			
    		}
	    }catch (IOException e) {
	        log.error("Error en la generacion del CSV  ", e);
	    }
		
	}

	/**
	 * @param writer
	 * @param usuarios
	 */
	public void writeUsuariosToCsv(PrintWriter writer, List<Usuario> usuarios) {
		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
			csvPrinter.printRecord(
        			"Id", "Nombres Completos", "Nombre de usuario", "Email", "Rol", "Contraseña"  );
    		for(Usuario usuario: usuarios) {
    			csvPrinter.printRecord(usuario.getId(), usuario.getNombre(), usuario.getNombreUsuario(), 
    					usuario.getEmail(), usuario.getRol().getNombre(), usuario.getContrasena());    			
    		}
	    }catch (IOException e) {
	        log.error("Error en la generacion del CSV  ", e);
	    }
		
	}

	/**
	 * @param writer
	 * @param politicas
	 */
	public void writePolitica(PrintWriter writer, Set<RutinaDTO> politicas) {
		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
    		for(RutinaDTO politica: politicas) {
    			csvPrinter.printRecord(politica.getNombre());
    			csvPrinter.printRecord("Id politica", "Nombre permiso", "Url", "Permitido");
    			for(PoliticaDTO detalle: politica.getPoliticas()) {
    				csvPrinter.printRecord(detalle.getIdPolitica(), detalle.getNombre(), detalle.getUrl(), detalle.getPermiso());
    			}
    		}
	    }catch (IOException e) {
	        log.error("Error en la generacion del CSV  ", e);
	    }
		
	}
	
	/**
	 * @param writer
	 * @param productos
	 * @param filtro
	 * @param orden
	 * @param usuario
	 */
	public void writeReporteVerificarToCsv(PrintWriter writer, List<Producto> productos, String filtro, String orden, Usuario usuario) {
		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
			int count = 1;
			csvPrinter.printRecord("No", "QR", "Descripcion", "Familia", "Tipo", "Medidas", "Área m2",
					"Ubicacion", "Estado", "Pallet", "Revisado");
    		for(Producto producto: productos) {
    				csvPrinter.printRecord(count, producto.getCodigoPieza(), producto.getDescripcion(), producto.getFamilia().getSigla(),
    						producto.getTipo().getNombre(), producto.getMedidas(), producto.getArea(), producto.getUbicacion().getNombre(),
    						producto.getEstado().getTipo(), producto.getPallet()  == null ? "":producto.getPallet(), producto.getVerificado() == true ? "Si":"No");
    				count++;
    		}
	    }catch (IOException e) {
	        log.error("Error en la generacion del CSV  ", e);
	    }
	}

	/**
	 * @param writer 
	 * @param comparativo
	 * @param ubicacion 
	 */
	public void writeDiferenciaInventarioToCsv(PrintWriter writer, List<ComparativoInventarioDTO> comparativo, Ubicacion ubicacion) {
		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
			int count = 1;
			csvPrinter.printRecord("No", "QR", "Descripcion", "Familia", "Tipo", "Medidas", "Área m2",
					"Estado", ubicacion.getNombre(), "INV-"+comparativo.get(0).getNumInv2());
    		for(ComparativoInventarioDTO producto: comparativo) {
    				csvPrinter.printRecord(count, producto.getCodigo(), producto.getDescripcion(), producto.getFamilia(),
    						producto.getTipo(), producto.getMedidas(), producto.getArea(), producto.getEstado(), 
    						producto.getInv1()? "Si":"No", producto.getInv2()? "Si":"No");
    				count++;
    		}
	    }catch (IOException e) {
	        log.error("Error en la generacion del CSV  ", e);
	    }
		
	}

	/**
	 * @param writer
	 * @param comparativo
	 */
	public void compararInventariosToCsv(PrintWriter writer, List<ComparativoInventarioDTO> comparativo) {
		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
			int count = 1;
			csvPrinter.printRecord("No", "QR", "Descripcion", "Familia", "Tipo", "Medidas", "Área m2",
					"Estado", "INV-"+comparativo.get(0).getNumInv1(), "INV-"+comparativo.get(0).getNumInv2());
    		for(ComparativoInventarioDTO producto: comparativo) {
    				csvPrinter.printRecord(count, producto.getCodigo(), producto.getDescripcion(), producto.getFamilia(),
    						producto.getTipo(), producto.getMedidas(), producto.getArea(), producto.getEstado(), 
    						producto.getInv1()? "Si":"No", producto.getInv2()? "Si":"No");
    				count++;
    		}
	    }catch (IOException e) {
	        log.error("Error en la generacion del CSV  ", e);
	    }
		
	}
}
