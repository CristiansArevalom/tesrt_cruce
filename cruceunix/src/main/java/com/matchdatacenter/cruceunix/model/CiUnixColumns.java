package com.matchdatacenter.cruceunix.model;

public enum CiUnixColumns {
	
	 TOWER_ADM_RESPONSABLE("ADM_RESPONSABLE"),
	 TOWER_CLIENTE("CLIENTE"),
	 TOWER_COD_HOSTNAME("COD_HOSTNAME"),
	 TOWER_COD_SERVICIO("COD_SERVICIO"),
	 TOWER_HOSTNAME("HOSTNAME"),
	 TOWER_SO("SO"),
	 TOWER_VER_SO("VER_SO"),
	 TOWER_TIPO("TIPO"),
	 TOWER_IP_MGMT("IP-MGMT"),
	 TOWER_IP_PROD("IP-PROD"),
	 TOWER_IP_ILO("IP-ILO"),
	 TOWER_SERVICIO_ROL("SERVICIO / RO"),
	 TOWER_ESCALAMIENTO("ESCALAMIENTO - APP"),
	 TOWER_MONITOREO("MONITOREO"),
	 TOWER_MARCA_MODELO("MARCA | MODELO"),
	 TOWER_SERIAL("SERIAL"),
	 TOWER_UBICACION("UBICACION"),
	 TOWER_FECHA_RECEPCION("FECHA RECEPCION"),
	 TOWER_FECHA_RETIRO("FECHA RETIRO"),
	 TOWER_ESTADO("ESTADO"),
	 TOWER_OBSERVACIONES("OBSERVACIONES"),
	 TOWER_CREATED_DATE("Created"),
	 TOWER_PATH("_Path"),
	 TOWER_ITEM_TYPE("_Item Type"),
	 ;
		
	private String columnName;
	
	
	private CiUnixColumns(String columnName) {
		this.columnName=columnName;		
	}
	public String getColumnName() {
		return columnName;
	}

	
	public static CiUnixColumns valueOfLabel(String textColumn) {
		CiUnixColumns columnEnumValue=null;
		for(CiUnixColumns column: values()) {
			if(column.getColumnName().equals(textColumn)) {
				columnEnumValue= column;
			}
		}
		return columnEnumValue;
		
	}

	
	
	
}
