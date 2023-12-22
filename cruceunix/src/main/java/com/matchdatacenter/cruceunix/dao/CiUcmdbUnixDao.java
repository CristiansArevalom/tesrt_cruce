package com.matchdatacenter.cruceunix.dao;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.matchdatacenter.cruceunix.model.CiUcmdbColumns;
import com.matchdatacenter.cruceunix.model.CiUcmdbUnix;
import com.matchdatacenter.cruceunix.utils.Libs;

public class CiUcmdbUnixDao {
	public static final String HEADER="numID"+"\t"+"Global Id"+"\t"+"SERVICECODE"+"\t"+"DISPLAYLABEL"+"\t"+"IP_GESTION"+"\t"+"IP_ADDRESS"+"\t"+"PROTOCOLO_DESCUBRIMIENTO"+"\t"+"LAST_ACCESS_TIME"+"\t"+"STATUS_LAST_ACCESS_TIME"+"\t"+"STATUS CREDENCIAL"+"\t"+"ESCALA DIAS";
	
	public List<CiUcmdbUnix> readUcmdbInventory(String ucmdbFilePath) throws Exception {
		int uidRowCount=0;
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
							case UCMDB_GLOBAL_ID:
								ciUcmdbUnix.setGlobalId(cellValue);
								break;
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
				if(ciUcmdbUnix.getDisplayLabel()!=null && ciUcmdbUnix.getDisplayLabel().length()>0 ){
					String uid=(uidRowCount++)+"|"+ciUcmdbUnix.getDisplayLabel()+"_"+ciUcmdbUnix.getOnyxServiceCodes();
					ciUcmdbUnix.setUid(uid);
					ciUcmdbUnix.setNumID(uidRowCount);
					
					String[][] scaleDaysAndstatusCredential = Libs.clasifyDaysLastAccessTimes(ciUcmdbUnix.getLastAccessTimeProtocolo(),Libs.PATTERN_DATE);
					String scaleDays = scaleDaysAndstatusCredential[0][0];
					String credentialStatus = scaleDaysAndstatusCredential[0][1];
					String lastAccessTimeStatus = scaleDaysAndstatusCredential[0][2];

					ciUcmdbUnix.setRecuentoDias(scaleDays);
					ciUcmdbUnix.setDiscoveryStatus(credentialStatus);
					ciUcmdbUnix.setStatusLastAccessTime(lastAccessTimeStatus);
					
					ucmdbUnixInventary.add(ciUcmdbUnix);
					
					
					
					
				}				
			}
			System.out.println("CANTIDAD REGISTROS EN UCMDB 2020 "+ucmdbUnixInventary.size());
			file.close();
			workbook.close();			
		}	
		return ucmdbUnixInventary;
		
	}
	/*
	public static void main(String args[]) {
		CiUcmdbUnixDao ciUcmdbDao = new CiUcmdbUnixDao();
		String ucmdbPath="D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Unix\\inv unix ucmdb.xlsx";
		try {
			List<CiUcmdbUnix> ucmdbInv = ciUcmdbDao.readUcmdbInventory(ucmdbPath);
			ucmdbInv.stream().forEach(ci -> System.out.println(ci.toString()));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
