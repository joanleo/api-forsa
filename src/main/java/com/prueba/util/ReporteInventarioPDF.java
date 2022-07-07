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
import com.prueba.entity.DetalleInv;
import com.prueba.entity.MovInventario;
import com.prueba.entity.Producto;

public class ReporteInventarioPDF {
	
	private MovInventario inventario;

	public ReporteInventarioPDF(MovInventario inventario) {
		super();
		this.inventario = inventario;
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
        
        cell.setPhrase(new Phrase("Item", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Código de pieza", font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Nombre", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Área", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
         
        //cell.setPhrase(new Phrase("Empresa", font));
        //table.addCell(cell);
         
        cell.setPhrase(new Phrase("Estado", font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Fabricante", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Familia", font));
        table.addCell(cell);
        
        //cell.setPhrase(new Phrase("Órden", font));
        //table.addCell(cell);
        
        cell.setPhrase(new Phrase("Ubicación", font));
        table.addCell(cell);
        
        //cell.setPhrase(new Phrase("Motivo ingreso", font));
        //table.addCell(cell);
        
        cell.setPhrase(new Phrase("Activo", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Verificado", font));
        table.addCell(cell);
	}
	
	private void tableData(PdfPTable table) {
		List<DetalleInv> detalles = inventario.getDetalles();
		int count = 1;
		for(DetalleInv detalle: detalles) {
			Font font = FontFactory.getFont(FontFactory.HELVETICA);
			font.setSize(8);
			Producto producto = detalle.getProducto();
			Phrase phrase = new Phrase(String.valueOf(count), font);
			PdfPCell cell = new PdfPCell(phrase);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);			
			table.addCell(cell);
			
			phrase = new Phrase(producto.getCodigoPieza(), font);
			cell = new PdfPCell(phrase);
			table.addCell(cell);
			
			phrase = new Phrase(producto.getDescripcion(), font);
			cell = new PdfPCell(phrase);
			table.addCell(cell);
			
			phrase = new Phrase(String.format("%.2f",producto.getArea()), font);
			cell.setPhrase(phrase);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);
			
			//table.addCell(producto.getEmpresa().getNombre());
			
			phrase = new Phrase(producto.getEstado() == null ? " ":producto.getEstado().getTipo(), font);
			cell = new PdfPCell(phrase);
			table.addCell(cell);
			
			phrase = new Phrase(producto.getFabricante().getNombre(), font);
			cell = new PdfPCell(phrase);
			table.addCell(cell);
			
			phrase = new Phrase(producto.getFamilia().getNombre(), font);
			cell = new PdfPCell(phrase);
			table.addCell(cell);
			
			//table.addCell(producto.getOrden());
			
			phrase = new Phrase(producto.getUbicacion().getNombre(), font);
			cell = new PdfPCell(phrase);
			table.addCell(cell);
			
			//table.addCell(producto.getMotivoIngreso());
			phrase = new Phrase(String.valueOf(producto.getEstaActivo()) == "true" ? "Si": "No", font);
			cell.setPhrase(phrase);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			phrase = new Phrase(String.valueOf(producto.getVerificado()) == "true" ? "Si": "No", font);
			cell.setPhrase(phrase);
			table.addCell(cell);
			//table.setHorizontalAlignment(count);
			
			count++;
		}
		
	}

	public void export(HttpServletResponse response) throws DocumentException, IOException {
		
		Document documento = new Document(PageSize.LETTER.rotate());
		PdfWriter.getInstance(documento, response.getOutputStream());
		
		documento.setMargins(30, 20, 20, 20);
		
		documento.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Font font1 = FontFactory.getFont(FontFactory.HELVETICA);
        font.setSize(18);
        font.setColor(new Color(226,119,12));
         
        Paragraph titulo = new Paragraph("REPORTE DE INVENTARIO", font);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        titulo.setSpacingBefore(20);
        
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
        Paragraph fechaCreacion = new Paragraph("Fecha de creacion: " + currentDateTime, font1);
        fechaCreacion.setAlignment(Paragraph.ALIGN_RIGHT);
        
        Paragraph porden =  new Paragraph("REALIZADO POR: " + inventario.getRealizo().getNombre().toUpperCase() + "         INVENTARIO No: " + String.valueOf(inventario.getIdMov()).toUpperCase(), font1);
        porden.setAlignment(Paragraph.ALIGN_LEFT);
        porden.setSpacingBefore(30);
        
        Paragraph cantidad = new Paragraph("Total [" + inventario.getDetalles().size() + "]");
        cantidad.setAlignment(Paragraph.ALIGN_RIGHT);
        cantidad.setSpacingBefore(20);
		
        
        documento.add(fechaCreacion);
        documento.add(titulo);
        documento.add(porden);
        documento.add(cantidad);
        
        PdfPTable table = new PdfPTable(10);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {0.6f, 2.0f, 2.0f, 0.8f, 1.5f, 2.0f, 2.0f, 2.0f, 0.7f, 1.0f});
        table.setSpacingBefore(10);
         
        tableHeader(table);
        tableData(table);
        
        documento.add(table);
        
        documento.close();
	}

}
