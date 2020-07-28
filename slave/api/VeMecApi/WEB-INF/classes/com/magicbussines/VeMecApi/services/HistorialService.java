package com.magicbussines.VeMecApi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.magicbussines.VeMecApi.interfaceServices.IHistorialService;
import com.magicbussines.VeMecApi.interfaces.IHistorial;
import com.magicbussines.VeMecApi.interfaces.IPaciente;
import com.magicbussines.VeMecApi.models.Historial;
import com.magicbussines.VeMecApi.models.Paciente;
@Service
public class HistorialService implements IHistorialService {

	@Autowired
	private IHistorial data;
	
	@Override
	public List<Historial> listar() {
		// TODO Auto-generated method stub
		return (List<Historial>) data.findAll();
	}

	@Override
	public Optional<Historial> listarId(int id) {
		// TODO Auto-generated method stub
		return data.findById(id);
	}
	
	@Override
	public Historial save(Historial historial) {
		Historial nuevoHistorial = data.save(historial);
		return nuevoHistorial;
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		data.deleteById(id);
	}

	@Override
	public List<Historial> historialByIdPaciente(String documentoId) {
		// TODO Auto-generated method stub
		return data.findByIdPaciente(documentoId);
	}

	@Override
	public void deleteAllForDocumento(String documentoId) {
		// TODO Auto-generated method stub
		data.deleteAll(data.findByIdPaciente(documentoId));
	}

	@Override
	public Page<Historial> historialPaginated(String documentoId, int page, int sizeOf) {
		// TODO Auto-generated method stub
		Pageable paging = PageRequest.of(page, sizeOf);
		return data.findByIdPacientePage(documentoId, paging);
		
	}

	
}
