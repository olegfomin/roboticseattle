package com.roboticseattle.mind;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketException;

import com.google.gson.Gson;
import com.roboticseattle.common.EarCommand;
import com.roboticseattle.common.EyeCommand;
import com.roboticseattle.common.HeartBeat;
import com.roboticseattle.common.ImgCommand;
import com.roboticseattle.socket.browser.BrowserWebSocket;
import com.roboticseattle.socket.spar.SparWebSocket;

/**
 * Takes the requests from the participants and routes them among. 
 * @author Robotic Seattle
 *
 */
public class ElectronicBrain {
	
	private static final ElectronicBrain electronicBrain = new ElectronicBrain();
	
	private Gson gson = new Gson();
	private SparWebSocket spar;
	private BrowserWebSocket browser;
	
	private JpegLumberjack lumberjack = new JpegLumberjack();
	private byte[] jpeg;
	private Queue<byte[]> wavQueue = new LinkedList<>();
	private int imgCount = 0;
	private boolean isMicrophoneOn = false;
	private boolean isCameraOn = false;
	private int chunkCount = 0;

	public synchronized boolean isBrowserConnected() {
		return (browser == null) ? false : browser.isBrowserConnected(); 
	}
	
	public synchronized boolean isSparConnected() {
		return (spar == null) ? false : spar.isSparConnected();
	}
	
	public synchronized int getSparLastLapse() {
		return (spar == null) ? 10000 : spar.getLastSparLapse();
	}
	
	public static final synchronized ElectronicBrain getBrain() {
		return electronicBrain;
	}
	
	public SparWebSocket getSpar() {
		return spar;
	}
	
	public BrowserWebSocket getBrowser() {
		return browser;
	}

	public static final synchronized ElectronicBrain getSparBrain(SparWebSocket sparWebSocket) {
		electronicBrain.spar = sparWebSocket;
		return electronicBrain;
	}
	
	public static final synchronized ElectronicBrain getBrowserBrain(BrowserWebSocket browserWebSocket) {
		if(electronicBrain.browser != null) {
			browserWebSocket.close(7, "Already connected");
			return null;
		}
		electronicBrain.browser = browserWebSocket;
		return electronicBrain;
	}

	private ElectronicBrain() {
	}
	
	
	public synchronized void onBrowserClose(Session session) {
		browser = null;
	}
	
	public void onBrowserText(String msgFromBrowser) {
   		if(spar != null) {
   			try {
				spar.send(msgFromBrowser);
			} catch (IOException | IllegalStateException e) {
				// TODO Log that + send to the browser
				e.printStackTrace();
			}
    	} else {
			// TODO Log that + send to the browser
    	}
	}
	
	public void onEarCommand(EarCommand command) {
		if("on".equals(command.getTurn())) {
			isMicrophoneOn = true;
		} else {
			isMicrophoneOn = false;
   			wavQueue.clear();
   			chunkCount = 0;
		}
		try {
			System.out.println(gson.toJson(command));
    		if(spar != null) spar.send(command); 
    		else {/*Send message to the browser*/}
		} catch (IOException e) {
		    // Log and send message to the browser
	    	e.printStackTrace();
    	} 
	}
	
	public void onEyeCommand(EyeCommand command) {
		if("on".equals(command.getTurn())) {
			isCameraOn = true;
			lumberjack = new JpegLumberjack();
		} else {
			isCameraOn = false;
		}
		try {
			System.out.println(gson.toJson(command));
    		if(spar != null) spar.send(command); 
    		else {/*Send message to the browser*/}
		} catch (IOException e) {
		    // Log and send message to the browser
	    	e.printStackTrace();
    	} 
	}
	
	public void onSparClose(Session session) {
	    spar = null;
	}
	
	public void onCameraBinaryData(Session session, byte buf[], int offset, int length) {
		if(browser != null && isCameraOn) {
			JpegLumberjack.State state= null;
			byte[] chunk = Arrays.copyOfRange(buf, offset, length);
			try{synchronized(this) {state  = lumberjack.feed(chunk, chunk.length); }} 
			catch(InvalidJpegException e) {
				e.printStackTrace(); // Closing camera due to an exception 
				EyeCommand eyeCommand = new EyeCommand();
				eyeCommand.setDest("eye");
				eyeCommand.setTurn("off");
				try {
					synchronized(this) {	
				      if(browser != null) browser.send(eyeCommand);
				      if(spar != null) spar.send(eyeCommand);
					}  
				} catch(IOException ioe) {
					ioe.printStackTrace();
				}
				isCameraOn = false;
				return;
			}
			    
			if(state == JpegLumberjack.State.D9) {
				synchronized(this) {jpeg = lumberjack.read();}
				System.out.println("Extracted jpeg successfully "+jpeg.length);
               	ImgCommand imgCommand = new ImgCommand(); 
               	imgCommand.setId(String.valueOf(imgCount++));
               	imgCommand.setSrc("/image/"+UUID.randomUUID());
            	try {
            		synchronized(this) {if(browser != null) browser.send(imgCommand);}
            	} catch(IOException ioe) {
					// Log this event and stop the SPAR!
					ioe.printStackTrace();
            	}
			}
		} 		
	}
	
	public void onMicrophoneBinaryData(Session session, byte buf[], int offset, int length) {
		if(isMicrophoneOn) {
			byte[] chunk = Arrays.copyOfRange(buf, offset, length);
    		wavQueue.offer(chunk);
    		chunkCount++;
    		if(chunkCount %25 == 0) System.out.println("chunkCount="+chunkCount);
		}	
	}
	
	
	public void onSparBinaryData(Session session, byte buf[], int offset, int length) {
		if(browser != null) {
			byte[] chunk = Arrays.copyOfRange(buf, offset+1, length);
			if(buf[offset] == (byte) 0) {
				if(isCameraOn) {
					JpegLumberjack.State state= null;			
					try{synchronized(this) {state  = lumberjack.feed(chunk, chunk.length); }} 
					catch(InvalidJpegException e) {
						e.printStackTrace(); // Closing camera due to an exception 
						EyeCommand eyeCommand = new EyeCommand();
						eyeCommand.setDest("eye");
						eyeCommand.setTurn("off");
						try {
						  if(browser != null) browser.send(eyeCommand);
						  if(spar != null) spar.send(eyeCommand);
						} catch(IOException ioe) {
							ioe.printStackTrace();
						}
						isCameraOn = false;
						return;
					}
					    
					if(state == JpegLumberjack.State.D9) {
						synchronized(this) {jpeg = lumberjack.read();}
						System.out.println("Extracted jpeg successfully "+jpeg.length);
	                   	ImgCommand imgCommand = new ImgCommand(); 
	                   	imgCommand.setId(String.valueOf(imgCount++));
	                   	imgCommand.setSrc("/image/"+UUID.randomUUID());
		            	try {
		            		synchronized(this) {if(browser != null) browser.send(imgCommand);}
		            	} catch(IOException ioe) {
							// Log this event and stop the SPAR!
							ioe.printStackTrace();
		            	}
					}
				}	
			} else {
				if(isMicrophoneOn) {
		    		wavQueue.offer(chunk);
		    		chunkCount++;
		    		if(chunkCount %25 == 0) System.out.println("chunkCount="+chunkCount);
				}	
			}
		}	
	}
	
	public void onSparText(Session session, String message) {
        if(browser != null) { 
        	try {
				browser.send(message);
//				System.out.println(message);
			} catch (IOException e) {
				// Log this event and stop the SPAR!
				e.printStackTrace();
			} 
        }
	}
	
	public byte[] getJpeg() {
		return jpeg;
	}
	
	public byte[] wavChunk() {
		byte[] chunk = wavQueue.poll();
		if(chunk != null) chunkCount--;
		return chunk;
	}
	
	public boolean isMicrophoneOn() {
		return isMicrophoneOn;
	}
	
	public boolean isCameraOn() {
		return isCameraOn;
	}

}
