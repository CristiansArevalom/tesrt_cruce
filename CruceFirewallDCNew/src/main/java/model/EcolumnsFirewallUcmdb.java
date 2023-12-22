package model;

public enum EcolumnsFirewallUcmdb {
	GLOBAL_ID("[Node] : Global Id"),
	DISPLAY_LABEL("[Node] : Display Label"),
	SNMP_SYSNAME("[Node] : SnmpSysName"),
	DESCRIPTION("[Node] : Description"),
	DISCOVERED_LOCATION("[Node] : DiscoveredLocation"),
	DISCOVERED_MODEL("[Node] : DiscoveredModel"),
	COD_SERVICIO("[Node] : [Onyx] - ServiceCode"),
	COMPANY_NAME("[Node] : [Onyx] - CompanyName"),
	COMPANY_NIT("[Node] : [Onyx] - CompanyNIT"),
	OWNER("[Node] : [Group] - Owner"),
	RESPONSIBLE("[Node] : [Group] - Responsible"),
	SERVICE_CATALOG("[Node] : [Service Catalog] - Name"),
	SERIAL_NUMBER("[Node] : [HardwareBoard] - SerialNumber"),
	HARDWARE_BOARD_DISPLAY_LABEL("[Node] : [HardwareBoard] - DisplayLabel"),
	HARDWARE_BOARD_SERVICE_CODE("[Node] : [HardwareBoard] - ServiceCode"),
	UNIDAD_RACK("[Node] : [Node] - UnidadRack"),
	UNIDAD_NODE("[Node] : [Node] - UnidadNode"),
	SEDE_CLIENTE("[Node] : [Node] - Sede Cliente"),
	IPADDRESS("[Node] : [Node] - IpAddress"),
	LAST_ACCESS_TIME("[Node] : Last Access Time"),
	
	
	;
	
	private String columnName;
	
	public String getColumnName() {
		return columnName;
	}

	private EcolumnsFirewallUcmdb(String columnName) {
		this.columnName=columnName;
	}
	
	public static EcolumnsFirewallUcmdb valueOfLabel(String textColumn) {
		EcolumnsFirewallUcmdb [] columns = EcolumnsFirewallUcmdb.values();
		EcolumnsFirewallUcmdb column=null;
		for(int i = 0; i<columns.length;i++) {
			if(columns[i].getColumnName().equals(textColumn)) {
				column= columns[i];
			}
		}
		return column;

	}
	
}
