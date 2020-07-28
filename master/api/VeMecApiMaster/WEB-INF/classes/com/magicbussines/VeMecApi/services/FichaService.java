package com.magicbussines.VeMecApi.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itextpdf.text.log.SysoCounter;
import com.magicbussines.VeMecApi.interfaceServices.IFichaService;
import com.magicbussines.VeMecApi.interfaces.IFicha;
import com.magicbussines.VeMecApi.models.Ficha;
@Service
public class FichaService implements IFichaService {

	@Autowired
	private IFicha data;
	@Override
	public List<Ficha> listar() {
		// TODO Auto-generated method stub
		return (List<Ficha>) data.findAll();
	}

	
	@Override
	public List<Ficha> listarId(String documentoId) {
		// TODO Auto-generated method stub
		return data.findAllByDoc(documentoId);
	}

	@Override
	public Ficha save(Ficha his) {
		// TODO Auto-generated method stub
		Ficha fichita = data.save(his);
		return fichita;
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		data.deleteById(id);
	}

	@Override
	public Page<Ficha> fichaPaginated(String documentoId, int page, int sizeOf) {
		// TODO Auto-generated method stub
		Pageable paging = PageRequest.of(page, sizeOf);
		return data.findByIdPacientePage(documentoId, paging);
	}

	@Override
	public void deleteAllForDocumento(String documentoId) {
		// TODO Auto-generated method stub
		data.deleteAll(data.findAllByDoc(documentoId));
		
	}


	@Override
	public List<Ficha> fichaRanged(String documentoId, LocalDateTime inicio, LocalDateTime fin) {
		// TODO Auto-generated method stub
		List<Ficha> listaReturn = new ArrayList<Ficha>();
		List<Ficha> listaOriginal = listarId(documentoId);
		
		for (Ficha ficha : listaOriginal ) {
			
			//2020-05-21
			LocalDate timestampF = ficha.getTimestamp().toLocalDate();
			LocalTime timestampT = ficha.getTimestamp().toLocalTime();
			
			//timestamppF es el de cada row
			if ((timestampF.equals(inicio.toLocalDate())) || (timestampF.equals(fin.toLocalDate()))){
				//o es el mismo dia de los limites
				if( (timestampT.equals(inicio.toLocalTime())) || (timestampT.equals(fin.toLocalTime())) ) {
					listaReturn.add(ficha);
				}else if( (timestampT.isAfter(inicio.toLocalTime())) && (timestampT.isBefore(fin.toLocalTime())) ) {
					listaReturn.add(ficha);
				}

			}else if ((timestampF.isAfter(inicio.toLocalDate())) && (timestampF.isBefore(fin.toLocalDate()))){
				// o es esta dentro de los limites
				if( (timestampT.equals(inicio.toLocalTime())) || (timestampT.equals(fin.toLocalTime())) ) {
					listaReturn.add(ficha);
				}else if( (timestampT.isAfter(inicio.toLocalTime())) && (timestampT.isBefore(fin.toLocalTime())) ) {
					listaReturn.add(ficha);
				}
				
			}
						
		}
		
		System.out.println(listaReturn.size());
		return listaReturn;
	}
}
