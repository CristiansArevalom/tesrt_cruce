package utils;

import static java.time.temporal.ChronoUnit.DAYS;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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

public class Utils {

    Locale defaultLocale = Locale.getDefault();

    //s�bado 25 de marzo de 2023 18H06' COT
    //public static final String OLD_PATTERN_DATE = ("EEEE d 'de' MMMM 'de' yyyy HH'H'mm'' z");
    //"Saturday, April 22, 2023 5:17:22 PM COT"
    //EEEE, MMMM d, yyyy h:mm:ss a z
    //public static final String PATTERN_DATE = ("dd-MM-yyyy HH:mm:ss");
    public static final String PATTERN_DATE = ("EEEE, MMMM d, yyyy h:mm:ss a z");
    //leer cualquier excel con un arraylist,e se arraylist viene de un enum

    public static void readExcel(String pathExcelFile, List<String> Columns) {
        //TODO METODO GENERICO LEER EXCEL
    }

    public static boolean excelFormatIsValid(String excelPathFile) throws Exception {
        if (excelPathFile.endsWith(".xlsx")) {
            //Checar si la ruta existe. 
            return true;
        } else {
            throw new Exception("El formato del archivo " + excelPathFile + "No es valido");
        }
    }

    private static boolean ipAddressIsValid(String ipAddress) {
        boolean ipAddressIsValid = false;
        String regexCode = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        Pattern pattern = Pattern.compile(regexCode);
        Matcher matcher = pattern.matcher(ipAddress);
        ipAddressIsValid = (matcher.find() ? true : false);
        return ipAddressIsValid;
    }

    private static List<String> extractIpFromText(String text) {
        //String regexCode = "\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b";
        String regexCode = "\\b(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\b";
        Pattern pattern = Pattern.compile(regexCode);
        Matcher matcher = pattern.matcher(text);
        List<String> listMatches = new ArrayList<String>();
        while (matcher.find()) {
            listMatches.add(matcher.group());
        }
        return listMatches;
    }

    public static String checkAndFormatIpAddress(String text) {
        String ipAdress = "";
        List<String> ipAddress = extractIpFromText(text);
        if (ipAddressIsValid(text)) {
            ipAdress = text;

        } else if (ipAddress.size() > 0) {
            ipAdress = ipAddress.toString();
            ipAdress = ipAdress.substring(1, ipAdress.length() - 1);//para quitar los []
        }
        return ipAdress;
    }

    public List<String> extractServiceCode(String text) {
        List<String> serviceCodes = null;
        //TODO: expresion regular extraer codigo de servicio, separar por coma
        return serviceCodes;
    }

    public LocalDateTime transformDate(String LastAccessTime, String PATTERN_DATE) {
        Locale defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);
        Locale usaLocale = Locale.getDefault();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_DATE, usaLocale);

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_DATE, new Locale("en"));
        LocalDateTime localDateTime = LocalDateTime.parse(LastAccessTime, formatter);
        return localDateTime;
    }

    public static LocalDateTime extractDateFromLastAccesstime(String LastAccessTime, String PATTERN_DATE) {
        //System.out.println(LastAccessTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_DATE, Locale.ENGLISH);
        LocalDateTime localDateTime = LocalDateTime.parse(LastAccessTime, formatter);
        return localDateTime;
    }
//ERROR AQui, al  separar por , los LAT de un ci
    public static long getDiferenceDaysLastAccessTime(String lastAccessTime) {
        long days = -1;
        if (lastAccessTime.length() > 0) {
            
            String[] lastAccess = lastAccessTime.split("; ");
            //LocalDateTime limitRecentAccessTime = LocalDateTime.now().minusDays(RANGE_DAYS_LAST_ACCESS_TIME);
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy",  Locale.ENGLISH);

            LocalDateTime maxLastAccessTime = LocalDateTime.MIN;

            for (String date : lastAccess) {
                LocalDateTime temporalDateTime = Utils.extractDateFromLastAccesstime(date, PATTERN_DATE);
                if (temporalDateTime.isAfter(maxLastAccessTime)) {
                    maxLastAccessTime = temporalDateTime;
                }
            }
            days = Math.abs(DAYS.between(LocalDateTime.now(), maxLastAccessTime));
        }
        return days;
    }

    public static String[][] clasifyDaysLastAccessTimes(String lastAccessTime, String PATTERN_DATE) {
        String credentialStatus = "Credencial PDT";
        String scaleDays = "Mas de 60 Dias";
        String lastAccessTimeStatus = "Sin last AccessTime";
        String[][] statusAccessTime = new String[1][3];
        statusAccessTime[0][0] = scaleDays;
        statusAccessTime[0][1] = credentialStatus;
        statusAccessTime[0][2] = lastAccessTimeStatus;
        if (lastAccessTime.length() > 0) {
            String[] lastAccess = lastAccessTime.split("; ");
            //LocalDateTime limitRecentAccessTime = LocalDateTime.now().minusDays(RANGE_DAYS_LAST_ACCESS_TIME);
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy HH'H'mm'' z");
            LocalDateTime maxLastAccessTime = LocalDateTime.MIN;

            for (String date : lastAccess) {
                LocalDateTime temporalDateTime = extractDateFromLastAccesstime(date, PATTERN_DATE);
                //LocalDateTime temporalDateTime = LocalDateTime.parse(date, formatter);	
                if (temporalDateTime.isAfter(maxLastAccessTime)) {
                    maxLastAccessTime = temporalDateTime;
                }
            }

            long dias = Math.abs(DAYS.between(LocalDateTime.now(), maxLastAccessTime));
            dias = Math.abs(dias);
            //System.out.println(dias+"******************");
            if (dias >= 0 && dias <= 15) {
                scaleDays = "0 - 15 Dias";
                credentialStatus = "Credencial OK";
                lastAccessTimeStatus = "Last Access Time Reciente";
            } else if (dias > 15 && dias <= 30) {
                scaleDays = "15 - 30 Dias";
                credentialStatus = "Credencial PDT";
                lastAccessTimeStatus = "Last Access Time Antiguo";
            } else if (dias > 30 && dias <= 60) {
                scaleDays = "30 - 60 Dias";
                credentialStatus = "Credencial PDT";
                lastAccessTimeStatus = "Last Access Time Antiguo";
            } else {
                scaleDays = "Mas de 60 Dias";
                credentialStatus = "Credencial PDT";
                lastAccessTimeStatus = "Sin last Access Time";
            }
            statusAccessTime[0][0] = scaleDays;
            statusAccessTime[0][1] = credentialStatus;
            statusAccessTime[0][2] = lastAccessTimeStatus;
        }
        return statusAccessTime;
    }

    public static String getStringCellValue(Cell cell) {
        String cellValue = "";
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

    public static String writeBook(ArrayList<String> rowText, String rowTextSeparator, String nameFile, String sheetName, String folderOutputPath) {
        Workbook libro = new XSSFWorkbook();
        //final String nombreArchivo="CruceUnix.xlsx";
        final String nombreArchivo = nameFile + ".xlsx";
        String ubicacionArchivoSalida = folderOutputPath + nombreArchivo;
        //Sheet hoja = libro.createSheet("CruceUnix");
        Sheet hoja = libro.createSheet(sheetName);
        final String rowSeparator = rowTextSeparator;

        int indiceFila = 0;
        Row fila = hoja.createRow(indiceFila);
        for (int i = 0; i < rowText.size(); i++) {
            fila = hoja.createRow(indiceFila);
            //String currentRow [] = rowText.get(i).split("\u0009");
            String currentRow[] = rowText.get(i).split(rowSeparator);
            for (int j = 0; j < currentRow.length; j++) {
                if (currentRow[j].length() == 0 || currentRow[j].equals("null")) {
                    fila.createCell(j).setBlank();
                } else {
                    fila.createCell(j).setCellValue(currentRow[j]);
                }

            }
            indiceFila++;
        }
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(ubicacionArchivoSalida);
            libro.write(outputStream);
            libro.close();
            System.out.println("Libro generado correctamente en: " + ubicacionArchivoSalida);
        } catch (FileNotFoundException ex) {
            System.out.println("Error file not found " + ex);

        } catch (IOException ex) {
            System.out.println("Error de IO" + ex);
        }
        return ubicacionArchivoSalida;
    }

    /*
	public static void main(String[] args) {
        String fecha = "Saturday, April 22, 2023 9:58:37 PM COT";
        System.out.println(extractDateFromLastAccesstime(fecha, PATTERN_DATE));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_DATE, Locale.ENGLISH);
        LocalDateTime localDateTime = LocalDateTime.parse(fecha, formatter);
        System.out.println(localDateTime);
	}*/
}
     
