package com.magicbussines.VeMecApi.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.magicbussines.VeMecApi.models.Historial;
import com.magicbussines.VeMecApi.models.Medico;
@Repository
public interface IMedico extends PagingAndSortingRepository<Medico, String> {

	//@Query(nativeQuery = true, value = "select * FROM paciente where vemec_id = ?1 and isnull(fecha_alta)")
	//Optional<Medico>findByIdVemec(String idVemec);
}
