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
import com.prueba.dto.TipoMovDTO;
import com.prueba.dto.TipoUbicacionDTO;
import com.prueba.dto.UbicacionDTO;
import com.prueba.entity.Producto;


@Service
public class CsvExportService {

	private static final Logger log = LoggerFactory.getLogger(CsvExportService.class);

    public void writeProductsToCsv(Writer writer, List<Producto> productos) {

        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            for (Producto producto : productos) {
                csvPrinter.printRecord(producto.getIdProducto().getCodigoPieza(), producto.getDescripcion(), producto.getArea(),
                		producto.getEmpresa().getNombre(), producto.getEstado() == null ? "" : producto.getEstado().getTipo(), producto.getFabricante().getNombre(), 
                		producto.getFamilia().getNombre(), producto.getOrden(), producto.getUbicacion() == null ? "":producto.getUbicacion().getNombre());
            }
        } catch (IOException e) {
            log.error("Error en la generacion del CSV ", e);
        }
    }
    
    public void writeEmpresasToCsv(Writer writer, List<EmpresaDTO> empresas) {
    	try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
    		for(EmpresaDTO empresa: empresas) {
    			csvPrinter.printRecord(empresa.getNit(), empresa.getNombre());    			
    		}
	    }catch (IOException e) {
	        log.error("Error en la generacion del CSV V ", e);
	    }
    }
    
    public void writeFabricantesToCsv(Writer writer, List<FabricanteDTO> fabricantes) {
    	try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
    		for(FabricanteDTO fabricante: fabricantes) {
    			csvPrinter.printRecord(fabricante.getNit(), fabricante.getNombre());    			
    		}
	    }catch (IOException e) {
	        log.error("Error en la generacion del CSV  ", e);
	    }
    }

	public void writeEstadosToCsv(PrintWriter writer, List<EstadoDTO> estados) {
		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
    		for(EstadoDTO estado: estados) {
    			csvPrinter.printRecord(estado.getId(), estado.getTipo());    			
    		}
	    }catch (IOException e) {
	        log.error("Error en la generacion del CSV  ", e);
	    }
	}
	
	public void writeFamiliasToCsv(PrintWriter writer, List<FamiliaDTO> familias) {
		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
    		for(FamiliaDTO familia: familias) {
    			csvPrinter.printRecord(familia.getId(), familia.getNombre());    			
    		}
	    }catch (IOException e) {
	        log.error("Error en la generacion del CSV  ", e);
	    }
	}

	public void writeTiposMovToCsv(PrintWriter writer, List<TipoMovDTO> tiposMov) {
		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
    		for(TipoMovDTO tipoMov: tiposMov) {
    			csvPrinter.printRecord(tipoMov.getId(), tipoMov.getNombre());    			
    		}
	    }catch (IOException e) {
	        log.error("Error en la generacion del CSV  ", e);
	    }
		
	}

	public void writeTiposUbiToCsv(PrintWriter writer, List<TipoUbicacionDTO> tiposUbic) {
		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
    		for(TipoUbicacionDTO tipoMov: tiposUbic) {
    			csvPrinter.printRecord(tipoMov.getId(), tipoMov.getNombre());    			
    		}
	    }catch (IOException e) {
	        log.error("Error en la generacion del CSV  ", e);
	    }
		
	}

	public void writeUbicacionesToCsv(PrintWriter writer, List<UbicacionDTO> ubicaciones) {
		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
    		for(UbicacionDTO ubicacion: ubicaciones) {
    			csvPrinter.printRecord(ubicacion.getId(), ubicacion.getNombre(), ubicacion.getCiudad(),
    								   ubicacion.getDireccion(), ubicacion.getTipo());    			
    		}
	    }catch (IOException e) {
	        log.error("Error en la generacion del CSV  ", e);
	    }
		
	}
}
