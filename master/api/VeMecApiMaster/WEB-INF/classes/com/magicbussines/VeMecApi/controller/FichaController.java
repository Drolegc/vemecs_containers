package com.magicbussines.VeMecApi.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.collection.PdfTargetDictionary;
import com.magicbussines.VeMecApi.VeMecApiApplication;
import com.magicbussines.VeMecApi.interfaceServices.IFichaService;
import com.magicbussines.VeMecApi.interfaceServices.IPacienteService;
import com.magicbussines.VeMecApi.models.Ficha;
import com.magicbussines.VeMecApi.models.Paciente;

@RestController
@CrossOrigin
@RequestMapping("/fichas")
public class FichaController {

	@Autowired
	private IFichaService _ficha;
	@Autowired
	private IPacienteService pacienteService;
	
	@GetMapping("/")
	public List<Ficha> index(){
		return _ficha.listar();
	}
	
	@GetMapping("/{documento_id}")
	public ResponseEntity<Object> paciente(@PathVariable String documento_id) {
		List<Ficha> ficha = _ficha.listarId(documento_id);
		if(!ficha.isEmpty()) {
			return new ResponseEntity<Object>("No tenemos paciente para el documento "+documento_id, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Object>(ficha,HttpStatus.OK);
	}
	
	@GetMapping("/ficha")
	ResponseEntity<byte[]> returnFichita(@RequestBody JsonNode data) throws JsonParseException, JsonMappingException, IOException {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime timeInicio = LocalDateTime.parse(data.get("timeInicio").asText(), formatter);
		LocalDateTime timeFin = LocalDateTime.parse(data.get("timeFin").asText(), formatter);
		String documento_id = data.get("paciente_id").asText();
		
		Paciente pac = pacienteService.obtener(documento_id).get();
		
		List<Ficha> lista = _ficha.fichaRanged(documento_id, timeInicio, timeFin);
		String pdfPath = VeMecApiApplication.getPdfPath();
		
		try {
			
			Document documento = new Document();
			String pdfName = documento_id+"-RangedFicha.pdf";
			PdfWriter.getInstance(documento, new FileOutputStream(pdfPath+pdfName));
			documento.open();
			
			Font titleFont = new Font(Font.FontFamily.COURIER, 28,
		            Font.BOLD);
			Font italicFont = new Font(Font.FontFamily.COURIER, 15,
		            Font.ITALIC);
			Font boldFont = new Font(Font.FontFamily.COURIER, 12,
		            Font.BOLD);
			Font colSpan = new Font(Font.FontFamily.COURIER, 20,
		            Font.BOLD);
			
			Paragraph infoHeader = new Paragraph();
			
			Paragraph title = new Paragraph("Fichas del paciente",titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			infoHeader.add(title);
			addEmptyLine(infoHeader, 2);
			
			documento.add(infoHeader); 
			//documento.newPage();
			
			if(lista.size() == 0) {
				
				Paragraph info = new Paragraph("No hay fichas en este intervalo de tiempo",italicFont);
				addEmptyLine(info, 1);
				Phrase frase = new Phrase("Inicio: "+timeInicio.toString().replace("T", " ")+" | Fin: "+timeFin.toString().replace("T", " "));
				info.add(frase);
				info.setAlignment(Element.ALIGN_CENTER);
				documento.add(info);

			}else {
				
			
			
			int i = 1;
			for (Ficha ficha : lista) {
				PdfPTable table = new PdfPTable(2);
				
				PdfPCell celda0 = new PdfPCell(new Phrase("Ficha Num. "+i,colSpan));
				//creo una tabla con dos columnas
				celda0.setHorizontalAlignment(Element.ALIGN_CENTER);
				celda0.setColspan(2);
				table.addCell(celda0);
				
				i++;
				
				PdfPCell celda = new PdfPCell(new Phrase("Nombres",boldFont));
				PdfPCell celdaInfo = new PdfPCell(new Phrase(
						pac.getApellido()+", "+pac.getNombre()
						));
				table.addCell(celda);
				table.addCell(celdaInfo);
				
				PdfPCell celda1 = new PdfPCell(new Phrase("Sexo",boldFont));
				PdfPCell celdaInfo1 = new PdfPCell(new Phrase(
						pac.getSexo()
						));
				table.addCell(celda1);
				table.addCell(celdaInfo1);
				
				PdfPCell celda2 = new PdfPCell(new Phrase("Edad",boldFont));
				PdfPCell celdaInfo2 = new PdfPCell(new Phrase(
						String.valueOf(pac.getEdad())
						));
				table.addCell(celda2);
				table.addCell(celdaInfo2);
				
				PdfPCell celda3 = new PdfPCell(new Phrase("Documento identidad / Nacionalidad",boldFont));
				PdfPCell celdaInfo3 = new PdfPCell(new Phrase(
						pac.getDocumento_id()+" / "+pac.getNacionalidad()
						));
				table.addCell(celda3);
				table.addCell(celdaInfo3);
				
				PdfPCell celda4 = new PdfPCell(new Phrase("Telefonos / Mail contacto",boldFont));
				PdfPCell celdaInfo4 = new PdfPCell(new Phrase(
						pac.getTelefonos()+" / "+pac.getMail()
						));
				table.addCell(celda4);
				table.addCell(celdaInfo4);
				
				PdfPCell celda5 = new PdfPCell(new Phrase("Datos Familiares",boldFont));
				PdfPCell celdaInfo5 = new PdfPCell(new Phrase(
						ficha.getDatosFamiliares()
						));
				table.addCell(celda5);
				table.addCell(celdaInfo5);
				
				PdfPCell celda6 = new PdfPCell(new Phrase("Lugar de Residencia",boldFont));
				PdfPCell celdaInfo6 = new PdfPCell(new Phrase(
						pac.getLugarResidencia()
						));
				table.addCell(celda6);
				table.addCell(celdaInfo6);
				
				PdfPCell celda7 = new PdfPCell(new Phrase("Antecedentes Clinicos",boldFont));
				PdfPCell celdaInfo7 = new PdfPCell(new Phrase(
						ficha.getAntecedentes()
						));
				table.addCell(celda7);
				table.addCell(celdaInfo7);
		
				
				PdfPCell celda8 = new PdfPCell(new Phrase("Fecha y Hora ingreso",boldFont));
				PdfPCell celdaInfo8 = new PdfPCell(new Phrase(
						ficha.getTimestamp().toString().replace("T"," ")
						));
				table.addCell(celda8);
				table.addCell(celdaInfo8);
				
				PdfPCell celda99 = new PdfPCell(new Phrase("Medico tratante",boldFont));
				PdfPCell celdaInfo99 = new PdfPCell(new Phrase(
						ficha.getMedicoTratante().getApellido()+", "+ficha.getMedicoTratante().getNombre()
						));
				table.addCell(celda99);
				table.addCell(celdaInfo99);
				
				PdfPCell celda9 = new PdfPCell(new Phrase("Motivos",boldFont));
				PdfPCell celdaInfo9 = new PdfPCell(new Phrase(
						ficha.getMotivos()
						));
				table.addCell(celda9);
				table.addCell(celdaInfo9);
				
				PdfPCell celdaA = new PdfPCell(new Phrase("Nivel de Riesgo",boldFont));
				PdfPCell celdaInfoA = new PdfPCell(new Phrase(
						ficha.getNivelRiesgo()
						));
				table.addCell(celdaA);
				table.addCell(celdaInfoA);
				
				PdfPCell celdaB = new PdfPCell(new Phrase("Medicacion",boldFont));
				PdfPCell celdaInfoB = new PdfPCell(new Phrase(
						ficha.getMedicacion()
						));
				table.addCell(celdaB);
				table.addCell(celdaInfoB);
				String vemec;
				if (ficha.getVemec() == null) {
					vemec = "Sin respiracion asistida";
				}else
				{
					vemec = ficha.getVemec().getId()+" / "+ficha.getVemec().getUbicacion();
				}
				PdfPCell celdaC = new PdfPCell(new Phrase("Conectado a VeMeC",boldFont));
				PdfPCell celdaInfoC = new PdfPCell(new Phrase(
						vemec
						));
				table.addCell(celdaC);
				table.addCell(celdaInfoC);
				
				Paragraph lineas = new Paragraph(" ");
				addEmptyLine(lineas, 2);
				documento.add(lineas);
				documento.add(table);
			}	
			
				
			}
			
			documento.close();
		} catch (DocumentException ex) {
			// TODO: handle exception
			ex.toString();
		}
		
		File file = new File(pdfPath+documento_id+"-RangedFicha.pdf");  // assume args[0] is the path to file
        byte[] data1 = FileUtils.readFileToByteArray(file);
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_PDF);
	    // Here you have to set the actual filename of your pdf
	    String filename = documento_id+"-RangedFicha.pdf";
	    headers.setContentDispositionFormData(filename, filename);
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    ResponseEntity<byte[]> response = new ResponseEntity<>(data1, headers, HttpStatus.OK);
	    return response;
		
	}
	
	private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
	
	
}
