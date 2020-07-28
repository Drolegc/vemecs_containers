package com.magicbussines.VeMecApi.models;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Ficha {
	
	@Id
	@GeneratedValue
	private int id;
	
	private String antecedentes;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale="America/Montevideo")
	private LocalDateTime timestamp;
	@ManyToOne
	@JoinColumn(name="medico_id")
	private Medico medicoTratante;
	private String datosFamiliares;
	private String motivos;
	private String nivelRiesgo;
	private String medicacion;
	@ManyToOne
	@JoinColumn(name="vemec_id")
	private VeMec vemec;
	
	@ManyToOne
	@JoinColumn(name="paciente_id")
	private Paciente paciente;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAntecedentes() {
		return antecedentes;
	}

	public void setAntecedentes(String antecedentes) {
		this.antecedentes = antecedentes;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Medico getMedicoTratante() {
		return medicoTratante;
	}

	public void setMedicoTratante(Medico medicoTratante) {
		this.medicoTratante = medicoTratante;
	}

	public String getDatosFamiliares() {
		return datosFamiliares;
	}

	public void setDatosFamiliares(String datosFamiliares) {
		this.datosFamiliares = datosFamiliares;
	}

	public String getMotivos() {
		return motivos;
	}

	public void setMotivos(String motivos) {
		this.motivos = motivos;
	}

	public String getNivelRiesgo() {
		return nivelRiesgo;
	}

	public void setNivelRiesgo(String nivelRiesgo) {
		this.nivelRiesgo = nivelRiesgo;
	}

	public String getMedicacion() {
		return medicacion;
	}

	public void setMedicacion(String medicacion) {
		this.medicacion = medicacion;
	}

	public VeMec getVemec() {
		return vemec;
	}

	public void setVemec(VeMec vemec) {
		this.vemec = vemec;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Ficha(int id, String antecedentes, LocalDateTime timestamp, Medico medicoTratante, String datosFamiliares,
			String motivos, String nivelRiesgo, String medicacion, VeMec vemec, Paciente paciente) {
		super();
		this.id = id;
		this.antecedentes = antecedentes;
		this.timestamp = timestamp;
		this.medicoTratante = medicoTratante;
		this.datosFamiliares = datosFamiliares;
		this.motivos = motivos;
		this.nivelRiesgo = nivelRiesgo;
		this.medicacion = medicacion;
		this.vemec = vemec;
		this.paciente = paciente;
	}

	public Ficha() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
