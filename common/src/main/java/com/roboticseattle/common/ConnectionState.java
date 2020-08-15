package com.roboticseattle.common;

public class ConnectionState extends Command {
	private int state; // 0- not connected, 1- connected to www.roboticseattle.com, 2 - connected all the way to SPAR
	private String sessionId;
	private String reason; // Reason for the connection termination
	
	public ConnectionState() {
		super();
		setDest("constate");
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}


}
