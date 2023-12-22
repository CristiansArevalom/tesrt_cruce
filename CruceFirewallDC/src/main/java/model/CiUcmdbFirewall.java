package model;

public class CiUcmdbFirewall {
	private Integer uid;
	private String globalId;
	private String displayLabel;
	private String snmpSysName;
	private String description;
	private String discoveredLocation;
	private String discoveredModel;
	private String onyxServiceCode;
	private String companyName;
	private String companyNit;

	private String functionalGroupOwner;
	private String functionalGroupResponsable;
	private String serviceCatalog;
	private String serialNumber;
	private String hardwareBoard;
	private String hardwareBoardServiceCode;

	private String ipAddress;
	private String unidadRack;
	private String unidadNode;
	private String sedeCliente;
	private String lastAccessTime;
	private String recuentoDias;
	private String DiscoveryStatus;
	private String statusLastAccessTime;

	public CiUcmdbFirewall() {
		this.uid=null;
		this.globalId="";
		this.displayLabel="";
		this.snmpSysName="";
		this.description="";
		this.discoveredLocation="";
		this.discoveredModel="";
		this.onyxServiceCode="";
		this.companyName="";
		this.companyNit="";

		this.functionalGroupOwner="";
		this.functionalGroupResponsable="";
		this.serviceCatalog="";
		this.serialNumber="";
		this.hardwareBoard="";
		this.hardwareBoardServiceCode="";

		this.ipAddress="";
		this.unidadRack="";
		this.unidadNode="";
		this.sedeCliente="";
		this.DiscoveryStatus="Credencial PDT";
		this.recuentoDias="Mas de 60 Dias";
		this.lastAccessTime="";
		this.statusLastAccessTime="Sin last Access Time";
	}


	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getGlobalId() {
		return globalId;
	}


	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}
	public String getDisplayLabel() {
		return displayLabel;
	}
	public void setDisplayLabel(String displayLabel) {
		this.displayLabel = displayLabel;
	}
	public String getSnmpSysName() {
		return snmpSysName;
	}
	public void setSnmpSysName(String snmpSysName) {
		this.snmpSysName = snmpSysName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDiscoveredLocation() {
		return discoveredLocation;
	}
	public void setDiscoveredLocation(String discoveredLocation) {
		this.discoveredLocation = discoveredLocation;
	}
	public String getDiscoveredModel() {
		return discoveredModel;
	}
	public void setDiscoveredModel(String discoveredModel) {
		this.discoveredModel = discoveredModel;
	}
	public String getOnyxServiceCode() {
		return onyxServiceCode;
	}
	public void setOnyxServiceCode(String onyxServiceCode) {
		this.onyxServiceCode = onyxServiceCode;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyNit() {
		return companyNit;
	}
	public void setCompanyNit(String companyNit) {
		this.companyNit = companyNit;
	}
	public String getFunctionalGroupOwner() {
		return functionalGroupOwner;
	}
	public void setFunctionalGroupOwner(String functionalGroupOwner) {
		this.functionalGroupOwner = functionalGroupOwner;
	}
	public String getFunctionalGroupResponsable() {
		return functionalGroupResponsable;
	}
	public void setFunctionalGroupResponsable(String functionalGroupResponsable) {
		this.functionalGroupResponsable = functionalGroupResponsable;
	}
	public String getServiceCatalog() {
		return serviceCatalog;
	}
	public void setServiceCatalog(String serviceCatalog) {
		this.serviceCatalog = serviceCatalog;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getHardwareBoard() {
		return hardwareBoard;
	}
	public void setHardwareBoard(String hardwareBoard) {
		this.hardwareBoard = hardwareBoard;
	}
	public String getHardwareBoardServiceCode() {
		return hardwareBoardServiceCode;
	}
	public void setHardwareBoardServiceCode(String hardwareBoardServiceCode) {
		this.hardwareBoardServiceCode = hardwareBoardServiceCode;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getUnidadRack() {
		return unidadRack;
	}
	public void setUnidadRack(String unidadRack) {
		this.unidadRack = unidadRack;
	}
	public String getUnidadNode() {
		return unidadNode;
	}
	public void setUnidadNode(String unidadNode) {
		this.unidadNode = unidadNode;
	}
	public String getSedeCliente() {
		return sedeCliente;
	}
	public void setSedeCliente(String sedeCliente) {
		this.sedeCliente = sedeCliente;
	}
	
	public String getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(String lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public String getRecuentoDias() {
		return recuentoDias;
	}


	public void setRecuentoDias(String recuentoDias) {
		this.recuentoDias = recuentoDias;
	}


	public String getDiscoveryStatus() {
		return DiscoveryStatus;
	}


	public void setDiscoveryStatus(String discoveryStatus) {
		DiscoveryStatus = discoveryStatus;
	}
	

	public String getStatusLastAccessTime() {
		return statusLastAccessTime;
	}


	public void setStatusLastAccessTime(String statusLastAccessTime) {
		this.statusLastAccessTime = statusLastAccessTime;
	}
	
	@Override
	public String toString() {
		return  uid+"\t"+globalId+"\t"+displayLabel + "\t" + snmpSysName + "\t"
				+ description + "\t" + discoveredLocation + "\t" + discoveredModel
				+ "\t" + onyxServiceCode + "\t"+companyNit+"\t" +companyName + "\t"
				+ functionalGroupOwner + "\t" + functionalGroupResponsable
				+ "\t" + serviceCatalog + "\t" + serialNumber + "\t"
				+ hardwareBoard + "\t"+hardwareBoardServiceCode+"\t"
				+  unidadRack +"\t"
				+ unidadNode + "\t" + sedeCliente+"\t"+ipAddress+"\t"+lastAccessTime
				+"\t"+recuentoDias+"\t"+DiscoveryStatus+"\t"+statusLastAccessTime;
	}

	

}
