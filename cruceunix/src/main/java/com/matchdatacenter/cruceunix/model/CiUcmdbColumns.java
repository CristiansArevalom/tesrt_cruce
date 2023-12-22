package com.matchdatacenter.cruceunix.model;

public enum CiUcmdbColumns {
	UCMDB_GLOBAL_ID("[Node] : Global Id")
,	UCMDB_DISPLAY_LABEL("[Node] : Display Label"),
	UCMDB_SERVICE_CODE("[Node] : Onyx ServiceCodes"),
	UCMDB_IP_GESTION("[Node] : Ip Gestion"),
	UCMDB_IP_ADDRESS("[Node] : IpAddress"),
	UCMDB_DISCOVERY_PROTOCOL("[Node] : Discovery Protocol"),
	UCMDB_LAST_ACCESS_TIME("[Node] : Ultima Fecha de Acceso"),
	;

	private String columnName;

	private CiUcmdbColumns(String columnName) {
		this.columnName=columnName;		
	}
	public String getColumnName() {
		return columnName;
	}


	public static CiUcmdbColumns valueOfLabel(String textColumn) {
		CiUcmdbColumns [] columns = CiUcmdbColumns.values();
		CiUcmdbColumns column=null;
		for(int i = 0; i<columns.length;i++) {
			if(columns[i].getColumnName().equals(textColumn)) {
				column= columns[i];
			}
		}
		return column;

	}
}
