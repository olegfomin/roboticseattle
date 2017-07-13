package com.roboticseattle.socket.browser;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService; 
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.roboticseattle.common.Acknowledgement;
import com.roboticseattle.common.Command;
import com.roboticseattle.common.EarCommand;
import com.roboticseattle.common.EyeCommand;
import com.roboticseattle.common.HeartBeat;
import com.roboticseattle.mind.ElectronicBrain;

@WebSocket
public class BrowserWebSocket implements Runnable {
	private Gson gson = new Gson();
	private ElectronicBrain electronicBrain;
	private Session session;
	
	private String sessionId;
	private boolean isBrowserConnected = false;
	private int     lastBrowserLapse = 10000;
	private boolean isShutdown = false;
	private Map<String, HeartBeat> id2Beat = new HashMap<>();
	private Thread thread = new Thread(this); 
	

	public boolean isBrowserConnected() {
		return isBrowserConnected;
	}

	public String getSessionId() {
		return sessionId;
	}
	
	public int getLastBrowserLapse() {
		return lastBrowserLapse;
	}

	@OnWebSocketMessage
	public void onText(Session session, String message) throws IOException {
		if(electronicBrain == null) {
			session.close();
			return;
		}
		System.out.println(message);
	    Command command = gson.fromJson(message, Command.class);
	    
		switch(command.getDest()) {
		  case "ear":
			  EarCommand earCommand = gson.fromJson(message, EarCommand.class);
			  electronicBrain.onEarCommand(earCommand);
			  break;
		  case "eye":
			  EyeCommand eyeCommand = gson.fromJson(message, EyeCommand.class);
			  electronicBrain.onEyeCommand(eyeCommand);
              break;
		  case "heart":
			  HeartBeat hb = gson.fromJson(message, HeartBeat.class);
			  HeartBeat origHb = id2Beat.get(hb.getId());
			  if(origHb != null) {
				  id2Beat.remove(hb.getId());
				  lastBrowserLapse = (int)(System.currentTimeMillis() - origHb.getCurrentTime());
			  }
			  isBrowserConnected = true;
			  break;
		  default:
			  electronicBrain.onBrowserText(message);
    	}
	}

	@OnWebSocketConnect
	public void onConnect(Session session) throws IOException {
		this.session = session;
		electronicBrain = ElectronicBrain.getBrowserBrain(this);
		thread.start();
	}

	@OnWebSocketClose
	public synchronized void onClose(Session session, int status, String reason) {
		isShutdown = true;
		isBrowserConnected = false;
		if(electronicBrain != null && electronicBrain.getBrowser() != null) electronicBrain.onBrowserClose(session); 
		this.session = null;
	}
	
	public synchronized void send(Command command) throws IOException, WebSocketException {
		if(session != null) session.getRemote().sendString(gson.toJson(command)); 
	}

	public synchronized void send(String msg) throws IOException, WebSocketException {
		if(session != null) session.getRemote().sendString(msg); 
	}
	
	public synchronized void close()  {
		isShutdown = true;
		isBrowserConnected = false;
		if(session != null) session.close(); 
		this.session = null;
	}
	
	public synchronized void close(int status, String reason)  {
		isShutdown = true;
		isBrowserConnected = false;
		if(session != null) session.close(status, reason); 
		this.session = null;
	}


	@Override
	public void run() {
		int counter = 0;
		while(!isShutdown) {
			long now = System.currentTimeMillis();
			HeartBeat hb = new HeartBeat();
			hb.setId(String.valueOf(counter));
			hb.setCurrentTime(now);
			hb.setLastLapse(lastBrowserLapse + (electronicBrain.isSparConnected() ?  electronicBrain.getSparLastLapse() : 0));
			hb.setStatus(electronicBrain.isSparConnected() ? 2 : 1);
			id2Beat.put(hb.getId(), hb);
			try {
				send(hb);
			} catch (WebSocketException | IOException e) {
				// Log it
				isShutdown = true;
				break;
			}
			
	        List<String> what2Remove = new LinkedList<>();
	        for(Map.Entry<String, HeartBeat> entry : id2Beat.entrySet()) { // Anything longer than 8 seconds should go
	            if(now - entry.getValue().getCurrentTime() > 8000) what2Remove.add(entry.getKey());       
	        }   
	        for(String key : what2Remove) {id2Beat.remove(key);}
	        
	        if(id2Beat.size() >= 5) {
	        	System.out.println("Closing browser connection due to too many hanging pings");
	            close(5, "ping");
	            break;
	        }
			
			counter++;
			try {Thread.sleep(1000);} catch (InterruptedException e) {}
		}
	}
	
}
