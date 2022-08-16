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
import com.prueba.dto.ComparativoInventarioDTO;
import com.prueba.security.entity.Usuario;

public class ReporteComparativo {

	private List<ComparativoInventarioDTO> comparativo;
	
	private Usuario usuarioCrea;
	
	private Integer inv1;
	
	private Integer inv2;
	
	public ReporteComparativo(List<ComparativoInventarioDTO> comparativo) {
		super();
		this.comparativo = comparativo;
		this.usuarioCrea = comparativo.get(0).getUsuarioRealizo();
		this.inv1 = comparativo.get(0).getNumInv1();
		this.inv2 = comparativo.get(0).getNumInv2();
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
         
        cell.setPhrase(new Phrase("QR", font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Familia", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Tipo", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Medidas", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Estado", font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("INV-"+inv1.toString(), font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("INV-"+inv2.toString(), font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
	}
	
	private void tableData(PdfPTable table) {

		int count = 1;
		System.out.println("inv1: "+comparativo.get(0).getNumInv1());
		for(ComparativoInventarioDTO detalle: comparativo) {
			Font font = FontFactory.getFont(FontFactory.HELVETICA);
			font.setSize(8);
			
			Phrase phrase = new Phrase(String.valueOf(count), font);
			PdfPCell cell = new PdfPCell(phrase);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);			
			table.addCell(cell);
			
			phrase = new Phrase(detalle.getCodigo(), font);
			cell = new PdfPCell(phrase);
			table.addCell(cell);
			
			phrase = new Phrase(detalle.getFamilia(), font);
			cell = new PdfPCell(phrase);
			table.addCell(cell);

						
			phrase = new Phrase(detalle.getTipo(), font);
			cell = new PdfPCell(phrase);
			table.addCell(cell);
			
			phrase = new Phrase(detalle.getMedidas(), font);
			cell = new PdfPCell(phrase);
			table.addCell(cell);
			
			phrase = new Phrase(detalle.getEstado(), font);
			cell = new PdfPCell(phrase);
			table.addCell(cell);
			
			phrase = new Phrase(detalle.getInv1() ? "Si":"No", font);
			cell = new PdfPCell(phrase);
			table.addCell(cell);
			
			phrase = new Phrase(detalle.getInv2() ? "Si":"No", font);
			cell = new PdfPCell(phrase);
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
        font.setSize(18);
        font.setColor(new Color(226,119,12));
         
        Paragraph titulo = new Paragraph("COMPARATIVO DE INVENTARIOS", font);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        titulo.setSpacingBefore(20);
        
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
        Paragraph fechaCreacion = new Paragraph("Fecha de creacion: " + currentDateTime, font1);
        fechaCreacion.setAlignment(Paragraph.ALIGN_RIGHT);
        
        Paragraph porden =  new Paragraph("REALIZADO POR: " + usuarioCrea.getNombre().toUpperCase() + "               INV-"+ inv1 + " vs INV-"+ inv2 , font1);
        porden.setAlignment(Paragraph.ALIGN_LEFT);
        porden.setSpacingBefore(30);
        
        Paragraph cantidad = new Paragraph("Total [" + comparativo.size() + "]");
        cantidad.setAlignment(Paragraph.ALIGN_RIGHT);
        cantidad.setSpacingBefore(20);
		
        
        documento.add(fechaCreacion);
        documento.add(titulo);
        documento.add(porden);
        documento.add(cantidad);
        
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100f);
        //table.setWidths(new float[] {0.6f, 2.0f, 2.0f, 0.8f, 1.5f, 2.0f, 2.0f, 2.0f, 0.7f, 1.0f});
        table.setSpacingBefore(10);
         
        tableHeader(table);
        tableData(table);
        
        documento.add(table);
        
        documento.close();
	}
}
