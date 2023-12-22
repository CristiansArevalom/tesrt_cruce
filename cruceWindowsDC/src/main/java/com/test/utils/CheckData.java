package com.test.utils;
import java.io.File;
import java.io.FileInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;

import com.test.exceptions.InventoryException;


public class CheckData {

	public CheckData() {
		// TODO Auto-generated constructor stub
	}
	
	public static void menu() {
		System.out.println("1 = Cruce de inventario torre vs UCMDB");
		System.out.println("2 = Cruce de UCMDB vs Inventario torre");
		System.out.println("0 = Salir");
	}
	
	public static String getStringCellValue(Cell cell) {
		String cellValue ="";
		switch (cell.getCellType()) {
		case STRING:
			cellValue = cell.getStringCellValue();
			break;
		case NUMERIC:
			cellValue = cell.getNumericCellValue() + "";
			break;
		case BLANK:
			cellValue = "";
			break;
		case BOOLEAN:
			cellValue = cell.getBooleanCellValue() + "";
		default:
			break;
		}
		return cellValue;
	}
	
	
	public static boolean checkIpAddressOK(String text) {
		String regexCode = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		Pattern pattern = Pattern.compile(regexCode);
		Matcher matcher = pattern.matcher(text);
		return (matcher.find() ? true : false);
	}
	public static boolean checkIpAddressWithError(String text) {
		String regexCode = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])(\\s|\\s\\s)";
		Pattern pattern = Pattern.compile(regexCode);
		Matcher matcher = pattern.matcher(text);
		return (matcher.find() ? true : false);
	}
	public static String checkAndFormatIpAddress(String text) {
		String ipAdress ="";
		if(checkIpAddressOK(text)) {
			ipAdress = text;
		}else if(checkIpAddressWithError(text)) {
			ipAdress=text.replaceAll(" ", "");
		}else {
			throw new InventoryException("La Ip "+text+" no es valida");
		}

		return ipAdress;
	}
	//Mira si el codigoy hostname cumple con estandar de guionbajo, si no, en los casos que se pueda lo ajusta y retorna
	public static String checkAndFormatServCodeAndHostname(String text){
		String ServCodeAndHostnameOK="";
		if(checkServCodeAndHostnameOK(text)) { // si cumple con codigo de servicio y nombre, retorna el texto
			ServCodeAndHostnameOK = text;
		}else if(checkServCodeWithError(text)) {// si entrra, es que tiene algun guin bajo
			ServCodeAndHostnameOK =  text.replace("\37", "")  
					.replaceFirst(" _ ", "_")
					.replaceFirst(" _", "_")
					.replaceFirst("_ ", "_")
					.replaceFirst(" - ", "_")
					.replaceFirst("- ", "_")
					.replaceFirst(" -", "_")
					.replaceFirst("-", "_");
		}else {
			ServCodeAndHostnameOK = text;
			//throw new InventoryException("El valor "+text+" No cumple con estandar codserv_hostame");
		}
		//ARROJJAR ERROR, NO CUMPLE ESTANDAR
		return ServCodeAndHostnameOK;
	}
	public static boolean checkServCodeWithError(String text) {
		String regexCode = ("(([A-Z]{3}[0-9]{4}|[A-Z]{5}[0-9]{2}|[A-Z]{4}[0-9]{3})|([A-Z]{4}[0-9]{4}|[A-Z]{5}[0-9]{3}))"
				+ "(\\_\\37\\w.*)|((\\ _ \\w*|\\ _\\w*|\\_ \\w*)|(\\ - \\w*|\\- \\w*|\\-\\w*)|\\ -\\w*)"); // codserv _hostname
		Pattern pattern = Pattern.compile(regexCode);
		Matcher matcher = pattern.matcher(text);
		return (matcher.find() ? true : false);
	}

	public static boolean checkServCodeAndHostnameOK(String text) {
		String regexCodeHostname = ("^(([A-Z]{3}[0-9]{4}|[A-Z]{5}[0-9]{2}|[A-Z]{4}[0-9]{3})|([A-Z]{4}[0-9]{4}|[A-Z]{5}[0-9]{3}))(\\_\\w.*)");// codserv_hostname
		Pattern pattern = Pattern.compile(regexCodeHostname);
		Matcher matcher = pattern.matcher(text);
		return (matcher.find() ? true : false);
	}
	
	
	
}
