package com.magicbussines.VeMecApi.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.ZoneIdConverter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magicbussines.VeMecApi.VeMecApiApplication;
import com.magicbussines.VeMecApi.helpers.ChartsCreator;
import com.magicbussines.VeMecApi.interfaceServices.IHistorialService;
import com.magicbussines.VeMecApi.interfaceServices.IPacienteService;
import com.magicbussines.VeMecApi.interfaceServices.IVeMecService;
import com.magicbussines.VeMecApi.models.Historial;
import com.magicbussines.VeMecApi.models.Paciente;
import com.magicbussines.VeMecApi.models.VeMec;

import org.apache.commons.io.FileUtils;
import org.jfree.chart.text.TextFragment;

import java.io.FileOutputStream;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.clipper.Path;
import com.itextpdf.text.pdf.*;

@RestController
@CrossOrigin
@RequestMapping("/historiales")
public class HistorialController {
	
	@Autowired
	private IHistorialService historialService;
	@Autowired
	private IPacienteService pacienteService;
	@Autowired
	private IVeMecService vemecService;
	@Autowired
	private ObjectMapper objectMapper;
	
	@GetMapping("/")
	public List<Historial> index(){
		return historialService.listar();
	}
	
	//localhost:8080/historiales/#####
	@GetMapping("/{documento_id}")
	public ResponseEntity<Object> historial(@PathVariable String documento_id) {
		List<Historial> historial = historialService.historialByIdPaciente(documento_id);
		if(historial.isEmpty()) {
			return new ResponseEntity<Object>("No tenemos historiales para el documento "+documento_id, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Object>(historial,HttpStatus.OK);
	}
	@GetMapping("/{documento_id}/{page}/{size}")
	public ResponseEntity<Object> historial(@PathVariable String documento_id, @PathVariable int page, @PathVariable int size) {
		Page<Historial> historial = historialService.historialPaginated(documento_id, page, size);
		if(historial.isEmpty()) {
			return new ResponseEntity<Object>("No tenemos historiales para el documento "+documento_id, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Object>(historial,HttpStatus.OK);
	}
	
	/*@GetMapping("/{page}/{size}")
	public ResponseEntity<Object> historial(@PathVariable int page, @PathVariable int size) {
		Page<Historial> historial = historialService.historialPaginated(documentoId, page, size);
		if(historial.isEmpty()) {
			return new ResponseEntity<Object>("Cant Paciente find CI", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Object>(historial,HttpStatus.OK);
	}*/
	
	// http://localhost:8080/historiales/ 
	@PostMapping("/") // agrega un historial nuevo
	public ResponseEntity<Object> saveHistorial(@RequestBody JsonNode data) throws JsonParseException, JsonMappingException, IOException {
		
		Paciente paciente;
		VeMec vemec;
		String vemec_id;
		
		try {
			vemec_id = data.get("vemec_id").asText();
			vemec = vemecService.obtener(vemec_id).get();
			
		} catch(Exception e) {
			return new ResponseEntity<Object>("No se encontro vemec con el id dado", HttpStatus.NOT_FOUND);
		}
		
		try {
			paciente = pacienteService.obtenerPacientePorIdVemec(vemec_id).get();
		} catch(Exception e) {
			return new ResponseEntity<Object>("No se encontro paciente con el documento dado", HttpStatus.NOT_FOUND);
		}
				
		Historial nuevoHistorial = objectMapper.readValue(data.get("data_historial").toString(), Historial.class);
		nuevoHistorial.setPaciente(paciente);
		historialService.save(nuevoHistorial);
		
		return new ResponseEntity<Object>(nuevoHistorial,HttpStatus.CREATED);
		
	}
	
	@PostMapping("/varios_registros") // agrega un historial nuevo
	public ResponseEntity<Object> saveHistorialAll(@RequestBody JsonNode data) throws JsonParseException, JsonMappingException, IOException {
		
		Paciente paciente;
		VeMec vemec;
		String vemec_id;
		
		try {
			vemec_id = data.get(1).get("vemec_id").asText();
			vemec = vemecService.obtener(vemec_id).get();
			
		} catch(Exception e) {
			return new ResponseEntity<Object>("No se encontro vemec con el id dado", HttpStatus.NOT_FOUND);
		}
		
		try {
			paciente = pacienteService.obtenerPacientePorIdVemec(vemec_id).get();
		} catch(Exception e) {
			return new ResponseEntity<Object>("No se encontro paciente con el documento dado", HttpStatus.NOT_FOUND);
		}
		
		Historial nuevoHistorial;
		for (JsonNode data1 : data) {
			nuevoHistorial = objectMapper.readValue(data1.get("data_historial").toString(), Historial.class);
			nuevoHistorial.setPaciente(paciente);
			historialService.save(nuevoHistorial);
		}
		
		return new ResponseEntity<Object>("El historial fue registrado en la base de datos.",HttpStatus.CREATED);
		
	}
	
	@PutMapping("/")
	public ResponseEntity<Object> updateHistorial(@RequestBody JsonNode data) throws JsonParseException, JsonMappingException, IOException {
		
		Paciente paciente;
		VeMec vemec;
		String vemec_id;
		
		try {
			vemec_id = data.get("vemec_id").asText();
			vemec = vemecService.obtener(vemec_id).get();
			
		} catch(Exception e) {
			return new ResponseEntity<Object>("No se encontro vemec con el id dado", HttpStatus.NOT_FOUND);
		}
		
		try {
			paciente = pacienteService.obtenerPacientePorIdVemec(vemec_id).get();
		} catch(Exception e) {
			return new ResponseEntity<Object>("No se encontro paciente con el documento dado", HttpStatus.NOT_FOUND);
		}
		
		Historial nuevoHistorial = objectMapper.readValue(data.get("data_historial").toString(), Historial.class);
		nuevoHistorial.setPaciente(paciente);
		historialService.save(nuevoHistorial);
		
		return new ResponseEntity<Object>(nuevoHistorial,HttpStatus.CREATED);
		
	}
	
	
	@DeleteMapping("/{documento_id}")
	public ResponseEntity<Object> borrarAll(@PathVariable String documento_id) {
		List<Historial> histo = historialService.historialByIdPaciente(documento_id);
		if (histo != null) {
			historialService.deleteAllForDocumento(documento_id);
			return new ResponseEntity<Object>(histo, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("Cant find Paciente for this Document ID", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
//	@GetMapping("/byRange")
//	public ResponseEntity<Object> responderTexto(@RequestBody JsonNode data) throws JsonParseException, JsonMappingException, IOException {
//		
//		
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//		LocalDateTime timeInicio = LocalDateTime.parse(data.get("timeInicio").asText(), formatter);
//		LocalDateTime timeFin = LocalDateTime.parse(data.get("timeFin").asText(), formatter);
//				
//		List<ArrayList<Double>> histo = historialService.historialRanged(data.get("paciente_id").asText(), timeInicio, timeFin);
//		if (!histo.isEmpty()) {
//			return new ResponseEntity<Object>(histo, HttpStatus.OK);
//		} else {
//			return new ResponseEntity<Object>("No podemos encontrar datos en ese rango", HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//
//	}
	
	
	@GetMapping("/pdf")
	public ResponseEntity<byte[]> getPDF(@RequestBody JsonNode data) throws JsonParseException, JsonMappingException, IOException {
	
		//public ResponseEntity<Object> responderTexto(@RequestBody JsonNode data) throws JsonParseException, JsonMappingException, IOException {
			
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime timeInicio = LocalDateTime.parse(data.get("timeInicio").asText(), formatter);
		LocalDateTime timeFin = LocalDateTime.parse(data.get("timeFin").asText(), formatter);
		String documento_id = data.get("paciente_id").asText();
		
		Paciente pac = pacienteService.obtener(documento_id).get();
		
		List<ArrayList<Double>> histo = historialService.historialRanged(documento_id, timeInicio, timeFin);
		
		String pdfPath = VeMecApiApplication.getPdfPath();
		String chartPath = VeMecApiApplication.getChartPath();
		
		try {
			ChartsCreator cc = new ChartsCreator();
			Document documento = new Document();
			String pdfName = documento_id+"-RangedInfo.pdf";
			PdfWriter.getInstance(documento, new FileOutputStream(pdfPath+pdfName));
			documento.open();
			
			//---------------------
			//ENCABEZADO DE ARCHIVO
			//---------------------
			Font titleFont = new Font(Font.FontFamily.COURIER, 30,
		            Font.BOLD);
			Font textBoldFont = new Font(Font.FontFamily.HELVETICA, 14,
		            Font.BOLD);
			Font textNormalFont = new Font(Font.FontFamily.HELVETICA, 12,
		            Font.NORMAL);
			Font italicFont = new Font(Font.FontFamily.HELVETICA, 20,
		            Font.BOLDITALIC);
			

			Paragraph infoHeader = new Paragraph();
			
			Paragraph title = new Paragraph("Graficas del VeMeC",titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			
			infoHeader.add(title);
			addEmptyLine(infoHeader, 3);
			
			
			//parrafo de info
			Paragraph PacInfo = new Paragraph();
			
				//nombreApellido
				PacInfo.add(new Phrase("Nombre paciente: ",textBoldFont));
				PacInfo.add(new Phrase(pac.getApellido()+", "+pac.getNombre(),textNormalFont));
				addEmptyLine(PacInfo, 1);
				
				PacInfo.add(new Phrase("Sexo: ",textBoldFont));
				PacInfo.add(new Phrase(pac.getSexo()));
				addEmptyLine(PacInfo, 1);
				//Doc
				PacInfo.add(new Phrase("Documento: ",textBoldFont));
				PacInfo.add(new Phrase(pac.getDocumento_id(),textNormalFont));
				addEmptyLine(PacInfo, 1);
				
				//edad
				PacInfo.add(new Phrase("Edad: ",textBoldFont));
				PacInfo.add(new Phrase(String.valueOf(pac.getEdad()),textNormalFont));
				addEmptyLine(PacInfo, 1);
				
				PacInfo.add(new Phrase("Fecha de internacion: ",textBoldFont));
				PacInfo.add(new Phrase(pac.getFechaInternacion().toString().replace("T", " "),textNormalFont));
				addEmptyLine(PacInfo, 1);
				
				PacInfo.add(new Phrase("Conectado a VeMeC: ",textBoldFont));
				PacInfo.add(new Phrase(pac.getVemec().getId(),textNormalFont));
				PacInfo.add(new Phrase(".     Ubicado en: ",textBoldFont));
				PacInfo.add(new Phrase(pac.getVemec().getUbicacion(),textNormalFont));
				addEmptyLine(PacInfo, 1);
				
				
				PacInfo.add(new Phrase("Medico tratante: ",textBoldFont));
				PacInfo.add(new Phrase(pac.getMedicoTratante().getApellido()+", "+pac.getMedicoTratante().getNombre()+"; ",textNormalFont));
				PacInfo.add(new Phrase("  Documento: ",textBoldFont));
				PacInfo.add(new Phrase(pac.getMedicoTratante().getDocumento_id(),textNormalFont));
				addEmptyLine(PacInfo, 1);
				
				PacInfo.add(new Phrase("Historial Medico: ",textBoldFont));
				PacInfo.add(new Phrase(pac.getHistoriaClinica(),textNormalFont));
				addEmptyLine(PacInfo, 1);
				

			
			infoHeader.add(PacInfo);
			
			documento.add(infoHeader); 
			documento.newPage();
			
			//---------------------
			//ENCABEZADO DE ARCHIVO
			//---------------------
			
			//Index
			//0:Pulsaciones / 1-2 Presion-m-M / 3-4 TempAire / 5-6 PresionAire-m-M 
			// 7 volGas / 8 humedad / 9 freqAporte / 10 porcenMezcla
			

			
			//------------------
			//---- GRAFICA -----
			//PULSACIONES 
			//------------------
//				Paragraph pulsacionesPar = new Paragraph();
//				pulsacionesPar.setAlignment(Element.ALIGN_CENTER);
//				pulsacionesPar.add(new Phrase("1. Pulsaciones",italicFont));
//				documento.add(pulsacionesPar);
//				addEmptyLine(pulsacionesPar, 3);
				
				List<Double> pulsaciones = histo.get(0);
				int fromIndex = 0;
				int toIndex = 20;
				//boolean done = false;
				while(pulsaciones.size() > fromIndex){
					if (pulsaciones.size() - toIndex >= 0 ) {
						//Obtengo una lista de 20
						//Hago la grafica
						String titleGraph0 = "Pulsaciones";
						cc.OneLineChart(documento_id,titleGraph0,"Segundos","Pulsaciones (PPM)",pulsaciones,fromIndex,toIndex);
						Image imagen0 = Image.getInstance(chartPath+documento_id+"-"+titleGraph0+"start"+fromIndex+"-end"+toIndex+"-Graph.jpeg");
						imagen0.scaleToFit(525, documento.getPageSize().getHeight());
						documento.add(imagen0);
						
						//muevo los index
						fromIndex = fromIndex+20;
						toIndex = toIndex+20;
						
					}else { //tiene menos de 20 elementos
						//Hago la grafica

						String titleGraph0 = "Pulsaciones";
						cc.OneLineChart(documento_id,titleGraph0,"Segundos","Pulsaciones (PPM)",pulsaciones,fromIndex,toIndex);
						Image imagen00 = Image.getInstance(chartPath+documento_id+"-"+titleGraph0+"start"+fromIndex+"-end"+toIndex+"-Graph.jpeg");
						imagen00.scaleToFit(525, documento.getPageSize().getHeight());
						documento.add(imagen00);
						fromIndex = fromIndex+20;
						toIndex = toIndex+20;
					}
				}
				
			//Espacio entre graficas
			
			documento.newPage();
	        
			//------------------
			//---- GRAFICA -----
			//Presiones
			//------------------
			fromIndex = 0;
			toIndex = 20;
			List<Double> presMin = histo.get(1);
			List<Double> presMax = histo.get(2);

			while(presMin.size() > fromIndex){
				if (presMin.size() - toIndex >= 0 ) {
					//Obtengo una lista de 20
					//Hago la grafica
					String titleGraph = "Presion";
					cc.TwoLineChart(documento_id,titleGraph,"Segundos","Presion (mmHg)",presMin,presMax,fromIndex,toIndex);
					Image imagen = Image.getInstance(chartPath+documento_id+"-"+titleGraph+"start"+fromIndex+"-end"+toIndex+"-Graph.jpeg");
					imagen.scaleToFit(525, documento.getPageSize().getHeight());
					documento.add(imagen);
										
					//muevo los index
					fromIndex = fromIndex+20;
					toIndex = toIndex+20;
					
				}else { //tiene menos de 20 elementos
					//Hago la grafica
					String titleGraph1 = "Presion";
					cc.TwoLineChart(documento_id,titleGraph1,"Segundos","Presion (mmHg)",presMin,presMax,fromIndex,toIndex);
					Image imagen11 = Image.getInstance(chartPath+documento_id+"-"+titleGraph1+"start"+fromIndex+"-end"+toIndex+"-Graph.jpeg");
					imagen11.scaleToFit(525, documento.getPageSize().getHeight());
					documento.add(imagen11);
					
					fromIndex = fromIndex+20;
					toIndex = toIndex+20;
				}
			}
			
			//Espacio entre graficas
			addEmptyLine(new Paragraph(), 2);
			documento.newPage();
			//------------------
			//---- GRAFICA -----
			//- TempAire
	
			
			fromIndex = 0;
			toIndex = 20;
			List<Double> tempAireE = histo.get(3);
			List<Double> tempAireS = histo.get(4);

			while(tempAireE.size() > fromIndex){
				if (tempAireE.size() - toIndex >= 0 ) {
					
					//Obtengo una lista de 20
					//Hago la grafica
					
					String titleGraph2 = "Temperatura del Aire";
					cc.TwoLineChart(documento_id,titleGraph2,"Segundos","Temperatura (Celcius)",tempAireE,tempAireS,fromIndex,toIndex);
					Image imagen2 = Image.getInstance(chartPath+documento_id+"-"+titleGraph2+"start"+fromIndex+"-end"+toIndex+"-Graph.jpeg");
					imagen2.scaleToFit(525, documento.getPageSize().getHeight());
					documento.add(imagen2);
										
					//muevo los index
					fromIndex = fromIndex+20;
					toIndex = toIndex+20;
					
				}else { //tiene menos de 20 elementos
					//Hago la grafica
					String titleGraph2 = "Temperatura del Aire";
					cc.TwoLineChart(documento_id,titleGraph2,"Segundos","Temperatura (Celcius)",tempAireE,tempAireS,fromIndex,toIndex);
					Image imagen2 = Image.getInstance(chartPath+documento_id+"-"+titleGraph2+"start"+fromIndex+"-end"+toIndex+"-Graph.jpeg");
					imagen2.scaleToFit(525, documento.getPageSize().getHeight());
					documento.add(imagen2);
					
					fromIndex = fromIndex+20;
					toIndex = toIndex+20;
				}
			}
			
			//Espacio entre graficas
			addEmptyLine(new Paragraph(), 2);
			documento.newPage();		

			//------------------
			//---- GRAFICA -----
			//presionAire
			
			fromIndex = 0;
			toIndex = 20;
			List<Double> presionAireE = histo.get(5);
			List<Double> presionAireS = histo.get(6);

			while(presionAireE.size() > fromIndex){
				if (presionAireE.size() - toIndex >= 0 ) {
					//Obtengo una lista de 20
					//Hago la grafica
					
					String titleGraph3 = "Presion del Aire";
					cc.TwoLineChart(documento_id,titleGraph3,"Segundos","Presion (mm..)",presionAireE,presionAireS,fromIndex,toIndex);
					Image imagen3 = Image.getInstance(chartPath+documento_id+"-"+titleGraph3+"start"+fromIndex+"-end"+toIndex+"-Graph.jpeg");
					imagen3.scaleToFit(525, documento.getPageSize().getHeight());
					documento.add(imagen3);
										
					//muevo los index
					fromIndex = fromIndex+20;
					toIndex = toIndex+20;
					
				}else { //tiene menos de 20 elementos
					//Hago la grafica
					String titleGraph3 = "Presion del Aire";
					cc.TwoLineChart(documento_id,titleGraph3,"Segundos","Presion (mm..)",presionAireE,presionAireS,fromIndex,toIndex);
					Image imagen3 = Image.getInstance(chartPath+documento_id+"-"+titleGraph3+"start"+fromIndex+"-end"+toIndex+"-Graph.jpeg");
					imagen3.scaleToFit(525, documento.getPageSize().getHeight());
					documento.add(imagen3);
					
					fromIndex = fromIndex+20;
					toIndex = toIndex+20;
				}
			}
			
			//Espacio entre graficas
			addEmptyLine(new Paragraph(), 2);
			documento.newPage();
			//------------------
			//---- GRAFICA -----
			//private float volGas;

			fromIndex = 0;
			toIndex = 20;
			List<Double> volGas = histo.get(7);

			while(volGas.size() > fromIndex){
				if (volGas.size() - toIndex >= 0 ) {
					//Obtengo una lista de 20
					//Hago la grafica
					
					String titleGraph4 = "Volumen del Gas";
					cc.OneLineChart(documento_id,titleGraph4,"Segundos","Volumen Gas (mm..)",volGas,fromIndex,toIndex);
					Image imagen4 = Image.getInstance(chartPath+documento_id+"-"+titleGraph4+"start"+fromIndex+"-end"+toIndex+"-Graph.jpeg");
					imagen4.scaleToFit(525, documento.getPageSize().getHeight());
					documento.add(imagen4);
										
					//muevo los index
					fromIndex = fromIndex+20;
					toIndex = toIndex+20;
					
				}else { //tiene menos de 20 elementos
					//Hago la grafica
					String titleGraph4 = "Volumen del Gas";
					cc.OneLineChart(documento_id,titleGraph4,"Segundos","Volumen Gas (mm..)",volGas,fromIndex,toIndex);
					Image imagen4 = Image.getInstance(chartPath+documento_id+"-"+titleGraph4+"start"+fromIndex+"-end"+toIndex+"-Graph.jpeg");
					imagen4.scaleToFit(525, documento.getPageSize().getHeight());
					documento.add(imagen4);
					
					fromIndex = fromIndex+20;
					toIndex = toIndex+20;
				}
			}
			
			//Espacio entre graficas
			addEmptyLine(new Paragraph(), 2);
			documento.newPage();
			//------------------
			//---- GRAFICA -----
			//private int humedadAire;
			
			fromIndex = 0;
			toIndex = 20;
			List<Double> humedadAire = histo.get(8);

			while(humedadAire.size() > fromIndex){
				if (humedadAire.size() - toIndex >= 0 ) {
					//Obtengo una lista de 20
					//Hago la grafica
					
					String titleGraph5 = "Humedad del Aire";
					cc.OneLineChart(documento_id,titleGraph5,"Segundos","Humedad Aire (mm..)",humedadAire,fromIndex,toIndex);
					Image imagen5 = Image.getInstance(chartPath+documento_id+"-"+titleGraph5+"start"+fromIndex+"-end"+toIndex+"-Graph.jpeg");
					imagen5.scaleToFit(525, documento.getPageSize().getHeight());
					documento.add(imagen5);
										
					//muevo los index
					fromIndex = fromIndex+20;
					toIndex = toIndex+20;
					
				}else { //tiene menos de 20 elementos
					//Hago la grafica
					String titleGraph5 = "Humedad del Aire";
					cc.OneLineChart(documento_id,titleGraph5,"Segundos","Humedad Aire (mm..)",humedadAire,fromIndex,toIndex);
					Image imagen5 = Image.getInstance(chartPath+documento_id+"-"+titleGraph5+"start"+fromIndex+"-end"+toIndex+"-Graph.jpeg");
					imagen5.scaleToFit(525, documento.getPageSize().getHeight());
					documento.add(imagen5);
					
					fromIndex = fromIndex+20;
					toIndex = toIndex+20;
				}
			}		
			
			//Espacio entre graficas
			addEmptyLine(new Paragraph(), 2);
			documento.newPage();
			
			//------------------
			//---- GRAFICA -----
			//private int freqAporte;
			
			fromIndex = 0;
			toIndex = 20;
			List<Double> freqAporte = histo.get(9);

			while(freqAporte.size() > fromIndex){
				if (freqAporte.size() - toIndex >= 0 ) {
					//Obtengo una lista de 20
					//Hago la grafica
					
					
					String titleGraph7 = "Frecuencia de aporte O2";
					cc.OneLineChart(documento_id,titleGraph7,"Segundos","Freq. de Aporte (mm..)",freqAporte,fromIndex,toIndex);
					Image imagen7 = Image.getInstance(chartPath+documento_id+"-"+titleGraph7+"start"+fromIndex+"-end"+toIndex+"-Graph.jpeg");
					imagen7.scaleToFit(525, documento.getPageSize().getHeight());
					documento.add(imagen7);
										
					//muevo los index
					fromIndex = fromIndex+20;
					toIndex = toIndex+20;
					
				}else { //tiene menos de 20 elementos
					//Hago la grafica
					String titleGraph7 = "Frecuencia de aporte O2";
					cc.OneLineChart(documento_id,titleGraph7,"Segundos","Freq. de Aporte (mm..)",freqAporte,fromIndex,toIndex);
					Image imagen7 = Image.getInstance(chartPath+documento_id+"-"+titleGraph7+"start"+fromIndex+"-end"+toIndex+"-Graph.jpeg");
					imagen7.scaleToFit(525, documento.getPageSize().getHeight());
					documento.add(imagen7);
					
					fromIndex = fromIndex+20;
					toIndex = toIndex+20;
				}
			}
			
			//Espacio entre graficas
			addEmptyLine(new Paragraph(), 2);
			documento.newPage();
			//------------------
			//---- GRAFICA -----
			//private int porcenMezclaO2;
			
			fromIndex = 0;
			toIndex = 20;
			List<Double> porcenMezclaO2 = histo.get(10);

			while(porcenMezclaO2.size() > fromIndex){
				if (porcenMezclaO2.size() - toIndex >= 0 ) {
					//Obtengo una lista de 20
					//Hago la grafica
					
					
					String titleGraph6 = "Porcentaje mezcla de O2";
					cc.OneLineChart(documento_id,titleGraph6,"Segundos","Volumen Gas (mm..)",porcenMezclaO2,fromIndex,toIndex);
					Image imagen6 = Image.getInstance(chartPath+documento_id+"-"+titleGraph6+"start"+fromIndex+"-end"+toIndex+"-Graph.jpeg");
					imagen6.scaleToFit(525, documento.getPageSize().getHeight());
					documento.add(imagen6);
										
					//muevo los index
					fromIndex = fromIndex+20;
					toIndex = toIndex+20;
					
				}else { //tiene menos de 20 elementos
					//Hago la grafica
					String titleGraph6 = "Porcentaje mezcla de O2";
					cc.OneLineChart(documento_id,titleGraph6,"Segundos","Volumen Gas (mm..)",porcenMezclaO2,fromIndex,toIndex);
					Image imagen6 = Image.getInstance(chartPath+documento_id+"-"+titleGraph6+"start"+fromIndex+"-end"+toIndex+"-Graph.jpeg");
					imagen6.scaleToFit(525, documento.getPageSize().getHeight());
					documento.add(imagen6);
					
					fromIndex = fromIndex+20;
					toIndex = toIndex+20;
				}
			}
			
			//Espacio entre graficas
			addEmptyLine(new Paragraph(), 2);
			documento.close();
		
		} catch (DocumentException ex) {
			ex.toString();  
		}
		
		
			//RETORNA BIEN EL PDF COMO PDF
			File file = new File(pdfPath+documento_id+"-RangedInfo.pdf");  // assume args[0] is the path to file
	        byte[] data1 = FileUtils.readFileToByteArray(file);
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_PDF);
		    // Here you have to set the actual filename of your pdf
		    String filename = documento_id+"-RangedInfo.pdf";
		    headers.setContentDispositionFormData(filename, filename);
		    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		    ResponseEntity<byte[]> response = new ResponseEntity<>(data1, headers, HttpStatus.OK);
		    return response;
	}
	
	//Helper with pdf.
	 private static void addEmptyLine(Paragraph paragraph, int number) {
	        for (int i = 0; i < number; i++) {
	            paragraph.add(new Paragraph(" "));
	        }
	    }
	

}
