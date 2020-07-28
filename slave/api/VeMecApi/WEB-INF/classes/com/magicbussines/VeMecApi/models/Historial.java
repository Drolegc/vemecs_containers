package com.magicbussines.VeMecApi.models;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.TimeZone;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.convert.JodaTimeConverters.DateToLocalDateTimeConverter;
import org.springframework.data.convert.ThreeTenBackPortConverters.LocalDateTimeToDateConverter;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers.DateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.*;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Entity
@Table(name="historial")
public class Historial {
	
	@Id
	@GeneratedValue
	private int id;
	
	@OneToOne
	@JoinColumn(name="paciente_id")
	private Paciente paciente;
	

	//todos los otros datos 
	private float presionMin;
	private float presionMax;
	private float volGas;
	private int freqAporte;
	private float porcenMezclaO2;
	private float humedadAire;
	private float tempEntradaAire;
	private float tempSalidaAire;
	private float presionEntradaAire;
	private float presionSalidaAire;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale="America/Montevideo")
	private LocalDateTime timestamp;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Paciente getPaciente() {
		return paciente;
	}
	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public float getPresionMin() {
		return presionMin;
	}
	public void setPresionMin(float presionMin) {
		this.presionMin = presionMin;
	}
	public float getPresionMax() {
		return presionMax;
	}
	public void setPresionMax(float presionMax) {
		this.presionMax = presionMax;
	}
	public float getVolGas() {
		return volGas;
	}
	public void setVolGas(float volGas) {
		this.volGas = volGas;
	}
	public int getFreqAporte() {
		return freqAporte;
	}
	public void setFreqAporte(int freqAporte) {
		this.freqAporte = freqAporte;
	}
	public float getPorcenMezclaO2() {
		return porcenMezclaO2;
	}
	public void setPorcenMezclaO2(float porcenMezclaO2) {
		this.porcenMezclaO2 = porcenMezclaO2;
	}
	public float getHumedadAire() {
		return humedadAire;
	}
	public void setHumedadAire(float humedadAire) {
		this.humedadAire = humedadAire;
	}
	public float getTempEntradaAire() {
		return tempEntradaAire;
	}
	public void setTempEntradaAire(float tempEntradaAire) {
		this.tempEntradaAire = tempEntradaAire;
	}
	public float getTempSalidaAire() {
		return tempSalidaAire;
	}
	public void setTempSalidaAire(float tempSalidaAire) {
		this.tempSalidaAire = tempSalidaAire;
	}
	public float getPresionEntradaAire() {
		return presionEntradaAire;
	}
	public void setPresionEntradaAire(float presionEntradaAire) {
		this.presionEntradaAire = presionEntradaAire;
	}
	public float getPresionSalidaAire() {
		return presionSalidaAire;
	}
	public void setPresionSalidaAire(float presionSalidaAire) {
		this.presionSalidaAire = presionSalidaAire;
	}
	
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setDate(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public Historial(int id, Paciente paciente, float presionMin, float presionMax, float volGas,
			int freqAporte, float porcenMezclaO2, float humedadAire, float tempEntradaAire, float tempSalidaAire,
			float presionEntradaAire, float presionSalidaAire, LocalDateTime timestamp) {
		super();
		this.id = id;
		this.paciente = paciente;
		this.presionMin = presionMin;
		this.presionMax = presionMax;
		this.volGas = volGas;
		this.freqAporte = freqAporte;
		this.porcenMezclaO2 = porcenMezclaO2;
		this.humedadAire = humedadAire;
		this.tempEntradaAire = tempEntradaAire;
		this.tempSalidaAire = tempSalidaAire;
		this.presionEntradaAire = presionEntradaAire;
		this.presionSalidaAire = presionSalidaAire;
		this.timestamp = timestamp;
	}
	
	public Historial() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
