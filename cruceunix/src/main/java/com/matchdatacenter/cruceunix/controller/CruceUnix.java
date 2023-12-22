package com.matchdatacenter.cruceunix.controller;
//VALIDAR DUPLICADOS HASSHET.  COMO SON OBJETOS SOBREESCRIBIR EL MNETODO EQUALS.
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.matchdatacenter.cruceunix.model.CiUnix;
import com.matchdatacenter.cruceunix.model.CiUnixColumns;
import com.matchdatacenter.cruceunix.utils.Libs;
import com.matchdatacenter.cruceunix.model.CiUcmdbColumns;
import com.matchdatacenter.cruceunix.model.CiUcmdbUnix;

public class CruceUnix {	
	private static final String TOWER_VS_UCMDB_HEADER="#"+'\u0009'+"ADM_RESPONSABLE"+'\u0009'+"CLIENTE"+'\u0009'+"COD_HOSTNAME"+'\u0009'+"COD_SERVICIO"+'\u0009'+"HOSTNAME"+'\u0009'+"SO"+'\u0009'+"VER_SO"+'\u0009'+"TIPO"+'\u0009'+"IP-MGMT"+'\u0009'+"IP-PROD"+'\u0009'+"IP-ILO"+'\u0009'+"SERVICIO / ROL"+'\u0009'+"ESCALAMIENTO - APP"+'\u0009'+"MONITOREO"+'\u0009'+"MARCA | MODELO"+'\u0009'+"SERIAL"+'\u0009'+"UBICACION"+'\u0009'+"FECHA RECEPCION"+'\u0009'+"FECHA RETIRO"+'\u0009'+"ESTADO"+'\u0009'+"OBSERVACIONES TORRE"+'\u0009'+"Created"+'\u0009'+"_Path"+'\u0009'+"_Item Type"+'\u0009'+"OBSERVACIONES REVISION"+
			'\u0009'+"UID"+'\u0009'+"SERVICECODE"+'\u0009'+"DISPLAYLABEL"+'\u0009'+"IP_GESTION"+'\u0009'+"IP_ADDRESS"+'\u0009'+"PROTOCOLO_DESCUBRIMIENTO"+'\u0009'+"LAST_ACCESS_TIME"+'\u0009'+"STATUS_LAST_ACCESS_TIME"+'\u0009'+"STATUS CREDENCIAL"+'\u0009'+"ESCALA DIAS"+ '\u0009'+ "APLICA UCMDB"+'\u0009'+"STATUS CRUCE CONTRA UCMDB"+'\u0009'+"OBSERVACIONES CRUCE CONTRA UCMDB";

	private static final String UCMDB_VS_TOWER_HEADER="UID"+'\u0009'+"SERVICECODE"+'\u0009'+"DISPLAYLABEL"+'\u0009'+"IP_GESTION"+'\u0009'+"IP_ADDRESS"+'\u0009'+"PROTOCOLO_DESCUBRIMIENTO"+'\u0009'+"LAST_ACCESS_TIME"+'\u0009'+"STATUS CRUCE CONTRA TORRE"+'\u0009'+"STATUS_LAST_ACCESS_TIME"+'\u0009'+"STATUS CREDENCIAL"+'\u0009'+"ESCALA DIAS"+
			'\u0009'+TOWER_VS_UCMDB_HEADER;
	//private static final String UCMDB_FILE_PATH="D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Unix\\inv unix ucmdb.xlsx";
	//private static final String UNIX_FILE_PATH="D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Unix\\inv unix torre.xlsx";
	private static final String UCMDB_FILE_PATH="D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Unix\\inv unix ucmdb.xlsx";
	private static final String UNIX_FILE_PATH="D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Unix\\inv unix torre.xlsx";
	
	public CruceUnix() {

	}

	public static ArrayList<CiUcmdbUnix> readUcmdbInventory(String ucmdbFilePath) throws Exception {
		int uidRowCount=1;
		ArrayList<CiUcmdbUnix> ucmdbUnixInventary = new ArrayList<>();
		if(Libs.excelFormatIsValid(ucmdbFilePath)) {	

			FileInputStream file = new FileInputStream(new File(ucmdbFilePath));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet ucmdbSheet = workbook.getSheetAt(0);
			Iterator<Row> rowUcmdbIterator = ucmdbSheet.iterator();
			ArrayList<String> header = new ArrayList<String>();
			//// recorre filas del archivo
			while (rowUcmdbIterator.hasNext()) { 
				CiUcmdbUnix ciUcmdbUnix = new CiUcmdbUnix();
				Row rowUcmdb = rowUcmdbIterator.next();
				short cellCount = rowUcmdb.getLastCellNum();
				// recorre las columbas del inv
				for (short currentCell = 0; currentCell < cellCount; currentCell++) { 
					String cellValue = "";
					cellValue = Libs.getStringCellValue(rowUcmdb.getCell(currentCell));
					if (rowUcmdb.getRowNum() == 0) { // Guarda el encabezado
						header.add(cellValue);
						//System.out.print(cellValue+"|");
					} else {
						String headerValue=header.get(currentCell);	
						CiUcmdbColumns columnValue=CiUcmdbColumns.valueOfLabel(headerValue);
						if(columnValue!=null ) { //En caso de que alguna columna no este definida en "ciUCMDBColumbs
							switch (columnValue) { // Busca en el encabezado y en la clase enum los titulos y guarda el valor a la columna que se necesita.
							case UCMDB_DISPLAY_LABEL:
								ciUcmdbUnix.setDisplayLabel(cellValue);
								break;
							case UCMDB_SERVICE_CODE:
								ciUcmdbUnix.setOnyxServiceCodes(cellValue);
								break;
							case UCMDB_IP_GESTION:
								ciUcmdbUnix.setIpGestion(cellValue);
								break;
							case UCMDB_IP_ADDRESS:
								ciUcmdbUnix.setIpAddress(cellValue);
								break;
							case UCMDB_DISCOVERY_PROTOCOL:
								ciUcmdbUnix.setDiscoveryProtocol(cellValue);
								break;
							case UCMDB_LAST_ACCESS_TIME :
								ciUcmdbUnix.setLastAccessTimeProtocolo(cellValue);
								break;
							}
						}
					}
				}
				if(ciUcmdbUnix.getDisplayLabel()!=null){
					String uid=(uidRowCount++)+"|"+ciUcmdbUnix.getDisplayLabel()+"_"+ciUcmdbUnix.getOnyxServiceCodes();
					ciUcmdbUnix.setUid(uid);
					ucmdbUnixInventary.add(ciUcmdbUnix);
				}				
			}
			System.out.println("CANTIDAD REGISTROS EN UCMDB 2020 "+ucmdbUnixInventary.size());
			file.close();
			workbook.close();			
		}	
		return ucmdbUnixInventary;
	}

	public static ArrayList<CiUnix> readUnixInventary(String unixFilePath) {
		ArrayList<CiUnix> unixInventary= new ArrayList<>();
		try {
			///PDT AJUSTAR QUE SI ENCUENTRA CELDAS VACIAS, PASE A LA SIGUIENTE.
			FileInputStream fileUnix = new FileInputStream(new File(unixFilePath));
			XSSFWorkbook workbookUnix = new XSSFWorkbook(fileUnix);
			XSSFSheet unixSheet = workbookUnix.getSheetAt(0);
			Iterator<Row> rowIterator = unixSheet.iterator();
			ArrayList<String> header = new ArrayList<String>();
			HashSet<String> checkDuplicate = new HashSet();
			String cellUnixValue = "";
			short cellCount=0;			
			while (rowIterator.hasNext()) { // recorre filas de archivo unix
				Row row = rowIterator.next();
				Iterator<Cell> cellUnixIterator = row.cellIterator();

				CiUnix ciUnix = new CiUnix();

				cellCount = row.getLastCellNum();
				for (short currentCell = 0; currentCell < cellCount; currentCell++) { // recorre las columbas del inv					
					cellUnixValue = Libs.getStringCellValue(cellUnixIterator.next());
					if (row.getRowNum() == 0) { // Guarda el encabezado
						header.add(cellUnixValue);
						//System.out.print(cellWindowsValue + "|"); // si la fila es cero, imprime el totulo
					}else {
						String headerValue=header.get(currentCell);	
						CiUnixColumns columnValue=CiUnixColumns.valueOfLabel(headerValue);
						if(columnValue!=null ) { 
							switch (columnValue) { // Busca en el encabezado los titulos y guarda el valor a la	columna que se necesita.
							case TOWER_ADM_RESPONSABLE:ciUnix.setAdmResponsable(cellUnixValue);break;
							case TOWER_CLIENTE:ciUnix.setCliente(cellUnixValue);break;
							case TOWER_COD_HOSTNAME:
								try{
									ciUnix.setCodHostname(Libs.checkAndFormatServCodeAndHostname(cellUnixValue.toUpperCase().replaceAll(" ", "")));								
								}catch(Exception ex) {
									ciUnix.setCodHostname(cellUnixValue.toUpperCase().replaceAll(" ", ""));									
									ciUnix.setObservacionesRevision(ciUnix.getObservacionesRevision()+"| No cumple con estandar codserv_hostame |");
								}
								break;
							case TOWER_COD_SERVICIO:
								ciUnix.setCodigoServicio(cellUnixValue.replaceAll("\\s+","").replaceAll(" ",""));break;
							case TOWER_HOSTNAME:ciUnix.setHostname(cellUnixValue.trim().replaceAll("\\s+","").replaceAll(" ",""));break;
							case TOWER_SO:ciUnix.setSistemaOperativo(cellUnixValue);break;
							case TOWER_VER_SO:ciUnix.setVerSO(cellUnixValue);break;
							case TOWER_TIPO:ciUnix.setTipo(cellUnixValue);break;
							case TOWER_IP_MGMT:
								//System.out.println(cellUnixValue.replaceAll("\\s+", ""));
								try{
									ciUnix.setIpGestion(Libs.checkAndFormatIpAddress(cellUnixValue));
								}catch(Exception ex) {
									ciUnix.setIpGestion(cellUnixValue);
									ciUnix.setObservacionesRevision(ciUnix.getObservacionesRevision()+"|  IP gestion no cumple con estandar |");
								}								
								break;
							case TOWER_IP_PROD:ciUnix.setIpProduccion(Libs.checkAndFormatIpAddress(cellUnixValue));break;
							case TOWER_IP_ILO:ciUnix.setIpIlo(cellUnixValue);break;
							case TOWER_SERVICIO_ROL:ciUnix.setServicioRol(cellUnixValue);break;
							case TOWER_ESCALAMIENTO:ciUnix.setEscalamientoApp(cellUnixValue);break;
							case TOWER_MONITOREO:ciUnix.setMonitoreo(cellUnixValue);break;
							case TOWER_MARCA_MODELO:ciUnix.setMarcaModelo(cellUnixValue);break;
							case TOWER_SERIAL:ciUnix.setSerial(cellUnixValue);break;
							case TOWER_UBICACION:ciUnix.setUbicacion(cellUnixValue);break;
							case TOWER_FECHA_RECEPCION:ciUnix.setFechaRecepcion(cellUnixValue);break;
							case TOWER_FECHA_RETIRO:ciUnix.setFechaRetiro(cellUnixValue);break;
							case TOWER_ESTADO:ciUnix.setEstado(cellUnixValue);break;
							case TOWER_OBSERVACIONES:ciUnix.setObservaciones(ciUnix.getObservaciones()+cellUnixValue);break;
							case TOWER_CREATED_DATE:ciUnix.setCreated(cellUnixValue);break;
							case TOWER_PATH:ciUnix.setPath(cellUnixValue);break;
							case TOWER_ITEM_TYPE:ciUnix.setItemType(cellUnixValue);break;
							default :
								break;
							}
						}
					}

				} //Valida duplicados a partir de la concatenacion de codigo de servicio y hostname
				if(ciUnix.getCodigoServicio() !=null && ciUnix.getHostname() !=null) { // si cod de serv y hostname estan diligeniados, crea codigo_hostname
					ciUnix.setCodigoHostname();
					if(checkDuplicate.add(ciUnix.getCodigoHostname()+"_"+ciUnix.getIpGestion())) {
						unixInventary.add(ciUnix);
					}
					else {
						ciUnix.setObservacionesRevision(ciUnix.getObservacionesRevision()+" |  Posible ci duplicado en inv torre |");

						unixInventary.add(ciUnix);
					}
				}
			}

			System.out.println("CANTIDAD REGISTROS EN INVENTARIO TORRE "+unixInventary.size());

			fileUnix.close();
			workbookUnix.close();
		}catch (Exception ex) {
			System.err.println("ERROR inv torre: "+ex);
		}
		return unixInventary;
	}
	
	
	public static CiUcmdbUnix getRecentCi(CiUcmdbUnix firstCiUmcbd, CiUcmdbUnix secondCiUmcbd){
		CiUcmdbUnix currentCi = null;
		String [] lastAccesTimesFirstCi =firstCiUmcbd.getLastAccessTimeProtocolo().split(", ");
		String [] lastAccessTimesSecondCi =secondCiUmcbd.getLastAccessTimeProtocolo().split(", ");
		LocalDateTime maxLastAccessTime = LocalDateTime.MIN;
		LocalDateTime maxFirstCiLastAccessTime;
		LocalDateTime maxSecondCiLastAccessTime;

		boolean firstCiHasAccessTimeProtocol=firstCiUmcbd.getLastAccessTimeProtocolo().length()>0;
		boolean secondCiHasAccessTimeProtocol=secondCiUmcbd.getLastAccessTimeProtocolo().length()>0;

		//mirar los LAT de los datos que ya estan, debe tomar el qe tenga el lat ma reciente. 
		if(!firstCiHasAccessTimeProtocol && secondCiHasAccessTimeProtocol) {
			currentCi=secondCiUmcbd;
		}else if(firstCiHasAccessTimeProtocol && !secondCiHasAccessTimeProtocol) {
			currentCi=firstCiUmcbd;
		}else if(firstCiHasAccessTimeProtocol && secondCiHasAccessTimeProtocol ) {
			//TRAER EL OBJETO CON EL MAYOR LAST ACCESS TIME
			for(String firstLastAccesstime : lastAccesTimesFirstCi) { //TRAER EL MAYOR LASTACCESS TIME DEL OBJ1			
				LocalDateTime temporalDateTime = Libs.extractDateFromLastAccesstime(firstLastAccesstime, Libs.PATTERN_DATE);
				//https://www.campusmvp.es/recursos/post/como-manejar-correctamente-fechas-en-java-el-paquete-java-time.aspx
				if(temporalDateTime.isAfter(maxLastAccessTime)) {
					maxLastAccessTime= temporalDateTime;
				}
			}
			maxFirstCiLastAccessTime= maxLastAccessTime;
			maxLastAccessTime = LocalDateTime.MIN;
			for(String secondLastAccesstime : lastAccessTimesSecondCi) { //TRAER El MAYOR LASTACCESS TIME DEL OBJ2
				LocalDateTime temporalDateTime = Libs.extractDateFromLastAccesstime(secondLastAccesstime, Libs.PATTERN_DATE);
				if(temporalDateTime.isAfter(maxLastAccessTime)) {
					maxLastAccessTime= temporalDateTime;
				}
			}
			maxSecondCiLastAccessTime= maxLastAccessTime;
			if(maxFirstCiLastAccessTime.isAfter(maxSecondCiLastAccessTime)) { //compara los mayores de ambos LAT
				currentCi=firstCiUmcbd;
			}else {
				currentCi=secondCiUmcbd;
			}
		}else {
			currentCi = firstCiUmcbd;
		}
		return currentCi;
	}

	//valida en otro hashmap e solo dup0licados, si coinide la ip, entonces toma ese, si no, deja el que hizo match antes
	public static ArrayList<String> towerInventaryMergeUcmdbInventary(ArrayList<CiUnix> unixInventary, ArrayList<CiUcmdbUnix> ucmdbInventary) throws Exception{
		ArrayList<String> rowText=new ArrayList<>();
		if(!(unixInventary.size()==0 ||ucmdbInventary.size()==0 )) {
			HashMap<String, CiUcmdbUnix> inventoryUCMDBhm = new HashMap<>(); //AQI SE PUEDE MIRAR E IDENTIFICAR LOS DUPLICADOS.
			HashMap<String, CiUcmdbUnix> inventoryUCMDBhmDuplicates = new HashMap<>(); //AQI SE PUEDE MIRAR E IDENTIFICAR LOS DUPLICADOS.
			//LLENANDO HASHMAP CON DATOS DE UCMDB
			for (CiUcmdbUnix ciUcmdbUnix : ucmdbInventary) {
				String ucmdbCodHostname=ciUcmdbUnix.getOnyxServiceCodes().toUpperCase()+"_"+ciUcmdbUnix.getDisplayLabel().toUpperCase();			
				if(inventoryUCMDBhm.get(ucmdbCodHostname)== null) {
					inventoryUCMDBhm.put(ucmdbCodHostname, ciUcmdbUnix);
				}else {
					//en caso de que ya exista la clave, se debe insertar en el hashmap la que este mas actcualizada y gurdar en otro hashmap el duplicado 
					CiUcmdbUnix secondCiUmcbd = ciUcmdbUnix;
					CiUcmdbUnix firstCiUmcbd = inventoryUCMDBhm.get(ucmdbCodHostname);
					CiUcmdbUnix recentCiUcmdb = getRecentCi(firstCiUmcbd,secondCiUmcbd );

					String recentUcmdbCodHostname=recentCiUcmdb.getOnyxServiceCodes().toUpperCase()+"_"+recentCiUcmdb.getDisplayLabel().toUpperCase();
					String firstCiUmcbdCodHostname=firstCiUmcbd.getOnyxServiceCodes().toUpperCase()+"_"+firstCiUmcbd.getDisplayLabel().toUpperCase();
					String secondCiUmcbdCodHostname=secondCiUmcbd.getOnyxServiceCodes().toUpperCase()+"_"+secondCiUmcbd.getDisplayLabel().toUpperCase();

					inventoryUCMDBhm.replace(recentUcmdbCodHostname, recentCiUcmdb);				

					if(recentCiUcmdb.equals(firstCiUmcbd)) {
						inventoryUCMDBhmDuplicates.put(secondCiUmcbdCodHostname, secondCiUmcbd);
					}else {
						inventoryUCMDBhmDuplicates.put(firstCiUmcbdCodHostname, firstCiUmcbd);
					}
				}
			}

			System.out.println(TOWER_VS_UCMDB_HEADER);
			rowText.add(TOWER_VS_UCMDB_HEADER);
			
			//RECORRIENDO INVENTARIO UNIX 
			for ( int i = 0 ; i<unixInventary.size();i++) {
				CiUnix ciUnix= unixInventary.get(i);
				String ciUnixcodHostname=ciUnix.getCodigoHostname();
				String ciUnixIpGestion=ciUnix.getIpGestion();
				
				String lastAccessTimeStatus="";
				String credentialStatus="";
				String scaleDays="";
				String [][]scaleDaysAndstatusCredential;
				String checkAppyUCMDB="";
				String rowValue="";

				String ciUcmdbIpGestion="";
				String ciUcmdbIpAddress="";
				boolean hasMatchByIpAddress=false;

				if(inventoryUCMDBhm.containsKey(ciUnix.getCodigoHostname().toUpperCase())){
					//VALIDA X IP-> CODSERVICIOhOSTNAME
					String ipAdressUnix[]= ciUnix.getIpGestion().split(",");
					//ARREGLAR PARA QUE RELAICE LA VALIDACIÓN por cada ip en IPADdres del inv torre
					ciUcmdbIpGestion=inventoryUCMDBhm.get(ciUnixcodHostname).getIpGestion();
					ciUcmdbIpAddress = inventoryUCMDBhm.get(ciUnixcodHostname).getIpAddress();
					//VALIDA X CADA IP DE GESITÓN QUE TENGA EL ci DEL INVENTARIO DE LA TORRE
					for (int j= 0 ; j<ipAdressUnix.length && !hasMatchByIpAddress ;j++) {
						String ipGestionUnix=ipAdressUnix[j];
						//si ip de ucmdb es igual o esta cntenida en el listado deip de UCMDB. 
						if(ciUcmdbIpGestion.equals(ipGestionUnix)
								|| (ciUcmdbIpGestion.contains(ipGestionUnix+","))
								|| ((ciUcmdbIpGestion.endsWith(ipGestionUnix)))
								|| (ciUcmdbIpAddress.contains(ipGestionUnix+","))
								|| (ciUcmdbIpAddress.endsWith(ipGestionUnix))
								){
							
							scaleDaysAndstatusCredential=Libs.clasifyDaysLastAccessTimes(inventoryUCMDBhm.get(ciUnixcodHostname).getLastAccessTimeProtocolo(),Libs.PATTERN_DATE);
							scaleDays = scaleDaysAndstatusCredential[0][0];
							credentialStatus = scaleDaysAndstatusCredential[0][1];
							lastAccessTimeStatus=scaleDaysAndstatusCredential[0][2];
							checkAppyUCMDB=Libs.checkIfCiApplyUcmdb(ciUnix.getObservaciones());

							rowValue=(i+1)+""+'\u0009'+""+ciUnix.toString()+
									'\u0009'+inventoryUCMDBhm.get(ciUnix.getCodigoHostname()).toString()+
									'\u0009'+lastAccessTimeStatus+
									'\u0009'+credentialStatus+
									'\u0009'+scaleDays+
									'\u0009'+checkAppyUCMDB+
									'\u0009'+"OK";
							rowText.add(rowValue);
							System.out.println(rowValue);
							inventoryUCMDBhm.remove(ciUnix.getCodigoHostname()); //si se encuentra, lo quita para no tenerlo en cuenta en otros casos
							hasMatchByIpAddress=true;
						}						

					}
					//aqui validar si en el hashmap con duplicados esta el que tiene la ip correcta	
					if(!hasMatchByIpAddress && inventoryUCMDBhmDuplicates.containsKey(ciUnix.getCodigoHostname().toUpperCase())) {
						ciUcmdbIpGestion=inventoryUCMDBhmDuplicates.get(ciUnixcodHostname).getIpGestion();
						ciUcmdbIpAddress = inventoryUCMDBhmDuplicates.get(ciUnixcodHostname).getIpAddress();
						for (int j= 0 ; j<ipAdressUnix.length && !hasMatchByIpAddress ;j++) {
							String ipGestionUnix=ipAdressUnix[j];
							//si ip de ucmdb es igual o esta cntenida en el listado deip de UCMDB. 
							if(ciUcmdbIpGestion.equals(ipGestionUnix)
									|| (ciUcmdbIpGestion.contains(ipGestionUnix+","))
									|| ((ciUcmdbIpGestion.endsWith(ipGestionUnix)))
									|| (ciUcmdbIpAddress.contains(ipGestionUnix+","))
									|| (ciUcmdbIpAddress.endsWith(ipGestionUnix))
									){
								scaleDaysAndstatusCredential=Libs.clasifyDaysLastAccessTimes(inventoryUCMDBhm.get(ciUnixcodHostname).getLastAccessTimeProtocolo(),Libs.PATTERN_DATE);
								scaleDays = scaleDaysAndstatusCredential[0][0];
								credentialStatus = scaleDaysAndstatusCredential[0][1];
								lastAccessTimeStatus=scaleDaysAndstatusCredential[0][2];
								
								checkAppyUCMDB=Libs.checkIfCiApplyUcmdb(ciUnix.getObservaciones());

								rowValue=(i+1)+""+'\u0009'+""+ciUnix.toString()+
										'\u0009'+inventoryUCMDBhm.get(ciUnix.getCodigoHostname()).toString()+
										'\u0009'+lastAccessTimeStatus+
										'\u0009'+credentialStatus+
										'\u0009'+scaleDays+
										'\u0009'+checkAppyUCMDB+
										'\u0009'+"OK";
								rowText.add(rowValue);
								System.out.println(rowValue);
								inventoryUCMDBhm.remove(ciUnix.getCodigoHostname()); //si se encuentra, lo quita para no tenerlo en cuenta en otros casos
								hasMatchByIpAddress=true;
							}						

						}
					}
					if(!hasMatchByIpAddress) {
						//vALIDA X CODIGO_HOSTNAME
						scaleDaysAndstatusCredential=Libs.clasifyDaysLastAccessTimes(inventoryUCMDBhm.get(ciUnixcodHostname).getLastAccessTimeProtocolo(),Libs.PATTERN_DATE);
						scaleDays = scaleDaysAndstatusCredential[0][0];
						credentialStatus = scaleDaysAndstatusCredential[0][1];
						lastAccessTimeStatus=scaleDaysAndstatusCredential[0][2];

						checkAppyUCMDB=Libs.checkIfCiApplyUcmdb(ciUnix.getObservaciones());
						rowValue=(i+1)+""+'\u0009'+""+ciUnix.toString()+
								'\u0009'+inventoryUCMDBhm.get(ciUnix.getCodigoHostname()).toString()+
								'\u0009'+lastAccessTimeStatus+
								'\u0009'+credentialStatus+
								'\u0009'+scaleDays+
								'\u0009'+checkAppyUCMDB+
								'\u0009'+"PDT"+'\u0009'+"Validar, No coincide ip de gestion dada por inv torre";
						rowText.add(rowValue);
						System.out.println(rowValue);
					}
				}else {
					boolean match=false;
					//busca por ip
					for (CiUcmdbUnix ciUcmdbUnix : ucmdbInventary) {
						String errorMatch ="";
						if (ciUcmdbUnix.getIpGestion().equals(ciUnix.getIpGestion())
								|| ciUcmdbUnix.getIpGestion().contains(ciUnix.getIpGestion()+",")
								|| ciUcmdbUnix.getIpGestion().endsWith(ciUnix.getIpGestion())
								|| ciUcmdbUnix.getIpAddress().contains(ciUnix.getIpGestion()+",")
								|| ciUcmdbUnix.getIpAddress().endsWith(ciUnix.getIpGestion())
								){
							//1 obtener codigo de servicio , primer _ que encuentre antes del codhostname
							//2 obtener displayLabel, es lo que este despues de _ 
							String ciUnixServCode =  ciUnix.getCodigoServicio();
							String ciUnixDispLabel=  ciUnix.getHostname();
							boolean displayLabelAreEqual =ciUcmdbUnix.getDisplayLabel().equalsIgnoreCase(ciUnixDispLabel);
							boolean serviceCodeAreEqual=ciUcmdbUnix.getOnyxServiceCodes().equalsIgnoreCase(ciUnixServCode);
							
							if(displayLabelAreEqual && serviceCodeAreEqual ) {
								errorMatch="ok";
								inventoryUCMDBhm.remove(ciUnix.getCodHostname().toUpperCase()); //si se encuentra, lo quita para no tenerlo en cuenta en otros casos
							}
							else if(!displayLabelAreEqual && serviceCodeAreEqual) {
								errorMatch = "ok"+'\u0009'+"No coincide displayLabeL ";
								
								inventoryUCMDBhm.remove(ciUnix.getCodHostname().toUpperCase()); //si se encuentra, lo quita para no tenerlo en cuenta en otros casos

							}else if (displayLabelAreEqual && !serviceCodeAreEqual) {
								errorMatch = "PDT"+'\u0009'+"No coincide codigo de servicio ";

							}else if(ciUnix.getObservacionesRevision().contains("Posible ci duplicado en inv torre")) {
								errorMatch = "PDT,duplicado"+'\u0009'+"Posible duplicado en inv torre";
							}
							else {
								errorMatch = "PDT"+'\u0009'+"No coincide codigo de servicio ni displayLabel";
							}

							scaleDaysAndstatusCredential=Libs.clasifyDaysLastAccessTimes(ciUcmdbUnix.getLastAccessTimeProtocolo(),Libs.PATTERN_DATE);
							scaleDays = scaleDaysAndstatusCredential[0][0];
							credentialStatus = scaleDaysAndstatusCredential[0][1];
							lastAccessTimeStatus=scaleDaysAndstatusCredential[0][2];
							
							checkAppyUCMDB=Libs.checkIfCiApplyUcmdb(ciUnix.getObservaciones());

							rowValue=(i+1)+""+'\u0009'+""+ciUnix.toString()+
									'\u0009'+ciUcmdbUnix.toString()+
									'\u0009'+lastAccessTimeStatus+
									'\u0009'+credentialStatus+
									'\u0009'+scaleDays+
									'\u0009'+checkAppyUCMDB+
									'\u0009'+errorMatch;
							rowText.add(rowValue);
							System.out.println(rowValue);
							match=true;
							break;
						}
					}if(!match) {
						checkAppyUCMDB=Libs.checkIfCiApplyUcmdb(ciUnix.getObservaciones());
						rowValue = (i+1)+""+'\u0009'+""+ciUnix.toString()+'\u0009'+'\u0009'+
								'\u0009'+'\u0009'+'\u0009'+'\u0009'+'\u0009'+'\u0009'+"Sin last AccessTime"+'\u0009'+"Credencial PDT"+'\u0009'
								+"Mas de 60 Dias"+'\u0009'+checkAppyUCMDB+'\u0009'+"PDT";
						rowText.add(rowValue);
						System.out.println(rowValue);
					}
				}
			}			
		}else {
			throw new Exception("el inventario se encuentra vacio");
		}

		return rowText;
	}

	public static ArrayList<String>ucmdbInventaryMergeTowerInventary(ArrayList<CiUnix> unixInventary, ArrayList<CiUcmdbUnix> ucmdbInventary) throws Exception{
		System.out.println("");
		ArrayList<String>matchTowervsUcmdb=towerInventaryMergeUcmdbInventary(unixInventary,ucmdbInventary);
		ArrayList<String>rowMatchUcmdbVsTower=new ArrayList<>();
		//1)cruce de torre a UCMDB
		//2 fomrar llave primaria con los datos que haya hecho mach de cruce hacia ucmdb
		//3 buscar en el inventario umcbd los que hicieron match y traer las misma sobservaciones
		if(matchTowervsUcmdb.size()!=0) {
			//CREANDO HASHMAP DE LOS QUE HICIERON MATCH TORRE VS UCMDB
			HashMap<String, String> matchTowervsUcmdbhm= new HashMap<String, String>();
			int uidTowervsUcmdbPosition=0;
			int lastAccessTimeStatusPosition=0;
			int statusmatchVsUcmdbPosition=0;
			
			for (int i = 0; i<matchTowervsUcmdb.size();i++) {
				String[]rowMatch=matchTowervsUcmdb.get(i).split("\u0009");
				if(i==0) {
					uidTowervsUcmdbPosition=Arrays.asList(rowMatch).indexOf("UID");
					lastAccessTimeStatusPosition=matchTowervsUcmdb.get(i).indexOf("STATUS_LAST_ACCESS_TIME");
					statusmatchVsUcmdbPosition=Arrays.asList(rowMatch).indexOf("STATUS CRUCE CONTRA UCMDB");
				}
				String uidMatchTowervsUcmdb=rowMatch[uidTowervsUcmdbPosition];
				matchTowervsUcmdbhm.put(uidMatchTowervsUcmdb,matchTowervsUcmdb.get(i));
			}
			System.out.println(UCMDB_VS_TOWER_HEADER);
			rowMatchUcmdbVsTower.add(UCMDB_VS_TOWER_HEADER);
			//Recorriendo inventario UCMDB
			for(int i =0; i<ucmdbInventary.size();i++) {
				String uidUcmdbInventary=ucmdbInventary.get(i).getUid();
				
				String rowText="";
				String[][]scaleDaysAndstatusCredential=Libs.clasifyDaysLastAccessTimes(ucmdbInventary.get(i).getLastAccessTimeProtocolo(), Libs.PATTERN_DATE);
				
				String scaleDays = scaleDaysAndstatusCredential[0][0];
				String credentialStatus = scaleDaysAndstatusCredential[0][1];
				String lastAccessTimeStatus=scaleDaysAndstatusCredential[0][2];
				
				String rowLastAccessTime=lastAccessTimeStatus+"\u0009"+credentialStatus+"\u0009"+scaleDays;
				
				if(matchTowervsUcmdbhm.containsKey(uidUcmdbInventary)) {
					String [] townVsUcmdbDetail=matchTowervsUcmdbhm.get(uidUcmdbInventary).split("\u0009");
					if(townVsUcmdbDetail[statusmatchVsUcmdbPosition].toUpperCase().equals("OK")) {						
						rowText=ucmdbInventary.get(i)+"\u0009"+"administrado por la torre"+"\u0009"+rowLastAccessTime+"\u0009"+matchTowervsUcmdbhm.get(uidUcmdbInventary);
					}else {
						rowText=ucmdbInventary.get(i)+"\u0009"+"Validar,posible administrado por la torre "+"\u0009"+rowLastAccessTime+"\u0009"+matchTowervsUcmdbhm.get(uidUcmdbInventary);;
					}				
				}
				else{				
					rowText=ucmdbInventary.get(i)+"\u0009"+"No se encuentra en Inv torre"+"\u0009"+rowLastAccessTime;
				}
				rowMatchUcmdbVsTower.add(rowText);
				System.out.println(rowText);
			}
		}
		return rowMatchUcmdbVsTower;
	}

	public static ArrayList<String> matchInvUcmdbAndTownvsServiceManager(ArrayList<CiUnix> unixInventary, ArrayList<CiUcmdbUnix> ucmdbInventary,String serviceManagerPath) throws Exception{
		ArrayList<String>matchUcmdbVsTower=ucmdbInventaryMergeTowerInventary(unixInventary,ucmdbInventary);
		ArrayList<String>rowMatchUcmdbVsTower=new ArrayList<>();
		ArrayList<String>serviceManagerInventaryr=new ArrayList<>();
		
		//LEER INVENTARIO SM		
		FileInputStream fileSm= new FileInputStream(new File(TOWER_VS_UCMDB_HEADER));
		//leer archivo excel
		XSSFWorkbook smWb= new XSSFWorkbook(fileSm);
		//leer hoja excel
		try {
			
		}catch (Exception ex) {
			System.out.println("");
		}
		HashMap <String,String> matchUcmdbVsTowerhm= new HashMap<>();
		
		
		
		return rowMatchUcmdbVsTower;
	}
        /*
	public static void main(String[] args) {
		CruceUnix cu= new CruceUnix();
		String ucmdbFilePath = UCMDB_FILE_PATH;
		String unixFilePath = UNIX_FILE_PATH;
		try {
			ArrayList<String> rowTextTowerVsUcmdb=towerInventaryMergeUcmdbInventary(readUnixInventary(unixFilePath), readUcmdbInventory(ucmdbFilePath));
			ArrayList<String> rowTextUcmdbvsTower=ucmdbInventaryMergeTowerInventary(readUnixInventary(unixFilePath), readUcmdbInventory(ucmdbFilePath));
			Libs.writeBook(rowTextTowerVsUcmdb,"\u0009","CruceUNIX2","Hoja1","D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Unix\\");
			Libs.writeBook(rowTextUcmdbvsTower,"\u0009","CruceUCMDBvsUnix","Hoja1","D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Unix\\");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}

