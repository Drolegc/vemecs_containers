package com.magicbussines.VeMecApi.interfaceServices;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.magicbussines.VeMecApi.models.Medico;

public interface IMedicoService {

		public List<Medico> listar();
		public Page<Medico> listarPaged(int page, int sizeOf);
		public Optional<Medico> obtener(String docuId);
		public Medico save(Medico p);
		public void delete(String docuId);
		//public Optional<Medico> obtenerPacientePorIdVemec(String idVemec);
		
}
