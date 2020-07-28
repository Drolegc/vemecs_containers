package com.magicbussines.VeMecApi.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.magicbussines.VeMecApi.interfaceServices.IVeMecService;
import com.magicbussines.VeMecApi.models.VeMec;

@RestController
@CrossOrigin
@RequestMapping("/vemecs")
public class VeMecController {
	@Autowired
	private IVeMecService vemecService;
	
	@GetMapping("/")
	public List<VeMec> index(){
		return vemecService.listar();
	}
	//localhost:8080/vemec/buscarPor?id=#####
	
	@GetMapping("/{id}")
		public ResponseEntity<Object> vemec(@PathVariable String id) {
			Optional<VeMec> vemec = vemecService.obtener(id);
			if(!vemec.isPresent()) {
				return new ResponseEntity<Object>("No se encontro vemec con el identificador recibido", HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<Object>(vemec,HttpStatus.OK);
		}
	
	@GetMapping("/{page}/{size}")
	public ResponseEntity<Object> vemecPaged(@PathVariable int page, @PathVariable int size) {
		Page<VeMec> vemec = vemecService.listarPaged(page, size);
		if(vemec.isEmpty()) {
			return new ResponseEntity<Object>("No tenemos VeMec en nuestro sistema", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Object>(vemec,HttpStatus.OK);
	}
	
	// http://localhost:8080/paciente/agregar se le envia un json
	@PostMapping("/")
	public ResponseEntity<VeMec> saveUser(@Valid @RequestBody VeMec vemec) {
		VeMec vemecSaved = vemecService.save(vemec);
		return new ResponseEntity<VeMec>(vemecSaved, HttpStatus.OK);
	}
	

	// http://localhost:8080/borrar?documentoId=#####
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> borrar(@PathVariable(value = "id") String id) {
		Optional<VeMec> vemec = vemecService.obtener(id);
		if (vemec != null) {
			try {
				vemecService.delete(id);
				return new ResponseEntity<Object>(vemec, HttpStatus.OK);
			} catch (Exception e) {
				// TODO: handle exception
				return new ResponseEntity<Object>("Hay historiales asociados al VeMec con Id: "+id, HttpStatus.UNPROCESSABLE_ENTITY);
			}
		} else {
			return new ResponseEntity<Object>("Cant find Paciente for CI", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
