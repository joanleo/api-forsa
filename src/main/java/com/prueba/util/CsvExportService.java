package com.prueba.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
        			"Id", "Nombres Completos", "Nombre de usuario", "Contraseña", "Email", "Rol", "Contraseña");
    		for(Usuario usuario: usuarios) {
    			csvPrinter.printRecord(usuario.getId(), usuario.getNombre(), usuario.getNombreUsuario(), 
    					usuario.getEmail(), usuario.getRol().getNombre(), usuario.getContrasena());    			
    		}
	    }catch (IOException e) {
	        log.error("Error en la generacion del CSV  ", e);
	    }
		
	}
}
