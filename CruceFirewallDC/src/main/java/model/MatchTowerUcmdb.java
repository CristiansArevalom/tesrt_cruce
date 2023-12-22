package model;

public class MatchTowerUcmdb {

	private CiFirewall ciFirewall;
	private CiUcmdbFirewall ciUcmdbFirewall;
	private String statusMatch;
	private CiServiceManagerFw ciSM; //crear consturctor del match ucmvvs torre&SM

	//constructro TORRE VS UCMDB (MATCH)
	public MatchTowerUcmdb(CiFirewall ciFirewall,CiUcmdbFirewall ciUcmdbFirewall,String statusMatch) {
		this.ciFirewall=ciFirewall;
		this.ciUcmdbFirewall=ciUcmdbFirewall;
		this.statusMatch= statusMatch;
	}
	//constructro TORRE VS UCMDB (SIN MATCH)
	public MatchTowerUcmdb(CiFirewall ciFirewall,String statusMatch) {
		this.ciFirewall= ciFirewall;
		this.ciUcmdbFirewall = new CiUcmdbFirewall();
		this.statusMatch=statusMatch;

	}

	//CONSTRUCTOR UCMDB VS TORRE (SIN MATCH)
	public MatchTowerUcmdb(CiUcmdbFirewall ciUcmdbFirewall,String statusMatch) {
		this.ciFirewall= new CiFirewall();
		this.ciUcmdbFirewall = ciUcmdbFirewall;
		this.statusMatch=statusMatch;
	}
	//CONSTRUCTOR UCMDB VS TORRE vs SM  MATCH)
	public MatchTowerUcmdb(CiUcmdbFirewall ciUcmdbFirewall,CiFirewall ciFirewall,CiServiceManagerFw ciServiceManagerFw,String statusMatch) {
		this.ciFirewall=ciFirewall;
		this.ciSM=ciServiceManagerFw;
		this.ciUcmdbFirewall=ciUcmdbFirewall;
		this.statusMatch= statusMatch;
	}
	//CONSTRUCTOR UCMDB VS TORRE vs SM sin MATCH)
	public MatchTowerUcmdb(CiUcmdbFirewall ciUcmdbFirewall,CiFirewall ciFirewall,String statusMatch) {
		this.ciFirewall=ciFirewall;
		this.ciSM=new CiServiceManagerFw();
		this.ciUcmdbFirewall=ciUcmdbFirewall;
		this.statusMatch= statusMatch;
	}
	
	


	public CiFirewall getCiFirewall() {
		return ciFirewall;
	}

	public void setCiFirewall(CiFirewall ciFirewall) {
		this.ciFirewall = ciFirewall;
	}

	public CiUcmdbFirewall getCiUcmdbFirewall() {
		return ciUcmdbFirewall;
	}

	public void setCiUcmdbFirewall(CiUcmdbFirewall ciUcmdbFirewall) {
		this.ciUcmdbFirewall = ciUcmdbFirewall;
	}

	
	public CiServiceManagerFw getCiSM() {
		return ciSM;
	}

	public void setCiSM(CiServiceManagerFw ciSM) {
		this.ciSM = ciSM;
	}

	public String getStatusMatch() {
		return statusMatch;
	}

	public void setStatusMatch(String statusMatch) {
		this.statusMatch = statusMatch;
	}
	@Override
	public String toString() {
		return ciFirewall.toString() +"\t"+ ciUcmdbFirewall.toString() + "\t"+ statusMatch ;
	}
	



}
