package com.test.model;

public class CiServiceManager {


	@Override
	public String toString() {
		return  uid +"\t"+nombre + "\t" + nuevaUcmdb + "\t" + serviceCode
				+ "\t" + subtipo + "\t" + administracion + "\t" + status + "\t"
				+ titulo + "\t" + comentario + "\t" + location + "\t"
				+ fechaModificacion;
	}
	private Integer uid;
	private String nombre;
	private String nuevaUcmdb;
	private String serviceCode;
	private String subtipo;
	private String administracion;
	private String status;
	private String titulo;
	private String comentario;
	private String location;
	private String fechaModificacion;

	public CiServiceManager() {
		//llenando por defecto con valores vacio, para que no sean null, se asignaran atributos mientras se le el excel
		this.uid=null;
		this.nombre="";
		this.nuevaUcmdb="";
		this.serviceCode="";
		this.subtipo="";
		this.administracion="";
		this.status="";
		this.titulo="";
		this.comentario="";
		this.location="";
		this.fechaModificacion="";

	}
	public int getUid() {
		return uid;
	}


	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNuevaUcmdb() {
		return nuevaUcmdb;
	}

	public void setNuevaUcmdb(String nuevaUcmdb) {
		this.nuevaUcmdb = nuevaUcmdb;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getSubtipo() {
		return subtipo;
	}

	public void setSubtipo(String subtipo) {
		this.subtipo = subtipo;
	}

	public String getAdministracion() {
		return administracion;
	}

	public void setAdministracion(String administracion) {
		this.administracion = administracion;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario.replaceAll("\u0009", "");
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
}

