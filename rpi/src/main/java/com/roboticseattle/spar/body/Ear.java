package com.roboticseattle.spar.body;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.roboticseattle.spar.mind.EarNerve;

public class Ear extends Part {

	private static String MICROPHONE_DIR="/home/pi/";
    private static String START_MICROPHONE_COMMAND = MICROPHONE_DIR+"startmicrophone.sh"; 
    private static String STOP_MICROPHONE_COMMAND = MICROPHONE_DIR+"stopmicrophone.sh"; 
    
    private InputStream inputStream;
    private Process microphoneProcess = null;    

	@Override
	public synchronized void shutdown() {
		super.shutdown();
		try {
			(new ProcessBuilder(STOP_MICROPHONE_COMMAND)).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(STOP_MICROPHONE_COMMAND);
		if(microphoneProcess.isAlive()) {
    		microphoneProcess.destroy();
	    	microphoneProcess = null;
		}	
	}
    
    
	@Override
	public void beforeLoop() throws Exception {
		microphoneProcess = (new ProcessBuilder(START_MICROPHONE_COMMAND)).start();
		Thread.sleep(500);
		inputStream = new FileInputStream("/home/pi/microphone");
		System.out.println(START_MICROPHONE_COMMAND);
	}

	@Override
	public void insideLoop(int counter) throws Exception {
		byte[] chunk = new byte[4096];
		int len;
        EarNerve earNerve = (EarNerve) nerve;
		while((len=inputStream.read(chunk)) != -1) {
			if(!isShutdown) earNerve.onNewAudible(chunk, 0, len);
			else break;
		}
		
	}

	@Override
	public void afterLoop() {
		if(inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}	
	}

	@Override
	public String painfulStuff() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insideInterrupted() {
		// TODO Auto-generated method stub

	}

}
