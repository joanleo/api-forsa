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
import com.prueba.dto.TipoEmpresaDTO;
import com.prueba.entity.Producto;


@Service
public class CsvExportService {

	private static final Logger log = LoggerFactory.getLogger(CsvExportService.class);

    public void writeEmployeesToCsv(Writer writer, List<Producto> productos) {

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
            log.error("Error While writing CSV ", e);
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
}
