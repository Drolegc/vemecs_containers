package com.magicbussines.VeMecApi.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.magicbussines.VeMecApi.models.Ficha;
import com.magicbussines.VeMecApi.models.Historial;

public interface IFicha extends PagingAndSortingRepository<Ficha, Integer> {
	
	@Query(nativeQuery = true, value = "select * FROM ficha where paciente_id = ?1")
	List<Ficha> findAllByDoc(String documentoId);
	
	@Query(nativeQuery = true, value = "select * FROM ficha where paciente_id = ?1")
	Page<Ficha> findByIdPacientePage(String documentoId, org.springframework.data.domain.Pageable paging);
	
	
}
