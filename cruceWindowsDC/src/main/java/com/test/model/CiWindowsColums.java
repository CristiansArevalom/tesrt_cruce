package com.test.model;

public enum CiWindowsColums {

	CLIENTE("CLIENTE"),
	TIPO_PASO("TIPO_PASO"),
	ADM_RESPONSABLE("ADM_RESPONSABLE"),
	ADM_BACKUP("ADM_BACKUP"),
	COD_HOSTNAME("COD_HOSTNAME"),
	HOSTNAME("HOSTNAME"),
	SO("SO"),
	COD_SERVICIO("COD_SERVICIO"),
	IP_GESTION("IP-GESTION"),
	UBICACION("UBICACION"),
	FECHA_RECEPCION("FECHA RECEPCION"),
	FECHA_RETIRO("FECHA RETIRO"),
	ESTADO("ESTADO"),
	OBSERVACIONES("OBSERVACIONES"),
	TIPO("TIPO"),
	ITEM_TYPE("Item Type"),
	PATH("Path"),
	;

	
	private String columnName;
	
	private CiWindowsColums(String columnName) {
		this.columnName=columnName;		
	}
	public String getColumnName() {
		return columnName;
	}
	
	
	public static CiWindowsColums valueOfLabel(String textColumn) {
		
		CiWindowsColums [] columns = CiWindowsColums.values(); //RETURN all values present inside the enum.
		CiWindowsColums column=null;
		for(int i = 0; i<columns.length;i++) {
			if(columns[i].getColumnName().equals(textColumn)) {
				column= columns[i];
			}
		}
		return column;
	
	}

	
}
