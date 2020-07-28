package com.magicbussines.VeMecApi.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.magicbussines.VeMecApi.interfaceServices.IHistorialService;
import com.magicbussines.VeMecApi.interfaces.IHistorial;
import com.magicbussines.VeMecApi.interfaces.IPaciente;
import com.magicbussines.VeMecApi.models.Historial;
import com.magicbussines.VeMecApi.models.Paciente;
@Service
public class HistorialService implements IHistorialService {

	@Autowired
	private IHistorial data;
	
	@Override
	public List<Historial> listar() {
		// TODO Auto-generated method stub
		return (List<Historial>) data.findAll();
	}

	@Override
	public Optional<Historial> listarId(int id) {
		// TODO Auto-generated method stub
		return data.findById(id);
	}
	
	@Override
	public Historial save(Historial historial) {
		Historial nuevoHistorial = data.save(historial);
		return nuevoHistorial;
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		data.deleteById(id);
	}

	@Override
	public List<Historial> historialByIdPaciente(String documentoId) {
		// TODO Auto-generated method stub
		return data.findByIdPaciente(documentoId);
	}

	@Override
	public void deleteAllForDocumento(String documentoId) {
		// TODO Auto-generated method stub
		data.deleteAll(data.findByIdPaciente(documentoId));
	}

	@Override
	public Page<Historial> historialPaginated(String documentoId, int page, int sizeOf) {
		// TODO Auto-generated method stub
		Pageable paging = PageRequest.of(page, sizeOf);
		return data.findByIdPacientePage(documentoId, paging);
		
	}

	@Override
	public List<ArrayList<Double>> historialRanged(String documentoId, LocalDateTime inicio, LocalDateTime fin) {
		// TODO Auto-generated method stub
		
		
		List<Historial> listita = data.findByIdPaciente(documentoId);
		
		//List<Historial> listaReturn = new ArrayList<Historial>();
		//Creo un array de arrays de tipo double (incluye a los int y float)
		List<ArrayList<Double>> listaRetorno = new ArrayList<>();
		
		//Creacion de listas particulares
		List<Double> presionMin = new ArrayList<>();
		List<Double> presionMax = new ArrayList<>();
		List<Double> volGas = new ArrayList<>();
		List<Double> freqAporte = new ArrayList<>();
		List<Double> porcenMezclaO2 = new ArrayList<>();
		List<Double> humedadAire = new ArrayList<>();
		List<Double> pulsaciones = new ArrayList<>();
		List<Double> tempEntradaAire = new ArrayList<>();
		List<Double> tempSalidaAire = new ArrayList<>();
		List<Double> presionEntradaAire = new ArrayList<>();
		List<Double> presionSalidaAire = new ArrayList<>();
		
		for (Historial historial : listita) {			
			
			//2020-05-21
			LocalDate timestampF = historial.getTimestamp().toLocalDate();
			LocalTime timestampT = historial.getTimestamp().toLocalTime();
			
			//timestamppF es el de cada row
			System.out.println(timestampF.toString());
			System.out.println(">>"+inicio.toLocalDate());
			System.out.println(">>>>"+fin.toLocalDate());
			if (timestampF.equals(fin.toLocalDate())) {
				System.out.println("OOOOOOOOOOK");
			}
			if (timestampF.equals(inicio.toLocalDate()) || timestampF.equals(fin.toLocalDate())){
				//o es el mismo dia de los limites
				System.out.println("FECHA IGUAL");
				if( (timestampT.equals(inicio.toLocalTime())) || (timestampT.equals(fin.toLocalTime())) ) {
					
					presionMin.add((double) historial.getPresionMin());
					presionMax.add((double) historial.getPresionMax());
					volGas.add((double) historial.getVolGas());
					freqAporte.add((double) historial.getFreqAporte());
					porcenMezclaO2.add((double) historial.getPorcenMezclaO2());
					humedadAire.add((double) historial.getHumedadAire());
					pulsaciones.add((double) historial.getPulsaciones());
					tempEntradaAire.add((double) historial.getTempEntradaAire());
					tempSalidaAire.add((double) historial.getTempSalidaAire());
					presionEntradaAire.add((double) historial.getPresionEntradaAire());
					presionSalidaAire.add((double) historial.getPresionSalidaAire());
					
				}else if( (timestampT.isAfter(inicio.toLocalTime())) && (timestampT.isBefore(fin.toLocalTime())) ) {
					
					presionMin.add((double) historial.getPresionMin());
					presionMax.add((double) historial.getPresionMax());
					volGas.add((double) historial.getVolGas());
					freqAporte.add((double) historial.getFreqAporte());
					porcenMezclaO2.add((double) historial.getPorcenMezclaO2());
					humedadAire.add((double) historial.getHumedadAire());
					pulsaciones.add((double) historial.getPulsaciones());
					tempEntradaAire.add((double) historial.getTempEntradaAire());
					tempSalidaAire.add((double) historial.getTempSalidaAire());
					presionEntradaAire.add((double) historial.getPresionEntradaAire());
					presionSalidaAire.add((double) historial.getPresionSalidaAire());
				}
				
			}else if ((timestampF.isAfter(inicio.toLocalDate())) && (timestampF.isBefore(fin.toLocalDate()))){
				System.out.println("FECHA ENTRE");
				if( (timestampT.equals(inicio.toLocalTime())) || (timestampT.equals(fin.toLocalTime())) ) {
					presionMin.add((double) historial.getPresionMin());
					presionMax.add((double) historial.getPresionMax());
					volGas.add((double) historial.getVolGas());
					freqAporte.add((double) historial.getFreqAporte());
					porcenMezclaO2.add((double) historial.getPorcenMezclaO2());
					humedadAire.add((double) historial.getHumedadAire());
					pulsaciones.add((double) historial.getPulsaciones());
					tempEntradaAire.add((double) historial.getTempEntradaAire());
					tempSalidaAire.add((double) historial.getTempSalidaAire());
					presionEntradaAire.add((double) historial.getPresionEntradaAire());
					presionSalidaAire.add((double) historial.getPresionSalidaAire());
				}else if( (timestampT.isAfter(inicio.toLocalTime())) && (timestampT.isBefore(fin.toLocalTime())) ) {
					System.out.println("conchitmare");
					presionMin.add((double) historial.getPresionMin());
					presionMax.add((double) historial.getPresionMax());
					volGas.add((double) historial.getVolGas());
					freqAporte.add((double) historial.getFreqAporte());
					porcenMezclaO2.add((double) historial.getPorcenMezclaO2());
					humedadAire.add((double) historial.getHumedadAire());
					pulsaciones.add((double) historial.getPulsaciones());
					tempEntradaAire.add((double) historial.getTempEntradaAire());
					tempSalidaAire.add((double) historial.getTempSalidaAire());
					presionEntradaAire.add((double) historial.getPresionEntradaAire());
					presionSalidaAire.add((double) historial.getPresionSalidaAire());
				}
				
			}
			
		}
	
		//0 to 10 : index
		listaRetorno.add((ArrayList<Double>) pulsaciones);
		listaRetorno.add((ArrayList<Double>) presionMin);
		listaRetorno.add((ArrayList<Double>) presionMax);
		listaRetorno.add((ArrayList<Double>) tempEntradaAire);
		listaRetorno.add((ArrayList<Double>) tempSalidaAire);
		listaRetorno.add((ArrayList<Double>) presionEntradaAire);
		listaRetorno.add((ArrayList<Double>) presionSalidaAire);
		listaRetorno.add((ArrayList<Double>) volGas);
		listaRetorno.add((ArrayList<Double>) humedadAire);
		listaRetorno.add((ArrayList<Double>) freqAporte);
		listaRetorno.add((ArrayList<Double>) porcenMezclaO2);
				
		System.out.println(listaRetorno.get(0).size());
		return listaRetorno;
	}

}
