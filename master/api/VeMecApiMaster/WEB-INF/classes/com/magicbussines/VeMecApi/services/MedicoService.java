package com.magicbussines.VeMecApi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.magicbussines.VeMecApi.interfaceServices.IMedicoService;
import com.magicbussines.VeMecApi.interfaceServices.IPacienteService;
import com.magicbussines.VeMecApi.interfaces.IMedico;
import com.magicbussines.VeMecApi.interfaces.IPaciente;
import com.magicbussines.VeMecApi.models.Historial;
import com.magicbussines.VeMecApi.models.Medico;
import com.magicbussines.VeMecApi.models.Paciente;
@Service
public class MedicoService implements IMedicoService{
	
	@Autowired
	private IMedico data;

	@Override
	public List<Medico> listar() {
		// TODO Auto-generated method stub
		return (List<Medico>) data.findAll();
	}

	@Override
	public Page<Medico> listarPaged(int page, int sizeOf) {
		// TODO Auto-generated method stub
		Pageable paging = PageRequest.of(page, sizeOf);
		return data.findAll(paging);
	}

	@Override
	public Optional<Medico> obtener(String docuId) {
		// TODO Auto-generated method stub
		return data.findById(docuId);
	}

	@Override
	public Medico save(Medico p) {
		// TODO Auto-generated method stub
		Medico medic = data.save(p);
		return medic;
	}

	@Override
	public void delete(String docuId) {
		// TODO Auto-generated method stub
		data.deleteById(docuId);
	}
	
	
}
