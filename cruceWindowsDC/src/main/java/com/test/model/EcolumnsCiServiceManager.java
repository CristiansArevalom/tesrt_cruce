package com.test.model;

public enum EcolumnsCiServiceManager {

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

	private EcolumnsCiServiceManager(String columnName) {
		this.columnName=columnName;
	}
	
	//METODO PARA EXTRAER EL VALOR DE LA COLUMNA
	public static EcolumnsCiServiceManager valueOfLabel(String textColumn) {
		EcolumnsCiServiceManager[] columns = EcolumnsCiServiceManager.values();
		EcolumnsCiServiceManager column = null;
		
		for(int i = 0; i<columns.length;i++) {
			if(columns[i].getColumnName().equals(textColumn)) {
				column= columns[i];
			}
		}
		return column;

	}
}
