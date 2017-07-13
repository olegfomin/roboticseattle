package com.roboticseattle.spar.bigkahuna;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.roboticseattle.common.Acknowledgement;
import com.roboticseattle.common.Command;
import com.roboticseattle.common.EyeCommand;
import com.roboticseattle.common.FaceCommand;
import com.roboticseattle.common.HeartBeat;
import com.roboticseattle.common.MotorCommand;
import com.roboticseattle.common.MouthCommand;
import com.roboticseattle.common.ServoCommand;
import com.roboticseattle.common.EarCommand;
import com.roboticseattle.spar.Spar;
import com.roboticseattle.spar.body.CannotStartException;
import com.roboticseattle.spar.body.Face;
import com.roboticseattle.spar.body.Startable;
import com.roboticseattle.spar.mind.BigKahunaNerve;
import com.roboticseattle.spar.mind.Nerve;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.eclipse.jetty.websocket.api.WebSocketException;

@WebSocket
public class BigKahuna implements Startable{
	private String address;
	private Session session;
	private Gson gson = new Gson();
	private WebSocketClient client;
	private CountDownLatch latch;
	private BigKahunaNerve nerve;
	
	public BigKahuna(String address) {
		this.address = address;
	}
	
	public void start(Nerve nerve) throws CannotStartException {
		this.nerve = (BigKahunaNerve)nerve;
	}
	
	public void connect() {
		latch = new CountDownLatch(1);
		try {
		    client = new WebSocketClient();
			client.start();
			client.connect(this, new URI(address));
		} catch(Exception e) {
			nerve.onPainfulEvent(this, e, address);
		}
	    try{latch.await(8, TimeUnit.SECONDS);} catch(InterruptedException ie) {};
	    
		if(latch.getCount() != 0) {
		    client.destroy();
		    nerve.onPainfulEvent(this, new CannotConnectException(), address);
        }    
	}
	
	public String getAddress() {
	    return this.address;
	}    
	
	@OnWebSocketMessage
	public void onText(Session session, String message) throws IOException {
		Command command = gson.fromJson(message, Command.class);
		switch(command.getDest()) {
			case "servo": nerve.onServoCommand(gson.fromJson(message, ServoCommand.class)); break;
			case "motor": nerve.onMotorCommand(gson.fromJson(message, MotorCommand.class)); break;
			case "mouth": nerve.onMouthCommand(gson.fromJson(message, MouthCommand.class)); break;
			case "heart": nerve.onHeartBeat(gson.fromJson(message, HeartBeat.class)); break;
			case "face": nerve.onFaceCommand(gson.fromJson(message, FaceCommand.class)); break; 
			case "eye" : nerve.onEyeCommand(gson.fromJson(message, EyeCommand.class)); break;
			case "ear" : nerve.onEarCommand(gson.fromJson(message, EarCommand.class)); break;
			default: nerve.onPainfulEvent(this, new UnknownCommandException("dest="+command.getDest()), command.getDest()+"; id="+command.getId());
		}
		
		
	}

	@OnWebSocketConnect
	public synchronized void onConnect(Session session) {
		this.session=session;
		latch.countDown();
		nerve.onConnected();
	}
	
	@OnWebSocketClose
	public synchronized void onDisconnect(Session session, int status, String reason) {
		this.session=null;
		nerve.onDisconnect(status, reason);
	}
	
	
	public synchronized void sendMessage(String str) {
		try {
			if(session != null && session.isOpen()) session.getRemote().sendString(str);
		} catch (IOException | WebSocketException e) {
             nerve.onPainfulEvent(this, e, str);
		}
	}
	
	public synchronized void sendAcknowledgement(Acknowledgement ack) {
	    if(ack.getCommandId() != null) {
    		try {
    			if(session != null) session.getRemote().sendString(gson.toJson(ack));
    		} catch (IOException | WebSocketException e) {
                nerve.onPainfulEvent(this, e, gson.toJson(ack));
    		}
        }	
	}
	
	public synchronized void sendCommand(Command command) {
   		try {
    		if(session != null) session.getRemote().sendString(gson.toJson(command));
   		} catch (IOException | WebSocketException e) {
   			nerve.onPainfulEvent(this, e, gson.toJson(command));
   		}
	}    
	
	public synchronized void sendEyeBytes(byte[] bytes, int offset, int length) throws IOException {
	    byte[] array2Send = new byte[length+1];
	    array2Send[0] = (byte) 0;
	    System.arraycopy(bytes, offset, array2Send, 1, length);
		if(session != null) session.getRemote().sendBytes(ByteBuffer.wrap(array2Send));
	}
	
	public synchronized void sendEarBytes(byte[] bytes, int offset, int length) throws IOException {
   	    byte[] array2Send = new byte[length+1];
	    array2Send[0] = (byte) 1;
	    System.arraycopy(bytes, offset, array2Send, 1, length);
		if(session != null) session.getRemote().sendBytes(ByteBuffer.wrap(array2Send));
	}
	
	public synchronized void disconnect(int state, String reason) {
	    if(session != null) session.close(state, reason);
	}    
	
	
	public CountDownLatch getLatch() {
		return latch;
	}
	
	private class UnknownCommandException extends Exception {

		private static final long serialVersionUID = 7294143019116850497L;

		public UnknownCommandException(String arg0) {
			super(arg0);
		}

		
	}

}
