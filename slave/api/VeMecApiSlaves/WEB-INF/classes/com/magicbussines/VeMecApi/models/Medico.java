package com.magicbussines.VeMecApi.models;

import javax.persistence.Entity;

@Entity
public class Medico  extends Persona{
	private String especialidad;

	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}
	
}
