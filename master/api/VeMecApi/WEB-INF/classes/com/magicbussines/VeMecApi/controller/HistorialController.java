package com.magicbussines.VeMecApi.controller;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.ZoneIdConverter;
import org.springframework.http.HttpStatus;
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
import com.magicbussines.VeMecApi.interfaceServices.IHistorialService;
import com.magicbussines.VeMecApi.interfaceServices.IPacienteService;
import com.magicbussines.VeMecApi.interfaceServices.IVeMecService;
import com.magicbussines.VeMecApi.models.Historial;
import com.magicbussines.VeMecApi.models.Paciente;
import com.magicbussines.VeMecApi.models.VeMec;

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

}
