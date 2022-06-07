package com.prueba.util;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.prueba.entity.Producto;


@Service
public class CsvExportService {

	private static final Logger log = LoggerFactory.getLogger(CsvExportService.class);

    public void writeEmployeesToCsv(Writer writer, List<Producto> productos) {

        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            for (Producto producto : productos) {
            	/*try {
            		producto.getEstado().getTipo();
            		producto.getUbicacion().getNombre();
				} catch (Exception e) {
					String estado = "Sin estado";
					String ubicacion = "Sin ubicacion";
				}*/
                csvPrinter.printRecord(producto.getCodigoPieza(), producto.getDescripcion(), producto.getArea(),
                		producto.getEmpresa().getNombre(), producto.getEstado() == null ? "" : producto.getEstado().getTipo(), producto.getFabricante().getNombre(), 
                		producto.getFamilia().getNombre(), producto.getOrden(), producto.getUbicacion() == null ? "":producto.getUbicacion().getNombre());
            }
        } catch (IOException e) {
            log.error("Error While writing CSV ", e);
        }
    }
}
