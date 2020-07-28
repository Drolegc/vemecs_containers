package com.magicbussines.VeMecApi.interfaceServices;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
	public List<ArrayList<Double>> historialRanged(String documentoId, LocalDateTime inicio,LocalDateTime fin);
	public void deleteAllForDocumento(String documentoId);
}
