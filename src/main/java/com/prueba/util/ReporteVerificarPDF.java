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
import com.prueba.entity.Producto;
import com.prueba.security.entity.Usuario;


public class ReporteVerificarPDF {
		
	private List<Producto> productos;
	private String filtro;
	private String orden;
	private Usuario usuario;

	public ReporteVerificarPDF(List<Producto> productos, String filtro, String orden, Usuario usuario) {
		super();
		this.productos = productos;
		this.filtro = filtro;
		this.orden = orden;
		this.usuario = usuario;
	}

	private void tableHeader(PdfPTable table) {
		
		PdfPCell cell = new PdfPCell();
	 	cell.setBackgroundColor(new Color(46,60,115));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
         
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);
        
        cell.setPhrase(new Phrase("No", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("QR", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Descripcion", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Familia", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Tipo", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Medidas", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Área m2", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Ubicacion", font));
        table.addCell(cell);
         
         
        cell.setPhrase(new Phrase("Estado", font));
        table.addCell(cell);
               
        cell.setPhrase(new Phrase("Revisado", font));
        table.addCell(cell);
		
	}
	
	private void tableData(PdfPTable table) {
		int count = 1;
		for(Producto producto: productos) {
				Phrase phrase = new Phrase(String.valueOf(count));
				PdfPCell cell = new PdfPCell(phrase);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
				
				table.addCell(cell);
				
				phrase = new Phrase(producto.getCodigoPieza());
				cell.setPhrase(phrase);
				table.addCell(cell);
				
				table.addCell(producto.getDescripcion());
				
				phrase = new Phrase(producto.getFamilia().getSigla());
				cell.setPhrase(phrase);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				phrase = new Phrase(producto.getTipo().getNombre());
				cell.setPhrase(phrase);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);
				
				phrase = new Phrase(producto.getMedidas());
				cell.setPhrase(phrase);
				table.addCell(cell);
				
				phrase = new Phrase(String.format("%.2f",producto.getArea()));
				cell.setPhrase(phrase);
				table.addCell(cell);
				
				phrase = new Phrase(producto.getUbicacion().getNombre());
				cell.setPhrase(phrase);
				table.addCell(cell);
				
				phrase = new Phrase(producto.getEstado() == null ? " ":producto.getEstado().getTipo());
				cell.setPhrase(phrase);
				table.addCell(cell);
			
				phrase = new Phrase(String.valueOf(producto.getVerificado()) == "true" ? "Si": "No");
				cell.setPhrase(phrase);
				table.addCell(cell);

				
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
        font.setSize(14);
        font.setColor(new Color(226,119,12));
         
        Paragraph titulo = new Paragraph("REPORTE DE VERIFICACION DE ORDEN", font);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        titulo.setSpacingBefore(20);
        
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
        Paragraph fechaCreacion = new Paragraph("Fecha de creacion: " + currentDateTime, font1);
        fechaCreacion.setAlignment(Paragraph.ALIGN_RIGHT);
        
        Paragraph usuarioGenera =  new Paragraph("GENERADO POR POR: " + usuario.getNombre().toUpperCase());
        usuarioGenera.setAlignment(Paragraph.ALIGN_LEFT);
        usuarioGenera.setSpacingBefore(30);
        
        Paragraph porden =  new Paragraph("ORDEN: " + orden.toUpperCase() + "         FILTRO: " + filtro.toUpperCase(), font1);
        porden.setAlignment(Paragraph.ALIGN_LEFT);
        porden.setSpacingBefore(30);
        
        Paragraph cantidad = new Paragraph("Total [" + productos.size() + "]");
        cantidad.setAlignment(Paragraph.ALIGN_RIGHT);
        cantidad.setSpacingBefore(20);
        
        documento.add(fechaCreacion);
        documento.add(titulo);
        documento.add(usuarioGenera);
        documento.add(porden);
        documento.add(cantidad);
                 
       
        
        
        
        PdfPTable table = new PdfPTable(10);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {0.5f, 1.0f, 3.0f, 0.88f, 0.9f, 1.35f, 0.91f, 1.35f, 0.93f, 1.0f});
        table.setSpacingBefore(10);
         
        tableHeader(table);
        tableData(table);
         
        documento.add(table);
        
        Paragraph recibido =  new Paragraph("Firma Recibido : "
        		+ "\n"
        		+ "\n"
        		+ "__________________________________________________" , font1);
        recibido.setAlignment(Paragraph.ALIGN_LEFT);
        recibido.setSpacingBefore(60);
        
        documento.add(recibido);
         
        documento.close();
		
	}
}
