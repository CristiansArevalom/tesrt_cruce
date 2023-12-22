#Cruce Unix semi automatico

Se realiza cruce semi automatico tomando 3 archivos. inventario torre (Excel .xlsx), inventario ucmdb (excel.xlsx) e inventario de service manager (csv separador ; ) para identificar diferencias preeliminares de registro de cis entre inventarios

##Requisitios:
####inventario torre unix
debe contar con las columnas descritas abajo, en caso de querer añadir o modificarlas se puede ajustar los ENUM "CiUnixColumns: 

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
####Inventario UCMDB
debe contar con las columnas descritas abajo, en caso de querer añadir o modificarlas se puede ajustar los ENUM 
"CiUcmdbColumns: 

	UCMDB_GLOBAL_ID("[Node] : Global Id")
	UCMDB_DISPLAY_LABEL("[Node] : Display Label"),
	UCMDB_SERVICE_CODE("[Node] : Onyx ServiceCodes"),
	UCMDB_IP_GESTION("[Node] : Ip Gestion"),
	UCMDB_IP_ADDRESS("[Node] : IpAddress"),
	UCMDB_DISCOVERY_PROTOCOL("[Node] : Discovery Protocol"),
	UCMDB_LAST_ACCESS_TIME("[Node] : Ultima Fecha de Acceso"),
####Inventario ITSM Service manager
 debe contar con las columnas descritas abajo, en caso de querer añadir o modificarlas se puede ajustar los ENUM "EcolumnsCiServiceManager:

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



##Restricciones:
	*	Los codigos de servicio deben cumplir con el estandar definido por claro a nivel de longitud, este es validadoa partir de expresiones regulares
	Las ip de gestión deben ser validas y cumplir con el estandar de IPV4 , esta es validada a partir de expresiones regulares

	 
	 
	 