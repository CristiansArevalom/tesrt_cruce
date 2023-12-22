package com.matchdatacenter.cruceunix.model;

public class MatchTowerUcmdb {

	private CiUnix ciUnix;
	private CiUcmdbUnix ciUcmdbUnix;
	private String statusMatch;
	private CiServiceManager ciSM;
	
	
	public CiServiceManager getCiSM() {
		return ciSM;
	}
	public void setCiSM(CiServiceManager ciSM) {
		this.ciSM = ciSM;
	}
	//constructor hace match torre vs ucmdb
	public MatchTowerUcmdb(CiUnix ciUnix, CiUcmdbUnix ciUcmdbUnix,String statusMatch) {
		this.ciUnix=ciUnix;
		this.ciUcmdbUnix = ciUcmdbUnix;
		this.statusMatch=statusMatch;		
	}
	//constructor NO hace match torre vs ucmdb
	public MatchTowerUcmdb(CiUnix ciUnix,String statusMatch) {
		this.ciUnix= ciUnix;
		this.ciUcmdbUnix=new CiUcmdbUnix();
		this.statusMatch=statusMatch;
	}
	//constructor NO hace match UCMVB vs torre
	public MatchTowerUcmdb(CiUcmdbUnix ciUcmdbUnix,String statusMatch) {
		/*constructor NO hace match UCMVB vs torre */
		this.ciUnix= new CiUnix();
		this.ciUcmdbUnix=ciUcmdbUnix;
		this.statusMatch=statusMatch;
	}
	
	//CONSTRUCTOR UCMDB VS TORRE vs SM  MATCH)
	public MatchTowerUcmdb(CiUcmdbUnix ciUcmdbUnix,CiUnix ciUnix, String statusMatch,CiServiceManager ciServiceManager) {
		this.ciUnix=ciUnix;
		this.ciUcmdbUnix = ciUcmdbUnix;
		this.ciSM=ciServiceManager;
		this.statusMatch=statusMatch;		
	}
	//CONSTRUCTOR UCMDB VS TORRE vs SM sin MATCH)
	public MatchTowerUcmdb(CiUcmdbUnix ciUcmdbUnix,CiUnix ciUnix,String statusMatch) {
		this.ciUnix=ciUnix;
		this.ciUcmdbUnix = ciUcmdbUnix;
		this.ciSM=new CiServiceManager();
		this.statusMatch=statusMatch;		
	}
	

	public CiUnix getCiUnix() {
		return ciUnix;
	}
	public void setCiUnix(CiUnix ciUnix) {
		this.ciUnix = ciUnix;
	}
	public CiUcmdbUnix getCiUcmdbUnix() {
		return ciUcmdbUnix;
	}
	public void setCiUcmdbUnix(CiUcmdbUnix ciUcmdbUnix) {
		this.ciUcmdbUnix = ciUcmdbUnix;
	}
	public String getStatusMatch() {
		return statusMatch;
	}
	public void setStatusMatch(String statusMatch) {
		this.statusMatch = statusMatch;
	}
	
	
	
}
