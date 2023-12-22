package com.matchdatacenter.cruceunix.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.matchdatacenter.cruceunix.model.CiServiceManager;
import com.matchdatacenter.cruceunix.model.EcolumnsCiServiceManager;

public class CiServiceManagerDao {

	public static String HEADER = "UID SM"+"\t"+"Nombre para mostrar"+"\t"+"Clr Clb Newucmdb"+"\t"+"Clr Service Code"+"\t"+"Subtipo"+"\t"+"Grupo de administraci�n de configuraci�n"+"\t"+"Clr Istatus"+"\t"+"titulo"+"\t"+"Comentarios"+"\t"+"Clr Arr Location"+"\t"+"Fecha y hora de modificaci�n del sistema";	
	
	/*		return  uid +"\t"+nombre + "\t" + nuevaUcmdb + "\t" + serviceCode
				+ "\t" + subtipo + "\t" + administracion + "\t" + status + "\t"
				+ titulo + "\t" + comentario + "\t" + location + "\t"
				+ fechaModificacion;*/
	List<String>header =null;

	public List<CiServiceManager> readSmInventary(String smFilePath){
		int cellCount=0;
		ArrayList <CiServiceManager> smInventary = new ArrayList<>();
		HashSet<CiServiceManager> test = new HashSet<>();
		try {

			FileInputStream fp =  new FileInputStream(new File(smFilePath));
			//procesandoarchivo
			InputStreamReader isr = new InputStreamReader(fp,"UTF-8");
			//leyendo csv
			BufferedReader br = new BufferedReader(isr);
			String text=null;
			header = new ArrayList<String>();
			String cellSmValue = "";
			String separatorCSV="\";\"";
			String separatorHeaderCsv=";";
			int currentRow=0;
			while((text=br.readLine())!=null) { //recorre filas
				CiServiceManager ciSmFw = new CiServiceManager();
				String valuesText [ ]= text.split(separatorCSV);
				cellCount=valuesText.length;
				for(short currentCell =0;currentCell<cellCount;currentCell++) {
					cellSmValue=valuesText[currentCell].replaceAll("\t", "").replaceAll("\"","");
					if(currentRow==0) { ///obteniendo header
						String valuesHeader [] = text.split(separatorHeaderCsv);
						for(String val : valuesHeader) {						
							header.add(val);//error aqui
						}

					}else if(currentCell>=0 && currentCell < header.size()) {
						String headerValue=header.get(currentCell);
						EcolumnsCiServiceManager columnValue = EcolumnsCiServiceManager.valueOfLabel(headerValue);
						if(columnValue!=null) {							
							switch (columnValue) {
							case NOMBRE:
								ciSmFw.setNombre(cellSmValue);								
								break;
							case NUEVA_UCMDB:
								ciSmFw.setNuevaUcmdb(cellSmValue);
								break;
							case SERVICE_CODE:
								ciSmFw.setServiceCode(cellSmValue);
								break;
							case SUBTIPO:
								ciSmFw.setSubtipo(cellSmValue);
								break;
							case ADMINISTRACION:
								ciSmFw.setAdministracion(cellSmValue);
								break;
							case STATUS:
								ciSmFw.setStatus(cellSmValue);
								break;
							case TITULO:
								ciSmFw.setTitulo(cellSmValue);
								break;
							case COMENTARIOS:
								ciSmFw.setComentario(cellSmValue);
								break;
							case LOCATION:
								ciSmFw.setLocation(cellSmValue);
								break;
							case FECHA_MODIFICACION:
								ciSmFw.setFechaModificacion(cellSmValue);
								break;
							}
						}
					}
				}
				if(ciSmFw.getNombre()!=null) {
					ciSmFw.setUid(currentRow);
					smInventary.add(ciSmFw);
				}
				currentRow ++;
			}


			System.out.println("CANTIDAD REGISTROS EN INVENTARIO sm "+smInventary.size());

		}catch(Exception ex) {
			System.out.print("Error al leer "+ex +" ");
			ex.printStackTrace();
		}
		return smInventary;
	}



	/*
	public static void main (String []args) {
		String filePath="inv Sm.csv";
		CiServiceManagerFwDao ciServiceManagerFwDao = new CiServiceManagerFwDao();		
		List<CiServiceManagerFw> smInventary =ciServiceManagerFwDao.readSmInventary(filePath);
				smInventary.stream().forEach(ciSm->System.out.println(ciSm.toString()));	
	}*/	
}
