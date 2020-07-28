package com.magicbussines.VeMecApi.interfaceServices;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.magicbussines.VeMecApi.models.Historial;

public interface IHistorialService {
	public List<Historial> listar();
	public Optional<Historial>listarId(int id);
	public Historial save(Historial his);
	public void delete(int id);
	public List<Historial> historialByIdPaciente(String documentoId);
	public Page<Historial> historialPaginated(String documentoId, int page, int sizeOf);
	public void deleteAllForDocumento(String documentoId);
}
