package com.matchdatacenter.cruceunix.dao;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.matchdatacenter.cruceunix.model.CiUnix;
import com.matchdatacenter.cruceunix.model.CiUnixColumns;
import com.matchdatacenter.cruceunix.utils.Libs;

public class CiUnixDao {

	public static final String HEADER="#"+"\t"+"ADM_RESPONSABLE"+"\t"+"CLIENTE"+"\t"+"COD_HOSTNAME"+"\t"+"CODIGO_HOSTNAME"+"\t"+"COD_SERVICIO"+"\t"+"HOSTNAME"+"\t"+"SO"+"\t"+"VER_SO"+"\t"+"TIPO"+"\t"+"IP-MGMT"+"\t"+"IP-PROD"+"\t"+"IP-ILO"+"\t"+"SERVICIO / ROL"+"\t"+"ESCALAMIENTO-APP"+"\t"+"MONITOREO"+"\t"+"MARCA_MODELO"+"\t"+"SERIAL"+"\t"+"UBICACION"+"\t"+"FECHA_RECEPCION"+"\t"+"FECHA_RETIRO"+"\t"+"ESTADO"+"\t"+"OBSERVACIONES"+"\t"+"Created"+"\t"+"_Path"+"\t"+"_Item_Type";



	public List<CiUnix> readUnixInventary(String unixFilePath) {
		List<CiUnix> unixInventary= new ArrayList<>();
		try {
			///PDT AJUSTAR QUE SI ENCUENTRA CELDAS VACIAS, PASE A LA SIGUIENTE.
			FileInputStream fileUnix = new FileInputStream(new File(unixFilePath));
			XSSFWorkbook workbookUnix = new XSSFWorkbook(fileUnix);
			XSSFSheet unixSheet = workbookUnix.getSheetAt(0);
			Iterator<Row> rowIterator = unixSheet.iterator();
			ArrayList<String> header = new ArrayList<String>();
			HashMap<String, CiUnix> invUnixhm= new HashMap<String, CiUnix>();
			HashMap<String, CiUnix> duplicateCodHostname= new HashMap<String, CiUnix>();

			String cellUnixValue = "";
			short cellCount=0;			
			while (rowIterator.hasNext()) { // recorre filas de archivo unix
				Row row = rowIterator.next();
				//Iterator<Cell> cellUnixIterator = row.cellIterator();
				CiUnix ciUnix = new CiUnix();
				cellCount = row.getLastCellNum();
				for (short currentCell = 0; currentCell < cellCount; currentCell++) { // recorre las columbas del inv					
					//cellUnixValue = Libs.getStringCellValue(cellUnixIterator.next());
					cellUnixValue = Libs.getStringCellValue(row.getCell(currentCell,MissingCellPolicy.CREATE_NULL_AS_BLANK));
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

				}
				if(ciUnix.getCodigoServicio() !=null && ciUnix.getHostname() !=null &&ciUnix.getHostname().length()>0 ) {// si cod de serv y hostname estan diligeniados, crea codigo_hostname
					ciUnix.setCodigoHostname();
					ciUnix.setAplicaUmcbd(Libs.checkIfCiApplyUcmdb(ciUnix.getObservaciones()));
					int num = row.getRowNum();
					ciUnix.setNumID(num);					
					unixInventary.add(ciUnix);
					
					
					
					
				//checando duplicados. // se hace aqui asi no es necesario recorrer de nuevo el objeto
					unixInventary=identyfyDuplicateIPAndCodHostname(unixInventary, invUnixhm, duplicateCodHostname, ciUnix);
				}
			}
			fileUnix.close();
			workbookUnix.close();
			//unixInventary=clasifyDuplicates(unixInventary);
			System.out.println("CANTIDAD REGISTROS EN INVENTARIO TORRE "+unixInventary.size());
		}catch (Exception ex) {
			System.err.println("ERROR inv torre: "+ex);
		}
		return unixInventary;
	}

	
	
	private List<CiUnix> identyfyDuplicateIPAndCodHostname(List<CiUnix> unixInventary, HashMap<String, CiUnix> invUnixhm,
			HashMap<String, CiUnix> duplicateCodHostname, CiUnix ciUnix) {
		if(invUnixhm.get(ciUnix.getIpGestion())==null) {
			invUnixhm.put(ciUnix.getIpGestion(), ciUnix);
		}else {
			CiUnix firstOcurrence=invUnixhm.get(ciUnix.getIpGestion());
			CiUnix secondOcurrence=ciUnix;
			int idNumFirstOcurrence=firstOcurrence.getNumID()-1;
			int idNumSecondOcurrence=secondOcurrence.getNumID()-1;
			boolean hasEqualCodHostname=firstOcurrence.getCodigoHostname().equalsIgnoreCase(secondOcurrence.getCodigoHostname());
			boolean hasEqualIpMgmt=firstOcurrence.getIpGestion().equals(secondOcurrence.getIpGestion());
			boolean cisAreActive=firstOcurrence.getEstado().toUpperCase().contains("ACTIVO") && secondOcurrence.getEstado().toUpperCase().contains("ACTIVO");
			/*CASO 1 DUPLICADO IP  COG_HOSTNAME &&  && STATUS ACTIVO
			Posible ci duplicado en inv torre*/
			if(hasEqualCodHostname && cisAreActive) {
				String observacion =  "|  Posible ci duplicado en inv torre |";
				if((firstOcurrence.getObservacionesRevision().contains(observacion))) {
					firstOcurrence.setObservacionesRevision(firstOcurrence.getObservacionesRevision()+observacion);
					unixInventary.set(idNumFirstOcurrence, firstOcurrence);	
				}else if(!(secondOcurrence.getObservacionesRevision().contains(observacion))) {
					secondOcurrence.setObservacionesRevision(secondOcurrence.getObservacionesRevision()+observacion);
					unixInventary.set(idNumSecondOcurrence, secondOcurrence);
				}
				/*
				CASO 2 DUPLICADO IP GESTION && STATUS ACTIVO
				IP GESTION DUPLICADA EN CIS ACTIVOS.
				 */
			}else if(hasEqualIpMgmt && cisAreActive) {
				String observacion = "| ip gestion activa duplicada en inventario torre";

				if(!(firstOcurrence.getObservacionesRevision().contains(observacion))) {
					firstOcurrence.setObservacionesRevision(firstOcurrence.getObservacionesRevision()+observacion);
					unixInventary.set(idNumFirstOcurrence, firstOcurrence);
				}if(!(secondOcurrence.getObservacionesRevision().contains(observacion))) {
					secondOcurrence.setObservacionesRevision(secondOcurrence.getObservacionesRevision()+observacion);
					unixInventary.set(idNumSecondOcurrence, secondOcurrence);
				}
			}
		}					
		//DUPLICADOS POR COD_HOSTNAM Y ACTIVOS.
		if(duplicateCodHostname.get(ciUnix.getCodigoHostname())==null) {
			duplicateCodHostname.put(ciUnix.getCodigoHostname(), ciUnix);
		}else {
			String observacion =  "|  Codigo y hoostname activo duplicado en inv torre |";
			CiUnix firstOcurrence=duplicateCodHostname.get(ciUnix.getCodigoHostname());
			CiUnix secondOcurrence=ciUnix;
			boolean cisAreActive=firstOcurrence.getEstado().toUpperCase().contains("ACTIVO") && secondOcurrence.getEstado().toUpperCase().contains("ACTIVO");
			boolean hasEqualCodHostname=firstOcurrence.getCodigoHostname().equalsIgnoreCase(secondOcurrence.getCodigoHostname());
			int idNumFirstOcurrence=firstOcurrence.getNumID()-1;
			int idNumSecondOcurrence=secondOcurrence.getNumID()-1;
			if(cisAreActive && hasEqualCodHostname ) {
				if(!(firstOcurrence.getObservacionesRevision().contains(observacion))) {
					firstOcurrence.setObservacionesRevision(firstOcurrence.getObservacionesRevision()+observacion);
					unixInventary.set(idNumFirstOcurrence, firstOcurrence);
				}if(!(secondOcurrence.getObservacionesRevision().contains(observacion))) {
					secondOcurrence.setObservacionesRevision(secondOcurrence.getObservacionesRevision()+observacion);
					unixInventary.set(idNumSecondOcurrence, secondOcurrence);
				}
				
			}
		}
		return unixInventary;
	}
	
	
	
	
	
/*
	public static void main (String []args) {	
		CiUnixDao ciFirewallDao = new CiUnixDao();
		System.out.println(HEADER);
		final String UNIX_FILE_PATH="D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Unix\\inv unix torre.xlsx";
		ciFirewallDao.readUnixInventary(UNIX_FILE_PATH).stream().forEach(row->System.out.println(row));
	}*/
}
