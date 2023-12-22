package com.test.utils;

import static java.time.temporal.ChronoUnit.DAYS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class Libs {

	public static final String OLD_PATTERN_DATE=("EEEE d 'de' MMMM 'de' yyyy HH'H'mm'' z");
            //"Saturday, April 22, 2023 5:17:22 PM COT"
    //EEEE, MMMM d, yyyy h:mm:ss a z
        public static final String PATTERN_DATE = ("EEEE, MMMM d, yyyy h:mm:ss a z");
	private static final Long RANGE_DAYS_LAST_ACCESS_TIME = (long) 15;

	
	private static boolean serviceCodeHostnameIsValid(String serviceCode) { 
		boolean serviceCodeIsValid=false;
		String regexServiceCode = ("^(?i)(([A-Z]{3}[0-9]{4}|[A-Z]{5}[0-9]{2}|[A-Z]{4}[0-9]{3})|([A-Z]{4}[0-9]{4}|[A-Z]{5}[0-9]{3}))(\\_\\w.*)$");// codserv
		Pattern pattern = Pattern.compile(regexServiceCode);
		Matcher matcher = pattern.matcher(serviceCode);
		serviceCodeIsValid = (matcher.find() ? true : false);		
		return serviceCodeIsValid;
	}
	private static boolean serviceCodeHostHasBadUnderscore(String serviceCodeHostname) {
		boolean hasBadUnderScore=false;
		String regexServiceCode = ("^(?i)(([A-Z]{3}[0-9]{4}|[A-Z]{5}[0-9]{2}|[A-Z]{4}[0-9]{3})|([A-Z]{4}[0-9]{4}|[A-Z]{5}[0-9]{3}))");
		String regexHostname=("((\\_\\37|\\ _ |\\ _|\\_ |\\ - |\\- |\\-|\\ -)?(\\w.*))$");
		Pattern pattern = Pattern.compile(regexServiceCode+regexHostname);
		Matcher matcher = pattern.matcher(serviceCodeHostname);
		hasBadUnderScore = matcher.find();
		return hasBadUnderScore;
	}

	public static String extactServiceCode(String text) { //XXXXXX_XXXX
		String serviceCode =text;
		String regexServiceCode = ("(?i)(([A-Z]{3}[0-9]{4}|[A-Z]{5}[0-9]{2}|[A-Z]{4}[0-9]{3})|([A-Z]{4}[0-9]{4}|[A-Z]{5}[0-9]{3}))");// codserv
		Pattern pattern = Pattern.compile(regexServiceCode);
		Matcher matcher = pattern.matcher(serviceCode);
		if(matcher.find()) {
			serviceCode=matcher.group();
		}
		//serviceCode = (matcher.find() ? matcher.group() : text);
                
		return serviceCode;
	}

	private static boolean ipAddressIsValid(String ipAddress) {
		boolean ipAddressIsValid=false;
		String regexCode = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		Pattern pattern = Pattern.compile(regexCode);
		Matcher matcher = pattern.matcher(ipAddress);
		ipAddressIsValid = (matcher.find() ? true : false);
		return ipAddressIsValid;
	}

	private static List <String> extractIpFromText(String text) {
		//String regexCode = "\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b";
		String regexCode = "\\b(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\b";
		Pattern pattern = Pattern.compile(regexCode);
		Matcher matcher = pattern.matcher(text);
		List<String> listMatches = new ArrayList<String>();
		while(matcher.find())
		{
			listMatches.add(matcher.group());
		}
		return listMatches;
	}
	
	public static String checkAndFormatIpAddress(String text) {
		String ipAdress ="";
		List <String>ipAddress = extractIpFromText(text);
		if(ipAddressIsValid(text)) {
			ipAdress = text;

		}else if (ipAddress.size()>0){
			ipAdress=ipAddress.toString();
			ipAdress = ipAdress.substring(1, ipAdress.length()-1);
		}
		return ipAdress;
	}
	

	public static String checkAndFormatServCodeAndHostname(String text) throws Exception {
		String ServCodeAndHostnameOK="";
		if(serviceCodeHostnameIsValid(text)) { // si cumple con codigo de servicio y nombre, retorna el texto
			ServCodeAndHostnameOK = text;
		}else if(serviceCodeHostHasBadUnderscore(text)) {// si entrra, es que tiene algun guin bajo
			ServCodeAndHostnameOK =  text.replace("\37", "")  
					.replaceFirst(" _ ", "_")
					.replaceFirst(" _", "_")
					.replaceFirst("_ ", "_")
					.replaceFirst(" - ", "_")
					.replaceFirst("- ", "_")
					.replaceFirst(" -", "_")
					.replaceFirst("-", "_");
		}else {
			throw new Exception("El valor "+text+" No cumple con estandar codserv_hostame | ");
			//throw new InventoryException("El valor "+text+" No cumple con estandar codserv_hostame | ");
		}
		//ARROJJAR ERROR, NO CUMPLE ESTANDAR
		return ServCodeAndHostnameOK;
	}

	public static String extractServCodeAndHostname(String text) {
		String codHostname="";
		return codHostname;				
	}

	public static String getStringCellValue(Cell cell) {
		String cellValue ="";
		switch (cell.getCellType()) {
		case STRING:
			cellValue = cell.getStringCellValue();
			break;
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				cellValue = dateFormat.format(cell.getDateCellValue()) + "";
			} else {
				cellValue = cell.getNumericCellValue() + "";
			}
			break;
		case BLANK:
			cellValue = "";
			break;
		case BOOLEAN:
			cellValue = cell.getBooleanCellValue() + "";

			break;		
		default:
			break;
		}
		return cellValue;
	}

	public static LocalDateTime extractDateFromLastAccesstime(String LastAccessTime,String PATTERN_DATE) {
            Locale defaultLocale = Locale.getDefault();
            Locale.setDefault(Locale.ENGLISH);
            Locale usaLocale = Locale.getDefault();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_DATE, usaLocale);
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_DATE, new Locale("en"));
            LocalDateTime localDateTime = LocalDateTime.parse(LastAccessTime, formatter);
        return localDateTime;
	}
	public static long getDiferenceDaysLastAccessTime(String lastAccessTime) {
		long days=-1;
		if (lastAccessTime.length()>0) {
			String [] lastAccess = lastAccessTime.split("; ");
			//LocalDateTime limitRecentAccessTime = LocalDateTime.now().minusDays(RANGE_DAYS_LAST_ACCESS_TIME);
			//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy",  Locale.ENGLISH);

			LocalDateTime maxLastAccessTime = LocalDateTime.MIN;

			for (String date:lastAccess) {
				LocalDateTime temporalDateTime = Libs.extractDateFromLastAccesstime(date, PATTERN_DATE);
				if(temporalDateTime.isAfter(maxLastAccessTime)) {
					maxLastAccessTime= temporalDateTime;
				}
			} 
			days = Math.abs(DAYS.between(LocalDateTime.now(), maxLastAccessTime));
		}
		return days;
	}
	
	public static String clasifyDaysLastAccessTime(String lastAccessTime) {
		String scaleDays = "Mas de 60 Dias";
		if (lastAccessTime.length()>0) {
			/*
			String [] lastAccess = lastAccessTime.split(", ");
			//LocalDateTime limitRecentAccessTime = LocalDateTime.now().minusDays(RANGE_DAYS_LAST_ACCESS_TIME);
			//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy",  Locale.ENGLISH);

			LocalDateTime maxLastAccessTime = LocalDateTime.MIN;

			for (String date:lastAccess) {
				LocalDateTime temporalDateTime = Libs.extractDateFromLastAccesstime(date, PATTERN_DATE);
				if(temporalDateTime.isAfter(maxLastAccessTime)) {
					maxLastAccessTime= temporalDateTime;
				}
			} */
			
			long dias = Math.abs(getDiferenceDaysLastAccessTime(lastAccessTime));
			dias=Math.abs(dias);
			if(dias>0 && dias <=15) {
				scaleDays= "0 - 15 Dias";
			}else if(dias>15 && dias <=30) {
				scaleDays= "15 - 30 Dias";
			}else if(dias>30 && dias <=60) {
				scaleDays= "30 - 60 Dias";
			}else {
				scaleDays= "Mas de 60 Dias";
			}

		}
		return scaleDays;
	}
	public static String checkLastAccessTime(String lastAccessTime) {
		String lastAccessTimeStatus = "";
		switch(lastAccessTime) {
			case "0 - 15 Dias":
				lastAccessTimeStatus ="Last Access Time Reciente"+'\u0009'+"Credencial OK";
				break;
			case "Mas de 60 Dias":
				lastAccessTimeStatus = "sin last access time"+'\u0009'+"Credencial PDT";
				break;
			default:
				lastAccessTimeStatus ="Last Access Time Antiguo"+'\u0009'+"Credencial PDT";
				break;
		}
		return lastAccessTimeStatus;
	}
	
/*
	public static String checkLastAccessTime(String lastAccessTime) {
		String lastAccessTimeStatus = "sin last access time"+'\u0009'+"Credencial PDT";

		if (lastAccessTime.length()>0) {
			String [] lastAccess = lastAccessTime.split(", ");
			LocalDateTime limitRecentAccessTime = LocalDateTime.now().minusDays(RANGE_DAYS_LAST_ACCESS_TIME);
			//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy",  Locale.ENGLISH);

			LocalDateTime maxLastAccessTime = LocalDateTime.MIN;

			for (String date:lastAccess) {
				LocalDateTime temporalDateTime = Libs.extractDateFromLastAccesstime(date, PATTERN_DATE);
				if(temporalDateTime.isAfter(maxLastAccessTime)) {
					maxLastAccessTime= temporalDateTime;
				}
			}
			if(maxLastAccessTime.isBefore(limitRecentAccessTime)) {
				lastAccessTimeStatus ="Last Access Time Antiguo"+'\u0009'+"Credencial PDT";
			}else {
				lastAccessTimeStatus ="Last Access Time Reciente"+'\u0009'+"Credencial OK";
			}

		}
		return lastAccessTimeStatus;
	}
	*/
	
	
	public static String [][] clasifyDaysLastAccessTimes(String lastAccessTime,String PATTERN_DATE) {
		String credentialStatus="Credencial PDT";
		String scaleDays = "Mas de 60 Dias";
		String lastAccessTimeStatus="Sin last Access Time";
		String [][] statusAccessTime = new String [1][3];
		statusAccessTime[0][0]=scaleDays;
		statusAccessTime[0][1]=credentialStatus;
		statusAccessTime[0][2]=lastAccessTimeStatus;	
		if (lastAccessTime.length()>0) {
			String [] lastAccess = lastAccessTime.split("; ");
			//LocalDateTime limitRecentAccessTime = LocalDateTime.now().minusDays(RANGE_DAYS_LAST_ACCESS_TIME);
			//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy HH'H'mm'' z");
			LocalDateTime maxLastAccessTime = LocalDateTime.MIN;

			for (String date:lastAccess) {
				LocalDateTime temporalDateTime = extractDateFromLastAccesstime(date, PATTERN_DATE);
				//LocalDateTime temporalDateTime = LocalDateTime.parse(date, formatter);	
				if(temporalDateTime.isAfter(maxLastAccessTime)) {
					maxLastAccessTime= temporalDateTime;
				}
			} 
			
			long dias = Math.abs(DAYS.between(LocalDateTime.now(), maxLastAccessTime));
			dias=Math.abs(dias);
			//System.out.println(dias+"******************");
			if(dias>=0 && dias <=15) {
				scaleDays= "0 - 15 Dias";
				credentialStatus="Credencial OK";
				lastAccessTimeStatus="Last Access Time Reciente";
			}else if(dias>15 && dias <=30) {
				scaleDays= "15 - 30 Dias";
				credentialStatus="Credencial PDT";
				lastAccessTimeStatus="Last Access Time Antiguo";
			}else if(dias>30 && dias <=60) {
				scaleDays= "30 - 60 Dias";
				credentialStatus="Credencial PDT";
				lastAccessTimeStatus="Last Access Time Antiguo";
			}else {
				scaleDays= "Mas de 60 Dias";
				credentialStatus="Credencial PDT";
				lastAccessTimeStatus="Sin last Access Time";
			}
			statusAccessTime[0][0]=scaleDays;
			statusAccessTime[0][1]=credentialStatus;
			statusAccessTime[0][2]=lastAccessTimeStatus;
		}
		return statusAccessTime;
	}

	public static boolean excelFormatIsValid(String excelPathFile) throws Exception {
		if(excelPathFile.endsWith(".xlsx")) {
			//Checar si la ruta existe. 
			return true;
		}else {
			throw new Exception("El formato del archivo "+excelPathFile+"No es valido");
		}
	}
	
	public static boolean excelFormatIsValid(String excelPathFile,String sheetName) throws Exception {
		boolean excelSheetIsValid=false;
		if(excelPathFile.endsWith(".xlsx")) {
			FileInputStream file = new FileInputStream(new File(excelPathFile));
			XSSFWorkbook wb = new XSSFWorkbook(file);
			List<String> sheetNames = new ArrayList<String>();
			/*** Obtiene el listado de hojas en el archivo excel y compara si esta "sheetName"***/
			for (int i=0; i<wb.getNumberOfSheets(); i++) {
			    sheetNames.add( wb.getSheetName(i) );
			   if(sheetNames.contains(sheetName)) {
				   excelSheetIsValid= true;
				   break;
			   }
			}
			file.close();
			wb.close();
			if(!excelSheetIsValid) {
				throw new Exception("La hoja '"+sheetName+"' No esta en el archivo "+excelPathFile);
			}
		}else {
			throw new Exception("El formato del archivo '"+excelPathFile+"' No es valido");
		}
		return excelSheetIsValid;
	}
	
	public static String writeBook(ArrayList<String> rowText, String rowTextSeparator,String nameFile,String sheetName,String folderOutputPath) {
		Workbook libro = new XSSFWorkbook();
		//final String nombreArchivo="CruceUnix.xlsx";
		final String nombreArchivo=nameFile+".xlsx";
		String ubicacionArchivoSalida = folderOutputPath+nombreArchivo;
		//Sheet hoja = libro.createSheet("CruceUnix");
		Sheet hoja = libro.createSheet(sheetName);
		final String rowSeparator=rowTextSeparator;

		int indiceFila =0;
		Row fila = hoja.createRow(indiceFila);
		for (int i =0;i<rowText.size();i++) {
			fila = hoja.createRow(indiceFila);
			//String currentRow [] = rowText.get(i).split("\u0009");
			String currentRow [] = rowText.get(i).split(rowSeparator);
			for (int j =0; j<currentRow.length;j++) {
				
				if(currentRow[j].length()==0 || currentRow[j].equals("null")) {
					fila.createCell(j).setBlank();
				}else {
					fila.createCell(j).setCellValue(currentRow[j]);	
				}
				
			}
			indiceFila++;
		}

		//File directorioActual = new File(".");
		//String ubicacion = directorioActual.getAbsolutePath();
		//String ubicacionArchivoSalida = ubicacion.substring(0,ubicacion.length()-1)+nombreArchivo;
		//ubicacionArchivoSalida = "D:\\OneDrive - GLOBAL HITSS\\Automatizmos\\ArchivosParaPowerBi\\Unix\\"+nombreArchivo;
		
		FileOutputStream outputStream;
		try {
			outputStream= new FileOutputStream(ubicacionArchivoSalida);
			libro.write(outputStream);
			libro.close();
			System.out.println("Libro generado correctamente en: " +ubicacionArchivoSalida);
		}catch (FileNotFoundException ex) {
			System.out.println("Error file not found "+ex);

		}catch(IOException ex) {
			System.out.println("Error de IO"+ ex);
		}
		return ubicacionArchivoSalida;
	}
	
	public static String checkIfCiApplyUcmdb(String observacionTorre) {
		String aplica ="SI";
		if(observacionTorre.contains("excluir de la UCMDB") || observacionTorre.contains("UCMDB")) {
			aplica ="NO";
		}
		return aplica;
	}
	

}
