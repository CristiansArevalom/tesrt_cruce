package model;

public enum EcolumnsCiServiceManagerFw {

	NOMBRE("﻿Nombre para mostrar"),
	NUEVA_UCMDB("Clr Clb Newucmdb"),
	SERVICE_CODE("Clr Service Code"),
	SUBTIPO("Subtipo"),
	ADMINISTRACION("Grupo de administración de configuración"),
	STATUS("Clr Istatus"),
	TITULO("Título"),
	COMENTARIOS("Comentarios"),
	LOCATION("Clr Arr Location"),
	FECHA_MODIFICACION("Fecha y hora de modificación del sistema"),
	;
	
	
	private String columnName;
	
	public String getColumnName() {
		return columnName;
	}

	private EcolumnsCiServiceManagerFw(String columnName) {
		this.columnName=columnName;
	}
	
	//METODO PARA EXTRAER EL VALOR DE LA COLUMNA
	public static EcolumnsCiServiceManagerFw valueOfLabel(String textColumn) {
		EcolumnsCiServiceManagerFw[] columns = EcolumnsCiServiceManagerFw.values();
		EcolumnsCiServiceManagerFw column = null;
		
		for(int i = 0; i<columns.length;i++) {
			if(columns[i].getColumnName().equals(textColumn)) {
				column= columns[i];
			}
		}
		return column;

	}
}
