package com.test.model;
 
public class CiWindows {
	
	public static final String HEADER = "numId"+"\t"+"CodServExtraido"+"\t"+"IpGestionExtraido"+"\t"+"codHostname"+"+\t"+"cod servicio"+"\t"+"ipGestion"+"\t"+"estado"+"\t"+"tipo"+"\t"+"cliente"+"\t"+"observaciones"+"\t"+"observacionesRevision";
	
	private Integer numId;
	private String cliente;
	private String tipoPaso;
	private String admResponsable;
	private String adm_backup;
	private String codHostname;
	private String Hostname;
	private String so;
	private String codServicio;
	private String ipGestion;
	private String ubicacion;
	private String fechaRecepcion;
	private String fechaRetiro;
	private String estado;
	private String observaciones;
	private String tipo;
	private String itemType;
	private String path;
	private String observacionesRevision;
    private String codServicioExtraido;
    private String ipGestionExtraido;


	public CiWindows() {
		super();
		this.numId = null;
		this.cliente = "";
		this.tipoPaso = "";
		this.admResponsable = "";
		this.adm_backup = "";
		this.codHostname = "";
		this.Hostname = "";
		this.so = "";
		this.codServicio = "";
		this.codServicioExtraido = "";
		this.ipGestion = "";
		this.ubicacion = "";
		this.fechaRecepcion = "";
		this.fechaRetiro = "";
		this.estado = "";
		this.observaciones = "";
		this.tipo = "";
		this.itemType = "";
		this.path = "";
		this.observacionesRevision = "";
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

	public String getAdm_backup() {
		return adm_backup;
	}

	public void setAdm_backup(String adm_backup) {
		this.adm_backup = adm_backup;
	}

	public String getSo() {
		return so;
	}

	public void setSo(String so) {
		this.so = so;
	}

	public String getCodServicio() {
		return codServicio;
	}

	public void setCodServicio(String codServicio) {
		this.codServicio = codServicio;
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

	public String getCodHostname() {
		return codHostname;
	}

	public void setCodHostname(String codHostname) {
		this.codHostname = codHostname;
	}

	public String getIpGestion() {
		return ipGestion;
	}

	public void setIpGestion(String ipGestion) {
		this.ipGestion = ipGestion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String status) {
		this.estado = status;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getHostname() {
		return Hostname;
	}

	public void setHostname(String hostname) {
		Hostname = hostname;
	}
	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}


	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


	public String getObservacionesRevision() {
		return observacionesRevision;
	}

	public void setObservacionesRevision(String observacionesRevision) {
		this.observacionesRevision = observacionesRevision;
	}


	public Integer getNumId() {
		return numId;
	}

	public void setNumId(Integer numId) {
		this.numId = numId;
	}

    /**
     * @return the codServicioExtraido
     */
    public String getCodServicioExtraido() {
        return codServicioExtraido;
    }

    /**
     * @param codServicioExtraido the codServicioExtraido to set
     */
    public void setCodServicioExtraido(String codServicioExtraido) {
        this.codServicioExtraido = codServicioExtraido;
    }



	public String getIpGestionExtraido() {
		return ipGestionExtraido;
	}

	public void setIpGestionExtraido(String ipGestionExtraido) {
		this.ipGestionExtraido = ipGestionExtraido;
	}

	
	@Override
	public String toString() {
		return numId+"\t"+codServicioExtraido+'\u0009'+ipGestionExtraido+"\t"+codHostname+"\t"+ codServicio+'\u0009'+ipGestion+'\u0009'+estado+'\u0009'+tipo+'\u0009'+cliente+'\u0009'+observaciones.replaceAll("\n", "")+"\t"+observacionesRevision;

	}



}
