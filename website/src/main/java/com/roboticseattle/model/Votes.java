package com.roboticseattle.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Votes {
	
	private static final Gson gson = new Gson(); 
	
	@SerializedName("spn-spar-like")
	private int sparFor=0;
	@SerializedName("spn-spar-dislike")
	private int sparAgainst=0;
	@SerializedName("spn-abes-like")
	private int abesFor = 0;
	@SerializedName("spn-abes-dislike")
	private int abesAgainst = 0;
	@SerializedName("spn-target-like")
	private int targetFor =0;
	@SerializedName("spn-target-dislike")
	private int targetAgainst =0;
	@SerializedName("spn-search-like")
	private int searchFor = 0;
	@SerializedName("spn-search-dislike")
	private int searchAgainst = 0;
	@SerializedName("spn-tractor-like")
	private int tractorFor =0;
	@SerializedName("spn-tractor-dislike")
	private int tractorAgainst = 0;
	@SerializedName("spn-drive-like")
	private int driveFor =0;
	@SerializedName("spn-drive-dislike")
	private int driveAgainst = 0;
	@SerializedName("spn-session-like")
	private int sessionFor =0;
	@SerializedName("spn-session-dislike")
	private int sessionAgainst = 0;
	
	
	
	public int getSparFor() {
		return sparFor;
	}
	public void setSparFor(int sparFor) {
		this.sparFor = sparFor;
	}
	public int getSparAgainst() {
		return sparAgainst;
	}
	public void setSparAgainst(int sparAgainst) {
		this.sparAgainst = sparAgainst;
	}
	public int getAbesFor() {
		return abesFor;
	}
	public void setAbesFor(int abesFor) {
		this.abesFor = abesFor;
	}
	public int getAbesAgainst() {
		return abesAgainst;
	}
	public void setAbesAgainst(int abesAgainst) {
		this.abesAgainst = abesAgainst;
	}
	public int getTargetFor() {
		return targetFor;
	}
	public void setTargetFor(int targetFor) {
		this.targetFor = targetFor;
	}
	public int getTargetAgainst() {
		return targetAgainst;
	}
	public void setTargetAgainst(int targetAgainst) {
		this.targetAgainst = targetAgainst;
	}
	public int getSearchFor() {
		return searchFor;
	}
	public void setSearchFor(int searchFor) {
		this.searchFor = searchFor;
	}
	public int getSearchAgainst() {
		return searchAgainst;
	}
	public void setSearchAgainst(int searchAgainst) {
		this.searchAgainst = searchAgainst;
	}
	public int getTractorFor() {
		return tractorFor;
	}
	public void setTractorFor(int tractorFor) {
		this.tractorFor = tractorFor;
	}
	public int getTractorAgainst() {
		return tractorAgainst;
	}
	public void setTractorAgainst(int tractorAgainst) {
		this.tractorAgainst = tractorAgainst;
	}
    public int getDriveFor() {
		return driveFor;
	}
	public void setDriveFor(int driveFor) {
		this.driveFor = driveFor;
	}
	public int getDriveAgainst() {
		return driveAgainst;
	}
	public void setDriveAgainst(int driveAgainst) {
		this.driveAgainst = driveAgainst;
	}
	public int getSessionFor() {
		return sessionFor;
	}
	public void setSessionFor(int sessionFor) {
		this.sessionFor = sessionFor;
	}
	public int getSessionAgainst() {
		return sessionAgainst;
	}
	public void setSessionAgainst(int sessionAgainst) {
		this.sessionAgainst = sessionAgainst;
	}
	public synchronized void add(String name) {
        
    	switch(name) {
    	  case "btn-spar-like" : sparFor++; break;
    	  case "btn-spar-dislike" : sparAgainst++; break;
    	  case "btn-abes-like" : abesFor++; break;
    	  case "btn-abes-dislike" : abesAgainst++; break;
    	  case "btn-target-like" : targetFor++; break;
    	  case "btn-target-dislike" : targetAgainst++; break;
    	  case "btn-search-like" : searchFor++; break;
    	  case "btn-search-dislike" : searchAgainst++; break;
    	  case "btn-tractor-like" : tractorFor++; break;
    	  case "btn-tractor-dislike" : tractorAgainst++; break;
    	  case "btn-drive-like" : driveFor++; break;
    	  case "btn-drive-dislike" : driveAgainst++; break;
    	  case "btn-session-like" : sessionFor++; break;
    	  case "btn-session-dislike" : sessionAgainst++; break;
    	  default: throw new IllegalArgumentException(name);
    	}
        try(BufferedWriter out = new BufferedWriter(new FileWriter(System.getProperty("user.home")+"/repo/votes.json"))) {
        	out.write(gson.toJson(this)+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
	
}
