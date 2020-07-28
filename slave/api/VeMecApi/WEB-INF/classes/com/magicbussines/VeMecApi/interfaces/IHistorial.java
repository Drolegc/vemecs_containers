package com.magicbussines.VeMecApi.interfaces;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.magicbussines.VeMecApi.models.Historial;
import com.magicbussines.VeMecApi.models.Paciente;

public interface IHistorial extends PagingAndSortingRepository<Historial, Integer> {
	
	
	@Query(nativeQuery = true, value = "select * FROM historial where paciente_id = ?1")
	List<Historial> findByIdPaciente(String documentoId);
	
	@Query(nativeQuery = true, value = "select * FROM historial where paciente_id = ?1")
	Page<Historial> findByIdPacientePage(String documentoId, org.springframework.data.domain.Pageable paging);
	
	@Query("FROM Paciente where documento_id = ?1 ")// ?1 es el primer parametro, de tener dos el segundo seria ?2
	Paciente findPaciente(Long documentioId);
	
}
