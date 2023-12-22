package utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class unmergeCellsFile {
	//private static String path="D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Firewall\\INVENTARIO PLATAFORMAS SEGURIDAD-V18.xlsx";
	private static String path="D:\\ECM3200I\\Desktop\\umergeFilesss.xlsx";

	public String unmergeCellsFromFile(String path) {
		/*
	    For a given file, opens each sheet and unmerges the cells.
	    Aligns to left and highlights in red the unmerged cells.
	    Saves a new excel file with the _UNMERGED suffix.
		 */
		//1 leer excel.
		String ubicacionArchivoSalida="";
		try (FileInputStream file = new FileInputStream(new File(path));
			XSSFWorkbook workbook = new XSSFWorkbook(file)){
			int numSheets=workbook.getNumberOfSheets();
			for (int i =0; i<numSheets;i++) {
			XSSFSheet worksheet = workbook.getSheetAt(i);//aqui se,puede colocar el for para que coja todos los archivos.
			//ESTILO ROJO PARA LA CELDAS COMBINADAS
			CellStyle styleRed = workbook.createCellStyle();
			Font font = workbook.createFont();
			font.setColor(Font.COLOR_RED);
			styleRed.setFont(font);
			
			//2 identificar cuales celdas estan combinadas, los merge estan en la hoja, no directamente en las celdas, porlo que no es necesario recorrer filasxcolumnas
			//no se usa for porque al hacer un pop de la lista de merged, queda en posicion diferente ya que el i que se incrementa pero la lista se reduce
			while(worksheet.getNumMergedRegions()>0){
				int firstMergedPosition=0;
				CellRangeAddress mergedRegion= worksheet.getMergedRegion(firstMergedPosition);//forma un grupo con las primeras cooredenadas de las celdas combinadas
			//Cuenta los rango de las celdas que forman el merged
				int minCol = mergedRegion.getFirstColumn();
				int maxCol = mergedRegion.getLastColumn();
				int minRow= mergedRegion.getFirstRow();
				int maxRow= mergedRegion.getLastRow();
			//3 se toma el valor de la celda 
				Row rowValueTemp=worksheet.getRow(minRow);
				Cell cellValueTemp = rowValueTemp.getCell(minCol);
				String cellValue= getStringCellValue(cellValueTemp);
			//4se desAgrupan las celdas del grupo mergedRegion antes de asignar el valor; remueve las primeras celdas combinadas
				worksheet.removeMergedRegion(firstMergedPosition);//
				for(int iRow=minRow; iRow>=minRow && iRow<=maxRow;iRow++) {
					for(int jCol=minCol; jCol>=minCol && jCol<=maxCol;jCol++) {
						Row row = worksheet.getRow(iRow);
						Cell cell = row.getCell(jCol);	
						//5 se asgina a cada celda del grupo que hace parte el merge el valor
						cell.setCellValue(cellValue);
						cell.setCellStyle(styleRed);						
					}
				}
			}
			//6 Se guarda el archivo
			FileOutputStream outputStream;
			ubicacionArchivoSalida=path.substring(0, path.lastIndexOf(".xlsx"))+"_UNMERGED.xlsx";
			outputStream= new FileOutputStream(ubicacionArchivoSalida);
			workbook.write(outputStream);
			//workbook.close();
			}
			System.out.println("Libro generado correctamente en: " +ubicacionArchivoSalida);				

		}catch (FileNotFoundException ex) {
			System.out.println("Error file not found "+ex);

		}
		catch(Exception ex) {
			System.out.println("Error"+ex);
		}
		return ubicacionArchivoSalida;

		
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		unmergeCellsFile unmergeCellsFile = new unmergeCellsFile();
		unmergeCellsFile.unmergeCellsFromFile(path);

	}

}
