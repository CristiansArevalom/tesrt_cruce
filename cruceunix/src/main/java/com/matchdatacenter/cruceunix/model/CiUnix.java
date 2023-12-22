package com.matchdatacenter.cruceunix.model;

import java.util.Objects;

public class CiUnix {
	private Integer numID;
	private String cliente;
	private String tipoPaso;
	private String admResponsable;
	private String codHostname;
	private String codigoHostname;
	private String codigoServicio;
	private String hostname;
	private String sistemaOperativo;
	private String verSO;
	private String tipo;
	private String ipGestion;
	private String ipGestionFormateada;
	private String ipProduccion;
	private String ipIlo;
	private String servicioRol;
	private String escalamientoApp;
	private String monitoreo;
	private String marcaModelo;
	private String serial;
	private String ubicacion;
	private String fechaRecepcion;
	private String fechaRetiro;
	private String estado;
	private String observaciones;
	private String created;
	private String path;
	private String itemType;
	private String observacionesRevision;
	private String aplicaUmcbd;
	

	public CiUnix() {
		super();
		this.cliente="";
		this.tipoPaso="";
		this.admResponsable="";
		this.codHostname="";
		this.codigoHostname="";
		this.codigoServicio="";
		this.hostname="";
		this.sistemaOperativo="";
		this.verSO="";
		this.tipo="";
		this.ipGestion="";
		this.ipGestionFormateada="";
		this.ipProduccion="";
		this.ipIlo="";
		this.servicioRol="";
		this.escalamientoApp="";
		this.monitoreo="";
		this.marcaModelo="";
		this.serial="";
		this.ubicacion="";
		this.fechaRecepcion="";
		this.fechaRetiro="";
		this.estado="";
		this.observaciones="";
		this.created="";
		this.path="";
		this.itemType="";
		this.observacionesRevision="";
		this.aplicaUmcbd="";

		
	}
	
	public int getNumID() {
		return numID;
	}
	public void setNumID(int numID) {
		this.numID = numID;
	}
	public String setCodigoHostname() {
		return this.codigoHostname = this.codigoServicio.toUpperCase()+"_"+this.hostname.toUpperCase().replaceAll(" ", "");
	}
	public String getCodigoHostname() {
		return this.codigoHostname;
	}
	
	/*
	@Override
	public String toString() {
		
		return this.codHostname.replaceAll("\u0009", "")+
				'\u0009'+this.codigoServicio.replaceAll("\u0009", "")+'\u0009'+this.hostname.replaceAll("\u0009", "")+'\u0009'+this.ipGestion.replaceAll("\u0009", "")+'\u0009'+
				this.estado + '\u0009'+this.tipo + '\u0009'+this.cliente + '\u0009'+this.observaciones.replaceAll("\n", "")+ '\u0009'+this.observacionesRevision;
	}*/
	

	
	
	public String getCliente() {
		return cliente;
	}



	public void setCliente(String cliente) {
		this.cliente = cliente;
	}



	public String getTipoPaso() {
		return tipoPaso;
	}



	public void setTipoPaso(String tipoPaso) {
		this.tipoPaso = tipoPaso;
	}



	public String getAdmResponsable() {
		return admResponsable;
	}



	public void setAdmResponsable(String admResponsable) {
		this.admResponsable = admResponsable;
	}



	public String getCodHostname() {
		return codHostname;
	}



	public void setCodHostname(String codHostname) {
		this.codHostname = codHostname;
	}



	public String getCodigoServicio() {
		return codigoServicio;
	}



	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}



	public String getHostname() {
		return hostname;
	}



	public void setHostname(String hostname) {
		this.hostname = hostname;
	}



	public String getSistemaOperativo() {
		return sistemaOperativo;
	}



	public void setSistemaOperativo(String sistemaOperativo) {
		this.sistemaOperativo = sistemaOperativo;
	}



	public String getVerSO() {
		return verSO;
	}



	public void setVerSO(String verSO) {
		this.verSO = verSO;
	}



	public String getTipo() {
		return tipo;
	}



	public void setTipo(String tipo) {
		this.tipo = tipo;
	}



	public String getIpGestion() {
		return ipGestion;
	}



	public void setIpGestion(String ipGestion) {
		this.ipGestion = ipGestion;
	}



	public String getIpProduccion() {
		return ipProduccion;
	}



	public void setIpProduccion(String ipProduccion) {
		this.ipProduccion = ipProduccion;
	}



	public String getIpIlo() {
		return ipIlo;
	}



	public void setIpIlo(String ipIlo) {
		this.ipIlo = ipIlo;
	}



	public String getServicioRol() {
		return servicioRol;
	}



	public void setServicioRol(String servicioRol) {
		this.servicioRol = servicioRol;
	}



	public String getEscalamientoApp() {
		return escalamientoApp;
	}



	public void setEscalamientoApp(String escalamientoApp) {
		this.escalamientoApp = escalamientoApp;
	}



	public String getMonitoreo() {
		return monitoreo;
	}



	public void setMonitoreo(String monitoreo) {
		this.monitoreo = monitoreo;
	}



	public String getMarcaModelo() {
		return marcaModelo;
	}



	public void setMarcaModelo(String marcaModelo) {
		this.marcaModelo = marcaModelo;
	}



	public String getSerial() {
		return serial;
	}



	public void setSerial(String serial) {
		this.serial = serial;
	}



	public String getUbicacion() {
		return ubicacion;
	}



	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}



	public String getFechaRecepcion() {
		return fechaRecepcion;
	}



	public void setFechaRecepcion(String fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}



	public String getFechaRetiro() {
		return fechaRetiro;
	}



	public void setFechaRetiro(String fechaRetiro) {
		this.fechaRetiro = fechaRetiro;
	}



	public String getEstado() {
		return estado;
	}



	public void setEstado(String estado) {
		this.estado = estado;
	}



	public String getObservaciones() {
		return observaciones;
	}



	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones.replaceAll("\n\r", " ");
	}



	public String getCreated() {
		return created;
	}



	public void setCreated(String created) {
		this.created = created;
	}



	public String getPath() {
		return path;
	}



	public void setPath(String path) {
		this.path = path;
	}


	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getObservacionesRevision() {
		return observacionesRevision;
	}
	public void setObservacionesRevision(String observacionesRevision) {
		this.observacionesRevision = observacionesRevision;
	}
	private String getIpGestionFormateada() {
		return ipGestionFormateada;
	}
	private void setIpGestionFormateada(String ipGestionFormateada) {
		this.ipGestionFormateada = ipGestionFormateada;
	}
	

	@Override
	public String toString() {
		String.format("txt&d", 2);
		return this.numID+"\t"+admResponsable+'\u0009'+cliente
				+ '\u0009' + codHostname.replaceAll("\u0009", "")+"\t"+codigoHostname+ '\u0009'
				+ codigoServicio.replaceAll("\u0009", "") + '\u0009' + hostname.replaceAll("\u0009", "") + '\u0009' + sistemaOperativo + '\u0009'
				+ verSO +'\u0009' + tipo + '\u0009' + ipGestion.replaceAll("\\u0009", "") +""
				+ '\u0009' + ipProduccion.replaceAll("\\u0009", "")+'\u0009' + ipIlo.replaceAll("\\u0009", "") + '\u0009'
				+ servicioRol
				+ '\u0009'+ escalamientoApp + '\u0009' + monitoreo + '\u0009' + marcaModelo
				+ '\u0009' + serial.replaceAll("\u0009", "")  + '\u0009' + ubicacion.replaceAll("\u0009", "") + '\u0009' + fechaRecepcion.replaceAll("\u0009", "")
				+ '\u0009' + fechaRetiro.replaceAll("\u0009", "") + '\u0009' + estado + '\u0009' + this.observaciones.replaceAll("\n", "")
				+ '\u0009' + created + '\u0009' + path + '\u0009' + itemType + '\u0009'
				+ observacionesRevision+"\t"+aplicaUmcbd;
	}
	public String getAplicaUmcbd() {
		return aplicaUmcbd;
	}
	public void setAplicaUmcbd(String aplicaUmcbd) {
		this.aplicaUmcbd = aplicaUmcbd;
	}

	@Override
	public int hashCode() {
		return Objects.hash(admResponsable, aplicaUmcbd, cliente, codHostname, codigoHostname, codigoServicio, created,
				escalamientoApp, estado, fechaRecepcion, fechaRetiro, hostname, ipGestion, ipGestionFormateada, ipIlo,
				ipProduccion, itemType, marcaModelo, monitoreo, numID, observaciones, observacionesRevision, path,
				serial, servicioRol, sistemaOperativo, tipo, tipoPaso, ubicacion, verSO);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CiUnix other = (CiUnix) obj;
		return Objects.equals(admResponsable, other.admResponsable) && Objects.equals(aplicaUmcbd, other.aplicaUmcbd)
				&& Objects.equals(cliente, other.cliente) && Objects.equals(codHostname, other.codHostname)
				&& Objects.equals(codigoHostname, other.codigoHostname)
				&& Objects.equals(codigoServicio, other.codigoServicio) && Objects.equals(created, other.created)
				&& Objects.equals(escalamientoApp, other.escalamientoApp) && Objects.equals(estado, other.estado)
				&& Objects.equals(fechaRecepcion, other.fechaRecepcion)
				&& Objects.equals(fechaRetiro, other.fechaRetiro) && Objects.equals(hostname, other.hostname)
				&& Objects.equals(ipGestion, other.ipGestion)
				&& Objects.equals(ipGestionFormateada, other.ipGestionFormateada) && Objects.equals(ipIlo, other.ipIlo)
				&& Objects.equals(ipProduccion, other.ipProduccion) && Objects.equals(itemType, other.itemType)
				&& Objects.equals(marcaModelo, other.marcaModelo) && Objects.equals(monitoreo, other.monitoreo)
				&& numID == other.numID && Objects.equals(observaciones, other.observaciones)
				&& Objects.equals(observacionesRevision, other.observacionesRevision)
				&& Objects.equals(path, other.path) && Objects.equals(serial, other.serial)
				&& Objects.equals(servicioRol, other.servicioRol)
				&& Objects.equals(sistemaOperativo, other.sistemaOperativo) && Objects.equals(tipo, other.tipo)
				&& Objects.equals(tipoPaso, other.tipoPaso) && Objects.equals(ubicacion, other.ubicacion)
				&& Objects.equals(verSO, other.verSO);
	}
	

}
