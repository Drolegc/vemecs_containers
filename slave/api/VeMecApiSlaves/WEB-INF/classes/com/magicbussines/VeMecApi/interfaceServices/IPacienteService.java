package com.magicbussines.VeMecApi.interfaceServices;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.magicbussines.VeMecApi.models.Paciente;

public interface IPacienteService {

		public List<Paciente> listar();
		public Page<Paciente> listarPaged(int page, int sizeOf);
		public Optional<Paciente> obtener(String docuId);
		public Paciente save(Paciente p);
		public void delete(String docuId);
		public Optional<Paciente> obtenerPacientePorIdVemec(String idVemec);
		
}
