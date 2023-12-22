package com.test.controller;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
import java.util.Scanner;
import com.test.model.CiUcmdbWindowsColumns;
import com.test.model.CiWindowsColums;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.test.exceptions.InventoryException;
import com.test.model.CiUcmdbWindows;
import com.test.model.CiWindows;
import com.test.utils.Libs;



public class CruceWindows {
	// VALIDAR UCMDB VS TORRE, DUPLICADOS, UNO OK, OTRO ERROR.
	private static final String TOWER_VS_UCMDB_HEADER="#"+'\u0009'+"ADM_RESPONSABLE"+'\u0009'+"CLIENTE"+'\u0009'+"COD_HOSTNAME"+'\u0009'+"COD_SERVICIO"+'\u0009'+"HOSTNAME"+'\u0009'+"SO"+'\u0009'+"VER_SO"+'\u0009'+"TIPO"+'\u0009'+"IP-MGMT"+'\u0009'+"IP-PROD"+'\u0009'+"IP-ILO"+'\u0009'+"SERVICIO / ROL"+'\u0009'+"ESCALAMIENTO - APP"+'\u0009'+"MONITOREO"+'\u0009'+"MARCA | MODELO"+'\u0009'+"SERIAL"+'\u0009'+"UBICACION"+'\u0009'+"FECHA RECEPCION"+'\u0009'+"FECHA RETIRO"+'\u0009'+"ESTADO"+'\u0009'+"OBSERVACIONES TORRE"+'\u0009'+"Created"+'\u0009'+"_Path"+'\u0009'+"_Item Type"+'\u0009'+"OBSERVACIONES REVISION"+
			'\u0009'+"UID"+'\u0009'+"SERVICECODE"+'\u0009'+"DISPLAYLABEL"+'\u0009'+"IP_GESTION"+'\u0009'+"IP_ADDRESS"+'\u0009'+"PROTOCOLO_DESCUBRIMIENTO"+'\u0009'+"LAST_ACCESS_TIME"+'\u0009'+"STATUS_LAST_ACCESS_TIME"+'\u0009'+"STATUS CREDENCIAL"+'\u0009'+"ESCALA DIAS"+ '\u0009'+ "APLICA UCMDB"+'\u0009'+"STATUS CRUCE CONTRA UCMDB"+'\u0009'+"OBSERVACIONES CRUCE CONTRA UCMDB";

	private static final String UCMDB_VS_TOWER_HEADER="UID"+'\u0009'+"SERVICECODE"+'\u0009'+"DISPLAYLABEL"+'\u0009'+"IP_GESTION"+'\u0009'+"IP_ADDRESS"+'\u0009'+"PROTOCOLO_DESCUBRIMIENTO"+'\u0009'+"LAST_ACCESS_TIME"+'\u0009'+"STATUS CRUCE CONTRA TORRE"+'\u0009'+"STATUS_LAST_ACCESS_TIME"+'\u0009'+"STATUS CREDENCIAL"+'\u0009'+"ESCALA DIAS"+
			'\u0009'+TOWER_VS_UCMDB_HEADER;

	private static final String windowsFilePath ="D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Windows\\inv windows torre.xlsx";
	private static final String ucmdbFilePath = "D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Windows\\inv windows ucmdb.xlsx";
	private static final Long RANGE_DAYS_LAST_ACCESS_TIME = (long) 15;

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy HH'H'mm'' z");

	//mi�rcoles 27 de abril de 2022 17H26' COT
	//private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	//PDT CONTROLAR DUPLICADOS!!!!
        /*
	public static void main(String[] args) {
		try {
			//windowsFilePath = "D:\\ECM3200I\\Desktop\\inv torre.xlsx";
			//ucmdbFilePath = "D:\\ECM3200I\\Desktop\\inv ucmdb 2020.xlsx";

			Scanner sc = new Scanner (System.in);
			byte option=1;
			menu();
			//while (sc.hasNext() && (option>0 && option<3)) {
				option =Byte.parseByte(sc.nextLine());

				if(option == 1) {
					List <String>rowTextTowerVsUcmdb=inventaryWindowsContainsCodHostAndIP(readWindowsInventary(windowsFilePath), readUcmdbInventory(ucmdbFilePath));
					Libs.writeBook((ArrayList<String>) rowTextTowerVsUcmdb,"\u0009","CruceWindows","Hoja1","D:\\OneDrive - GLOBAL HITSS\\Automatizmos\\ArchivosParaPowerBi\\Windows\\");
					//PDT validar duplicados de inv ucmdb y torre

				}else if (option ==2) {
					//cruceUCDMBWindows(readWindowsInventary(windowsFilePath), readUcmdbInventory(ucmdbFilePath));
					//PDT CRUCE DE ucmdb a inv torre.
				}
				menu();
			//}

			sc.close();
		}catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error" + ex);
		}
	}
*/
	public static void menu() {
		System.out.println("1 = Cruce de inventario torre vs UCMDB");
		System.out.println("2 = Cruce de UCMDB vs Inventario torre");
		System.out.println("0 = Salir");
	}



	public static ArrayList<CiUcmdbWindows> readUcmdbInventory(String ucmdbFilePath) {
		ArrayList<CiUcmdbWindows> ucmdbWindowsInventary = new ArrayList<>();

		try(FileInputStream file = new FileInputStream(new File(ucmdbFilePath));
				XSSFWorkbook workbook = new XSSFWorkbook(file);){
			XSSFSheet ucmdbSheet = workbook.getSheetAt(0);
			Iterator<Row> rowUcmdbIterator = ucmdbSheet.iterator();
			ArrayList<String> header = new ArrayList<String>();

			while (rowUcmdbIterator.hasNext()) { // recorre filas
				CiUcmdbWindows ciUcmdbWindows = new CiUcmdbWindows();
				Row rowUcmdb = rowUcmdbIterator.next();
				short cellCount = rowUcmdb.getLastCellNum();
				for (short currentCell = 0; currentCell < cellCount; currentCell++) { // recorre las columbas del inv
					String cellValue = "";
					cellValue = Libs.getStringCellValue(rowUcmdb.getCell(currentCell));
					if (rowUcmdb.getRowNum() == 0) { // Guarda el encabezado
						header.add(cellValue);
						//System.out.print(cellValue+"|");
					} else {
						String headerValue=header.get(currentCell);	
						CiUcmdbWindowsColumns columnValue=CiUcmdbWindowsColumns.valueOfLabel(headerValue);
						if(columnValue!=null ) { //En caso de que alguna columna no este definida en "ciUCMDBColumbs
							switch (columnValue) { // Busca en el encabezado y en la clase enum los titulos y guarda el valor a la columna que se necesita.
							// columna que se necesita.
							case UCMDB_DISPLAY_LABEL:
								ciUcmdbWindows.setDisplayLabel(cellValue);
								break;
							case UCMDB_SERVICE_CODE:
								ciUcmdbWindows.setOnyxServiceCodes(cellValue);
								break;
							case UCMDB_IP_GESTION:
								ciUcmdbWindows.setIpGestion(cellValue);
								break;
							case UCMDB_IP_ADDRESS:
								ciUcmdbWindows.setIpAddress(cellValue);
								break;
							case UCMDB_DISCOVERY_PROTOCOL:
								ciUcmdbWindows.setProtocoloDescubrimiento(cellValue);
								break;
							case UCMDB_LAST_ACCESS_TIME:
								ciUcmdbWindows.setLastAccessTimeProtocolo(cellValue);
								break;
							case UCMDB_OWNER_GROUP:
								ciUcmdbWindows.setOwnerGroup(cellValue);
								break;
							case UCMDB_SERVICE_CATALOG:
								ciUcmdbWindows.setServiceCatalog(cellValue);
								break;
							case UCMDB_NODE_IS_VIRTUAL:
								ciUcmdbWindows.setNodeIsVirtual(cellValue);
								break;
							}
						}
					}
				}if(ciUcmdbWindows.getDisplayLabel()!=null){
					/*
					if(ciUcmdbWindows.getIpGestion().isEmpty()){
						
						if((!ciUcmdbWindows.getIpAddress().isEmpty() && Libs.checkIpAddressOK(ciUcmdbWindows.getIpAddress()))) {
							//ciUcmdbWindows.setIpGestion(ciUcmdbWindows.getIpAddress());					
						}else {
							//ciUcmdbWindows.setIpGestion("Sin ip gesti�n valida en UCMDB para hacer cruce");
						}
					}*/
					if(ciUcmdbWindows.getDisplayLabel()!=null){
						ucmdbWindowsInventary.add(ciUcmdbWindows);
					}
				}
			}
			System.out.println("CANTIDAD REGISTROS EN UCMDB 2020 "+ucmdbWindowsInventary.size());

		} catch (Exception ex) {
			System.out.println(ex);
		}
		return ucmdbWindowsInventary;
	}


	public static ArrayList<CiWindows> readWindowsInventary(String windowsFilePath) {
		ArrayList<CiWindows> windowsInventary= new ArrayList<>();

		try (FileInputStream fileWindows = new FileInputStream(new File(windowsFilePath));
				XSSFWorkbook workbookWindows = new XSSFWorkbook(fileWindows);
				){
			XSSFSheet windowsSheet = workbookWindows.getSheetAt(0);
			Iterator<Row> rowIterator = windowsSheet.iterator();
			ArrayList<String> header = new ArrayList<String>();
			String cellWindowsValue = "";
			short cellCount=0;			
			while (rowIterator.hasNext()) { // recorre filas de archvowindows
				Row row = rowIterator.next();
				Iterator<Cell> cellWindowsIterator = row.cellIterator();
				CiWindows ciWindows = new CiWindows();
				cellCount = row.getLastCellNum();
				for (short currentCell = 0; currentCell < cellCount; currentCell++) { // recorre las columbas del inv					
					cellWindowsValue = Libs.getStringCellValue(cellWindowsIterator.next());
					if (row.getRowNum() == 0) {
						header.add(cellWindowsValue);
					}else {
						String headerValue=header.get(currentCell);	
						CiWindowsColums columnValue=CiWindowsColums.valueOfLabel(headerValue);//TRAE EL VALOR col_ENUM
						if(columnValue!=null ) {
							switch (columnValue) { // Busca en el encabezado los titulos y guarda el valor a la	columna que se necesita.
							case CLIENTE:
								ciWindows.setCliente(cellWindowsValue);
								break;
							case TIPO_PASO:
								ciWindows.setTipoPaso(cellWindowsValue);
								break;
							case ADM_RESPONSABLE:
								ciWindows.setAdmResponsable(cellWindowsValue);
								break;
							case ADM_BACKUP:
								ciWindows.setAdm_backup(cellWindowsValue);
								break;
							case COD_HOSTNAME:
								ciWindows.setCodHostname(Libs.checkAndFormatServCodeAndHostname(cellWindowsValue));
								break;
							case HOSTNAME:
								ciWindows.setHostname(cellWindowsValue);
								break;
							case SO:
								ciWindows.setSo(cellWindowsValue);
								break;
							case COD_SERVICIO:
								ciWindows.setCodServicio(cellWindowsValue);
								break;
							case IP_GESTION:
								ciWindows.setIpGestion(Libs.checkAndFormatIpAddress(cellWindowsValue));						
								break;
							case FECHA_RECEPCION:
								ciWindows.setFechaRecepcion(cellWindowsValue);;
								break;
							case FECHA_RETIRO:
								ciWindows.setFechaRetiro(cellWindowsValue);;
								break;
							case ESTADO:
								ciWindows.setEstado(cellWindowsValue);
								break;
							case OBSERVACIONES:
								ciWindows.setObservaciones(cellWindowsValue);
								break;
							case TIPO:
								ciWindows.setTipo(cellWindowsValue);
								break;
							case ITEM_TYPE:
								ciWindows.setItemType(cellWindowsValue);
								break;
							case PATH:
								ciWindows.setPath(cellWindowsValue);
								break;								
							default :
								break;
							}
						}
					}
				}
				if(ciWindows.getCodHostname()!=null){
					windowsInventary.add(ciWindows);
				}
			}

			System.out.println("CANTIDAD REGISTROS EN INVENTARIO TORRE "+windowsInventary.size());

		} catch (Exception ex) {
			System.err.println("ERROR inv torre: "+ex);
		}
		return windowsInventary;
	}

	public static CiUcmdbWindows getRecentCi(CiUcmdbWindows firstCiUmcbd, CiUcmdbWindows secondCiUmcbd){
		CiUcmdbWindows currentCi = null;
		String [] lastAccesTimeFirstCi =firstCiUmcbd.getLastAccessTimeProtocolo().split(", ");
		String [] lastAccessTimeSecondCi =secondCiUmcbd.getLastAccessTimeProtocolo().split(", ");
		LocalDateTime maxLastAccessTime = LocalDateTime.MIN;
		LocalDateTime maxFirstLastAccessTime;
		LocalDateTime maxSecondLastAccessTime;
		//BUG AL LEER , DETECTA UN * DONDE NO HAY NADA
		//mirar los LAT de los datos que ya estan, debe tomar el qe tenga el lat ma reciente. 
		if(firstCiUmcbd.getLastAccessTimeProtocolo().length()==0 && secondCiUmcbd.getLastAccessTimeProtocolo().length()>0) {
			currentCi=secondCiUmcbd;
		}else if(firstCiUmcbd.getLastAccessTimeProtocolo().length() >0 && secondCiUmcbd.getLastAccessTimeProtocolo().length()==0) {
			currentCi=firstCiUmcbd;
		}else if(firstCiUmcbd.getLastAccessTimeProtocolo().length() >0 && secondCiUmcbd.getLastAccessTimeProtocolo().length()>0 ) {
			//validar cual esta actualizada,
			//			LocalDateTime maxLastAccessTime = LocalDateTime.MIN;
			//TRAER EL OBJETO CON EL MAYOR LAST ACCESS TIME
			//mi�rcoles 27 de abril de 2022 17H26' COT
			//System.out.println("entro" +secondCiUmcbd.getDisplayLabel() );
			for(String firstLastAccesstime : lastAccesTimeFirstCi) { //TRAER EL MAYOR LASTACCESS TIME DEL OBJ1
				LocalDateTime temporalDateTime = LocalDateTime.parse(firstLastAccesstime, formatter);
				//LocalDateTime temporalDateTime = LocalDateTime.parse(firstLastAccesstime, formatter);
				//https://www.campusmvp.es/recursos/post/como-manejar-correctamente-fechas-en-java-el-paquete-java-time.aspx
				if(temporalDateTime.isAfter(maxLastAccessTime)) {
					maxLastAccessTime= temporalDateTime;
				}
			}
			maxFirstLastAccessTime= maxLastAccessTime;
			maxLastAccessTime = LocalDateTime.MIN;
			for(String secondLastAccesstime : lastAccessTimeSecondCi) { //TRAER El MAYOR LASTACCESS TIME DEL OBJ2
				LocalDateTime temporalDateTime = LocalDateTime.parse(secondLastAccesstime, formatter);
				if(temporalDateTime.isAfter(maxLastAccessTime)) {
					maxLastAccessTime= temporalDateTime;
				}
			}
			maxSecondLastAccessTime= maxLastAccessTime;

			if(maxFirstLastAccessTime.isAfter(maxSecondLastAccessTime)) { //compara los mayores de ambos LAT
				currentCi=firstCiUmcbd;
			}else {
				currentCi=secondCiUmcbd;
			}



		}

		return currentCi;
	}


	public static String checkLastAccessTime(String lastAccessTime) {
		String lastAccessTimeisOld = "Sin last Access Time"+'\u0009'+"Credencial PDT";

		if (lastAccessTime.length()>0) {
			String [] lastAccess = lastAccessTime.split(", ");
			LocalDateTime limitRecentAccessTime = LocalDateTime.now().minusDays(RANGE_DAYS_LAST_ACCESS_TIME);
			//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy",  Locale.ENGLISH);

			LocalDateTime maxLastAccessTime = LocalDateTime.MIN;

			for (String date:lastAccess) {
				LocalDateTime temporalDateTime = LocalDateTime.parse(date, formatter);	
				if(temporalDateTime.isAfter(maxLastAccessTime)) {
					maxLastAccessTime= temporalDateTime;
				}
			}
			if(maxLastAccessTime.isBefore(limitRecentAccessTime)) {
				lastAccessTimeisOld ="Last Access Time Antiguo"+'\u0009'+"Credencial PDT";
			}else {
				lastAccessTimeisOld ="Last Access Time Reciente"+'\u0009'+"Credencial OK";
			}

		}
		return lastAccessTimeisOld;
	}


	//BUG, SI VALIDA EL PRIMER DATO Y ENCONTRA UN MATCH APROXIMADO, EN CASO DE QUE LEUGO ESTE EL DATO VERDADERO, NO LO CONTARA. 
	public static List <String> inventaryWindowsContainsCodHostAndIP(ArrayList<CiWindows> windowsInventary, ArrayList<CiUcmdbWindows> inventoryUCMDB) {
		List<String> inventaryWindowsvsUcmdb=new ArrayList<>();
		String lastAccessTimeStatus="";
		String credentialStatus="";
		String scaleDays="";
		String [][]scaleDaysAndstatusCredential;
		
		if(windowsInventary.size()==0 && inventoryUCMDB.size()==0) {
			throw new InventoryException("El inventario se encuentra vacio"); 
		}else {
			//System.out.println(windowsInventary.size());
			HashMap<String, CiUcmdbWindows> inventoryUCMDBhm = new HashMap<>(); //AQI SE PUEDE MIRAR E IDENTIFICAR LOS DUPLICADOS.
			HashMap<String, CiUcmdbWindows> inventoryUCMDBhmDuplicates = new HashMap<>(); //AQI SE PUEDE MIRAR E IDENTIFICAR LOS DUPLICADOS.

			for (CiUcmdbWindows ciUcmdbWindows : inventoryUCMDB) {
				if(inventoryUCMDBhm.get(ciUcmdbWindows.getOnyxServiceCodes()+"_"+ciUcmdbWindows.getDisplayLabel().toUpperCase())== null) {
					inventoryUCMDBhm.put(ciUcmdbWindows.getOnyxServiceCodes()+"_"+ciUcmdbWindows.getDisplayLabel().toUpperCase(), ciUcmdbWindows);
				}else {
					//en caso de que ya exista la clave, se debe insertar en el hashmap la que este mas actcualizada- 
					CiUcmdbWindows secondCiUmcbd = ciUcmdbWindows;
					CiUcmdbWindows firstCiUmcbd = inventoryUCMDBhm.get(ciUcmdbWindows.getOnyxServiceCodes()+"_"+ciUcmdbWindows.getDisplayLabel().toUpperCase());
					CiUcmdbWindows recentCiUcmdb = getRecentCi(firstCiUmcbd,secondCiUmcbd );

					String recentUcmdbCodHostname=recentCiUcmdb.getOnyxServiceCodes().toUpperCase()+"_"+recentCiUcmdb.getDisplayLabel().toUpperCase();
					String firstCiUmcbdCodHostname=firstCiUmcbd.getOnyxServiceCodes().toUpperCase()+"_"+firstCiUmcbd.getDisplayLabel().toUpperCase();
					String secondCiUmcbdCodHostname=secondCiUmcbd.getOnyxServiceCodes().toUpperCase()+"_"+secondCiUmcbd.getDisplayLabel().toUpperCase();


					inventoryUCMDBhm.replace(recentCiUcmdb.getOnyxServiceCodes()+"_"+recentCiUcmdb.getDisplayLabel().toUpperCase(), recentCiUcmdb);
					///rEEEMPLAZAR POR EL CI QUE ENTREGUE EL METOO GETrECENTci
					if(recentCiUcmdb.equals(firstCiUmcbd)) {
						inventoryUCMDBhmDuplicates.put(secondCiUmcbdCodHostname, secondCiUmcbd);
					}else {
						inventoryUCMDBhmDuplicates.put(firstCiUmcbdCodHostname, firstCiUmcbd);
					}					
				}
			}
			String headerTemp="#"+'\u0009'+"COD_HOSTNAME"+'\u0009'+"IP_GESTION"+'\u0009'+"ESTADO"+'\u0009'+"TIPO"+'\u0009'+"CLIENTE"+'\u0009'+"OBSERVACION TORRE"+
					'\u0009'+"SERVICECODE"+'\u0009'+"DISPLAYLABEL"+'\u0009'+"IP_GESTION"+'\u0009'+"IP_ADDRESS"+'\u0009'+"PROTOCOLO_DESCUBRIMIENTO"+'\u0009'+"LAST_ACCESS_TIME"+'\u0009'+"STATUS_LAST_ACCESS_TIME"+'\u0009'+"STATUS CREDENCIAL"+'\u0009'+"ESCALA DIAS"+'\u0009'+"STATUS CRUCE CONTRA UCMDB"+'\u0009'+"OBSERVACIONES";
					
			System.out.println(
					headerTemp
					);
			inventaryWindowsvsUcmdb.add(headerTemp);
			
			//se puede usar eso e for key, value, y que primero valide por key, si no cumple valide por value.
			for ( int i = 0 ; i<windowsInventary.size();i++) {
				String lastAccessTimeisOld="";
				CiWindows ciWindows = windowsInventary.get(i);
				boolean hasMatchByIpAddress=false;
				if(inventoryUCMDBhm.containsKey(ciWindows.getCodHostname().toUpperCase())){
					if(inventoryUCMDBhm.get(ciWindows.getCodHostname().toUpperCase()).getIpGestion().equals(ciWindows.getIpGestion())
							|| (inventoryUCMDBhm.get(ciWindows.getCodHostname().toUpperCase()).getIpGestion().contains(ciWindows.getIpGestion()+","))
							|| ((inventoryUCMDBhm.get(ciWindows.getCodHostname().toUpperCase()).getIpGestion().endsWith(ciWindows.getIpGestion())))
							|| (inventoryUCMDBhm.get(ciWindows.getCodHostname().toUpperCase()).getIpAddress().contains(ciWindows.getIpGestion()+","))
							|| (inventoryUCMDBhm.get(ciWindows.getCodHostname().toUpperCase()).getIpAddress().endsWith(ciWindows.getIpGestion()))


							){
						lastAccessTimeisOld = checkLastAccessTime(inventoryUCMDBhm.get(ciWindows.getCodHostname().toUpperCase()).getLastAccessTimeProtocolo());
						
						scaleDaysAndstatusCredential=Libs.clasifyDaysLastAccessTimes(inventoryUCMDBhm.get(ciWindows.getCodHostname().toUpperCase()).getLastAccessTimeProtocolo(),Libs.PATTERN_DATE);
						scaleDays = scaleDaysAndstatusCredential[0][0];
						credentialStatus = scaleDaysAndstatusCredential[0][1];
						lastAccessTimeStatus=scaleDaysAndstatusCredential[0][2];
						
						inventaryWindowsvsUcmdb.add((i+1)+""+'\u0009'+""+ciWindows.toString()+
								'\u0009'+inventoryUCMDBhm.get(ciWindows.getCodHostname().toUpperCase()).toString()+
								'\u0009'+lastAccessTimeStatus+'\u0009'+credentialStatus+'\u0009'+scaleDays+
								'\u0009'+"OK"+'\u0009'+"");

						
						System.out.println((i+1)+""+'\u0009'+""+ciWindows.toString()+
								'\u0009'+inventoryUCMDBhm.get(ciWindows.getCodHostname().toUpperCase()).toString()+
								'\u0009'+lastAccessTimeStatus+'\u0009'+credentialStatus+'\u0009'+scaleDays+
								'\u0009'+"OK"+'\u0009'+"");
						inventoryUCMDBhm.remove(ciWindows.getCodHostname().toUpperCase()); //si se encuentra, lo quita para no tenerlo en cuenta en otros casos
						hasMatchByIpAddress=true;


					}else {
						lastAccessTimeisOld = checkLastAccessTime(inventoryUCMDBhm.get(ciWindows.getCodHostname().toUpperCase()).getLastAccessTimeProtocolo());
						System.out.println((i+1)+""+'\u0009'+""+ciWindows.toString()+
								'\u0009'+inventoryUCMDBhm.get(ciWindows.getCodHostname().toUpperCase()).toString()+
								'\u0009'+lastAccessTimeStatus+'\u0009'+credentialStatus+'\u0009'+scaleDays+
								'\u0009'+"PDT"+'\u0009'+"Validar, No coincide ip de gestion dada por inv torre");
						
						inventaryWindowsvsUcmdb.add((i+1)+""+'\u0009'+""+ciWindows.toString()+
								'\u0009'+inventoryUCMDBhm.get(ciWindows.getCodHostname().toUpperCase()).toString()+
								'\u0009'+lastAccessTimeStatus+'\u0009'+credentialStatus+'\u0009'+scaleDays+
								'\u0009'+"PDT"+'\u0009'+"Validar, No coincide ip de gestion dada por inv torre");
					}

				}
				else {
					boolean match=false;
					//busca por ip
					for (CiUcmdbWindows ciUcmdbWindows : inventoryUCMDB) {
						String errorMatch ="";
						if (ciUcmdbWindows.getIpGestion().equals(ciWindows.getIpGestion())
								|| ciUcmdbWindows.getIpGestion().contains(ciWindows.getIpGestion()+",")
								|| ciUcmdbWindows.getIpGestion().endsWith(ciWindows.getIpGestion())
								|| ciUcmdbWindows.getIpAddress().contains(ciWindows.getIpGestion()+",")
								|| ciUcmdbWindows.getIpAddress().endsWith(ciWindows.getIpGestion())

								){

							//1 obtener codigo de servicio , primer _ que encuentre antes del codhostname
							//2 obtener displayLabel, es lo que este despues de _ 
							ciWindows.getCodHostname().indexOf('_');
							String ciWindowsServCode =  ciWindows.getCodHostname().substring(0, ciWindows.getCodHostname().indexOf('_'));							
							String ciWindowsDispLabel=  ciWindows.getCodHostname().substring(ciWindows.getCodHostname().indexOf('_')+1,ciWindows.getCodHostname().length());
							if((!ciUcmdbWindows.getDisplayLabel().equalsIgnoreCase(ciWindowsDispLabel)) && (ciUcmdbWindows.getOnyxServiceCodes().equalsIgnoreCase(ciWindowsServCode))) {
								errorMatch = "ok"+'\u0009'+"Validar, Coincide Ip de gestion y cod de servicio pero no coincide displayLabeL ";
								inventoryUCMDBhm.remove(ciWindows.getCodHostname().toUpperCase()); //si se encuentra, lo quita para no tenerlo en cuenta en otros casos


							}else if ((ciUcmdbWindows.getDisplayLabel().equalsIgnoreCase(ciWindowsDispLabel)) && !(ciUcmdbWindows.getOnyxServiceCodes().equalsIgnoreCase(ciWindowsServCode))) {
								errorMatch = "PDT"+'\u0009'+"Validar Modelado, Coincide Ip de gestion  y displayLabeL pero no coincide codigo de servicio ";
							}else {
								errorMatch = "PDT"+'\u0009'+"Validar Modelado, Coincide Ip de gestion y no coincide codigo de servicio ni displayLabel";
							}
							lastAccessTimeisOld = checkLastAccessTime(ciUcmdbWindows.getLastAccessTimeProtocolo());
							
							scaleDaysAndstatusCredential=Libs.clasifyDaysLastAccessTimes(ciUcmdbWindows.getLastAccessTimeProtocolo(),Libs.PATTERN_DATE);
							scaleDays = scaleDaysAndstatusCredential[0][0];
							credentialStatus = scaleDaysAndstatusCredential[0][1];
							lastAccessTimeStatus=scaleDaysAndstatusCredential[0][2];
							System.out.println((i+1)+""+'\u0009'+""+ciWindows.toString()+
									'\u0009'+ciUcmdbWindows.toString()+
									'\u0009'+lastAccessTimeStatus+'\u0009'+credentialStatus+'\u0009'+scaleDays+
									'\u0009'+errorMatch
									);
							inventaryWindowsvsUcmdb.add((i+1)+""+'\u0009'+""+ciWindows.toString()+
									'\u0009'+ciUcmdbWindows.toString()+
									'\u0009'+lastAccessTimeStatus+'\u0009'+credentialStatus+'\u0009'+scaleDays+
									'\u0009'+errorMatch
									);
							match=true;
							break;
						}
					}
					if(!match) {
						System.out.println((i+1)+""+'\u0009'+""+ciWindows.getCodHostname()+
								'\u0009'+ciWindows.getIpGestion()+'\u0009'+ciWindows.getEstado()+'\u0009'+ciWindows.getTipo()+'\u0009'+ciWindows.getCliente()+'\u0009'+ciWindows.getObservaciones()+'\u0009'+'\u0009'+'\u0009'+'\u0009'+'\u0009'+'\u0009'+'\u0009'+"Sin last Access Time"+'\u0009'
								+"Credencial PDT"+'\u0009'+"Mas de 60 Dias"+'\u0009'+"PDT"+'\u0009'+"No se logra identificar match con UCMDB");
						
						inventaryWindowsvsUcmdb.add((i+1)+""+'\u0009'+""+ciWindows.getCodHostname()+
								'\u0009'+ciWindows.getIpGestion()+'\u0009'+ciWindows.getEstado()+'\u0009'+ciWindows.getTipo()+'\u0009'+ciWindows.getCliente()+'\u0009'+ciWindows.getObservaciones()+'\u0009'+'\u0009'+'\u0009'+'\u0009'+'\u0009'+'\u0009'+'\u0009'+"Sin last Access Time"+'\u0009'
								+"Credencial PDT"+'\u0009'+"Mas de 60 Dias"+'\u0009'+"PDT"+'\u0009'+"No se logra identificar match con UCMDB");
					
					}

				}
			}

		}
		return inventaryWindowsvsUcmdb;
	}


	public static void cruceUCDMBWindows(ArrayList<CiWindows> windowsInventary, ArrayList<CiUcmdbWindows> inventoryUCMDB) {
		if(windowsInventary.size()==0 && inventoryUCMDB.size()==0) {
			throw new InventoryException("El inventario se encuentra vacio"); 
		}else {
			//System.out.println(windowsInventary.size()); //DETECTAR DUPLICADO Y MARCAR COMO DUPLICADO EL QUE TENGA LAT ANTIGUO. 
			HashMap<String, CiWindows> inventoryWinowshm = new HashMap<>();
			for (CiWindows ciWindows : windowsInventary) {
				inventoryWinowshm.put(ciWindows.getCodHostname().toUpperCase(),ciWindows);
				//creando hashmap de inv windows
				//clave. codserv, name, 
			}


			System.out.println("#|COD_HOSTNAME (inv torre)|IP_GESTION (inv torre)|SERVICECODE|DISPLAYLABEL (UCMDB)|IP_GESTION|IP_ADDRESS|PROTOCOLO_DESCUBRIMIENTO|LAST_ACCESS_TIME|STATUS_LAST_ACCESS_TIME|STATUS CREDENCIAL|STATUS CRUCE CONTRA INV TORRE|OBSERVACIONES");
			for ( int i = 0 ; i<inventoryUCMDB.size();i++) {
				String lastAccessTimeisOld="";
				CiUcmdbWindows ciUcmdbWindows = inventoryUCMDB.get(i);
				boolean ipMatch=false;
				if(inventoryWinowshm.containsKey(ciUcmdbWindows.getOnyxServiceCodes().toUpperCase()+"_"+ciUcmdbWindows.getDisplayLabel().toUpperCase()) ) {
					//se valida IP de gesiti�n //hacer un split de la ip
					String [] ucmdbipGestion  =  ciUcmdbWindows.getIpGestion().split(", ");
					String [] ucmdbipAdress  =  ciUcmdbWindows.getIpAddress().split(", ");
					for (int j = 0 ; j< ucmdbipAdress.length;j++) {
						if(inventoryWinowshm.get(ciUcmdbWindows.getOnyxServiceCodes().toUpperCase()+"_"+ciUcmdbWindows.getDisplayLabel().toUpperCase()).getIpGestion().equals(ucmdbipAdress[j])) { //mira si las ips son iguales, con cada una de ip adress							ipMatch=true;
							ipMatch=true;
							//si el dato no esta marcado como duplicado, continue con ete ciclo
							lastAccessTimeisOld = checkLastAccessTime(ciUcmdbWindows.getLastAccessTimeProtocolo());
							System.out.println((i+1)+
									"|"+inventoryWinowshm.get(ciUcmdbWindows.getOnyxServiceCodes().toUpperCase()+"_"+ciUcmdbWindows.getDisplayLabel().toUpperCase()).getCodHostname()+
									"|"+inventoryWinowshm.get(ciUcmdbWindows.getOnyxServiceCodes().toUpperCase()+"_"+ciUcmdbWindows.getDisplayLabel().toUpperCase()).getIpGestion()+
									"|"+ciUcmdbWindows.toString()+
									"|"+lastAccessTimeisOld+
									"|Administrado");

						}						
					}if(!ipMatch ) {
						lastAccessTimeisOld = checkLastAccessTime(ciUcmdbWindows.getLastAccessTimeProtocolo());
						System.out.println((i+1)+
								"|"+inventoryWinowshm.get(ciUcmdbWindows.getOnyxServiceCodes()+"_"+ciUcmdbWindows.getDisplayLabel().toUpperCase()).getCodHostname()+
								"|"+inventoryWinowshm.get(ciUcmdbWindows.getOnyxServiceCodes()+"_"+ciUcmdbWindows.getDisplayLabel().toUpperCase()).getIpGestion()+		
								"|"+ciUcmdbWindows.toString()+
								"|"+lastAccessTimeisOld+
								"|No Administrado|Validar, No coincide ip de gestion dada por inv torre");						
					}


				}else{
					boolean match=false;
					for (CiWindows ciWindows : windowsInventary) {
						String errorMatch ="";
						String [] ucmdbipAdress  =  ciUcmdbWindows.getIpAddress().split(", ");
						for (int j = 0 ; j< ucmdbipAdress.length;j++) {
							if(ciWindows.getIpGestion().equals(ucmdbipAdress[j])) { //mira si las ips son iguales, con cada una de ip adress						
								String ciWindowsServCode =  ciWindows.getCodHostname().substring(0, ciWindows.getCodHostname().indexOf('_'));
								String ciWindowsDispLabel=  ciWindows.getCodHostname().substring(ciWindows.getCodHostname().indexOf('_'),ciWindows.getCodHostname().length());
								if((!ciUcmdbWindows.getDisplayLabel().equalsIgnoreCase(ciWindowsDispLabel)) && (ciUcmdbWindows.getOnyxServiceCodes().equalsIgnoreCase(ciWindowsServCode))) {
									//if((!ciUcmdbWindows.getDisplayLabel().equalsIgnoreCase(ciWindowsDispLabel)) && (ciUcmdbWindows.getOnyxServiceCodes().equalsIgnoreCase(ciWindowsServCode))) {
									errorMatch = "Administrado|Validar, Coincide Ip de gestion y cod de servicio pero no coincide displayLabeL ";

								}else if ((ciUcmdbWindows.getDisplayLabel().equalsIgnoreCase(ciWindowsDispLabel)) && (!ciUcmdbWindows.getOnyxServiceCodes().equalsIgnoreCase(ciWindowsServCode))) {
									errorMatch = "NO Administrado|Validar Modelado, Coincide Ip de gestion y displayLabeL pero no coincide codigo de servicio ";
								}else {
									errorMatch = "NO Administrado|Validar Modelado, Coincide Ip de gestion y no coincide codigo de servicio ni displayLabel";
								}
								lastAccessTimeisOld = checkLastAccessTime(ciUcmdbWindows.getLastAccessTimeProtocolo());
								System.out.println((i+1)+
										"|"+ciWindows.getCodHostname()+
										"|"+ciWindows.getIpGestion()+					
										"|"+ciUcmdbWindows.toString()+
										"|"+lastAccessTimeisOld+
										"|"+errorMatch);									

								match=true;
								break;
							}
						}
					}
					if(!match) {
						lastAccessTimeisOld = checkLastAccessTime(ciUcmdbWindows.getLastAccessTimeProtocolo());
						System.out.println((i+1)+"|||"+ciUcmdbWindows.getOnyxServiceCodes()+
								"|"+ciUcmdbWindows.getDisplayLabel()+
								"|"+ciUcmdbWindows.getIpGestion()+
								"|"+ciUcmdbWindows.getIpAddress()+
								"|"+ciUcmdbWindows.getProtocoloDescubrimiento()+
								"|"+ciUcmdbWindows.getLastAccessTimeProtocolo()+
								"|"+lastAccessTimeisOld+
								"|No Administrado|No se encuentra en inv torre");
					}

					//mira porque no coincide el nombre de la maquina y codigo de servicio

				}

			}
		}

	}

}




