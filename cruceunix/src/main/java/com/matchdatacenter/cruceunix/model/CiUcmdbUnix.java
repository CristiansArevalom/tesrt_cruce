package com.matchdatacenter.cruceunix.model;

public class CiUcmdbUnix {


	@Override
	public String toString() {
		return numID+"\t"+globalId+"\t"+onyxServiceCodes + '\t'+ displayLabel
				+ '\t' + ipGestion+ '\t' + ipAddress +'\t' 
				+ discoveryProtocol+ '\t'+ LastAccessTimeProtocolo+"\t"+this.statusLastAccessTime+"\t"+this.DiscoveryStatus+"\t"+this.recuentoDias;
	}

	/*
	@Override
	public String toString() {
		return displayLabel + '\t'+ onyxServiceCodes
				+ '\t'+ claroCompanyName + '\t' + discoveryProtocol
				+ '\t'+ LastAccessTimeProtocolo + '\t' + ipGestion
				+ '\t'+ serialNumber + '\t' + discoveredModel
				+ '\t'+ discoveredOsName + '\t' + discoveredOsVendor 
				+ '\t'+ discoveredOsVersion + '\t' + discoveredVendor + '\t' + nodeIsVirtual
				+ '\t'+ memorySize + '\t'+ dnsServers + '\t' + cpuTotal
				+ '\t'+ cpuTotalCoreNumber + '\t' + fileSystemSize
				+ '\t'+ fileSystemTotalFreeSpaceAvailable 
				+ '\t'+ ipAddress + '\t' + locationDatacenter + '\t' + locationRack
				+ '\t'+ locationRackType + '\t' + locationType 
				+ '\t'+ locationUnidad;
	}
	*/
	private Integer numID;

	private String uid;
	private String globalId;


	private String displayLabel;
	private String onyxServiceCodes;
	private String claroCompanyName;
	private String discoveryProtocol;
	private String LastAccessTimeProtocolo;
	private String ipGestion;
	private String serialNumber;
	private String discoveredModel;
	private String discoveredOsName;
	private String discoveredOsVendor;
	private String discoveredOsVersion;
	private String discoveredVendor;
	private String nodeIsVirtual;
	private String memorySize;
	private String dnsServers;
	private String cpuTotal;
	private String cpuTotalCoreNumber;
	private String fileSystemSize;
	private String fileSystemTotalFreeSpaceAvailable;
	private String ipAddress;
	private String locationDatacenter;
	private String locationRack;
	private String locationRackType;
	private String locationType;
	private String locationUnidad;
	private String recuentoDias;
	private String DiscoveryStatus;
	private String statusLastAccessTime;

	
	public CiUcmdbUnix() {
		super();
		this.numID=null;
		this.globalId="";
		this.uid = "";
		this.displayLabel = "";
		this.onyxServiceCodes = "";
		this.claroCompanyName = "";
		this.discoveryProtocol = "";
		this.LastAccessTimeProtocolo = "";
		this.ipGestion = "";
		this.serialNumber ="";
		this.discoveredModel = "";
		this.discoveredOsName = "";
		this.discoveredOsVendor = "";
		this.discoveredOsVersion = "";
		this.discoveredVendor ="";
		this.nodeIsVirtual = "";
		this.memorySize = "";
		this.dnsServers = "";
		this.cpuTotal = "";
		this.cpuTotalCoreNumber = "";
		this.fileSystemSize = "";
		this.fileSystemTotalFreeSpaceAvailable = "";
		this.ipAddress = "";
		this.locationDatacenter = "";
		this.locationRack = "";
		this.locationRackType = "";
		this.locationType = "";
		this.locationUnidad = "";
		this.recuentoDias="Mas de 60 Dias";
		this.DiscoveryStatus="Credencial PDT";
		this.statusLastAccessTime="Sin last AccessTime";
		
		
		
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

	public String getOnyxServiceCodes() {
		return onyxServiceCodes;
	}

	public void setOnyxServiceCodes(String onyxServiceCodes) {
		this.onyxServiceCodes = onyxServiceCodes;
	}

	public String getClaroCompanyName() {
		return claroCompanyName;
	}

	public void setClaroCompanyName(String claroCompanyName) {
		this.claroCompanyName = claroCompanyName;
	}

	public String getDiscoveryProtocol() {
		return discoveryProtocol;
	}

	public void setDiscoveryProtocol(String discoveryProtocol) {
		this.discoveryProtocol = discoveryProtocol;
	}

	public String getLastAccessTimeProtocolo() {
		return LastAccessTimeProtocolo;
	}

	public void setLastAccessTimeProtocolo(String lastAccessTime) {
		this.LastAccessTimeProtocolo = lastAccessTime;
	}

	public String getIpGestion() {
		return ipGestion;
	}

	public void setIpGestion(String ipGestion) {
		this.ipGestion = ipGestion;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getDiscoveredModel() {
		return discoveredModel;
	}

	public void setDiscoveredModel(String discoveredModel) {
		this.discoveredModel = discoveredModel;
	}

	public String getDiscoveredOsName() {
		return discoveredOsName;
	}

	public void setDiscoveredOsName(String discoveredOsName) {
		this.discoveredOsName = discoveredOsName;
	}

	public String getDiscoveredOsVendor() {
		return discoveredOsVendor;
	}

	public void setDiscoveredOsVendor(String discoveredOsVendor) {
		this.discoveredOsVendor = discoveredOsVendor;
	}

	public String getDiscoveredOsVersion() {
		return discoveredOsVersion;
	}

	public void setDiscoveredOsVersion(String discoveredOsVersion) {
		this.discoveredOsVersion = discoveredOsVersion;
	}

	public String getDiscoveredVendor() {
		return discoveredVendor;
	}

	public void setDiscoveredVendor(String discoveredVendor) {
		this.discoveredVendor = discoveredVendor;
	}

	public String getNodeIsVirtual() {
		return nodeIsVirtual;
	}

	public void setNodeIsVirtual(String nodeIsVirtual) {
		this.nodeIsVirtual = nodeIsVirtual;
	}

	public String getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(String memorySize) {
		this.memorySize = memorySize;
	}

	public String getDnsServers() {
		return dnsServers;
	}

	public void setDnsServers(String dnsServers) {
		this.dnsServers = dnsServers;
	}

	public String getCpuTotal() {
		return cpuTotal;
	}

	public void setCpuTotal(String cpuTotal) {
		this.cpuTotal = cpuTotal;
	}

	public String getCpuTotalCoreNumber() {
		return cpuTotalCoreNumber;
	}

	public void setCpuTotalCoreNumber(String cpuTotalCoreNumber) {
		this.cpuTotalCoreNumber = cpuTotalCoreNumber;
	}

	public String getFileSystemSize() {
		return fileSystemSize;
	}

	public void setFileSystemSize(String fileSystemSize) {
		this.fileSystemSize = fileSystemSize;
	}

	public String getFileSystemTotalFreeSpaceAvailable() {
		return fileSystemTotalFreeSpaceAvailable;
	}

	public void setFileSystemTotalFreeSpaceAvailable(String fileSystemTotalFreeSpaceAvailable) {
		this.fileSystemTotalFreeSpaceAvailable = fileSystemTotalFreeSpaceAvailable;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getLocationDatacenter() {
		return locationDatacenter;
	}

	public void setLocationDatacenter(String locationDatacenter) {
		this.locationDatacenter = locationDatacenter;
	}

	public String getLocationRack() {
		return locationRack;
	}

	public void setLocationRack(String locationRack) {
		this.locationRack = locationRack;
	}

	public String getLocationRackType() {
		return locationRackType;
	}

	public void setLocationRackType(String locationRackType) {
		this.locationRackType = locationRackType;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getLocationUnidad() {
		return locationUnidad;
	}

	public void setLocationUnidad(String locationUnidad) {
		this.locationUnidad = locationUnidad;
	}
	public String getUid() {
		return uid;
	}
	public String setUid(String uid) {
		return uid;
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
	public int getNumID() {
		return numID;
	}

	public void setNumID(int numID) {
		this.numID = numID;
	}


	
	
}
