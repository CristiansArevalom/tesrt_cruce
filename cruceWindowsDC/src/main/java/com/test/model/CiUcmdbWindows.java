package com.test.model;

public class CiUcmdbWindows {
	public static final String HEADER = "numId"+"\t"+"Global Id"+"\t"+"OnyxServiceCodes"+"\t"+"DisplayLabel"+"\t"+"IpGestion"+"\t"+"IpAddress"+"\t"+"ProtocoloDescubrimiento"+"\t"+"LastAccessTimeProtocolo"+"\t"+"recuentoDias"+"\t"+"DiscoveryStatus"+"\t"+"statusLastAccessTime"+"\t"+"note";
		
	private Integer numId;
	private String globalId;
	private String DisplayLabel;
	private String OnyxServiceCodes;
	private String OwnerGroup;
	private String ServiceCatalog;
	private String ClaroCompanyName;
	private String SerialNumber;
	private String BiosDate;
	private String BiosUuid;
	private String DomainName;
	private String DiscoveredModel;
	private String DiscoveredOsName;
	private String DiscoveredOsVendor;
	private String DiscoveredOsVersion;
	private String DiscoveredVendor;
	private String NodeIsVirtual;
	private String DefaultGatewayIpAddress;
	private String ProtocoloDescubrimiento;
	private String LastAccessTimeProtocolo;
	private String IpServicios;
	private String IpGestion;
	private String IpProduccion;
	private String MemorySize;
	private String DnsServers;
	private String CpuTotal;
	private String DiscoveredProductName;
	private String CPUTotalCoreNumber;
	private String FileSystemTotalSize;
	private String FileSystemTotalFreeSpaceAvailable;
	private String IpAddress;
	private String LocationDatacenter;
	private String LocationRack;
	private String LocationRackType;
	private String LocationType;
	private String LocationUnidad;

	private String recuentoDias;
	private String DiscoveryStatus;
	private String statusLastAccessTime;
	private String note;
	
	
	


	//se genera el constructor por defecto sin atributos.
	public CiUcmdbWindows() {
		super();
		this.DisplayLabel="";
		this.OnyxServiceCodes="";
		this.OwnerGroup="";
		this.ServiceCatalog="";
		this.ClaroCompanyName="";
		this.SerialNumber="";
		this.BiosDate="";
		this.BiosUuid="";
		this.DomainName="";
		this.DiscoveredModel="";
		this.DiscoveredOsName="";
		this.DiscoveredOsVendor="";
		this.DiscoveredOsVersion="";
		this.DiscoveredVendor="";
		this.NodeIsVirtual="";
		this.DefaultGatewayIpAddress="";
		this.ProtocoloDescubrimiento="";
		this.LastAccessTimeProtocolo="";
		this.IpServicios="";
		this.IpGestion="";
		this.IpProduccion="";
		this.MemorySize="";
		this.DnsServers="";
		this.CpuTotal="";
		this.DiscoveredProductName="";
		this.CPUTotalCoreNumber="";
		this.FileSystemTotalSize="";
		this.FileSystemTotalFreeSpaceAvailable="";
		this.IpAddress="";
		this.LocationDatacenter="";
		this.LocationRack="";
		this.LocationRackType="";
		this.LocationType="";
		this.LocationUnidad="";

		this.recuentoDias="Mas de 60 Dias";
		this.DiscoveryStatus="Credencial PDT";
		this.statusLastAccessTime="Sin last AccessTime";
	}
	
	public String getDisplayLabel() {
		return DisplayLabel;
	}
	public void setDisplayLabel(String displayLabel) {
		DisplayLabel = displayLabel;
	}
	public void setOnyxServiceCodes(String onyxServiceCodes) {
		OnyxServiceCodes = onyxServiceCodes;
	}
	public void setOwnerGroup(String ownerGroup) {
		OwnerGroup = ownerGroup;
	}
	public void setServiceCatalog(String serviceCatalog) {
		ServiceCatalog = serviceCatalog;
	}
	public void setClaroCompanyName(String claroCompanyName) {
		ClaroCompanyName = claroCompanyName;
	}
	public void setSerialNumber(String serialNumber) {
		SerialNumber = serialNumber;
	}
	public void setBiosDate(String biosDate) {
		BiosDate = biosDate;
	}
	public void setBiosUuid(String biosUuid) {
		BiosUuid = biosUuid;
	}
	public void setDomainName(String domainName) {
		DomainName = domainName;
	}
	public void setDiscoveredModel(String discoveredModel) {
		DiscoveredModel = discoveredModel;
	}
	public void setDiscoveredOsName(String discoveredOsName) {
		DiscoveredOsName = discoveredOsName;
	}
	public void setDiscoveredOsVendor(String discoveredOsVendor) {
		DiscoveredOsVendor = discoveredOsVendor;
	}
	public void setDiscoveredOsVersion(String discoveredOsVersion) {
		DiscoveredOsVersion = discoveredOsVersion;
	}
	public void setDiscoveredVendor(String discoveredVendor) {
		DiscoveredVendor = discoveredVendor;
	}
	public void setNodeIsVirtual(String nodeIsVirtual) {
		NodeIsVirtual = nodeIsVirtual;
	}
	public void setDefaultGatewayIpAddress(String defaultGatewayIpAddress) {
		DefaultGatewayIpAddress = defaultGatewayIpAddress;
	}
	public void setProtocoloDescubrimiento(String protocoloDescubrimiento) {
		ProtocoloDescubrimiento = protocoloDescubrimiento;
	}
	public void setLastAccessTimeProtocolo(String lastAccessTimeProtocolo) {
		LastAccessTimeProtocolo = lastAccessTimeProtocolo;
	}
	public void setIpServicios(String ipServicios) {
		IpServicios = ipServicios;
	}
	public void setIpGestion(String ipGestion) {
		IpGestion = ipGestion;
	}
	public void setIpProduccion(String ipProduccion) {
		IpProduccion = ipProduccion;
	}
	public void setMemorySize(String memorySize) {
		MemorySize = memorySize;
	}
	public void setDnsServers(String dnsServers) {
		DnsServers = dnsServers;
	}
	public void setCpuTotal(String cpuTotal) {
		CpuTotal = cpuTotal;
	}
	public void setDiscoveredProductName(String discoveredProductName) {
		DiscoveredProductName = discoveredProductName;
	}
	public void setCPUTotalCoreNumber(String cPUTotalCoreNumber) {
		CPUTotalCoreNumber = cPUTotalCoreNumber;
	}
	public void setFileSystemTotalSize(String fileSystemTotalSize) {
		FileSystemTotalSize = fileSystemTotalSize;
	}
	public void setFileSystemTotalFreeSpaceAvailable(String fileSystemTotalFreeSpaceAvailable) {
		FileSystemTotalFreeSpaceAvailable = fileSystemTotalFreeSpaceAvailable;
	}
	public void setIpAddress(String ipAddress) {
		IpAddress = ipAddress;
	}
	public void setLocationDatacenter(String locationDatacenter) {
		LocationDatacenter = locationDatacenter;
	}
	public void setLocationRack(String locationRack) {
		LocationRack = locationRack;
	}
	public void setLocationRackType(String locationRackType) {
		LocationRackType = locationRackType;
	}
	public void setLocationType(String locationType) {
		LocationType = locationType;
	}
	public void setLocationUnidad(String locationUnidad) {
		LocationUnidad = locationUnidad;
	}
	public String getOnyxServiceCodes() {
		return OnyxServiceCodes;
	}
	public String getOwnerGroup() {
		return OwnerGroup;
	}
	public String getServiceCatalog() {
		return ServiceCatalog;
	}
	public String getClaroCompanyName() {
		return ClaroCompanyName;
	}
	public String getSerialNumber() {
		return SerialNumber;
	}
	public String getBiosDate() {
		return BiosDate;
	}
	public String getBiosUuid() {
		return BiosUuid;
	}
	public String getDomainName() {
		return DomainName;
	}
	public String getDiscoveredModel() {
		return DiscoveredModel;
	}
	public String getDiscoveredOsName() {
		return DiscoveredOsName;
	}
	public String getDiscoveredOsVendor() {
		return DiscoveredOsVendor;
	}
	public String getDiscoveredOsVersion() {
		return DiscoveredOsVersion;
	}
	public String getDiscoveredVendor() {
		return DiscoveredVendor;
	}
	public String getNodeIsVirtual() {
		return NodeIsVirtual;
	}
	public String getDefaultGatewayIpAddress() {
		return DefaultGatewayIpAddress;
	}
	public String getProtocoloDescubrimiento() {
		return ProtocoloDescubrimiento;
	}
	public String getLastAccessTimeProtocolo() {
		return LastAccessTimeProtocolo;
	}
	public String getIpServicios() {
		return IpServicios;
	}
	public String getIpGestion() {
		return IpGestion;
	}
	public String getIpProduccion() {
		return IpProduccion;
	}
	public String getMemorySize() {
		return MemorySize;
	}
	public String getDnsServers() {
		return DnsServers;
	}
	public String getCpuTotal() {
		return CpuTotal;
	}
	public String getDiscoveredProductName() {
		return DiscoveredProductName;
	}
	public String getCPUTotalCoreNumber() {
		return CPUTotalCoreNumber;
	}
	public String getFileSystemTotalSize() {
		return FileSystemTotalSize;
	}
	public String getFileSystemTotalFreeSpaceAvailable() {
		return FileSystemTotalFreeSpaceAvailable;
	}
	public String getIpAddress() {
		return IpAddress;
	}
	public String getLocationDatacenter() {
		return LocationDatacenter;
	}
	public String getLocationRack() {
		return LocationRack;
	}
	public String getLocationRackType() {
		return LocationRackType;
	}
	public String getLocationType() {
		return LocationType;
	}
	public String getLocationUnidad() {
		return LocationUnidad;
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

	public Integer getNumId() {
		return numId;
	}

	public void setNumId(Integer numId) {
		this.numId = numId;
	}
	public String getGlobalId() {
		return globalId;
	}

	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		
		
		return numId+"\t"+globalId+"\t"+OnyxServiceCodes+'\u0009'+DisplayLabel+							
		'\u0009'+IpGestion+							
		'\u0009'+IpAddress+
		'\u0009'+ProtocoloDescubrimiento+
		'\u0009'+LastAccessTimeProtocolo+"\t"+recuentoDias+"\t"+DiscoveryStatus+"\t"+statusLastAccessTime+"\t"+note
		 ;
		
	}


	 
}
