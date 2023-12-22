package com.test.model;

public class MatchTowerUcmdb {


	private String statusMatch;
	private CiWindows ciWindows;
	private CiUcmdbWindows ciUcmdbWindows;
	private CiServiceManager ciServiceManager;

	//CONSTURCTOR TORRE VS UCMDB MATCH
	public MatchTowerUcmdb(CiWindows ciWindows, CiUcmdbWindows ciUcmdbWindows,String statusMatch) {
		super();
		this.statusMatch = statusMatch;
		this.ciWindows = ciWindows;
		this.ciUcmdbWindows = ciUcmdbWindows;
	}
	
	//CONSTURCTOR TORRE VS UCMDB SIN MATCH
	public MatchTowerUcmdb(CiWindows ciWindows, String statusMatch) {
		this.statusMatch = statusMatch;
		this.ciWindows = ciWindows;
		this.ciUcmdbWindows = new CiUcmdbWindows();
	}

	//CONSTURCTOR UCMDB VS TORRE SIN MATCH
	public MatchTowerUcmdb(CiUcmdbWindows ciUcmdbWindows, String statusMatch) {
		this.statusMatch = statusMatch;
		this.ciWindows = new CiWindows();
		this.ciUcmdbWindows = ciUcmdbWindows;
	}
/*
	//CONSTURCTOR UCMDB VS TORRE MATCH
	public MatchTowerUcmdb(CiUcmdbWindows ciUcmdbWindows,CiWindows ciWindows, String statusMatch) {
		super();
		this.statusMatch = statusMatch;
		this.ciWindows = ciWindows;
		this.ciUcmdbWindows = ciUcmdbWindows;
	
	}*/
	
	//CONSTRUCTOR UCMDB VS TORRE vs SM  MATCH)
	public MatchTowerUcmdb(CiUcmdbWindows ciUcmdbWindows,CiWindows ciWindows, String statusMatch,CiServiceManager ciServiceManager) {
		this.ciWindows=ciWindows;
		this.ciUcmdbWindows = ciUcmdbWindows;
		this.ciServiceManager=ciServiceManager;
		this.statusMatch=statusMatch;		
	}
	//CONSTRUCTOR UCMDB VS TORRE vs SM sin MATCH)
	public MatchTowerUcmdb(CiUcmdbWindows ciUcmdbWindows,CiWindows ciWindows,String statusMatch) {
		this.ciWindows=ciWindows;
		this.ciUcmdbWindows = ciUcmdbWindows;
		this.ciServiceManager=new CiServiceManager();
		this.statusMatch=statusMatch;		
	}


	public String getStatusMatch() {
		return statusMatch;
	}

	public void setStatusMatch(String statusMatch) {
		this.statusMatch = statusMatch;
	}

	public CiWindows getCiWindows() {
		return ciWindows;
	}

	public void setCiWindows(CiWindows ciWindows) {
		this.ciWindows = ciWindows;
	}

	public CiUcmdbWindows getCiUcmdbWindows() {
		return ciUcmdbWindows;
	}

	public void setCiUcmdbWindows(CiUcmdbWindows ciUcmdbWindows) {
		this.ciUcmdbWindows = ciUcmdbWindows;
	}
	public CiServiceManager getCiServiceManager() {
		return ciServiceManager;
	}

	public void setCiServiceManager(CiServiceManager ciServiceManager) {
		this.ciServiceManager = ciServiceManager;
	}

	
	
}
