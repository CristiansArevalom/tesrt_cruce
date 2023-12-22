package com.test.model;


public enum CiUcmdbWindowsColumns {
	UCMDB_GLOBAL_ID("[Windows] : Global Id"),
	UCMDB_DISPLAY_LABEL("[Windows] : Display Label"),
	UCMDB_SERVICE_CODE("[Windows] : Onyx ServiceCodes"),
	UCMDB_IP_GESTION("[Windows] : IpAddress"),
	UCMDB_IP_ADDRESS("[Windows] : Ip Gestion"),
	UCMDB_DISCOVERY_PROTOCOL("[Windows] : Protocolo Descubrimiento"),
	UCMDB_LAST_ACCESS_TIME("[Windows] : Ultima Fecha de Acceso Protocolo"),
	UCMDB_OWNER_GROUP("[Windows] : Owner Group"),
	UCMDB_SERVICE_CATALOG("[Windows] : Service Catalog"),
	UCMDB_NODE_IS_VIRTUAL("[Windows] : Node Is Virtual"),
	UCMDB_NOTE("[Windows] : Note")
	;

	private String columnName;

	private CiUcmdbWindowsColumns(String columnName) {
		this.columnName=columnName;		
	}
	public String getColumnName() {
		return columnName;
	}


	public static CiUcmdbWindowsColumns valueOfLabel(String textColumn) {
		CiUcmdbWindowsColumns [] columns = CiUcmdbWindowsColumns.values();
		CiUcmdbWindowsColumns column=null;
		for(int i = 0; i<columns.length;i++) {
			if(columns[i].getColumnName().equals(textColumn)) {
				column= columns[i];
			}
		}
		return column;

	}
}
