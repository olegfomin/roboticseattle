package com.roboticseattle.spar.body;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.roboticseattle.spar.mind.EyeNerve;

public class Eye extends Part {
	
    private static String CAMERA_DIR="/home/pi/";
    private static String START_CAMERA_COMMAND = CAMERA_DIR+"startcamera.sh"; 
    private static String STOP_CAMERA_COMMAND = CAMERA_DIR+"stopcamera.sh"; 
	private InputStream inputStream;
	private Process cameraProcess = null;

	@Override
	public synchronized void shutdown() {
		super.shutdown();
		try {
			(new ProcessBuilder(STOP_CAMERA_COMMAND)).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(cameraProcess.isAlive()) {
		    cameraProcess.destroy();
		    cameraProcess = null;
		}
		System.out.println(STOP_CAMERA_COMMAND);
	}

	@Override
	public void beforeLoop() throws Exception {
		cameraProcess = (new ProcessBuilder(START_CAMERA_COMMAND)).start();
		Thread.sleep(1500);
		inputStream = new FileInputStream("/home/pi/camera");
		System.out.println(START_CAMERA_COMMAND);
	}

	@Override
	public void insideLoop(int counter) throws Exception {
		byte[] chunk = new byte[4096];
		int len;
        EyeNerve eyeNerve = (EyeNerve) nerve;
		while((len=inputStream.read(chunk)) != -1) {
			if(!isShutdown) eyeNerve.onNewVisual(chunk, 0, len);
			else break;
		}
	}

	@Override
	public void afterLoop() {
		try {
			if(inputStream != null) inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
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
