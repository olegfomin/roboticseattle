package com.roboticseattle.spar.body;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import com.roboticseattle.common.SonarReading;
import com.roboticseattle.spar.mind.SonarNerve;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Sonars extends Part {
    
   	private SerialPort serialPort;
   	private InputStream in;
   	private OutputStream out;
   	private Gson gson = new Gson();

	@Override
	public synchronized void beforeLoop() throws Exception {
	    if(serialPort == null) {
    		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier("/dev/ttyS81");
    		
    		if(portIdentifier.isCurrentlyOwned()) throw new PortInUseException();
    		CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
    		serialPort = ( SerialPort )commPort;
            serialPort.setSerialPortParams( 19200,
                            SerialPort.DATABITS_8,
    	                    SerialPort.STOPBITS_1,
    	                    SerialPort.PARITY_NONE );
            in = serialPort.getInputStream();
            out = serialPort.getOutputStream();
        }
        sleepBetween = 25;
	}

	@Override
	public void insideLoop(int counter) throws Exception {
	    int fblr = counter%4; // forward, back, left, right;
	    byte[] sonar = new byte[1];
	    String direction;
	    switch(fblr) {
	        case 0: sonar[0] = (byte)'f'; direction = "forward"; break;
	        case 1: sonar[0] = (byte)'b'; direction = "back"; break;
	        case 2: sonar[0] = (byte)'l'; direction = "left"; break;
	        case 3: sonar[0] = (byte)'r'; direction = "right"; break;
	        default: throw new Exception("Never be here! Just satisfying the compiler");
	        
	    }    
	    out.write(sonar);
	    out.flush();
	    
	    int dist = in.read();
	    if(dist > 0 && dist < 128) {
	        SonarReading sonarReading = new SonarReading();
	        sonarReading.setDirection(direction);
	        sonarReading.setDistance(dist);
	        ((SonarNerve)nerve).onNewSonarReading(sonarReading);
	    }    
	    
    }  

	@Override
	public synchronized void afterLoop() {
		if(serialPort != null) serialPort.close();
		serialPort = null;
	}

	@Override
	public String painfulStuff() {
		return "";
	}

	@Override
	public void insideInterrupted() {
		// TODO Auto-generated method stub

	}

}
