package dao;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.CiUcmdbFirewall;
import model.EcolumnsFirewallUcmdb;
import utils.Utils;

public class CiFirewallUcmdbDao {
	private static final String ucmdbFilePath = "D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Firewall\\inv firewall ucmdb.xlsx";
	public static final String HEADER ="UID FW UCMDB"+"\t"+"[Node] : Global Id"+"\t"+"[Node] : Display Label"+"\t"+"[Node] : SnmpSysName"+"\t"+"[Node] : Description"+"\t"+"[Node] : DiscoveredLocation"+"\t"+"[Node] : DiscoveredModel"+"\t"+"[Node] : [Onyx] - ServiceCode"+"\t"+"[Node] : [Onyx] - CompanyNIT"+"\t"+"[Node] : [Onyx] - CompanyName"+"\t"+"[Node] : [Group] - Owner"+"\t"+"[Node] : [Group] - Responsible"+"\t"+"[Node] : [Service Catalog] - Name"+"\t"+"[Node] : [HardwareBoard] - SerialNumber"+"\t"+"[Node] : [HardwareBoard] - DisplayLabel"+"\t"+"[Node] : [HardwareBoard] - ServiceCode"+"\t"+"[Node] : [Node] - UnidadRack"+"\t"+"[Node] : [Node] - UnidadNode"+"\t"+"[Node] : [Node] - Sede Cliente"+"\t"+"[Node] : [Node] - IpAddress"+"\t"+"Last access time"+"\t"+"Recuento Dias"+"\t"+"Status credencial"+"\t"+"Status last access time";
	public static final String TAB_HEADERS="\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";
	private static List<String>header =null;


	public List <CiUcmdbFirewall> readFirewallInventary(String firewallFilePath){
		ArrayList<CiUcmdbFirewall> ucmdbInventary = new ArrayList<>();
		short cellCount=0;
		String lastAccessTimeStatus="";
		String credentialStatus="";
		String scaleDays="";
		String [][]scaleDaysAndstatusCredential;
		
		try(FileInputStream file = new FileInputStream(new File(firewallFilePath));
				XSSFWorkbook workbook = new XSSFWorkbook(file);
				){
			XSSFSheet firewallSheet = workbook.getSheetAt(0);
			Iterator <Row> rowIterator = firewallSheet.iterator();
			header = new ArrayList<String>();
			String cellFirewallValue = "";
			int currentRow=0;
			while (rowIterator.hasNext()) { // recorre filas de archivoFirewall				
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				CiUcmdbFirewall ciUcmdbFirewall = new CiUcmdbFirewall();
				currentRow=row.getRowNum();
				cellCount = (short) row.getLastCellNum();
				for (short currentCell = 0; currentCell < cellCount; currentCell++) { // recorre las columbas del inv
					//Obteniendo header
					cellFirewallValue = Utils.getStringCellValue(cellIterator.next());
					if (row.getRowNum() == 0) {
						header.add(cellFirewallValue);
						
					}else if ((row.getRowNum() >0 && currentCell< header.size())) {
						String headerValue=header.get(currentCell);	
						EcolumnsFirewallUcmdb columnValue=EcolumnsFirewallUcmdb.valueOfLabel(headerValue);
						if(columnValue!=null ) {
							switch (columnValue) {
							// columna que se necesita.
							case GLOBAL_ID:
								ciUcmdbFirewall.setGlobalId(cellFirewallValue);
							case DISPLAY_LABEL:
								ciUcmdbFirewall.setDisplayLabel(cellFirewallValue);
								break;
							case SNMP_SYSNAME:
								ciUcmdbFirewall.setSnmpSysName(cellFirewallValue);
								break;
							case DESCRIPTION:
								ciUcmdbFirewall.setDescription(cellFirewallValue);
								break;
							case DISCOVERED_LOCATION:
								ciUcmdbFirewall.setDiscoveredLocation(cellFirewallValue);
							case DISCOVERED_MODEL:
								ciUcmdbFirewall.setDiscoveredModel(cellFirewallValue);;
							case COD_SERVICIO:
								ciUcmdbFirewall.setOnyxServiceCode(cellFirewallValue);
								break;
							case COMPANY_NIT:								
								ciUcmdbFirewall.setCompanyNit(cellFirewallValue);
								break;								
							case COMPANY_NAME:
								ciUcmdbFirewall.setCompanyName(cellFirewallValue);
							case OWNER:
								ciUcmdbFirewall.setFunctionalGroupOwner(cellFirewallValue);
								break;
							case RESPONSIBLE:
								ciUcmdbFirewall.setFunctionalGroupResponsable(cellFirewallValue);
								break;
							case SERVICE_CATALOG:
								ciUcmdbFirewall.setServiceCatalog(cellFirewallValue);
								break;
							case SERIAL_NUMBER:
								ciUcmdbFirewall.setSerialNumber(cellFirewallValue);
								break;
							case HARDWARE_BOARD_DISPLAY_LABEL:
								ciUcmdbFirewall.setHardwareBoard(cellFirewallValue);
								break;
							case HARDWARE_BOARD_SERVICE_CODE:
								ciUcmdbFirewall.setHardwareBoardServiceCode(cellFirewallValue);
							case IPADDRESS:
								ciUcmdbFirewall.setIpAddress(cellFirewallValue);
								break;
							case UNIDAD_RACK:
								ciUcmdbFirewall.setUnidadRack(cellFirewallValue);
								break;
							case UNIDAD_NODE:
								ciUcmdbFirewall.setUnidadNode(cellFirewallValue);
								break;
							case SEDE_CLIENTE:
								ciUcmdbFirewall.setSedeCliente(cellFirewallValue);
								break;
							case LAST_ACCESS_TIME:
								ciUcmdbFirewall.setLastAccessTime(cellFirewallValue);
								break;
								/*
							case "[Firewall] :  ( [HardwareBoard] - Last Access Time )":
								ciUcmdbFirewall.setHardwareBoardLastAccessTime(cellValue);
								break;
								 */
							}						

						}

					}
				}if(ciUcmdbFirewall.getIpAddress()!=null){
					scaleDaysAndstatusCredential=Utils.clasifyDaysLastAccessTimes(ciUcmdbFirewall.getLastAccessTime(), Utils.PATTERN_DATE);
					scaleDays = scaleDaysAndstatusCredential[0][0];
					credentialStatus = scaleDaysAndstatusCredential[0][1];
					lastAccessTimeStatus=scaleDaysAndstatusCredential[0][2];
					
					ciUcmdbFirewall.setUid(currentRow);
					ciUcmdbFirewall.setRecuentoDias(scaleDays);
					ciUcmdbFirewall.setDiscoveryStatus(credentialStatus);
					ciUcmdbFirewall.setStatusLastAccessTime(lastAccessTimeStatus);
					ucmdbInventary.add(ciUcmdbFirewall);
					//System.out.println(ciUcmdbFirewall);
				}	

			}
			System.out.println("CANTIDAD REGISTROS EN INVENTARIO UCMDB "+ucmdbInventary.size());

		}catch(Exception ex) {
			System.out.print("Error al leer "+ex +" ");
			ex.printStackTrace();
		}

		return ucmdbInventary;
	}
	
	
	
	/*
	public static void main (String []args) {	
		CiFirewallUcmdbDao ciFirewallUcmdbDao = new CiFirewallUcmdbDao();		
		List<CiUcmdbFirewall> ucmdbInventary =ciFirewallUcmdbDao.readFirewallInventary(ucmdbFilePath);
		header.stream().forEach(head ->System.out.print(head+"\t"));
		System.out.println();
		ucmdbInventary.stream().forEach(ciFirewallUcmdb->System.out.println(ciFirewallUcmdb));
	}
	*/
}
