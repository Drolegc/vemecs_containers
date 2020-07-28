package com.magicbussines.VeMecApi.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="vemec")
public class VeMec {

	@Id
	private String id;
	
	private String marca;
	private String modelo;
	private String sectorHospitalario;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public VeMec(String id) {
		super();
		this.id = id;
	}
	
	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getSectorHospitalario() {
		return sectorHospitalario;
	}

	public void setSectorHospitalario(String sectorHospitalario) {
		this.sectorHospitalario = sectorHospitalario;
	}

	public VeMec(String id, String marca, String modelo, String sectorHospitalario) {
		super();
		this.id = id;
		this.marca = marca;
		this.modelo = modelo;
		this.sectorHospitalario = sectorHospitalario;
	}

	public VeMec() {
		
	}
	
}
