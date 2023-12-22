package model;

public enum EcolumnsCiFirewall {
	ADMINISTRACION("ADMINISTRADO POR"),
	DISPOSITIVO("DISPOSITIVO"),
	TIPO_DISPOSITIVO("TIPO DE DISPOSITIVO"),
	FABRICANTE("FABRICANTE"),
	NOMBRE("NOMBRE"),
	CLIENTE("CLIENTE"),
	IP_GESTION("IP"),
	COD_SERVICIO("COD. SERVICIO"),
	MODELO("MODELO"),
	SERIAL("SERIAL"),
	FAILOVER("FAILOVER"),
	VERSION_FIRMWARE("VERSION FIRMWARE"),
	DATACENTER("Datacenter"),
	BUNKER("Bunker"),
	NODO("Nodo"),
	PISO("Piso"),
	RACK("Rack"),
	UNIDAD("Unidad"),
	IPS("IPS"),
	TIPO("tipo")
	;
	private String columnName;
	public String getColumnName() {
		return columnName;
	}
	private EcolumnsCiFirewall(String columnName) {
		this.columnName=columnName;
	}
	public static EcolumnsCiFirewall valueOfLabel(String textColumn) {
		EcolumnsCiFirewall [] columns = EcolumnsCiFirewall.values();
		EcolumnsCiFirewall column=null;
		for(int i = 0; i<columns.length;i++) {
			if(columns[i].getColumnName().equals(textColumn)) {
				column= columns[i];
			}
		}
		return column;

	}
}
