package com.magicbussines.VeMecApi.models;


import java.sql.Date;

import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="paciente")
 
public class Paciente {
		
	@Id
	private String documento_id;
	private String nombre;
	private String apellido;
	private int edad;
	private Date fechaInternacion;
	private Date fechaAlta;
	@ManyToOne
	@JoinColumn(name="vemec_id")
	private VeMec vemec;
	public String getDocumento_id() {
		return documento_id;
	}
	public void setDocumento_id(String documento_id) {
		this.documento_id = documento_id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	public Date getFechaInternacion() {
		return fechaInternacion;
	}
	public void setFechaInternacion(Date fechaInternacion) {
		this.fechaInternacion = fechaInternacion;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public VeMec getVemec() {
		return vemec;
	}
	public void setVemec(VeMec vemec) {
		this.vemec = vemec;
	}
	public Paciente(String documento_id, String nombre, String apellido, int edad, Date fechaInternacion,
			Date fechaAlta, VeMec vemec) {
		super();
		this.documento_id = documento_id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.edad = edad;
		this.fechaInternacion = fechaInternacion;
		this.fechaAlta = fechaAlta;
		this.vemec = vemec;
	}
		
	public Paciente() {
		// TODO Auto-generated constructor stub
	}
}
