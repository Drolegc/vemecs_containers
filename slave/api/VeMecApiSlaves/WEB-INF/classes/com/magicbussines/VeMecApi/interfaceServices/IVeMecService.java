
package com.magicbussines.VeMecApi.interfaceServices;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.magicbussines.VeMecApi.models.Paciente;
import com.magicbussines.VeMecApi.models.VeMec;

public interface IVeMecService {
	
	public List<VeMec> listar();
	public Optional<VeMec> obtener(String id);
	public Page<VeMec> listarPaged(int page, int sizeOf);
	public VeMec save(VeMec vm);
	public void delete(String id);

}
