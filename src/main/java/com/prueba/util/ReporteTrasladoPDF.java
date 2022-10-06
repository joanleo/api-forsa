/**
 * 
 */
package com.prueba.util;

import java.awt.Color;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.prueba.entity.DetalleTrasl;
import com.prueba.entity.Producto;
import com.prueba.entity.Traslado;

/**
 * @author Joan Leon
 *
 */
public class ReporteTrasladoPDF {

	Traslado traslado;

	public ReporteTrasladoPDF(Traslado traslado) {
		super();
		this.traslado = traslado;
	}
	
	private void tableHeader(PdfPTable table) {
			
			PdfPCell cell = new PdfPCell();
		 	cell.setBackgroundColor(new Color(46,60,115));
	        cell.setPadding(5);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
	         
	        Font font = FontFactory.getFont(FontFactory.HELVETICA);
	        font.setColor(Color.WHITE);
	        font.setSize(9);
	        
	        cell.setPhrase(new Phrase("No", font));
	        table.addCell(cell);
	         
	        cell.setPhrase(new Phrase("QR", font));
	        table.addCell(cell);
	         
	        cell.setPhrase(new Phrase("Descripcion", font));
	        table.addCell(cell);
	        
	        cell.setPhrase(new Phrase("Origen", font));
	        table.addCell(cell);
	        
	        cell.setPhrase(new Phrase("Destino", font));
	        table.addCell(cell);
	        
	        cell.setPhrase(new Phrase("Confirmado por", font));
	        table.addCell(cell);
	        
	        cell.setPhrase(new Phrase("Recibido por", font));
	        table.addCell(cell);
	        
	        cell.setPhrase(new Phrase("Fecha recibido", font));
	        table.addCell(cell);
	}
	
	private void tableData(PdfPTable table) {
		
		List<DetalleTrasl> detalles = traslado.getDetalles();
		int count = 1;
		for(DetalleTrasl detalle: detalles) {
			Font font = FontFactory.getFont(FontFactory.HELVETICA);
			font.setSize(8);
			
			Producto producto = detalle.getProducto();
			
			String confirmadoPor = "Sin confirmar";
			if(detalle.getUsuarioconfirma() != null) {
				confirmadoPor = detalle.getUsuarioconfirma().getNombre();
			}
			String recibidoPor = "Sin recibir";
			if(detalle.getUsuarioRecibe() != null) {
				recibidoPor = detalle.getUsuarioRecibe().getNombre();
			}
			
			String fechaRecibido = "Sin recibir";
			if(detalle.getFechaRecibe() != null) {
				fechaRecibido = detalle.getUsuarioRecibe().getNombre();
			}
			
			Phrase phrase = new Phrase(String.valueOf(count), font);
			PdfPCell cell = new PdfPCell(phrase);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);			
			table.addCell(cell);
			
			phrase = new Phrase(producto.getCodigoPieza(), font);
			cell.setPhrase(phrase);
			table.addCell(cell);
			
			phrase = new Phrase(producto.getDescripcion(), font);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPhrase(phrase);
			table.addCell(cell);
			
			phrase = new Phrase(traslado.getOrigen().getNombre(), font);
			cell.setPhrase(phrase);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			phrase = new Phrase(traslado.getDestino().getNombre(), font);
			cell.setPhrase(phrase);
			table.addCell(cell);
			
			phrase = new Phrase(confirmadoPor, font);
			cell.setPhrase(phrase);
			table.addCell(cell);
			
			phrase = new Phrase(recibidoPor, font);
			cell.setPhrase(phrase);
			table.addCell(cell);
			
			phrase = new Phrase(fechaRecibido, font);
			cell.setPhrase(phrase);
			table.addCell(cell);
			
			count++;
		}
		
	}
	
public void export(HttpServletResponse response) throws DocumentException, IOException {
		
		Document documento = new Document(PageSize.LETTER);
		PdfWriter.getInstance(documento, response.getOutputStream());
		
		documento.setMargins(30, 20, 20, 20);
		
		documento.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Font font1 = FontFactory.getFont(FontFactory.HELVETICA);
        font.setSize(18);
        font.setColor(new Color(226,119,12));
         
        Paragraph titulo = new Paragraph("REPORTE DE TRASLADO", font);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        titulo.setSpacingBefore(20);
        
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
        Paragraph fechaCreacion = new Paragraph("Fecha de creacion: " + currentDateTime, font1);
        fechaCreacion.setAlignment(Paragraph.ALIGN_RIGHT);
        
        Paragraph porden =  new Paragraph("REALIZADO POR: " + traslado.getUsuarioEnvio().getNombre().toUpperCase() + "         DOCUMENTO: TR-" + traslado.getIdTraslado(), font1);
        porden.setAlignment(Paragraph.ALIGN_LEFT);
        porden.setSpacingBefore(30);
        
        Paragraph cantidad = new Paragraph("Total [" + traslado.getDetalles().size() + "]");
        cantidad.setAlignment(Paragraph.ALIGN_RIGHT);
        cantidad.setSpacingBefore(20);
        
        documento.add(fechaCreacion);
        documento.add(titulo);
        documento.add(porden);
        documento.add(cantidad);
        
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {0.5f, 1.0f, 3.0f, 1.2f, 1.2f, 1.8f, 1.8f, 1.8f});
        table.setSpacingBefore(10);
         
        tableHeader(table);
        tableData(table);
        
        documento.add(table);
        
        documento.close();
	}
}
