package model;

import java.util.List;

public class CiFirewall {
	private Integer uid=null;
	private String administracion="";
	private String dispositivo="";
	private String tipoDispositivo="";
	private String fabricante="";
	private String nombre="";
	private String cliente="";
	private String ipGestion="";
	private String iPFormateada="";
	private String codServicio="";
	private String modelo="";
	private String serial="";
	private String failOver="";
	private String versionFirmware="";
	private String datacenter="";
	private String bunker="";
	private String nodo="";
	private String piso="";
	private String rack="";
	private String unidad="";
	private String ips="";
	private String tipo="";
	private String ObservacionesRevision="";

	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getObservacionesRevision() {
		return ObservacionesRevision;
	}
	public void setObservacionesRevision(String observacionesRevision) {
		ObservacionesRevision = observacionesRevision;
	}
	public String getAdministracion() {
		return administracion;
	}
	public void setAdministracion(String administracion) {
		this.administracion = administracion;
	}
	public String getDispositivo() {
		return dispositivo;
	}
	public void setDispositivo(String dispositivo) {
		this.dispositivo = dispositivo;
	}
	public String getTipoDispositivo() {
		return tipoDispositivo;
	}
	public void setTipoDispositivo(String tipoDispositivo) {
		this.tipoDispositivo = tipoDispositivo.replaceAll("\\n", " ");
	}
	public String getFabricante() {
		return fabricante;
	}
	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre.replaceAll("\\n", " ");
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getIpGestion() {
		return ipGestion;
	}
	public void setIpGestion(String ipGestion) {
		this.ipGestion = ipGestion.replaceAll("\\n", " ");
	}
	public String getIPFormateada() {
		return iPFormateada;
	}
	public void setIPFormateada(String setIPFormateada) {
		this.iPFormateada = setIPFormateada;
	}
	public String getCodServicio() {
		return codServicio;
	}
	public void setCodServicio(String codServicio) {
		codServicio=codServicio.replaceAll("\\s","").replaceAll(" ", "").replaceAll(" ", "");
		this.codServicio = codServicio;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo.replaceAll("\\n", " ");
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		serial=serial.replaceAll("\\n", " ").replaceAll(" ", "");
		this.serial = serial;
	}
	public String getFailOver() {
		return failOver;
	}
	public void setFailOver(String failOver) {
		this.failOver = failOver.replaceAll("\r","").replaceAll("\n","");
	}
	public String getVersionFirmware() {
		return versionFirmware;
	}
	public void setVersionFirmware(String versionFirmware) {
		this.versionFirmware = versionFirmware;
	}
	public String getDatacenter() {
		return datacenter;
	}
	public void setDatacenter(String datacenter) {
		this.datacenter = datacenter.replaceAll("\r","").replaceAll("\n","");
	}
	public String getBunker() {
		return bunker;
	}
	public void setBunker(String bunker) {
		this.bunker = bunker;
	}
	public String getNodo() {
		return nodo;
	}
	public void setNodo(String nodo) {
		this.nodo = nodo;
	}
	public String getPiso() {
		return piso;
	}
	public void setPiso(String piso) {
		this.piso = piso;
	}
	public String getRack() {
		return rack;
	}
	public void setRack(String rack) {
		this.rack = rack;
	}
	public String getUnidad() {
		return unidad;
	}
	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}
	public String getIps() {
		return ips;
	}
	public void setIps(String ips) {
		this.ips = ips;
	}
	public String getTipo() {
		return tipo;
	}
	@Override
	public String toString() {
		return uid+"\t"+administracion + "\t" + dispositivo +"\t"
				+ tipoDispositivo + "\t" + fabricante + "\t" + nombre + "\t" + cliente
				+ "\t" + ipGestion + "\t" + iPFormateada + "\t" + codServicio
				+ "\t" + modelo + "\t" + serial + "\t" + failOver + "\t"
				+ versionFirmware + "\t" + datacenter + "\t"	
				/*+ bunker + "\t" + nodo +"\t"
				+ piso + "\t" + rack + "\t" + unidad + "\t" + ips +"\t" + tipo*/
				+tipo+ "\t" + ObservacionesRevision;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public void setHeader(List<String>header) {
		
		
		//crea un objeto con los valores de la columna
	}

}
