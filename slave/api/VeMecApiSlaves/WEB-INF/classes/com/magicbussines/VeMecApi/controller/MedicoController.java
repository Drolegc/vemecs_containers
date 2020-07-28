package com.magicbussines.VeMecApi.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
import com.magicbussines.VeMecApi.interfaceServices.IMedicoService;
//import com.magicbussines.VeMecApi.interfaceServices.IPostService;
import com.magicbussines.VeMecApi.interfaceServices.IPacienteService;
import com.magicbussines.VeMecApi.interfaceServices.IVeMecService;
import com.magicbussines.VeMecApi.models.Historial;
import com.magicbussines.VeMecApi.models.Medico;
//import com.magicbussines.VeMecApi.models.Post;
import com.magicbussines.VeMecApi.models.Paciente;
import com.magicbussines.VeMecApi.models.VeMec;
import com.magicbussines.VeMecApi.services.VeMecService;

@RestController
@CrossOrigin
@RequestMapping("/medicos")
public class MedicoController {

	@Autowired
	private IMedicoService _medico;
	@Autowired
	private IVeMecService vemecService;
	@Autowired
	private ObjectMapper objectMapper;
	//http://localhost:8080/paciente/listar
	@GetMapping("/")
	public List<Medico> index(){
		return _medico.listar();
	}
	
//	//localhost:8080/paciente/buscarPor?documentoId=#####
//	@GetMapping("/{documento_id}")
//	public ResponseEntity<Object> paciente(@PathVariable String documento_id) {
//		Optional<Paciente> paciente = _medico.obtener(documento_id);
//		if(!paciente.isPresent()) {
//			return new ResponseEntity<Object>("No tenemos paciente para el documento "+documento_id, HttpStatus.NOT_FOUND);
//		}
//		return new ResponseEntity<Object>(paciente,HttpStatus.OK);
//	}
	
//	@GetMapping("/{vemec_id}")
//	public ResponseEntity<Object> pacienteVemec(@PathVariable String vemec_id) {
//		Optional<Paciente> paciente = _medico.(vemec_id);
//		if(!paciente.isPresent()) {
//			return new ResponseEntity<Object>("No tenemos paciente para el documento "+vemec_id, HttpStatus.NOT_FOUND);
//		}
//		return new ResponseEntity<Object>(paciente,HttpStatus.OK);
//	}
//	
	
	@GetMapping("/{page}/{size}")
	public ResponseEntity<Object> paciente(@PathVariable int page, @PathVariable int size) {
		Page<Medico> medicos = _medico.listarPaged(page, size);
		if(medicos.isEmpty()) {
			return new ResponseEntity<Object>("No tenemos pacientes en nuestro sistema", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Object>(medicos,HttpStatus.OK);
	}
	
	// http://localhost:8080/paciente/agregar se le envia un json
	@PostMapping("/")
	public ResponseEntity<Object> saveUser(@RequestBody Medico data) throws JsonParseException, JsonMappingException, IOException {
						

		Medico nuevoMedico = _medico.save(data);
		
		return new ResponseEntity<Object>(nuevoMedico,HttpStatus.CREATED);
		
	}

	// http://localhost:8080/borrar?documentoId=#####
	@DeleteMapping("/{documento_id}")
	public ResponseEntity<Object> borrar(@PathVariable String documento_id) {
		Optional<Medico> medic = _medico.obtener(documento_id);
		if (medic != null) {
			try {
				_medico.delete(documento_id);
				return new ResponseEntity<Object>(medic, HttpStatus.OK);
			} catch (Exception e) {
				// TODO: handle exception
				return new ResponseEntity<Object>("Hay historiales asociados al Paciente con documentoId: "+documento_id, HttpStatus.UNPROCESSABLE_ENTITY);
			}
		} else {
			return new ResponseEntity<Object>("Cant find Paciente for CI", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
