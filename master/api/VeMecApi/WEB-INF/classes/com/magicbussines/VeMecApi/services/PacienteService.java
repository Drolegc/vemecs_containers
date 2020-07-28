package com.magicbussines.VeMecApi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.magicbussines.VeMecApi.interfaceServices.IPacienteService;
import com.magicbussines.VeMecApi.interfaces.IPaciente;
import com.magicbussines.VeMecApi.models.Historial;
import com.magicbussines.VeMecApi.models.Paciente;
@Service
public class PacienteService implements IPacienteService{
	
	@Autowired
	private IPaciente data;
	
	@Override
	public List<Paciente> listar() {
		return (List<Paciente>) data.findAll();
	}
	
	
	@Override
	public Optional<Paciente> obtener(String docuId) {
		return data.findById(docuId);
	}

	
	@Override
	public Paciente save(Paciente p) {
		Paciente user = data.save(p);
		return user;
	}

	@Override
	public void delete(String docuId) {
		data.deleteById(docuId);		
	}
	

	@Override
	public Page<Paciente> listarPaged(int page, int sizeOf) {
		// TODO Auto-generated method stub
		Pageable paging = PageRequest.of(page, sizeOf);
		return data.findAll(paging);
	}


	@Override
	public Optional<Paciente> obtenerPacientePorIdVemec(String idVemec) {
		// TODO Auto-generated method stub
		return data.findByIdVemec(idVemec);
	}
	
}
