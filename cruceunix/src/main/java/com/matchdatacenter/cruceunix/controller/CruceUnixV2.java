package com.matchdatacenter.cruceunix.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.matchdatacenter.cruceunix.dao.CiServiceManagerDao;
import com.matchdatacenter.cruceunix.dao.CiUcmdbUnixDao;
import com.matchdatacenter.cruceunix.dao.CiUnixDao;
import com.matchdatacenter.cruceunix.model.CiServiceManager;
import com.matchdatacenter.cruceunix.model.CiUcmdbUnix;
import com.matchdatacenter.cruceunix.model.CiUnix;
import com.matchdatacenter.cruceunix.model.MatchTowerUcmdb;
import com.matchdatacenter.cruceunix.utils.Libs;



public class CruceUnixV2 {
	private static final String TOWER_VS_UCMDB_HEADER=CiUnixDao.HEADER+"\t"+"Observaciones revision"+"\t"+"Aplica UCMDB"+"\t"+CiUcmdbUnixDao.HEADER+"\t"+"Status Match UCMDB"+"\t"+"Observaciones Match UCMDB";
	private static final String UCMDB_VS_TOWER_HEADER=CiUcmdbUnixDao.HEADER+"\t"+CiUnixDao.HEADER+"\t"+"Observaciones revision"+"\t"+"Aplica UCMDB"+"\t"+"Status Match UCMDB"+"\t"+"Observaciones Match UCMDB"+"\t"+"Status Match Torre"+"\t"+"Observaciones Match Torre";
	private static final String UCMDB_SM_VS_TOWER_HEADER = CiUcmdbUnixDao.HEADER+"\t"+CiUnixDao.HEADER+"\t"+"Observaciones revision"+"\t"+"Aplica UCMDB"+"\t"+CiServiceManagerDao.HEADER +"\t"+"Status Match UCMDB"+"\t"+"Observaciones Match UCMDB"+"\t"+"Status Match Torre"+"\t"+"Observaciones Match Torre"+"\t"+"Status match SM"+"\t"+"Observaciones SM";
/*
	private static String UCMDB_FILE_PATH="D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Unix\\inv unix ucmdb.xlsx";
	private static String UNIX_FILE_PATH="D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Unix\\inv unix torre.xlsx";
	private static String SM_FILE_PATH="D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Unix\\inv service manager.csv";

   */     
	private static String UCMDB_FILE_PATH="";
	private static String UNIX_FILE_PATH="";
	private static String SM_FILE_PATH="";

        
        
	public static CiUcmdbUnix getRecentCi(CiUcmdbUnix firstCiUmcbd, CiUcmdbUnix secondCiUmcbd){
		CiUcmdbUnix currentCi = null;
		String [] lastAccesTimesFirstCi =firstCiUmcbd.getLastAccessTimeProtocolo().split("; ");
		String [] lastAccessTimesSecondCi =secondCiUmcbd.getLastAccessTimeProtocolo().split("; ");
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
	public List<MatchTowerUcmdb> towerInventaryMergeUcmdbInventary(String unixInventaryPath, String ucmdbInventaryPath) throws Exception{
		List<MatchTowerUcmdb> resultMatchTowerUcmdb= new ArrayList<>();

		List<CiUnix> unixInventary = new CiUnixDao().readUnixInventary(unixInventaryPath);
		List<CiUcmdbUnix> ucmdbInventary = new CiUcmdbUnixDao().readUcmdbInventory(ucmdbInventaryPath);

		if(!(unixInventary.size()==0 ||ucmdbInventary.size()==0 )) {
			HashMap<String, CiUcmdbUnix> inventoryUCMDBhm = new HashMap<>(); //AQI SE PUEDE MIRAR E IDENTIFICAR LOS DUPLICADOS.
			HashMap<String, CiUcmdbUnix> inventoryUCMDBhmDuplicates = new HashMap<>(); //AQI SE PUEDE MIRAR E IDENTIFICAR LOS DUPLICADOS.
			//LLENANDO HASHMAP CON DATOS DE UCMDB y duplicados 
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
			////RECORRIENDO INVENTARIO UNIX 
			for ( int i = 0 ; i<unixInventary.size();i++) {
				CiUnix ciUnix= unixInventary.get(i);
				String ciUnixcodHostname=ciUnix.getCodigoHostname();
				String lastAccessTimeStatus="";
				String credentialStatus="";
				String scaleDays="";
				String [][]scaleDaysAndstatusCredential;
				String checkAppyUCMDB="";
				String statusMatch="PDT";

				CiUcmdbUnix ciUcmbdMatch=new CiUcmdbUnix();
				String ciUcmdbIpGestion="";
				String ciUcmdbIpAddress="";
				boolean hasMatchByIpAddress=false;
				//CASO 1 COINCIDE CODIGO_HOSTNAME
				if(inventoryUCMDBhm.containsKey(ciUnix.getCodigoHostname().toUpperCase())){

					ciUcmbdMatch = inventoryUCMDBhm.get(ciUnixcodHostname);
					scaleDaysAndstatusCredential=Libs.clasifyDaysLastAccessTimes(ciUcmbdMatch.getLastAccessTimeProtocolo(),Libs.PATTERN_DATE);
					ciUcmbdMatch.setRecuentoDias(scaleDaysAndstatusCredential[0][0]);
					ciUcmbdMatch.setDiscoveryStatus(scaleDaysAndstatusCredential[0][1]);
					ciUcmbdMatch.setStatusLastAccessTime(scaleDaysAndstatusCredential[0][2]);

					checkAppyUCMDB=Libs.checkIfCiApplyUcmdb(ciUnix.getObservaciones());
					ciUnix.setAplicaUmcbd(checkAppyUCMDB);

					//VALIDA X IP-> CODSERVICIOhOSTNAME
					String ipAdressUnix[]= ciUnix.getIpGestion().split(",");
					ciUcmdbIpGestion=ciUcmbdMatch.getIpGestion();
					ciUcmdbIpAddress = ciUcmbdMatch.getIpAddress();
					//VALIDA X CADA IP DE GESITÓN QUE TENGA EL ci DEL INVENTARIO DE LA TORRE
					for (int j= 0 ; j<ipAdressUnix.length && !hasMatchByIpAddress ;j++) {
						String ipGestionUnix=ipAdressUnix[j];
						boolean hasMatchIP = (ipGestionUnix.length()>0) && (ciUcmdbIpGestion.equals(ipGestionUnix)
								|| (ciUcmdbIpGestion.contains(ipGestionUnix+","))
								|| ((ciUcmdbIpGestion.endsWith(ipGestionUnix)))
								|| (ciUcmdbIpAddress.contains(ipGestionUnix+","))
								|| (ciUcmdbIpAddress.endsWith(ipGestionUnix)));
						//si ip de ucmdb es igual o esta cntenida en el listado deip de UCMDB. 
						if(hasMatchIP){
							statusMatch="ok";
							resultMatchTowerUcmdb.add(new MatchTowerUcmdb(ciUnix, ciUcmbdMatch,statusMatch ));
							//si se encuentra, lo quita para no tenerlo en cuenta en otros casos
							inventoryUCMDBhm.remove(ciUnix.getCodigoHostname()); 
							hasMatchByIpAddress=true;
						}						

					}
					//aqui validar si en el hashmap con duplicados x codserv_hostname esta el que tiene la ip correcta	
					if(!hasMatchByIpAddress && inventoryUCMDBhmDuplicates.containsKey(ciUnix.getCodigoHostname().toUpperCase())) {
						ciUcmbdMatch=inventoryUCMDBhmDuplicates.get(ciUnixcodHostname);
						ciUcmdbIpGestion=ciUcmbdMatch.getIpGestion();
						ciUcmdbIpAddress = ciUcmbdMatch.getIpAddress();
						for (int j= 0 ; j<ipAdressUnix.length && !hasMatchByIpAddress ;j++) {
							String ipGestionUnix=ipAdressUnix[j];
							//si ip de ucmdb es igual o esta cntenida en el listado deip de UCMDB. 
							boolean hasMatchIP=(ipGestionUnix.length()>0) && ciUcmdbIpGestion.equals(ipGestionUnix)
									|| (ciUcmdbIpGestion.contains(ipGestionUnix+","))
									|| ((ciUcmdbIpGestion.endsWith(ipGestionUnix)))
									|| (ciUcmdbIpAddress.contains(ipGestionUnix+","))
									|| (ciUcmdbIpAddress.endsWith(ipGestionUnix));
							if(hasMatchIP){
								scaleDaysAndstatusCredential=Libs.clasifyDaysLastAccessTimes(ciUcmbdMatch.getLastAccessTimeProtocolo(),Libs.PATTERN_DATE);
								checkAppyUCMDB=Libs.checkIfCiApplyUcmdb(ciUnix.getObservaciones());

								ciUcmbdMatch.setRecuentoDias(scaleDaysAndstatusCredential[0][0]);
								ciUcmbdMatch.setDiscoveryStatus(scaleDaysAndstatusCredential[0][1]);
								ciUcmbdMatch.setStatusLastAccessTime(scaleDaysAndstatusCredential[0][2]);
								ciUnix.setAplicaUmcbd(checkAppyUCMDB);
								statusMatch="OK";
								resultMatchTowerUcmdb.add(new MatchTowerUcmdb(ciUnix, ciUcmbdMatch, statusMatch));
								//si se encuentra, lo quita para no tenerlo en cuenta en otros casos
								inventoryUCMDBhm.remove(ciUnix.getCodigoHostname()); 
								hasMatchByIpAddress=true;
							}
						}
					}
					if(!hasMatchByIpAddress) {
						//vALIDA X CODIGO_HOSTNAME					
						statusMatch="PDT"+'\t'+"Validar, No coincide ip de gestion dada por inv torre";
						resultMatchTowerUcmdb.add(new MatchTowerUcmdb(ciUnix, ciUcmbdMatch, statusMatch));
					}
				}else {
					//CASO 2 BUSQUEDA X IP -> HOSTNAME
					//AQUI NO SE ELIMINAN SI ESTAN DUPLICADOS LOS MATCH HACIA UCMDB
					boolean hasMatch=false;
					//busca por ip
					for (CiUcmdbUnix ciUcmdbUnix : ucmdbInventary) {
						String errorMatch ="";
						//ERROR RECORRE TODAS LAS IP DE INV UNOX
						boolean hasMatchIP=(ciUnix.getIpGestion().length()>0)&& (ciUcmdbUnix.getIpGestion().equals(ciUnix.getIpGestion())
								|| ciUcmdbUnix.getIpGestion().contains(ciUnix.getIpGestion()+",")
								|| ciUcmdbUnix.getIpGestion().endsWith(ciUnix.getIpGestion())
								|| ciUcmdbUnix.getIpAddress().contains(ciUnix.getIpGestion()+",")
								|| ciUcmdbUnix.getIpAddress().endsWith(ciUnix.getIpGestion()));

						if (hasMatchIP && !hasMatch) {

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
								errorMatch = "ok"+'\t'+"No coincide displayLabeL ";

								inventoryUCMDBhm.remove(ciUnix.getCodHostname().toUpperCase()); //si se encuentra, lo quita para no tenerlo en cuenta en otros casos

							}else if (displayLabelAreEqual && !serviceCodeAreEqual) {
								errorMatch = "PDT"+'\t'+"No coincide codigo de servicio ";

							}else if(ciUnix.getObservacionesRevision().contains("Posible ci duplicado en inv torre")) {
								errorMatch = "PDT,duplicado"+'\t'+"Posible duplicado en inv torre";
							}
							else {
								errorMatch = "PDT"+'\u0009'+"No coincide codigo de servicio ni displayLabel";
							}
							/*
							scaleDaysAndstatusCredential=Libs.clasifyDaysLastAccessTimes(ciUcmdbUnix.getLastAccessTimeProtocolo(),Libs.PATTERN_DATE);
							scaleDays = scaleDaysAndstatusCredential[0][0];
							credentialStatus = scaleDaysAndstatusCredential[0][1];
							lastAccessTimeStatus=scaleDaysAndstatusCredential[0][2];

							ciUcmdbUnix.setRecuentoDias(scaleDays);
							ciUcmdbUnix.setDiscoveryStatus(credentialStatus);
							ciUcmdbUnix.setStatusLastAccessTime(lastAccessTimeStatus);
							 */
							checkAppyUCMDB=Libs.checkIfCiApplyUcmdb(ciUnix.getObservaciones());

							ciUnix.setAplicaUmcbd(checkAppyUCMDB);
							resultMatchTowerUcmdb.add(new MatchTowerUcmdb(ciUnix, ciUcmdbUnix, errorMatch));						

							hasMatch=true;
							break;

						}
					}if(!hasMatch) {
						checkAppyUCMDB=Libs.checkIfCiApplyUcmdb(ciUnix.getObservaciones());
						statusMatch="PDT";
						resultMatchTowerUcmdb.add(new MatchTowerUcmdb(ciUnix, statusMatch));
					}
				}
			}
			//RECORRE DE NUEVO EL INVENTARIO DE LOS QUE NO ENCONTRO POR COD_HOSTNAME + IP Y BUSCA 
		}
		return resultMatchTowerUcmdb;
	}

	public List <MatchTowerUcmdb> checkUcmdbVsInventaryUnix(List<MatchTowerUcmdb> inventaryVsUnix,String ucmdbUnixPath) throws Exception{
		//List<MatchTowerUcmdb> mergeUcmdbVsUnix = null;
		String statusMatch="";		
		System.out.println("******CRUCE UCMDB VS TORRE************");		
		List<CiUcmdbUnix> inventoryUCMDB = new CiUcmdbUnixDao().readUcmdbInventory(ucmdbUnixPath);
		Map<Integer, Integer> ucmdbUidDuplicateCounts = new HashMap<Integer, Integer>();

		//Llenando hashmap (cruce ucmdb vs torre) con los unicos que hicieron match de torre vs UCMDB
		List<MatchTowerUcmdb> mergeUcmdbVsUnix=inventaryVsUnix.stream()
				.filter(ci->!ci.getCiUcmdbUnix().getDisplayLabel().isBlank())
				.map(ci->{
					String matchTorreUcmdb = ci.getStatusMatch();
					String matchUcmdbVsTorre=matchTorreUcmdb;
					if(matchTorreUcmdb.contains("\t")) {
						matchUcmdbVsTorre=matchTorreUcmdb.substring(0,matchTorreUcmdb.indexOf("\t"));							
					}else {
						matchUcmdbVsTorre="\t"+matchUcmdbVsTorre;
					}
					ci.setStatusMatch(matchTorreUcmdb+"\t"+matchUcmdbVsTorre);
					return ci;
				}).collect(Collectors.toList());

		//creando hashset con solo los id unicos de los que hicieron match.
		Set<Integer> setInvTowerVsUcmdB= inventaryVsUnix.stream()
				.filter(ci->!ci.getCiUcmdbUnix().getDisplayLabel().isBlank())
				.map(ci->ci.getCiUcmdbUnix().getNumID()).distinct().collect(Collectors.toSet());


		ucmdbUidDuplicateCounts=findDuplicatesMatch(inventaryVsUnix);

		//trer status match del cruce torre vs UCMDB  colocarlo en status ,¿match
		for(int i = 0; i<inventoryUCMDB.size();i++) {
			CiUcmdbUnix ciUcmdbUnix = inventoryUCMDB.get(i);
			if(!setInvTowerVsUcmdB.contains(ciUcmdbUnix.getNumID())) {
				statusMatch = "\t"+"\t"+"No esta en Inv Torre";
				mergeUcmdbVsUnix.add(new MatchTowerUcmdb(ciUcmdbUnix,statusMatch));
			}else {
				//recorre el hashmap de lo duplicados
				for(Entry<Integer, Integer> entry : ucmdbUidDuplicateCounts.entrySet()) {
					if(ucmdbUidDuplicateCounts.containsKey(ciUcmdbUnix.getNumID())) {
						int repitedNumber=entry.getValue();						
						//se debe hacer set de la lista actual
						mergeUcmdbVsUnix.stream().filter(ci->ci.getCiUcmdbUnix().getNumID()==entry.getKey())
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
		mergeUcmdbVsUnix=mergeUcmdbVsUnix.stream().sorted(Comparator.comparingInt(ciUcmdb->ciUcmdb.getCiUcmdbUnix().getNumID())).collect(Collectors.toList());
		return mergeUcmdbVsUnix;
	}


	public List <MatchTowerUcmdb> checkUcmdbVsInventaryUnixVsSm(List<MatchTowerUcmdb>ucmdbVsUnix,String SM_FILE_PATH){
		System.out.println("Cruce UCMDB VS TORRE VS SM");
		CiServiceManagerDao ciServiceManagerDao = new CiServiceManagerDao();
		List<CiServiceManager> inventarySM = ciServiceManagerDao.readSmInventary(SM_FILE_PATH);
		List<MatchTowerUcmdb> mergeUcmdbvsUnixvsSM= new ArrayList<>();

		//UID SM = NOMBRE_codserv; hashmap , los que no encuente, hace la busqueda normal
		if(ucmdbVsUnix.size()>0 &&  inventarySM.size()>0) {
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
			for (int i = 0 ; i<ucmdbVsUnix.size();i++) {
				MatchTowerUcmdb ciMatchUcmdbVsTower = ucmdbVsUnix.get(i);
				String uidMatchTower=ciMatchUcmdbVsTower.getCiUcmdbUnix().getOnyxServiceCodes().toUpperCase()+"_"+ciMatchUcmdbVsTower.getCiUcmdbUnix().getDisplayLabel().toUpperCase();
				if(inventorySMhm.containsKey(uidMatchTower)) {
					if(ciMatchUcmdbVsTower.getCiUcmdbUnix().getOnyxServiceCodes().toUpperCase().contains(inventorySMhm.get(uidMatchTower).getServiceCode().toUpperCase())) {
						statusMatchSM="ok en SM";
						if(inventorySMhmDuplicates.containsKey(uidMatchTower)) {
							statusMatchSM="ok en SM"+"\t"+"Posible duplicado en service manager";
						}
					}else {
						statusMatchSM="pdt en SM"+"\t"+"No coincide cod servicio";
					}
					if(!ciMatchUcmdbVsTower.getStatusMatch().contains("validar")) {
						mergeUcmdbvsUnixvsSM.add(new MatchTowerUcmdb(ciMatchUcmdbVsTower.getCiUcmdbUnix(), ciMatchUcmdbVsTower.getCiUnix(), ciMatchUcmdbVsTower.getStatusMatch()+" "+"\t"+"\t"+statusMatchSM, inventorySMhm.get(uidMatchTower)) );
					}else {
						mergeUcmdbvsUnixvsSM.add(new MatchTowerUcmdb(ciMatchUcmdbVsTower.getCiUcmdbUnix(), ciMatchUcmdbVsTower.getCiUnix(), ciMatchUcmdbVsTower.getStatusMatch()+" "+"\t"+statusMatchSM, inventorySMhm.get(uidMatchTower)) );	
					}
					
				}else {
					//busque si el codigo de servicio esta contenido en el nombre,luego mire si el nomre esta contenido
					//esto apra controlar los casos de HA,m que solo se ve 1 cod de servicio en SM
					//TOCA VER COMO OPTIMIZAR
					boolean matchWithSM=false;
					CiServiceManager ciMathSm= new CiServiceManager();

					for (int j = 0; j<inventarySM.size();j++) {
						CiUcmdbUnix ucmdb = ucmdbVsUnix.get(i).getCiUcmdbUnix();
						boolean hasEqualServiceCode = ucmdb.getOnyxServiceCodes().toUpperCase().contains(inventarySM.get(j).getServiceCode().toUpperCase());
						boolean hasEqualHostname = ucmdbVsUnix.get(i).getCiUcmdbUnix().getDisplayLabel().toUpperCase().equals(inventarySM.get(j).getNombre().toUpperCase());
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
						mergeUcmdbvsUnixvsSM.add(new MatchTowerUcmdb(ciMatchUcmdbVsTower.getCiUcmdbUnix(), ciMatchUcmdbVsTower.getCiUnix(),ciMatchUcmdbVsTower.getStatusMatch()+" "+"\t"+"\t"+statusMatchSM,ciMathSm) );

					}else {
						mergeUcmdbvsUnixvsSM.add(new MatchTowerUcmdb(ciMatchUcmdbVsTower.getCiUcmdbUnix(), ciMatchUcmdbVsTower.getCiUnix(),ciMatchUcmdbVsTower.getStatusMatch()+" "+"\t"+statusMatchSM,ciMathSm) );

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


	private Map<Integer, Integer> findDuplicatesMatch(List<MatchTowerUcmdb> matchTowerUcmdb) {
		//retornar hashmap con solo los duplicados. (uid ucmdb + veces repetido)
		HashMap<Integer, MatchTowerUcmdb> matchUcmdb = new HashMap<Integer, MatchTowerUcmdb>();
		Map<Integer, Integer> ucmdbUidDuplicateCounts = new HashMap<Integer, Integer>();
		for (MatchTowerUcmdb ciMatch :matchTowerUcmdb ) {
			if(ciMatch.getCiUcmdbUnix().getDisplayLabel().length()>0) {//filtra los de ucmdb diligenciados
				if(!matchUcmdb.containsKey(ciMatch.getCiUcmdbUnix().getNumID())) {
					matchUcmdb.put(ciMatch.getCiUcmdbUnix().getNumID(), ciMatch);
					ucmdbUidDuplicateCounts.put(ciMatch.getCiUcmdbUnix().getNumID(), 1);
				}else {
					int count = ucmdbUidDuplicateCounts.get(ciMatch.getCiUcmdbUnix().getNumID());
					String statusFirstOcurrence=matchUcmdb.get(ciMatch.getCiUcmdbUnix().getNumID()).getCiUnix().getEstado();
					String statusSecondOcurrence=ciMatch.getCiUnix().getEstado();
					boolean bothAreActive = statusFirstOcurrence.toUpperCase().contains("ACTIVO") && statusSecondOcurrence.toUpperCase().contains("ACTIVO");					
					if(bothAreActive) {
						ucmdbUidDuplicateCounts.put(ciMatch.getCiUcmdbUnix().getNumID(),++count);
					}
				}
			}
		}
		// filtrar solo los que tengan mas de un registro en el hashmap.
		ucmdbUidDuplicateCounts=ucmdbUidDuplicateCounts.entrySet().stream()
				.filter(ci->ci.getValue()>1).collect(Collectors.toMap(c->c.getKey(),c->c.getValue()));
		return ucmdbUidDuplicateCounts;
	}
	
	public List<String> mainController() {
            List<String>fileOutputPath=new ArrayList();
		CruceUnixV2 cruceUnix = new CruceUnixV2();
		try {
			//TORRE VS UCMDB
			List<MatchTowerUcmdb> towerVsUcmdb = cruceUnix.towerInventaryMergeUcmdbInventary(getUNIX_FILE_PATH(), getUCMDB_FILE_PATH());
			ArrayList<String> rowTowervsUcmdb = (ArrayList<String>) towerVsUcmdb.stream()
					.map(row ->row.getCiUnix().toString()+"\t"+row.getCiUcmdbUnix().toString()+"\t"+row.getStatusMatch())
					.collect(Collectors.toList());
			rowTowervsUcmdb.add(0,TOWER_VS_UCMDB_HEADER);
			String cruceUnixVsUcmdbPath=Libs.writeBook(rowTowervsUcmdb,"\t","CruceUnixVsUcmdb","CruceUnixTvsUcmdb","D:\\OneDrive - GLOBAL HITSS\\Automatizmos\\ArchivosParaPowerBi\\Unix\\");

			//UCMDB VS TORRE
			List<MatchTowerUcmdb> ucmdbVsTower= cruceUnix.checkUcmdbVsInventaryUnix(towerVsUcmdb, getUCMDB_FILE_PATH());
			ArrayList<String> rowucmdbVsTower = (ArrayList<String>) ucmdbVsTower.stream()
					.map(row ->row.getCiUcmdbUnix().toString()+"\t"+row.getCiUnix().toString()+"\t"+row.getStatusMatch())
					.collect(Collectors.toList());
			rowucmdbVsTower.add(0,UCMDB_VS_TOWER_HEADER);
			String cruceUcmdbVsUnixPath=Libs.writeBook(rowucmdbVsTower,"\t","CruceUcmdbVsUnix","CruceUnixUcmdbVsTorre","D:\\OneDrive - GLOBAL HITSS\\Automatizmos\\ArchivosParaPowerBi\\Unix\\");
			
			//UCMDB VS TORRE vs SM
			List<MatchTowerUcmdb> cruceUcmdbvsTorrevsSM = cruceUnix.checkUcmdbVsInventaryUnixVsSm(ucmdbVsTower, getSM_FILE_PATH());
			ArrayList<String>rowUcmdbvsTorreVsSM=(ArrayList<String>) cruceUcmdbvsTorrevsSM.stream().map(row->row.getCiUcmdbUnix().toString()+"\t"+row.getCiUnix().toString()+"\t"+row.getCiSM().toString()+"\t"+row.getStatusMatch()
					).collect(Collectors.toList());
			rowUcmdbvsTorreVsSM.add(0, UCMDB_SM_VS_TOWER_HEADER);
			String CruceUcmdbVsUnixvsSM=Libs.writeBook(rowUcmdbvsTorreVsSM,"\t","CruceUcmdbVsUnixvsSM","CruceUnixUcmdbVsTorrevsSM","D:\\OneDrive - GLOBAL HITSS\\Automatizmos\\ArchivosParaPowerBi\\Unix\\");

			fileOutputPath.add("El cruce Unix vs Ucmdb fue generado en "+cruceUnixVsUcmdbPath);
                        fileOutputPath.add("El cruce Ucmdb vs Unix fue generado en "+cruceUcmdbVsUnixPath);
                        fileOutputPath.add("El cruce Ucmdb vs Unix vs SM fue generado en "+CruceUcmdbVsUnixvsSM);
                        
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
                return fileOutputPath;
	}

    /**
     * @return the UCMDB_FILE_PATH
     */
    public static String getUCMDB_FILE_PATH() {
        return UCMDB_FILE_PATH;
    }

    /**
     * @param aUCMDB_FILE_PATH the UCMDB_FILE_PATH to set
     */
    public static void setUCMDB_FILE_PATH(String aUCMDB_FILE_PATH) {
        UCMDB_FILE_PATH = aUCMDB_FILE_PATH;
    }

    /**
     * @return the UNIX_FILE_PATH
     */
    public static String getUNIX_FILE_PATH() {
        return UNIX_FILE_PATH;
    }

    /**
     * @param aUNIX_FILE_PATH the UNIX_FILE_PATH to set
     */
    public static void setUNIX_FILE_PATH(String aUNIX_FILE_PATH) {
        UNIX_FILE_PATH = aUNIX_FILE_PATH;
    }

    /**
     * @return the SM_FILE_PATH
     */
    public static String getSM_FILE_PATH() {
        return SM_FILE_PATH;
    }

    /**
     * @param aSM_FILE_PATH the SM_FILE_PATH to set
     */
    public static void setSM_FILE_PATH(String aSM_FILE_PATH) {
        SM_FILE_PATH = aSM_FILE_PATH;
    }
}
