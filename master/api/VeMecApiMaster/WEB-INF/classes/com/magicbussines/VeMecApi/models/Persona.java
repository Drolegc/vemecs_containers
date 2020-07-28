package com.magicbussines.VeMecApi.models;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Persona { 
	
	@Id
	protected String documento_id;
	protected String nacionalidad;
	protected String sexo;
	protected String telefonos;
	protected String nombre;
	protected String apellido;
	protected int edad;
	protected String mail;
	public String getDocumento_id() {
		return documento_id;
	}
	public void setDocumento_id(String documento_id) {
		this.documento_id = documento_id;
	}
	public String getNacionalidad() {
		return nacionalidad;
	}
	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getTelefonos() {
		return telefonos;
	}
	public void setTelefonos(String telefonos) {
		this.telefonos = telefonos;
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
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public Persona(String documento_id, String nacionalidad, String sexo, String telefonos, String nombre,
			String apellido, int edad, String mail) {
		super();
		this.documento_id = documento_id;
		this.nacionalidad = nacionalidad;
		this.sexo = sexo;
		this.telefonos = telefonos;
		this.nombre = nombre;
		this.apellido = apellido;
		this.edad = edad;
		this.mail = mail;
	}
	public Persona() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
