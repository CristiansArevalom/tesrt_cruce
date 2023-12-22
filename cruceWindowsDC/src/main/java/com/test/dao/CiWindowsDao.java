package com.test.dao;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.test.model.CiWindows;
import com.test.model.CiWindowsColums;
import com.test.utils.Libs;

public class CiWindowsDao {

	public List<CiWindows> readWindowsInventary(String windowsFilePath) {
		List<CiWindows> windowsInventary= new ArrayList<>();

		try (FileInputStream fileWindows = new FileInputStream(new File(windowsFilePath));
				XSSFWorkbook workbookWindows = new XSSFWorkbook(fileWindows);
				){
			XSSFSheet windowsSheet = workbookWindows.getSheetAt(0);
			Iterator<Row> rowIterator = windowsSheet.iterator();
			ArrayList<String> header = new ArrayList<String>();
			
			HashMap<String, CiWindows> invWindowshm= new HashMap<String, CiWindows>();
			HashMap<String, CiWindows> duplicateCodHostname= new HashMap<String, CiWindows>();
			
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
								try {
									ciWindows.setCodHostname(Libs.checkAndFormatServCodeAndHostname(cellWindowsValue.toUpperCase().replaceAll("�", "")));
									ciWindows.setCodServicioExtraido(Libs.extractServCodeAndHostname(cellWindowsValue));
								}catch ( Exception ex) {
									ciWindows.setCodHostname(cellWindowsValue.toUpperCase().replaceAll("�", ""));
									ciWindows.setObservacionesRevision(ciWindows.getObservacionesRevision()+"No cumple con estandar codserv_hostame |");
								}
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
								ciWindows.setIpGestionExtraido(cellWindowsValue);						
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
				if(ciWindows.getCodHostname()!=null && ciWindows.getCodHostname().length()>0){
					int num = row.getRowNum();
					ciWindows.setNumId(num);
                                        String srvCode=Libs.extactServiceCode(ciWindows.getCodHostname());
                                        ciWindows.setCodServicioExtraido(srvCode);
					windowsInventary.add(ciWindows);
					//checando duplicados. // se hace aqui asi no es necesario recorrer de nuevo el objeto
					windowsInventary=identyfyDuplicateIPAndCodHostname(windowsInventary,invWindowshm,duplicateCodHostname,ciWindows);
				}
			}

			System.out.println("CANTIDAD REGISTROS EN INVENTARIO TORRE "+windowsInventary.size());

		} catch (Exception ex) {
			System.err.println("ERROR inv torre: "+ex);
		}
		return windowsInventary;
	}

	private static List<CiWindows> identyfyDuplicateIPAndCodHostname(List<CiWindows> windowsInventary,
			HashMap<String, CiWindows> invWindowshm, HashMap<String, CiWindows> duplicateCodHostname,
			CiWindows ciWindows) {
		if(invWindowshm.get(ciWindows.getIpGestion())==null) {
			invWindowshm.put(ciWindows.getIpGestion(), ciWindows);
		}else {
			CiWindows firstOcurrence=invWindowshm.get((ciWindows).getIpGestion());			
			CiWindows secondOcurrence= ciWindows;
			int idNumFirstOcurrence=firstOcurrence.getNumId()-1;
			int idNumSecondOcurrence=secondOcurrence.getNumId()-1;
			boolean hasEqualCodHostname=firstOcurrence.getCodHostname().equalsIgnoreCase(secondOcurrence.getCodHostname());
			boolean hasEqualIpMgmt=firstOcurrence.getIpGestion().equals(secondOcurrence.getIpGestion());
			boolean cisAreActive=firstOcurrence.getEstado().toUpperCase().contains("ACTIVO") && secondOcurrence.getEstado().toUpperCase().contains("ACTIVO");
			/*CASO 1 DUPLICADO IP  COG_HOSTNAME &&  && STATUS ACTIVO
			Posible ci duplicado en inv torre*/
			if(hasEqualCodHostname && cisAreActive) {
				String observacion =  "|  Posible ci duplicado en inv torre |";
				if((firstOcurrence.getObservacionesRevision().contains(observacion))) {
					firstOcurrence.setObservacionesRevision(firstOcurrence.getObservacionesRevision()+observacion);
					windowsInventary.set(idNumFirstOcurrence, firstOcurrence);	
				}else if(!(secondOcurrence.getObservacionesRevision().contains(observacion))) {
					secondOcurrence.setObservacionesRevision(secondOcurrence.getObservacionesRevision()+observacion);
					windowsInventary.set(idNumSecondOcurrence, secondOcurrence);
				}
				/*
				CASO 2 DUPLICADO IP GESTION && STATUS ACTIVO
				IP GESTION DUPLICADA EN CIS ACTIVOS.
				 */
			}else if(hasEqualIpMgmt && cisAreActive) {
				String observacion = "| ip gestion activa duplicada en inventario torre";

				if(!(firstOcurrence.getObservacionesRevision().contains(observacion))) {
					firstOcurrence.setObservacionesRevision(firstOcurrence.getObservacionesRevision()+observacion);
					windowsInventary.set(idNumFirstOcurrence, firstOcurrence);
				}if(!(secondOcurrence.getObservacionesRevision().contains(observacion))) {
					secondOcurrence.setObservacionesRevision(secondOcurrence.getObservacionesRevision()+observacion);
					windowsInventary.set(idNumSecondOcurrence, secondOcurrence);
				}
			}
		}
		//DUPLICADOS POR COD_HOSTNAM Y ACTIVOS.
		if(duplicateCodHostname.get(ciWindows.getCodHostname())==null) {
			duplicateCodHostname.put(ciWindows.getCodHostname(), ciWindows);
		}else {
			String observacion =  "|  Codigo y hoostname activo duplicado en inv torre |";
			CiWindows firstOcurrence=duplicateCodHostname.get(ciWindows.getCodHostname());
			CiWindows secondOcurrence=ciWindows;
			boolean cisAreActive=firstOcurrence.getEstado().toUpperCase().contains("ACTIVO") && secondOcurrence.getEstado().toUpperCase().contains("ACTIVO");
			boolean hasEqualCodHostname=firstOcurrence.getCodHostname().equalsIgnoreCase(secondOcurrence.getCodHostname());
			int idNumFirstOcurrence=firstOcurrence.getNumId()-1;
			int idNumSecondOcurrence=secondOcurrence.getNumId()-1;
			if(cisAreActive && hasEqualCodHostname ) {
				
				if(!(firstOcurrence.getObservacionesRevision().contains(observacion))) {
					firstOcurrence.setObservacionesRevision(firstOcurrence.getObservacionesRevision()+observacion);
					windowsInventary.set(idNumFirstOcurrence, firstOcurrence);
				}if(!(secondOcurrence.getObservacionesRevision().contains(observacion))) {
					secondOcurrence.setObservacionesRevision(secondOcurrence.getObservacionesRevision()+observacion);
					windowsInventary.set(idNumSecondOcurrence, secondOcurrence);
				}
			}	
		}	
		return windowsInventary;
	}
	/*
	public static void main (String []args) {	
		CiWindowsDao ciWindowsDao = new CiWindowsDao();
		//System.out.println(HEADER);
		String windowsFilePath ="D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Windows\\inv windows torre.xlsx";
		ciWindowsDao.readWindowsInventary(windowsFilePath).stream().forEach(row->System.out.println(row.toString()));
	}*/
}
