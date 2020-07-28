package com.magicbussines.VeMecApi.models;


import java.sql.Date;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="paciente")
 
public class Paciente extends Persona{

	private String historiaClinica;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale="America/Montevideo")
	@Column
	private LocalDateTime fechaInternacion;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale="America/Montevideo")
	@Column
	private LocalDateTime fechaAlta;
	
	@ManyToOne
	@JoinColumn(name="vemec_id")
	private VeMec vemec;
	
	@ManyToOne
	@JoinColumn(name="medico_id")
	private Medico medicoTratante;
	
	private String lugarResidencia;
		
	public String getLugarResidencia() {
		return lugarResidencia;
	}
	public void setLugarResidencia(String lugarResidencia) {
		this.lugarResidencia = lugarResidencia;
	}
	public LocalDateTime getFechaInternacion() {
		return fechaInternacion;
	}
	public void setFechaInternacion(LocalDateTime fechaInternacion) {
		this.fechaInternacion = fechaInternacion;
	}
	public LocalDateTime getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(LocalDateTime fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public VeMec getVemec() {
		return vemec;
	}
	public void setVemec(VeMec vemec) {
		this.vemec = vemec;
	}
	
	public String getHistoriaClinica() {
		return historiaClinica;
	}
	public void setHistoriaClinica(String historiaClinica) {
		this.historiaClinica = historiaClinica;
	}
			
	public Medico getMedicoTratante() {
		return medicoTratante;
	}
	public void setMedicoTratante(Medico medicoTratante) {
		this.medicoTratante = medicoTratante;
	}
	public Paciente() {
		// TODO Auto-generated constructor stub
	}
	
}
