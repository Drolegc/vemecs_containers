package com.magicbussines.VeMecApi.interfaceServices;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.magicbussines.VeMecApi.models.Ficha;
import com.magicbussines.VeMecApi.models.Historial;

public interface IFichaService {
	public List<Ficha> listar();
	public List<Ficha> listarId(String documentoId);
	public Ficha save(Ficha his);
	public void delete(int id);
	public Page<Ficha> fichaPaginated(String documentoId, int page, int sizeOf);
	public void deleteAllForDocumento(String documentoId);
	public List<Ficha> fichaRanged(String documentoId, LocalDateTime inicio, LocalDateTime fin);

}
