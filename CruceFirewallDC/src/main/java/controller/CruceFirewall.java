package controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import utils.Utils;
import utils.unmergeCellsFile;

import dao.CiFirewallDao;
import dao.CiFirewallUcmdbDao;
import dao.CiServiceManagerFwDao;
import model.CiFirewall;
import model.CiServiceManagerFw;
import model.CiUcmdbFirewall;
import model.MatchTowerUcmdb;

import exceptions.InventoryException;
import java.util.Collections;




public class CruceFirewall {

	private static final String TOWER_VS_UCMDB_HEADER=CiFirewallDao.HEADER+"\t"+CiFirewallUcmdbDao.HEADER+"\t"+"Status Match ucmdb"+"\t"+"Observaciones Match ucmdb";
	private static final String UCMDB_VS_TOWER_HEADER=CiFirewallUcmdbDao.HEADER+"\t"+CiFirewallDao.HEADER+"\t"+"Status Match ucmdb"+"\t"+"Observaciones Match ucmdb"+"\t"+"Status match Torre";
	private static final String UCMDB_SM_VS_TOWER_HEADER =CiFirewallUcmdbDao.HEADER+"\t"+CiFirewallDao.HEADER+"\t"+CiServiceManagerFwDao.HEADER +"\t"+"Status Match torre"+"\t"+"Observaciones Match Torre"+"\t"+"Status match UCMDB"+"\t"+"Status match SM"+"\t"+"Observacion SM";
	//private static String firewallFilePath = "D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Firewall\\INVENTARIO PLATAFORMAS SEGURIDAD-V18 UNMERGED.xlsx";
	private static String PATH ="D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Firewall\\";
	private static String POWERBI_PATH="D:\\OneDrive - GLOBAL HITSS\\Automatizmos\\ArchivosParaPowerBi\\Firewall\\";
	
        /*
        private static String FIREWALL_FILE_PATH = "D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Firewall\\RF1526415_nov22_INVENTARIO PLATAFORMAS SEGURIDAD V18 (1).xlsx";
	private static final String UCMDB_FILE_PATH = "D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Firewall\\inv firewall ucmdb.xlsx";
	private static String SM_FILE_PATH="D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Firewall\\inv Sm.csv"; 
        */
        private static String FIREWALL_FILE_PATH ="";
        private static String UCMDB_FILE_PATH ="";
        private static String SM_FILE_PATH="";
         
        
    
	public String getHeadertowerVsUcmdb() {
		return CiFirewallDao.HEADER+"\t"+CiFirewallUcmdbDao.HEADER+"\t"+"Status Match ucmdb"+"\t"+"Observaciones Match ucmdb";
	}

	public String getHeaderUmcbdVsTower() {
		return CiFirewallUcmdbDao.HEADER+"\t"+CiFirewallDao.HEADER+"\t"+"Status Match ucmdb"+"\t"+"Observaciones Match ucmdb"+"\t"+"Status match Torre";
	}

	public String getHeaderUcmdbVsTowerVsSM() {
		return CiFirewallUcmdbDao.HEADER+"\t"+CiFirewallDao.HEADER+"\t"+CiServiceManagerFwDao.HEADER +"\t"+"Status Match torre"+"\t"+"Observaciones Match Torre"+"\t"+"Status match UCMDB"+"\t"+"Status match SM"+"\t"+"Observacion SM";
	}

	public List<MatchTowerUcmdb> checkInventaryFirewallvsUCMDB(String firewallPath,String ucmdbFirewallPath) {
		List<String> resultMergeFirewallVsUcmdb= new ArrayList<>();
		List<MatchTowerUcmdb> resultMatchTowerUcmdb= new ArrayList<>();

		CiFirewallDao ciFirewallDao=new CiFirewallDao();
		CiFirewallUcmdbDao  ciFirewallUcmdbDao= new CiFirewallUcmdbDao();
		List<CiFirewall> firewallInventary = ciFirewallDao.readFirewallInventary(firewallPath);
		List<CiUcmdbFirewall> inventoryUCMDB = ciFirewallUcmdbDao.readFirewallInventary(ucmdbFirewallPath);

		System.out.println("******CRUCE************");
		String headerMatch=CiFirewallDao.HEADER+"\t"+CiFirewallUcmdbDao.HEADER+"\t"+"Status Match"+"\t"+"Obervaciones";
		resultMergeFirewallVsUcmdb.add(headerMatch);
		//resultMatchTowerUcmdb.add(headerMatch);
		if(firewallInventary.size()==0 && inventoryUCMDB.size()==0) {
			throw new InventoryException("El inventario se encuentra vacio");
		}else {
                    
			for ( int i = 0 ; i<firewallInventary.size();i++) {	//RECORRE INVENTARIO FIREWALL			
				CiFirewall ciFirewall = firewallInventary.get(i);
				boolean match=false;   
				//NO PUEDO USAR HASHMAP para recorrer in firewall PORQUE NO EXISTE CLAVE UNICA. el serial a veces esta mal diligenciado, por eso no se usa
                                //RECORRA TODO EL INV DE FW UCMDB, SI ENCUENTRA MATCH COMPELTA, DEJE DE VALDIAR,SI NO. SIGA VALIDANDO..
				if(!ciFirewall.getIPFormateada().isEmpty()) {
					String ipAdress[]= ciFirewall.getIPFormateada().split(", ");
                                        boolean completeMatch=false;
					String errorMatch ="";
					CiUcmdbFirewall ciUcmdbFirewallMatch=null;
                                        Map<Integer,CiUcmdbFirewall> posibblematch = new HashMap<>();
					for (int j=0; (j<inventoryUCMDB.size() && !completeMatch);j++) { 
						CiUcmdbFirewall ciUcmdbFirewall = inventoryUCMDB.get(j);
						for (String ipInvFirewall : ipAdress) { //RECORRE IP ASOCIADAS A LA DEL INV FIREWALL
							boolean matchIP=ciUcmdbFirewall.getIpAddress().equals(ipInvFirewall) || ciUcmdbFirewall.getIpAddress().contains(ipInvFirewall+",") 
									||  ciUcmdbFirewall.getIpAddress().endsWith(ipInvFirewall);
							boolean matchServiceCode=ciUcmdbFirewall.getOnyxServiceCode().equalsIgnoreCase(ciFirewall.getCodServicio())
									|| (ciUcmdbFirewall.getOnyxServiceCode().contains(ciFirewall.getCodServicio()+",")
											|| ciUcmdbFirewall.getOnyxServiceCode().endsWith(ciFirewall.getCodServicio()));
							boolean matchSerial=(ciFirewall.getSerial()!=null && !ciFirewall.getSerial().equals(""))  && ciUcmdbFirewall.getSerialNumber().contains(ciFirewall.getSerial());

							//CASO 1 COINCIDE IP + COD SERV + SERIAL
							if(matchIP && matchServiceCode && matchSerial ) {
								errorMatch = "OK"+'\t'+"Coincide Ip de gestion,cod de servicio y serial";									
								ciUcmdbFirewallMatch=ciUcmdbFirewall;
								match=true;
                                                                completeMatch=true;
                                                                System.out.println(ciFirewall+"ºººº"+ciUcmdbFirewallMatch+"");
                                                                //si hace match completo, deje de validar en el inventario.
                                                                                                                                       posibblematch.put(2, ciUcmdbFirewall);
                                                            posibblematch.put(1, ciUcmdbFirewall);

							}
							///CASO 2 COINCIDE IP +COD SERV (SI SERIAL ES VACIA)
							else if(matchIP && matchServiceCode && ciFirewall.getSerial().equals("")) {
								errorMatch = "ok"+'\t'+"Validar, Coincide Ip de gestion,cod de servicio pero no serial.";							
								ciUcmdbFirewallMatch=ciUcmdbFirewall;
								match=true;
                                                                //si NO hace match completo, dCONTINUE VALIDANDO
                                                                posibblematch.put(2, ciUcmdbFirewall);

							}
							//CASO 3 COINCIDE IP +COD SERV (SI SERIAL NO ES VACIA) y aun no ha hecho match el ci
							else if(matchIP && matchServiceCode) {
								errorMatch = "ok"+'\t'+"Validar, Coincide Ip de gestion,cod de servicio pero no serial.";							
								ciUcmdbFirewallMatch=ciUcmdbFirewall;
								match=true;
                                                                posibblematch.put(3, ciUcmdbFirewall);
							}
							// CASO 4COINCIDE IP + SERIAL
							else if (matchIP && matchSerial && !matchServiceCode){
								errorMatch = "OK,validar modelado"+'\t'+"Validar, Coincide Ip de gestion,serial, pero no codigo de servicio";
								ciUcmdbFirewallMatch=ciUcmdbFirewall;
								match=true;
                                                                posibblematch.put(4, ciUcmdbFirewall);
							}else if (matchIP && !matchServiceCode && !matchSerial) {
								errorMatch = "OK,validar modelado"+'\t'+"Validar, Coincide Ip de gestion, pero no codigo de servicio ni serial.";
								match=true;
								ciUcmdbFirewallMatch=ciUcmdbFirewall;
                                                                posibblematch.put(5, ciUcmdbFirewall);
							}
						}
                                                //posible solucion, asignar numero tependiend del match seleccionar como ciucmdbfirewall el que tenga el numero menor
                                        System.out.println(ciFirewall+"ºººº"+ciUcmdbFirewallMatch+"   "+errorMatch);

					}
                                        

					//TERMINA DE RECORRER EL FOR DE IP ADDRES Y EL INV TORRE, Y VALIDA SI ENCONTRO ALGUN MATCH O NO
					//ERROR AQUI, SI ENCUENTRA LA MISMA IP EN DOS REGISTROS, ESCOGE EL PRIMER REGISTRO QUE ENCONTRO
                                        if(match) {
                                            ciUcmdbFirewallMatch = posibblematch.get(Collections.min(posibblematch.keySet()));
                                                
						resultMergeFirewallVsUcmdb.add(ciFirewall.toString()+"\t"+ciUcmdbFirewallMatch+"\t"+errorMatch );

						resultMatchTowerUcmdb.add(new MatchTowerUcmdb(ciFirewall, ciUcmdbFirewallMatch,errorMatch));

						//System.out.println((i+ciFirewallDao.getRowheaderfirewallinventary()+1)+"\t"+ciFirewall.toString()+"\t"+ciUcmdbFirewallMatch+"\t"+errorMatch );
					}else {
						resultMergeFirewallVsUcmdb.add(ciFirewall.toString()+"\t"+CiFirewallUcmdbDao.TAB_HEADERS+"PDT"+"\t"+"No se logro identificar en base a la ip");
						errorMatch="pdt"+"\t"+"No se logro identificar en base a la ip";
						resultMatchTowerUcmdb.add(new MatchTowerUcmdb(ciFirewall,errorMatch));
					}
				}else {
					resultMergeFirewallVsUcmdb.add(ciFirewall.toString()+"\t"+CiFirewallUcmdbDao.TAB_HEADERS+"PDT"+"\t"+"No se logro identificar en base a la ip");
					String errorMatch="pdt"+"\t"+"No se logro identificar en base a la ip";
					resultMatchTowerUcmdb.add(new MatchTowerUcmdb(ciFirewall,errorMatch));


					//System.out.println((i+ciFirewallDao.getRowheaderfirewallinventary()+1)+"\t"+ciFirewall.toString()+"\t"+"\t"+"PDT"+'\u0009'+"No se logro identificar en base a la ip");
				}
			}

		}
		//resultMatchTowerUcmdb.stream().forEach(ci->System.out.println(ci.toString()));
		return resultMatchTowerUcmdb;
	}

	public List<MatchTowerUcmdb> checkUcmdbVsInventaryFirewall(List<MatchTowerUcmdb> inventaryVsFirewall,String ucmdbFirewallPath){
		//NO SE PUEDE USAR HASHPAM ES RELACION UNO A VARIOS.
		String statusMatch="";		
		CiFirewallUcmdbDao  ciFirewallUcmdbDao= new CiFirewallUcmdbDao();
		List<CiUcmdbFirewall> inventoryUCMDB = ciFirewallUcmdbDao.readFirewallInventary(ucmdbFirewallPath);
		System.out.println("******CRUCE UCMDB VS TORRE************");		
		Map hmInvUcmdb= inventoryUCMDB.stream().collect(Collectors.toMap(ciUcmdb ->ciUcmdb.getUid(), ciUcmdb ->ciUcmdb));		

		//Llenando hashmap con los unicos que hicieron match
		List<MatchTowerUcmdb> mergeUcmdbvsFirewall=inventaryVsFirewall.stream()
				.filter(ci ->!ci.getCiUcmdbFirewall().getDisplayLabel().isBlank())
				.map(ci->{
					String matchTorreUcmdb=ci.getStatusMatch();
					String matchUcmdbvsTorre=matchTorreUcmdb.substring(0,matchTorreUcmdb.indexOf("\t")); //remover observacion torre vs UCMDB (antes del \t)
					ci.setStatusMatch(matchTorreUcmdb+"\t"+matchUcmdbvsTorre);
					return ci; 
				})
				.collect(Collectors.toList()) ;

		//creando hashset con solo los id unicos de los que hicieron match.
		Set <Integer> stInvTwVsUcmdb = new HashSet <Integer>(inventaryVsFirewall.stream()
				.filter(ci ->!ci.getCiUcmdbFirewall().getDisplayLabel().isBlank())
				.map(ci ->ci.getCiUcmdbFirewall().getUid()).distinct().collect(Collectors.toList()));

		//trer status match del cruce torre vs UCMDB y colocarlo en status ,¿match
		for(int i= 0; i<inventoryUCMDB.size();i++) {
			CiUcmdbFirewall ciUcmdbFirewall=inventoryUCMDB.get(i);
			int uidInvUcmdbWithMatch=ciUcmdbFirewall.getUid();
			if(!stInvTwVsUcmdb.contains(ciUcmdbFirewall.getUid())) {
				statusMatch="\t"+"\t"+"No esta en Inv torre";
				mergeUcmdbvsFirewall.add(new MatchTowerUcmdb(ciUcmdbFirewall, statusMatch));
			}
			else {
				mergeUcmdbvsFirewall.get(i);
			}
		}
		//ORDENANDO LISTA EN BASE A LOS UID DE INV UCMDB
		mergeUcmdbvsFirewall=mergeUcmdbvsFirewall.stream().sorted(Comparator.comparingInt(ciUcmdb -> ciUcmdb.getCiUcmdbFirewall().getUid())).collect(Collectors.toList());

		//mergeUcmdbvsFirewall.stream()
		//.forEach(row->System.out.println(row.getCiUcmdbFirewall()+""+row.getCiFirewall()+"\t"+row.getStatusMatch()));


		return mergeUcmdbvsFirewall;
	}

	public List<MatchTowerUcmdb> checkUcmdbVsInventaryFirewallVsSM(List<MatchTowerUcmdb>ucmdbVsFirewall){
		System.out.println("Cruce UCMDB VS TORRE VS SM");
		CiServiceManagerFwDao ciServiceManagerfwDao = new CiServiceManagerFwDao();
		List<CiServiceManagerFw> inventarySM = ciServiceManagerfwDao.readSmInventary(getSM_FILE_PATH());
		List<MatchTowerUcmdb> mergeUcmdbvsFirewallvsSM= new ArrayList<>();

		//UID SM = NOMBRE_codserv; hashmap , los que no encuente, hace la busqueda normal

		if(ucmdbVsFirewall.size()>0 &&  inventarySM.size()>0) {
			Map<String, CiServiceManagerFw> inventorySMhm=new HashMap<>();; //AQI SE PUEDE MIRAR E IDENTIFICAR LOS DUPLICADOS.
			Map<String, CiServiceManagerFw> inventorySMhmDuplicates = new HashMap<>(); //AQI SE PUEDE MIRAR E IDENTIFICAR LOS DUPLICADOS.

			//stream para hashmap INV Torre
			/*
			inventorySMhm=inventarySM.stream().collect(Collectors.toMap(uid ->{
				CiServiceManagerFw ciSM = new CiServiceManagerFw();
			return ciSM.getServiceCode().toLowerCase(null)+"_"+ciSM.getNombre().toLowerCase();},ciSM->ciSM)
			);*/
			//LLENAR HASHMAP SM
			for(CiServiceManagerFw  ciServiceManagerFw :inventarySM) {
				String uidSM=ciServiceManagerFw.getServiceCode().toUpperCase()+"_"+ciServiceManagerFw.getNombre().toUpperCase();
				if(inventorySMhm.get(uidSM)==null) {
					inventorySMhm.put(uidSM, ciServiceManagerFw);
				}else {					
					//en caso de que ya exista la clave, se debe insertar en el hashmap la que este mas actcualizada- 
					CiServiceManagerFw secondCiSM=ciServiceManagerFw;
					CiServiceManagerFw firstCiSM=inventorySMhm.get(uidSM);
					CiServiceManagerFw recentCiSM=getRecentCiSM(firstCiSM,secondCiSM);

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
			for (int i = 0 ; i<ucmdbVsFirewall.size();i++) {
				MatchTowerUcmdb ciMatchUcmdbVsTower = ucmdbVsFirewall.get(i);
				String uidMatchTower=ciMatchUcmdbVsTower.getCiUcmdbFirewall().getOnyxServiceCode().toUpperCase()+"_"+ciMatchUcmdbVsTower.getCiUcmdbFirewall().getDisplayLabel().toUpperCase();
				if(inventorySMhm.containsKey(uidMatchTower)) {
					if(ciMatchUcmdbVsTower.getCiUcmdbFirewall().getOnyxServiceCode().toUpperCase().contains(inventorySMhm.get(uidMatchTower).getServiceCode().toUpperCase())) {
						statusMatchSM="ok en SM";
						if(inventorySMhmDuplicates.containsKey(uidMatchTower)) {
							statusMatchSM+="\t"+"Posible duplicado en service manager";
						}												
					}else {
						statusMatchSM="pdt en SM"+"\t"+"No coincide cod servicio";
					}
					mergeUcmdbvsFirewallvsSM.add(new MatchTowerUcmdb(ciMatchUcmdbVsTower.getCiUcmdbFirewall(), ciMatchUcmdbVsTower.getCiFirewall(), inventorySMhm.get(uidMatchTower), ciMatchUcmdbVsTower.getStatusMatch()+"\t"+statusMatchSM) );
				}else {	//busque si el codigo de servicio esta contenido en el nombre,luego mire si el nomre esta contenido
					//esto apra controlar los casos de HA,m que solo se ve 1 cod de servicio en SM
					//TOCA VER COMO OPTIMIZAR
					boolean matchWithSM=false;
					CiServiceManagerFw ciMathSm= new CiServiceManagerFw();
					for (int j = 0; j<inventarySM.size();j++) {
						CiUcmdbFirewall ucmdbFw = ucmdbVsFirewall.get(i).getCiUcmdbFirewall();
						boolean hasEqualServiceCode = ucmdbFw.getOnyxServiceCode().toUpperCase().contains(inventarySM.get(j).getServiceCode().toUpperCase());
						boolean hasEqualHostname = ucmdbVsFirewall.get(i).getCiUcmdbFirewall().getDisplayLabel().toUpperCase().equals(inventarySM.get(j).getNombre().toUpperCase());
						if((hasEqualServiceCode && inventarySM.get(j).getServiceCode().length()>0 ) && (hasEqualHostname) && inventarySM.get(j).getNombre().length()>0) {
							statusMatchSM="ok en SM"+"\t"+"Coincide codigo y nombre";
							ciMathSm= inventarySM.get(j);
							matchWithSM=true;
							break;
						}else {
							statusMatchSM="pdt en SM"+"\t"+"No coincide cod servicio";
						}
					}
					mergeUcmdbvsFirewallvsSM.add(new MatchTowerUcmdb(ciMatchUcmdbVsTower.getCiUcmdbFirewall(), ciMatchUcmdbVsTower.getCiFirewall(),ciMathSm,ciMatchUcmdbVsTower.getStatusMatch()+"\t"+statusMatchSM) );
				}
			}
		}
		return mergeUcmdbvsFirewallvsSM;		
	}

	public CiServiceManagerFw getRecentCiSM(CiServiceManagerFw firstCiSM,CiServiceManagerFw secondCiSM) {
		//25/11/22 20:25:59
		String firstCiSMDate=firstCiSM.getFechaModificacion();
		String secondCiSMDate=secondCiSM.getFechaModificacion();
		//System.out.println(firstCiSM.toString()+"|||"+secondCiSM.toString());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");

		LocalDateTime firstDateTime=LocalDateTime.parse(firstCiSMDate,formatter);
		LocalDateTime secondDateTime=LocalDateTime.parse(secondCiSMDate,formatter);

		return firstDateTime.isAfter(secondDateTime) ? firstCiSM : secondCiSM;
	}
	/*	
public void appendInventarySheet(String towerPath,String ucmdbPath,String towerVsUcmdbPath) {

		//crear 3 workbook
		try(
		FileInputStream fileTowervsUcmdb = new FileInputStream(new File(towerPath));
		XSSFWorkbook wbTowerVsUcmdbPath= new XSSFWorkbook(fileTowervsUcmdb);
		FileOutputStream outputStream = 

		FileInputStream fileTower=new FileInputStream(new File(towerPath));
		XSSFWorkbook wbTower = new XSSFWorkbook(fileTower);

		FileInputStream fileUcmdb = new FileInputStream(new File(ucmdbPath));
		XSSFWorkbook wbUcmdb = new XSSFWorkbook(fileUcmdb);				
				){

			XSSFSheet towerSheet = wbTower.getSheetAt(0);
			XSSFSheet ucmdbSheet = wbUcmdb.getSheetAt(0);
			XSSFSheet outputSheet=wbTowerVsUcmdbPath.createSheet("Inventario Torre");
			copyExcelWB(towerSheet, outputSheet);
			//for each in tower write in tower sh
			try {
				outputStream= new FileOutputStream(towerVsUcmdbPath);
				wbTowerVsUcmdbPath.write(outputStream);
				wbTowerVsUcmdbPath.close();
				System.out.println("Libro generado correctamente en: " +towerVsUcmdbPath);
			}catch (FileNotFoundException ex) {
				System.out.println("Error file not found "+ex);			




		}catch (Exception e) {
			e.getStackTrace();
			// TODO: handle exception
		}


	}
	private void copyExcelWB(XSSFSheet towerSheet, XSSFSheet outputSheet) {


		// TODO Auto-generated method stub

	}
	 */

	public List<String> mainController() {
		CruceFirewall cruceFirewall = new CruceFirewall();
                List<String>fileOutputPath=new ArrayList();

		try {
			String unmergedFilePath=new unmergeCellsFile().unmergeCellsFromFile(getFIREWALL_FILE_PATH());

			//TORRE VS UCMDB
			List<MatchTowerUcmdb> cruce = cruceFirewall.checkInventaryFirewallvsUCMDB(unmergedFilePath, getUCMDB_FILE_PATH());
			//guardando los datos del objeto en list string
			ArrayList<String> rowTextTowerVsUcmdb = (ArrayList<String>) cruce.stream().
					map(row ->row.getCiFirewall().toString()+"\t"+row.getCiUcmdbFirewall().toString()+"\t"+row.getStatusMatch())
					.collect(Collectors.toList());
			//Se coloca el encabezado de la tabla
			rowTextTowerVsUcmdb.add(0,cruceFirewall.getHeadertowerVsUcmdb());
			//Se guarda archivoi
			String ubicacionTorrevsUcmdb=Utils.writeBook(rowTextTowerVsUcmdb,"\t","CruceFirewallVsUcmdb","FirewallvsUcmdb",POWERBI_PATH);

			//UCMDB VS TORRE
			List<MatchTowerUcmdb>cruceUcmdbvsTorre=cruceFirewall.checkUcmdbVsInventaryFirewall(cruce, getUCMDB_FILE_PATH());
			ArrayList<String> rowUmcbdvsTorre = (ArrayList<String>) cruceUcmdbvsTorre.stream()
					.map(row ->row.getCiUcmdbFirewall().toString()+"\t"+row.getCiFirewall().toString()+"\t"+row.getStatusMatch())
					.collect(Collectors.toList());
			rowUmcbdvsTorre.set(0,cruceFirewall.getHeaderUmcbdVsTower());
			String ubicacionUcmdbVsTorre=Utils.writeBook(rowUmcbdvsTorre,"\t","CruceUcmdbvsFirewall","ucmdbVsFirewall",POWERBI_PATH);

			//UCMDB VS TORRE VS SM

			List<MatchTowerUcmdb> cruceUcmdbvsTorrevsSM = cruceFirewall.checkUcmdbVsInventaryFirewallVsSM(cruceUcmdbvsTorre);
			ArrayList<String>rowUcmdbvsTorreVsSM=(ArrayList<String>) cruceUcmdbvsTorrevsSM.stream().map(row->row.getCiUcmdbFirewall().toString()+"\t"+row.getCiFirewall().toString()+"\t"+row.getCiSM().toString()+"\t"+row.getStatusMatch()
					).collect(Collectors.toList());
			rowUcmdbvsTorreVsSM.set(0, cruceFirewall.getHeaderUcmdbVsTowerVsSM());
			String ubicacionUcmdbVsTorrevsSM=Utils.writeBook(rowUcmdbvsTorreVsSM,"\t","CruceUcmdbvsFirewallvsSM","ucmdbVsFirewallvsSM",POWERBI_PATH);

                        fileOutputPath.add("El cruce Firewall vs Ucmdb fue generado en "+ubicacionTorrevsUcmdb);
                        fileOutputPath.add("El cruce Ucmdb vs Firewall fue generado en "+ubicacionUcmdbVsTorre);
                        fileOutputPath.add("El cruce Ucmdb vs Firewall vs SM fue generado en "+ubicacionUcmdbVsTorrevsSM);
                        
                        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                
                
            return fileOutputPath;


	}

    public static String getFIREWALL_FILE_PATH() {
        return FIREWALL_FILE_PATH;
    }

    public static void setFIREWALL_FILE_PATH(String aFIREWALL_FILE_PATH) {
        FIREWALL_FILE_PATH = aFIREWALL_FILE_PATH;
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
}
