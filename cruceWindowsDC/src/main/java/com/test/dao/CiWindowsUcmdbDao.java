package com.test.dao;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.test.model.CiUcmdbWindows;
import com.test.model.CiUcmdbWindowsColumns;
import com.test.utils.Libs;


public class CiWindowsUcmdbDao {

	private static final String ucmdbFilePath = "D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Windows\\inv windows ucmdb.xlsx";

	public List<CiUcmdbWindows> readUcmdbInventory(String ucmdbFilePath) {
		List<CiUcmdbWindows> ucmdbWindowsInventary = new ArrayList<>();
		String lastAccessTimeStatus="";
		String credentialStatus="";
		String scaleDays="";
		String [][]scaleDaysAndstatusCredential;

		try(FileInputStream file = new FileInputStream(new File(ucmdbFilePath));
				XSSFWorkbook workbook = new XSSFWorkbook(file);){
			XSSFSheet ucmdbSheet = workbook.getSheetAt(0);
			Iterator<Row> rowUcmdbIterator = ucmdbSheet.iterator();
			ArrayList<String> header = new ArrayList<String>();

			while (rowUcmdbIterator.hasNext()) { // recorre filas
				CiUcmdbWindows ciUcmdbWindows = new CiUcmdbWindows();
				Row rowUcmdb = rowUcmdbIterator.next();
				short cellCount = (short) rowUcmdb.getPhysicalNumberOfCells();
				for (short currentCell = 0; currentCell < cellCount; currentCell++) { // recorre las columbas del inv
					String cellValue = "";
					cellValue = Libs.getStringCellValue(rowUcmdb.getCell(currentCell,MissingCellPolicy.CREATE_NULL_AS_BLANK));
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
							case UCMDB_GLOBAL_ID:
								ciUcmdbWindows.setGlobalId(cellValue);
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
							case UCMDB_NOTE:
								ciUcmdbWindows.setNote(cellValue);
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
					if(ciUcmdbWindows.getDisplayLabel()!=null && ciUcmdbWindows.getDisplayLabel().length()>0){
						scaleDaysAndstatusCredential=Libs.clasifyDaysLastAccessTimes(ciUcmdbWindows.getLastAccessTimeProtocolo(), Libs.PATTERN_DATE);
						scaleDays = scaleDaysAndstatusCredential[0][0];
						credentialStatus = scaleDaysAndstatusCredential[0][1];
						lastAccessTimeStatus=scaleDaysAndstatusCredential[0][2];
						
						ciUcmdbWindows.setNumId(ucmdbWindowsInventary.size()+1);
						ciUcmdbWindows.setRecuentoDias(scaleDays);
						ciUcmdbWindows.setDiscoveryStatus(credentialStatus);
						ciUcmdbWindows.setStatusLastAccessTime(lastAccessTimeStatus);
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


/*
	public static void main (String []args) {	
		String path="";
		CiWindowsUcmdbDao ciWindowsUcmdbDao = new CiWindowsUcmdbDao();		
		List<CiUcmdbWindows> ucmdbInventary =ciWindowsUcmdbDao.readUcmdbInventory(ucmdbFilePath);
		System.out.println();
		ucmdbInventary.stream().forEach(ciFirewallUcmdb->System.out.println(ciFirewallUcmdb));
	}
*/

}
