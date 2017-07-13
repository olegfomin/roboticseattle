package com.roboticseattle.socket.spar;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.roboticseattle.common.Command;
import com.roboticseattle.common.HeartBeat;
import com.roboticseattle.mind.ElectronicBrain;
import com.roboticseattle.mind.JpegLumberjack;
import com.roboticseattle.mind.JpegLumberjack.State;

@WebSocket
public class SparWebSocket implements Runnable{

	private Gson gson = new Gson();
	private ElectronicBrain sparBrain;
	private Session session;
	private boolean isSparConnected = false;
	private int     lastSparLapse = 10000;
	private long    lastSparHbTime = System.currentTimeMillis();;
	private Thread  thread = new Thread(this);
	private boolean isShutdown = false;
	
	public boolean isSparConnected() {
		return isSparConnected;
	}

	public int getLastSparLapse() {
		return lastSparLapse;
	}

	@OnWebSocketMessage
	public synchronized void onText(Session session, String message) throws IOException {
		if(sparBrain == null) {
			session.close();
			return;
		}
	    Command command = gson.fromJson(message, Command.class);
	    switch(command.getDest()) {
	        case "heart":
	        	HeartBeat hb = gson.fromJson(message, HeartBeat.class);
	        	if(sparBrain.isBrowserConnected()) hb.setStatus(2);
	        	else hb.setStatus(1);
	        	lastSparLapse = hb.getLastLapse();
	        	lastSparHbTime = System.currentTimeMillis();
	        	try {
	        	  session.getRemote().sendString(gson.toJson(hb));
	        	} catch(WebSocketException wse) {
	        		wse.printStackTrace();
	        		isShutdown = true;
	        		isSparConnected = false;
	        	}
	        	break;
	    	default: sparBrain.onSparText(session, message);  
	    }
	}
	
	@OnWebSocketMessage
	public synchronized void onBinaryData(Session session, byte buf[], int offset, int length) throws IOException {
		if(sparBrain == null) {
			session.close();
			return;
		}
		sparBrain.onSparBinaryData(session, buf, offset, length);
	} 

	@OnWebSocketConnect
	public synchronized void onConnect(Session session) throws IOException {
		isSparConnected = true;
		this.session = session;
    	sparBrain = ElectronicBrain.getSparBrain(this);
    	lastSparHbTime = System.currentTimeMillis();
    	thread.start();
	}

	@OnWebSocketClose
	public synchronized void onClose(Session session, int status, String reason) {
		isSparConnected = false;
		isShutdown = true;
		if(sparBrain != null && sparBrain.getSpar() != null) sparBrain.onSparClose(session); 
	}
	
	public synchronized void send(Command command) throws IOException, WebSocketException {
		if(session != null) session.getRemote().sendString(gson.toJson(command)); 
	}
	
	public synchronized void send(String message) throws IOException, WebSocketException {
	    if(session != null)	session.getRemote().sendString(message);
	}
	
	public synchronized void close(int status, String reason) {
		isSparConnected = false;
		isShutdown = true;
		if(session != null) session.close();
		if(sparBrain != null && sparBrain.getSpar() != null) sparBrain.onSparClose(session); 
		session = null;
	}

	@Override
	public void run() {
		while(!isShutdown) {
			if(System.currentTimeMillis()-lastSparHbTime > 8000) {
				close(100, "timeout");
				break;
			}
			try {Thread.sleep(500);} catch (InterruptedException e) {}
		}
	}
	
}
