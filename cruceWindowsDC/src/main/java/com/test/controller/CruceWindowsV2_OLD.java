package com.test.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;


import com.test.dao.CiServiceManagerDao;
import com.test.dao.CiWindowsDao;
import com.test.dao.CiWindowsUcmdbDao;
import com.test.exceptions.InventoryException;
import com.test.model.CiServiceManager;
import com.test.model.CiUcmdbWindows;
import com.test.model.CiWindows;
import com.test.model.MatchTowerUcmdb;
import com.test.utils.Libs;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class CruceWindowsV2_OLD {


	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy HH'H'mm'' z");
	/*
        private static final String windowsFilePath ="D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Windows\\inv windows torre.xlsx";
	private static final String UCMDB_FILE_PATH = "D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Windows\\inv windows ucmdb.xlsx";
	private static final String SM_FILE_PATH="D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Windows\\inv service manager.csv";
        */
        private static String windowsFilePath ="";
	private static String UCMDB_FILE_PATH ="";
	private static String SM_FILE_PATH="";
        

	private static final String TOWER_VS_UCMDB_HEADER =CiWindows.HEADER+"\t"+CiUcmdbWindows.HEADER+"\t"+"Status Match UCMDB"+"\t"+"Observaciones Match UCMDB";
	private static final String UCMDB_VS_TOWER_HEADER =CiUcmdbWindows.HEADER+"\t"+CiWindows.HEADER+"\t"+"Status Match UCMDB"+"\t"+"Observaciones Match UCMDB"+"\t"+"Status Match Torre"+"\t"+"Observaciones Match Torre";
	private static final String UCMDB_SM_VS_TOWER_HEADER =CiUcmdbWindows.HEADER+"\t"+CiWindows.HEADER+"\t"+CiServiceManagerDao.HEADER+"\t"+"Status Match UCMDB"+"\t"+"Observaciones Match UCMDB"+"\t"+"Status Match Torre"+"\t"+"Observaciones Match Torre"+"\t"+"Status match SM"+"\t"+"Observaciones SM";

	
	public static CiUcmdbWindows getRecentCiOld(CiUcmdbWindows firstCiUmcbd, CiUcmdbWindows secondCiUmcbd){		
		CiUcmdbWindows recentCi = null;
		String [] lastAccesTimeFirstCi =firstCiUmcbd.getLastAccessTimeProtocolo().split(", ");
		String [] lastAccessTimeSecondCi =secondCiUmcbd.getLastAccessTimeProtocolo().split(", ");
		LocalDateTime maxLastAccessTime = LocalDateTime.MIN;
		LocalDateTime maxFirstLastAccessTime;
		LocalDateTime maxSecondLastAccessTime;
		//BUG AL LEER , DETECTA UN * DONDE NO HAY NADA
		//mirar los LAT de los datos que ya estan, debe tomar el qe tenga el lat ma reciente. 
		if(firstCiUmcbd.getLastAccessTimeProtocolo().length()==0 && secondCiUmcbd.getLastAccessTimeProtocolo().length()>0) {
			recentCi=secondCiUmcbd;
		}else if(firstCiUmcbd.getLastAccessTimeProtocolo().length() >0 && secondCiUmcbd.getLastAccessTimeProtocolo().length()==0) {
			recentCi=firstCiUmcbd;
		}else if(firstCiUmcbd.getLastAccessTimeProtocolo().length() >0 && secondCiUmcbd.getLastAccessTimeProtocolo().length()>0 ) {
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
				recentCi=firstCiUmcbd;
			}else {
				recentCi=secondCiUmcbd;
			}
		}
		return recentCi;
	}	
        

	public List<MatchTowerUcmdb> towerInventaryMergeUcmdbInventary(String windowsInventaryPath, String ucmdbInventaryPath) throws Exception{
		List<MatchTowerUcmdb> resultMatchTowerUcmdb= new ArrayList<>();
		List<CiWindows> windowsInventary = new CiWindowsDao().readWindowsInventary(windowsInventaryPath); 
		List<CiUcmdbWindows> inventoryUCMDB = new CiWindowsUcmdbDao().readUcmdbInventory(ucmdbInventaryPath);

		if((windowsInventary.size()==0 ||inventoryUCMDB.size()==0 )) {
			throw new InventoryException("El inventario se encuentra vacio"); 
		}else {
			HashMap<String, CiUcmdbWindows> inventoryUCMDBhm = new HashMap<>(); //AQI SE PUEDE MIRAR E IDENTIFICAR LOS DUPLICADOS.
			HashMap<String, CiUcmdbWindows> inventoryUCMDBhmDuplicates = new HashMap<>(); //AQI SE PUEDE MIRAR E IDENTIFICAR LOS DUPLICADOS.

			for (CiUcmdbWindows ciUcmdbWindows : inventoryUCMDB) {
				String ucmdbCodHostname=ciUcmdbWindows.getOnyxServiceCodes()+"_"+ciUcmdbWindows.getDisplayLabel().toUpperCase();
				if(inventoryUCMDBhm.get(ucmdbCodHostname)==null) {
					inventoryUCMDBhm.put(ucmdbCodHostname, ciUcmdbWindows);
				}else {
					//en caso de que ya exista la clave, se debe insertar en el hashmap la que este mas actcualizada-
					CiUcmdbWindows firstCiUmcbd = inventoryUCMDBhm.get(ucmdbCodHostname);
					CiUcmdbWindows secondCiUmcbd = ciUcmdbWindows;
					CiUcmdbWindows recentCiUcmdb = getRecentCi(firstCiUmcbd,secondCiUmcbd );

					String recentUcmdbCodHostname=recentCiUcmdb.getOnyxServiceCodes().toUpperCase()+"_"+recentCiUcmdb.getDisplayLabel().toUpperCase();
					String firstCiUmcbdCodHostname=firstCiUmcbd.getOnyxServiceCodes().toUpperCase()+"_"+firstCiUmcbd.getDisplayLabel().toUpperCase();
					String secondCiUmcbdCodHostname=secondCiUmcbd.getOnyxServiceCodes().toUpperCase()+"_"+secondCiUmcbd.getDisplayLabel().toUpperCase();

					inventoryUCMDBhm.replace(recentUcmdbCodHostname,recentCiUcmdb);
					///rEEEMPLAZAR POR EL CI QUE ENTREGUE EL METOO GETrECENTci
					if(recentCiUcmdb.equals(firstCiUmcbd)) {
						inventoryUCMDBhmDuplicates.put(secondCiUmcbdCodHostname, secondCiUmcbd);
					}else {
						inventoryUCMDBhmDuplicates.put(firstCiUmcbdCodHostname, firstCiUmcbd);
					}
				}
			}
			////RECORRIENDO INVENTARIO WINDOWS 
			for ( int i = 0 ; i<windowsInventary.size();i++) {
				CiWindows ciWindows = windowsInventary.get(i);
				String ciWindowscodHostname=ciWindows.getCodHostname();
				String lastAccessTimeStatus="";
				String credentialStatus="";
				String scaleDays="";
				String [][]scaleDaysAndstatusCredential;
				String checkAppyUCMDB="";
				String statusMatch="PDT";

				CiUcmdbWindows ciUcmbdMatch = new CiUcmdbWindows();
				String ciUcmdbIpGestion="";
				String ciUcmdbIpAddress="";
				boolean hasMatchByIpAddress=false;
				//CASO 1 COINCIDE CODIGO_HOSTNAME
				if(inventoryUCMDBhm.containsKey(ciWindows.getCodHostname().toUpperCase())){
					ciUcmbdMatch = inventoryUCMDBhm.get(ciWindowscodHostname);
					//VALIDA X IP-> CODSERVICIOhOSTNAME
					String ipAdressWindows[]= ciWindows.getIpGestion().split(",");
					ciUcmdbIpGestion=ciUcmbdMatch.getIpGestion();
					ciUcmdbIpAddress = ciUcmbdMatch.getIpAddress();
					//VALIDA X CADA IP DE GESIT�N QUE TENGA EL ci DEL INVENTARIO DE LA TORRE
					for (int j= 0 ; j<ipAdressWindows.length && !hasMatchByIpAddress ;j++) {
						String ipGestionWindows=ipAdressWindows[j];
						boolean hasMatchIP = (ipGestionWindows.length()>0) && (ciUcmdbIpGestion.equals(ipGestionWindows)
								|| (ciUcmdbIpGestion.contains(ipGestionWindows+","))
								|| ((ciUcmdbIpGestion.endsWith(ipGestionWindows)))
								|| (ciUcmdbIpAddress.contains(ipGestionWindows+","))
								|| (ciUcmdbIpAddress.endsWith(ipGestionWindows)));
						//si ip de ucmdb es igual o esta cntenida en el listado deip de UCMDB. 
						if(hasMatchIP){
							statusMatch="ok";
							resultMatchTowerUcmdb.add(new MatchTowerUcmdb(ciWindows, ciUcmbdMatch,statusMatch));
							//si se encuentra, lo quita para no tenerlo en cuenta en otros casos
							inventoryUCMDBhm.remove(ciWindows.getCodHostname()); 
							hasMatchByIpAddress=true;
						}
					}
					//aqui validar si en el hashmap con duplicados x codserv_hostname esta el que tiene la ip correcta
					if(!hasMatchByIpAddress && inventoryUCMDBhmDuplicates.containsKey(ciWindows.getCodHostname().toUpperCase())) {
						ciUcmbdMatch=inventoryUCMDBhmDuplicates.get(ciWindowscodHostname);
						ciUcmdbIpGestion=ciUcmbdMatch.getIpGestion();
						ciUcmdbIpAddress = ciUcmbdMatch.getIpAddress();
						for (int j= 0 ; j<ipAdressWindows.length && !hasMatchByIpAddress ;j++) {
							String ipGestionWindows=ipAdressWindows[j];
							//si ip de ucmdb es igual o esta cntenida en el listado deip de UCMDB. 
							boolean hasMatchIP = (ipGestionWindows.length()>0) && (ciUcmdbIpGestion.equals(ipGestionWindows)
									|| (ciUcmdbIpGestion.contains(ipGestionWindows+","))
									|| ((ciUcmdbIpGestion.endsWith(ipGestionWindows)))
									|| (ciUcmdbIpAddress.contains(ipGestionWindows+","))
									|| (ciUcmdbIpAddress.endsWith(ipGestionWindows)));
							if(hasMatchIP){
								statusMatch="ok";
								resultMatchTowerUcmdb.add(new MatchTowerUcmdb(ciWindows, ciUcmbdMatch,statusMatch));
								//si se encuentra, lo quita para no tenerlo en cuenta en otros casos
								inventoryUCMDBhm.remove(ciWindows.getCodHostname()); 
								hasMatchByIpAddress=true;
							}
						}
					}
					if(!hasMatchByIpAddress) {
						//vALIDA X CODIGO_HOSTNAME					
						statusMatch="PDT"+'\t'+"Validar, No coincide ip de gestion dada por inv torre";
						resultMatchTowerUcmdb.add(new MatchTowerUcmdb(ciWindows, ciUcmbdMatch, statusMatch));
					}
				}else {
					//CASO 2 BUSQUEDA X IP -> HOSTNAME
					//AQUI NO SE ELIMINAN SI ESTAN DUPLICADOS LOS MATCH HACIA UCMDB
					boolean hasMatch=false;//busca por ip
					for (CiUcmdbWindows ciUcmdbWindows :inventoryUCMDB) {
						String errorMatch ="";
						boolean hasMatchIP = (ciWindows.getIpGestion().length()>0) &&
								ciUcmdbWindows.getIpGestion().equals(ciWindows.getIpGestion())
								|| ciUcmdbWindows.getIpGestion().contains(ciWindows.getIpGestion()+",")
								|| ciUcmdbWindows.getIpGestion().endsWith(ciWindows.getIpGestion())
								|| ciUcmdbWindows.getIpAddress().contains(ciWindows.getIpGestion()+",")
								|| ciUcmdbWindows.getIpAddress().endsWith(ciWindows.getIpGestion());
								
						if (hasMatchIP && !hasMatch) {
							if(ciWindows.getNumId()==583) {
							System.out.println();	
							}
							//1 obtener codigo de servicio , primer _ que encuentre antes del codhostname
							//2 obtener displayLabel, es lo que este despues de _ 
							String ciWindowsServCode =  ciWindows.getCodHostname().substring(0, ciWindows.getCodHostname().indexOf('_'));							
							String ciWindowsDispLabel=  ciWindows.getCodHostname().substring(ciWindows.getCodHostname().indexOf('_')+1,ciWindows.getCodHostname().length());
							
							boolean displayLabelAreEqual =ciUcmdbWindows.getDisplayLabel().equalsIgnoreCase(ciWindowsDispLabel);
							boolean serviceCodeAreEqual=ciUcmdbWindows.getOnyxServiceCodes().equalsIgnoreCase(ciWindowsServCode);
							if(displayLabelAreEqual && serviceCodeAreEqual ) {
								errorMatch="ok";
								inventoryUCMDBhm.remove(ciWindows.getCodHostname().toUpperCase()); //si se encuentra, lo quita para no tenerlo en cuenta en otros casos
							}
							else if(!displayLabelAreEqual && serviceCodeAreEqual) {
								errorMatch = "ok"+'\t'+"No coincide displayLabeL ";

								inventoryUCMDBhm.remove(ciWindows.getCodHostname().toUpperCase()); //si se encuentra, lo quita para no tenerlo en cuenta en otros casos

							}else if (displayLabelAreEqual && !serviceCodeAreEqual) {
								errorMatch = "PDT"+'\t'+"No coincide codigo de servicio ";

							}else if(ciWindows.getObservacionesRevision().contains("Posible ci duplicado en inv torre")) {
								errorMatch = "PDT,duplicado"+'\t'+"Posible duplicado en inv torre";
							}
							else {
								errorMatch = "PDT"+'\u0009'+"No coincide codigo de servicio ni displayLabel";
							}

							resultMatchTowerUcmdb.add(new MatchTowerUcmdb(ciWindows, ciUcmdbWindows, errorMatch));						
							hasMatch=true;
							break;
						}

					}if(!hasMatch) {
						statusMatch="PDT";
						resultMatchTowerUcmdb.add(new MatchTowerUcmdb(ciWindows, statusMatch));
					}
				}
			}
		}
		return resultMatchTowerUcmdb;
	}

	public List <MatchTowerUcmdb>checkUcmdbVsInventaryWindows(List<MatchTowerUcmdb> inventaryWindowsVsUcmdb,String ucmdbWindowsPath){
		String statusMatch="";		
		System.out.println("******CRUCE UCMDB VS TORRE************");		
		List<CiUcmdbWindows> inventoryUCMDB = new CiWindowsUcmdbDao().readUcmdbInventory(ucmdbWindowsPath);
		Map<Integer, Integer> ucmdbUidDuplicateCounts = new HashMap<Integer, Integer>();
		
		//Llenando hashmap (cruce ucmdb vs torre) con los unicos que hicieron match de torre vs UCMDB
		List<MatchTowerUcmdb> mergeUcmdbVsWindows = inventaryWindowsVsUcmdb.stream()
				.filter(ci->!ci.getCiUcmdbWindows().getDisplayLabel().isBlank())
				.map(ci->{
					String matchTorreUcmdb= ci.getStatusMatch();
					String matchUcmdbVsTorre = matchTorreUcmdb;
					if(matchUcmdbVsTorre.contains("\t")) {
						matchUcmdbVsTorre=matchTorreUcmdb.substring(0,matchTorreUcmdb.indexOf("\t"));
					}else {
						matchUcmdbVsTorre="\t"+matchUcmdbVsTorre;
					}
					ci.setStatusMatch(matchTorreUcmdb+"\t"+matchUcmdbVsTorre);

					return ci;
				}).collect(Collectors.toList());
		//creando hashset con solo los id unicos de los que hicieron match.
		Set<Integer> setInvTowerVsUcmdB= inventaryWindowsVsUcmdb.stream()
				.filter(ci->!ci.getCiUcmdbWindows().getDisplayLabel().isBlank())
				.map(ci->ci.getCiUcmdbWindows().getNumId()).distinct().collect(Collectors.toSet());
		
		ucmdbUidDuplicateCounts=findDuplicatesMatch(inventaryWindowsVsUcmdb);
		//trer status match del cruce torre vs UCMDB  colocarlo en status ,�match
		for(int i = 0; i<inventoryUCMDB.size();i++) {
			CiUcmdbWindows ciUcmdbWindows = inventoryUCMDB.get(i);
			//validar si el id actual hizo match contra Torre
			if((setInvTowerVsUcmdB.size()>0) && !setInvTowerVsUcmdB.contains(ciUcmdbWindows.getNumId())) {
				statusMatch = "\t"+"\t"+"No esta en Inv Torre";
				mergeUcmdbVsWindows.add(new MatchTowerUcmdb(ciUcmdbWindows,statusMatch));
			}else {
				//recorre el hashmap de lo duplicados
				for(Entry<Integer, Integer> entry : ucmdbUidDuplicateCounts.entrySet()) {
					if(ucmdbUidDuplicateCounts.containsKey(ciUcmdbWindows.getNumId())) {
						int repitedNumber=entry.getValue();						
						//se debe hacer set de la lista actual
						mergeUcmdbVsWindows.stream().filter(ci->ci.getCiUcmdbWindows().getNumId()==entry.getKey())
						.forEach(ci->{
							String status="validar, se encontraron "+repitedNumber +" posibles coincidencias de ci activos en torre";
							if(!ci.getStatusMatch().contains(status)) {
								ci.setStatusMatch(ci.getStatusMatch()+"\t"+status);						
							}
						});
					}
					
				}
				
			}
		}
		//validar ordenando lista en base al # de uid ucmdb
		mergeUcmdbVsWindows = mergeUcmdbVsWindows.stream().sorted(Comparator.comparingInt(ciUcmdb->ciUcmdb.getCiUcmdbWindows().getNumId())).collect(Collectors.toList());
		return mergeUcmdbVsWindows;
		
	}

	
	private Map<Integer, Integer> findDuplicatesMatch(List<MatchTowerUcmdb> inventaryWindowsVsUcmdb) {
		//retornar hashmap con solo los duplicados. (uid ucmdb + veces repetido)
		HashMap<Integer, MatchTowerUcmdb> matchUcmdb = new HashMap<Integer, MatchTowerUcmdb>();
		Map<Integer, Integer> ucmdbUidDuplicateCounts = new HashMap<Integer, Integer>();
		for (MatchTowerUcmdb ciMatch :inventaryWindowsVsUcmdb ) {
			if(ciMatch.getCiUcmdbWindows().getDisplayLabel().length()>0) {
				if(!matchUcmdb.containsKey(ciMatch.getCiUcmdbWindows().getNumId())) {
					matchUcmdb.put(ciMatch.getCiUcmdbWindows().getNumId(), ciMatch);
					ucmdbUidDuplicateCounts.put(ciMatch.getCiUcmdbWindows().getNumId(), 1);
				}else {
					int count = ucmdbUidDuplicateCounts.get(ciMatch.getCiUcmdbWindows().getNumId());
					String statusFirstOcurrence=matchUcmdb.get(ciMatch.getCiUcmdbWindows().getNumId()).getCiWindows().getEstado();
					String statusSecondOcurrence=ciMatch.getCiWindows().getEstado();
					boolean bothAreActive = statusFirstOcurrence.toUpperCase().contains("ACTIVO") && statusSecondOcurrence.toUpperCase().contains("ACTIVO");					
					if(bothAreActive) {
						ucmdbUidDuplicateCounts.put(ciMatch.getCiUcmdbWindows().getNumId(),++count);
					}
				}
			}
		}
		// filtrar solo los que tengan mas de un registro en el hashmap.
		ucmdbUidDuplicateCounts=ucmdbUidDuplicateCounts.entrySet().stream().filter(ci->ci.getValue()>1).collect(Collectors.toMap(c->c.getKey(), c->c.getValue()));
		return ucmdbUidDuplicateCounts;
	}

	
	public List <MatchTowerUcmdb> checkUcmdbVsInventaryWindowsVsSm(List<MatchTowerUcmdb>ucmdbVsWindows,String SM_FILE_PATH){
		System.out.println("Cruce UCMDB VS TORRE VS SM");
		CiServiceManagerDao ciServiceManagerDao = new CiServiceManagerDao();
		List<CiServiceManager> inventarySM = ciServiceManagerDao.readSmInventary(SM_FILE_PATH);
		List<MatchTowerUcmdb> mergeUcmdbvsUnixvsSM= new ArrayList<>();

		//UID SM = NOMBRE_codserv; hashmap , los que no encuente, hace la busqueda normal
		if(ucmdbVsWindows.size()>0 &&  inventarySM.size()>0) {
			Map<String, CiServiceManager> inventorySMhm=new HashMap<>();; //AQI SE PUEDE MIRAR E IDENTIFICAR LOS DUPLICADOS.
			Map<String, CiServiceManager> inventorySMhmDuplicates = new HashMap<>(); //AQI SE PUEDE MIRAR E IDENTIFICAR LOS DUPLICADOS.
			//LLENAR HASHMAP SM

			for(CiServiceManager  ciServiceManager :inventarySM) {
				String uidSM=ciServiceManager.getServiceCode().toUpperCase()+"_"+ciServiceManager.getNombre().toUpperCase();
				if(inventorySMhm.get(uidSM)==null) {
					inventorySMhm.put(uidSM, ciServiceManager);
				}else {					
					//en caso de que ya exista la clave, se debe insertar en el hashmap la que este mas actcualizada- 
					CiServiceManager secondCiSM=ciServiceManager;
					CiServiceManager firstCiSM=inventorySMhm.get(uidSM);
					CiServiceManager recentCiSM=getRecentCiSM(firstCiSM,secondCiSM);

					String uidRecentCiSM=recentCiSM.getServiceCode().toUpperCase()+"_"+recentCiSM.getNombre().toUpperCase();
					String uidFirstCiSM=uidSM;
					String uidSecondCiSM=secondCiSM.getServiceCode().toUpperCase()+"_"+secondCiSM.getNombre().toUpperCase();
					//REEMPLAZA POR EL CI QUE ENTREGE EL METODO GET RECENCI
					inventorySMhm.replace(uidRecentCiSM, recentCiSM);
					//colocando el duplicado en el hashmap de duplicados..
					if(recentCiSM.equals(firstCiSM)) {
						inventorySMhmDuplicates.put(uidSecondCiSM, recentCiSM);						
					}else {
						inventorySMhmDuplicates.put(uidFirstCiSM, recentCiSM);
					}	

				}
			}
			//BUSUQEDA X NOMBRE, SI NOMBRE DE sm ES IGUAL A ucmdb BUSQUE SI EL COD SERVICIO DE ucmdb ESTA EN EL DE sm, SI ES ASI MARCAR COMO OK
			String statusMatchSM="";
			for (int i = 0 ; i<ucmdbVsWindows.size();i++) {
				MatchTowerUcmdb ciMatchUcmdbVsTower = ucmdbVsWindows.get(i);
				String uidMatchTower=ciMatchUcmdbVsTower.getCiUcmdbWindows().getOnyxServiceCodes().toUpperCase()+"_"+ciMatchUcmdbVsTower.getCiUcmdbWindows().getDisplayLabel().toUpperCase();
				if(inventorySMhm.containsKey(uidMatchTower)) {
					if(ciMatchUcmdbVsTower.getCiUcmdbWindows().getOnyxServiceCodes().toUpperCase().contains(inventorySMhm.get(uidMatchTower).getServiceCode().toUpperCase())) {
						statusMatchSM="ok en SM";
						if(inventorySMhmDuplicates.containsKey(uidMatchTower)) {
							statusMatchSM="ok en SM"+"\t"+"Posible duplicado en service manager";
						}
					}else {
						statusMatchSM="pdt en SM"+"\t"+"No coincide cod servicio";
					}
					if(!ciMatchUcmdbVsTower.getStatusMatch().contains("validar")) {
						mergeUcmdbvsUnixvsSM.add(new MatchTowerUcmdb(ciMatchUcmdbVsTower.getCiUcmdbWindows(), ciMatchUcmdbVsTower.getCiWindows(), ciMatchUcmdbVsTower.getStatusMatch()+" "+"\t"+"\t"+statusMatchSM, inventorySMhm.get(uidMatchTower)) );
					}else {
						mergeUcmdbvsUnixvsSM.add(new MatchTowerUcmdb(ciMatchUcmdbVsTower.getCiUcmdbWindows(), ciMatchUcmdbVsTower.getCiWindows(), ciMatchUcmdbVsTower.getStatusMatch()+" "+"\t"+statusMatchSM, inventorySMhm.get(uidMatchTower)) );	
					}
					
				}else {
					//busque si el codigo de servicio esta contenido en el nombre,luego mire si el nomre esta contenido
					//esto apra controlar los casos de HA,m que solo se ve 1 cod de servicio en SM
					//TOCA VER COMO OPTIMIZAR
					boolean matchWithSM=false;
					CiServiceManager ciMathSm= new CiServiceManager();

					for (int j = 0; j<inventarySM.size();j++) {
						CiUcmdbWindows ucmdb = ucmdbVsWindows.get(i).getCiUcmdbWindows();
						boolean hasEqualServiceCode = ucmdb.getOnyxServiceCodes().toUpperCase().contains(inventarySM.get(j).getServiceCode().toUpperCase());
						boolean hasEqualHostname = ucmdbVsWindows.get(i).getCiUcmdbWindows().getDisplayLabel().toUpperCase().equals(inventarySM.get(j).getNombre().toUpperCase());
						if((hasEqualServiceCode && inventarySM.get(j).getServiceCode().length()>0 ) && (hasEqualHostname) && inventarySM.get(j).getNombre().length()>0) {
							statusMatchSM="ok en SM"+"\t"+"Coincide codigo y nombre";
							ciMathSm= inventarySM.get(j);
							matchWithSM=true;
							break;
						}else {
							statusMatchSM="pdt en SM"+"\t"+"No coincide cod servicio";

						}
					}
					if(!ciMatchUcmdbVsTower.getStatusMatch().contains("validar")) {
						mergeUcmdbvsUnixvsSM.add(new MatchTowerUcmdb(ciMatchUcmdbVsTower.getCiUcmdbWindows(), ciMatchUcmdbVsTower.getCiWindows(),ciMatchUcmdbVsTower.getStatusMatch()+" "+"\t"+"\t"+statusMatchSM,ciMathSm) );

					}else {
						mergeUcmdbvsUnixvsSM.add(new MatchTowerUcmdb(ciMatchUcmdbVsTower.getCiUcmdbWindows(), ciMatchUcmdbVsTower.getCiWindows(),ciMatchUcmdbVsTower.getStatusMatch()+" "+"\t"+statusMatchSM,ciMathSm) );

					}

					
				}
			}
		}
		return mergeUcmdbvsUnixvsSM;
	}
	public CiServiceManager getRecentCiSM(CiServiceManager firstCiSM,CiServiceManager secondCiSM) {
		//25/11/22 20:25:59
		String firstCiSMDate=firstCiSM.getFechaModificacion();
		String secondCiSMDate=secondCiSM.getFechaModificacion();
		//System.out.println(firstCiSM.toString()+"|||"+secondCiSM.toString());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");

		LocalDateTime firstDateTime=LocalDateTime.parse(firstCiSMDate,formatter);
		LocalDateTime secondDateTime=LocalDateTime.parse(secondCiSMDate,formatter);

		return firstDateTime.isAfter(secondDateTime) ? firstCiSM : secondCiSM;
	}
	public List<String> mainController() {
		CruceWindowsV2 cruceWndows = new CruceWindowsV2();
                List<String>fileOutputPath=new ArrayList();

                    try {
			//TORRE VS UCMDB
			List<MatchTowerUcmdb> towerVsUcmdb = cruceWndows.towerInventaryMergeUcmdbInventary(getWindowsFilePath(), getUCMDB_FILE_PATH());
			ArrayList<String> rowTowervsUcmdb = (ArrayList<String>) towerVsUcmdb.stream()
					.map(row ->row.getCiWindows().toString()+"\t"+row.getCiUcmdbWindows().toString()+"\t"+row.getStatusMatch())
					.collect(Collectors.toList());
			rowTowervsUcmdb.add(0,TOWER_VS_UCMDB_HEADER);
			String cruceWindowsVsUcmdbPath=Libs.writeBook(rowTowervsUcmdb,"\t","CruceWindowsVsUcmdbTestNew","CruceWindowsTvsUcmdb","D:\\OneDrive - GLOBAL HITSS\\Automatizmos\\ArchivosParaPowerBi\\Windows\\");

			//UCMDB VS TORRE
			List<MatchTowerUcmdb> ucmdbVsTower= cruceWndows.checkUcmdbVsInventaryWindows(towerVsUcmdb, getUCMDB_FILE_PATH());
			ArrayList<String> rowUcmdbVsTower = (ArrayList<String>) ucmdbVsTower.stream().
					map(row->row.getCiUcmdbWindows().toString()+"\t"+row.getCiWindows().toString()+"\t"+row.getStatusMatch())
					.collect(Collectors.toList());
			rowUcmdbVsTower.add(0,UCMDB_VS_TOWER_HEADER);
			String cruceUcmdbVsWindowsPath=Libs.writeBook(rowUcmdbVsTower,"\t","CruceWindUcmdbvsTorreTestdNew","CruceWindowsUcmdbvsTorre","D:\\OneDrive - GLOBAL HITSS\\Automatizmos\\ArchivosParaPowerBi\\Windows\\");

			//UCMDB VS TORRE vs SM
			List<MatchTowerUcmdb> cruceUcmdbvsTorrevsSM = cruceWndows.checkUcmdbVsInventaryWindowsVsSm(ucmdbVsTower, getSM_FILE_PATH());
			ArrayList<String> rowUcmdbVsTowervsSM = (ArrayList<String>) cruceUcmdbvsTorrevsSM.stream().
					map(row->row.getCiUcmdbWindows().toString()+"\t"+row.getCiWindows().toString()+"\t"+row.getCiServiceManager().toString()+"\t"+row.getStatusMatch())
					.collect(Collectors.toList());
			rowUcmdbVsTowervsSM.add(0,UCMDB_SM_VS_TOWER_HEADER);
                        String CruceUcmdbVsWindowsvsSM=Libs.writeBook(rowUcmdbVsTowervsSM,"\t","CruceWindUcmdbvsTorreVsSmTestNew","CruceWindowsUcmdbvsTorrevsSM","D:\\OneDrive - GLOBAL HITSS\\Automatizmos\\ArchivosParaPowerBi\\Windows\\");

			fileOutputPath.add("El cruce Windows vs Ucmdb fue generado en "+cruceWindowsVsUcmdbPath);
                        fileOutputPath.add("El cruce Ucmdb vs Windows fue generado en "+cruceUcmdbVsWindowsPath);
                        fileOutputPath.add("El cruce Ucmdb vs Windows vs SM fue generado en "+CruceUcmdbVsWindowsvsSM);
                        
                        
                        
		}catch (Exception e) {
			// TODO: handle exception
		}
            return fileOutputPath;
	}

    public static String getWindowsFilePath() {
        return windowsFilePath;
    }

    public static void setWindowsFilePath(String aWindowsFilePath) {
        windowsFilePath = aWindowsFilePath;
    }

    public static String getUCMDB_FILE_PATH() {
        return UCMDB_FILE_PATH;
    }

    public static void setUCMDB_FILE_PATH(String aUCMDB_FILE_PATH) {
        UCMDB_FILE_PATH = aUCMDB_FILE_PATH;
    }

    public static String getSM_FILE_PATH() {
        return SM_FILE_PATH;
    }
    public static void setSM_FILE_PATH(String aSM_FILE_PATH) {
        SM_FILE_PATH = aSM_FILE_PATH;
    }

    
// Este método retorna un objeto CiUcmdbWindows que representa el más reciente de los dos objetos de entrada.
public static CiUcmdbWindows getRecentCi(CiUcmdbWindows firstCiUmcbd, CiUcmdbWindows secondCiUmcbd) {
    
    // Se separan las fechas en arreglos para los objetos firstCiUmcbd y secondCiUmcbd
    String[] lastAccesTimeFirstCi = firstCiUmcbd.getLastAccessTimeProtocolo().split(", ");
    String[] lastAccessTimeSecondCi = secondCiUmcbd.getLastAccessTimeProtocolo().split(", ");
    
    // Se calcula la fecha más reciente en el arreglo lastAccesTimeFirstCi y se asigna a maxFirstLastAccessTime
    // Se usa un Stream para convertir cada string en un objeto LocalDateTime y encontrar el más grande
    // En caso de que no haya fechas en el arreglo, se retorna LocalDateTime.MIN
    LocalDateTime maxFirstLastAccessTime = Stream.of(lastAccesTimeFirstCi)
        .map(time -> LocalDateTime.parse(time, formatter))
        .max(LocalDateTime::compareTo)
        .orElse(LocalDateTime.MIN);
    
    // Se hace lo mismo con el arreglo lastAccessTimeSecondCi
    LocalDateTime maxSecondLastAccessTime = Stream.of(lastAccessTimeSecondCi)
        .map(time -> LocalDateTime.parse(time, formatter))
        .max(LocalDateTime::compareTo)
        .orElse(LocalDateTime.MIN);
    
    // Se retorna el objeto firstCiUmcbd si su fecha más reciente es mayor a la fecha más reciente de secondCiUmcbd
    // De lo contrario, se retorna secondCiUmcbd
    return maxFirstLastAccessTime.isAfter(maxSecondLastAccessTime) ? firstCiUmcbd : secondCiUmcbd;
}
   
    

         
    
/*
    public static void main (String args[]){
        System.out.println("Test");
        CiUcmdbWindows firstCiUmcbd =  new CiUcmdbWindows();
        CiUcmdbWindows secondCiUmcbd = new CiUcmdbWindows();
        firstCiUmcbd.setLastAccessTimeProtocolo("domingo 12 de febrero de 2023 19H29' COT");
        secondCiUmcbd.setLastAccessTimeProtocolo("domingo 12 de febrero de 2023 09H47' COT");
        CiUcmdbWindows rta = getRecentCi(firstCiUmcbd, secondCiUmcbd);
        System.out.println(rta.toString());
        System.out.println(getRecentCiNew(firstCiUmcbd,secondCiUmcbd));
        
    }
*/
}
