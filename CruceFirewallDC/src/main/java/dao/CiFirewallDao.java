package dao;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.CiFirewall;
import model.EcolumnsCiFirewall;
import utils.Utils;


public class CiFirewallDao {
	//DAO DATA ACCESS OBJECT, aqui se tedran los querys para recuperar los datos, TODOS LOS QUERYS SE VAN A HACER EN EL DAO
	private static String FIREWALL_FILE_PATH = "D:\\ECM3200I\\Desktop\\BasesAutomatismo\\Firewall\\RF1526415_nov22_INVENTARIO PLATAFORMAS SEGURIDAD V18 (1).xlsx";
	private int uid;

	private  final int rowHeaderFirewallInventary=3;
	public  int getRowheaderfirewallinventary() {
		return rowHeaderFirewallInventary;
	}

	public static String HEADER="# INV FW"+"\t"+"ADMINISTRADO POR"+"\t"+"DISPOSITIVO"+"\t"+"TIPO DE DISPOSITIVO"+"\t"+"FABRICANTE"+"\t"+"NOMBRE"+"\t"+"CLIENTE"+"\t"+"IP"+"\t"+"IPFORMATEADA"+"\t"+"COD. SERVICIO"+"\t"+"MODELO"+"\t"+"SERIAL"+"\t"+"FAILOVER"+"\t"+"VERSION FIRMWARE"+"\t"+"Datacenter"+"\t"+"TIPO"+"\t"+"ObservacionRevision";
	//"\t"+"Nodo";
	//"\t"+"Piso"+"\t"+"Rack"
	//+"\t"+"Unidad"+"\t"+"IPS"+"\t"+"FILTRADO"+"\t"+"TIPO" +"\t"+"Modelado"+"\t"+"ObservacionesRevision"+


	public List <CiFirewall> readFirewallInventary(String firewallFilePath){
		List <CiFirewall> firewallInventary= new ArrayList<>();	
		short cellCount=0;	
		try(FileInputStream file = new FileInputStream(new File(firewallFilePath));
				XSSFWorkbook workbook = new XSSFWorkbook(file);){
			XSSFSheet firewallSheet = workbook.getSheetAt(0);
			Iterator <Row> rowIterator = firewallSheet.iterator();
			ArrayList<String> header = new ArrayList<String>();
			String cellFirewallValue = "";
			String typeFirewall ="";

			while (rowIterator.hasNext()) { // recorre filas de archivoFirewall
				CiFirewall ciFirewall = new CiFirewall();
				Row row = rowIterator.next();
				cellCount = (short) row.getPhysicalNumberOfCells();
				for (short currentCell = 0; currentCell < cellCount; currentCell++) { // recorre las columbas del inv
					//Obteniendo tipo de CI (si columna 0 esta diligenciada pero la siguiente esta vacia y columna actual es ==0
					cellFirewallValue = Utils.getStringCellValue(row.getCell(currentCell,MissingCellPolicy.CREATE_NULL_AS_BLANK));
					//cellFirewallValue = Utils.getStringCellValue(cellIterator.next());
					if(currentCell==0 && row.getRowNum()==0 &&  header.size()==0) {
						typeFirewall=cellFirewallValue;
					}
					if (row.getRowNum() == rowHeaderFirewallInventary) {
						header.add(cellFirewallValue);
						//trae el �primer tipo de firewall						
					}					
					else if ((currentCell< header.size())) {
						String headerValue=header.get(currentCell);
						EcolumnsCiFirewall columnValue=EcolumnsCiFirewall.valueOfLabel(headerValue);
						if(columnValue!=null ) {
							switch (columnValue) {
							case ADMINISTRACION:
								ciFirewall.setAdministracion(cellFirewallValue);
								//aqui se puede scar el tipo
								break;
							case DISPOSITIVO:
								ciFirewall.setDispositivo(cellFirewallValue);
								break;
							case TIPO_DISPOSITIVO:
								ciFirewall.setTipoDispositivo(cellFirewallValue);
								break;
							case FABRICANTE:
								ciFirewall.setFabricante(cellFirewallValue);
								break;
							case NOMBRE:
								ciFirewall.setNombre(cellFirewallValue);
								break;
							case CLIENTE:
								ciFirewall.setCliente(cellFirewallValue);
								break;
							case IP_GESTION:
								try{
									ciFirewall.setIpGestion((cellFirewallValue));
									ciFirewall.setIPFormateada(Utils.checkAndFormatIpAddress(cellFirewallValue));
								}catch(Exception ex) {
									ciFirewall.setIpGestion((cellFirewallValue));
									ciFirewall.setIPFormateada(Utils.checkAndFormatIpAddress(cellFirewallValue));
									ciFirewall.setObservacionesRevision(ciFirewall.getObservacionesRevision()+" IP NO CUMPLE ESTANDAR");
								}

								break;
							case COD_SERVICIO:
								ciFirewall.setCodServicio(cellFirewallValue);
								break;
							case MODELO:
								ciFirewall.setModelo(cellFirewallValue);
								break;
							case SERIAL:
								ciFirewall.setSerial(cellFirewallValue);
								break;
							case FAILOVER:
								ciFirewall.setFailOver(cellFirewallValue);
								break;
							case VERSION_FIRMWARE:
								//ciFirewall.setVersionFirmware(cellFirewallValue);
								break;
							case DATACENTER:
								//ciFirewall.setDatacenter(cellFirewallValue.replaceAll("\\n", " "));
								break;	
							case BUNKER:
								//ciFirewall.setBunker(cellFirewallValue);
								break;	
							case NODO:
								//ciFirewall.setNodo(cellFirewallValue);
								break;	
							case PISO:
								//ciFirewall.setPiso(cellFirewallValue);
								break;
							case RACK:
								//ciFirewall.setRack(cellFirewallValue);
								break;	
							case UNIDAD:
								//ciFirewall.setUnidad(cellFirewallValue);
								break;	
							case IPS:
								ciFirewall.setIps(cellFirewallValue);
								break;
							}
						}
					}
				} 
				boolean ciHasNotValues = (ciFirewall.getAdministracion()!=null) && (!ciFirewall.getAdministracion().isEmpty()) && (ciFirewall.getDispositivo().isEmpty());
				boolean ciFirewallHasValues = (ciFirewall.getAdministracion()!=null) && !(ciFirewall.getAdministracion().isEmpty()) && (ciFirewall.getDispositivo()!=null && !ciFirewall.getDispositivo().isEmpty() );
				boolean ciFirewallHasNotSerial=(ciFirewall.getSerial().length()==0);
				if(ciHasNotValues ){
					typeFirewall = ciFirewall.getAdministracion();
				}
				if (ciFirewallHasValues){
					ciFirewall.setTipo(typeFirewall);
					ciFirewall.setUid(firewallInventary.size()+1);
					if(ciFirewall.getDispositivo().toUpperCase().contains("BYPASS") && ciFirewallHasNotSerial) {
						ciFirewall.setSerial("BYPASS,_no_aplica_serial_e_ip");
					}else if(ciFirewall.getDispositivo().toUpperCase().contains("NETXPLORER") && ciFirewallHasNotSerial) {
						ciFirewall.setSerial("NETXPLORER,no_aplica_serial");
					}else if(ciFirewall.getDispositivo().toUpperCase().contains("ILO") && ciFirewallHasNotSerial) {
						ciFirewall.setObservacionesRevision(ciFirewall.getObservacionesRevision()+" No aplica serial. no se contabiliza");
					} else if (ciFirewall.getTipo().toUpperCase().contains("CONSOLAS ANTIVIRUS") || ciFirewall.getTipo().toUpperCase().contains("CORREO SEGURO") || ciFirewall.getTipo().toUpperCase().contains("OTROS") ) {
						ciFirewall.setObservacionesRevision(ciFirewall.getObservacionesRevision()+" Validar maquinas, posibles que tengan administraci�n compartida con sistemas operativos");
					}else if(ciFirewallHasNotSerial) {
						ciFirewall.setSerial("PDT_SERIAL_por_parte_de_torre");
						ciFirewall.setObservacionesRevision(ciFirewall.getObservacionesRevision()+"pdt serial por parte de torre");
					}
					
					firewallInventary.add(ciFirewall);
				}
			}

			System.out.println("CANTIDAD REGISTROS EN INVENTARIO TORRE "+firewallInventary.size());

		}catch(Exception ex) {
			System.out.print("Error al leer ");
			ex.printStackTrace();

		}

		return firewallInventary;
	}


/*
	public static void main (String []args) {	
		CiFirewallDao ciFirewallDao = new CiFirewallDao();
		System.out.println(HEADER);
		ciFirewallDao.readFirewallInventary(FIREWALL_FILE_PATH).stream().forEach(row->System.out.println(row));


	}
*/
}
