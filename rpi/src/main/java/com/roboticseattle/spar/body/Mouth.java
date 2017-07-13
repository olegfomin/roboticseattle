package com.roboticseattle.spar.body;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import com.roboticseattle.common.Song;
import com.roboticseattle.spar.mind.MouthNerve;
import com.roboticseattle.spar.mind.Nerve;

/**
 * The class plays the music and does the text-to-speech 
 * 
 * @author robotic seattle 
 * @version 0.99
 */
public class Mouth extends Part
{
    public Mouth(int aSleepBetween) {
		super(aSleepBetween);
	}

	public Mouth() {
		super();
	}

	public synchronized void sing(Song aSong) {
    	newSong = aSong;
        thread.interrupt();
    }
	
    public synchronized void stopSinging() {
    	isTerminateSongProcess = true;
        thread.interrupt();
    }
    
    public synchronized void speak(String text) {
    	newText = text;
        thread.interrupt();
    }
    
    public synchronized void stopSpeaking() {
    	isTerminateSpeechProcess = true;
        thread.interrupt();
    }
    
    public boolean isSinging() {
        return (newSong != null);
    }    
    
    private static String MEDIA_DIR="/home/pi/media/";

    private Song    newSong = null;
    private Song    currentSong = null;
    private Process songProcess = null;
    private boolean isTerminateSongProcess = false;
    
    private String  newText;
    private String  currentText;
    private Process speechProcess = null;
    private boolean isTerminateSpeechProcess = false;

    
    public static void main(String[] args) throws Exception {
        Mouth voice = new Mouth();
        voice.start(null);
        voice.sing(Song.THE_ROBOTS);
        Thread.sleep(12000);
        voice.speak("the song has been interrupted for this important announcement.");
        
        Thread.sleep(10000);
    }

	@Override
	public void beforeLoop() {
		// TODO Remove all the omxplayer and pico2text instances
	}

	@Override
	public void insideLoop(int counter) throws Exception {
		if(newSong != currentSong) startNewSong();
		if(isTerminateSongProcess) terminateCurrentSongProcess();
		if(songProcess != null && !songProcess.isAlive()) onSongCompleted();
		if(currentText != newText) startNewSpeech();
		if(isTerminateSpeechProcess) terminateSpeechProcess();
		if(speechProcess != null && !speechProcess.isAlive()) onSpeechComplete();
	}


	@Override
	public void afterLoop() {
		// TODO Remove all the omxplayer and pico2text instances
		
	}

	@Override
	public String painfulStuff() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void insideInterrupted() {
		// TODO Auto-generated method stub
		
	} 
	
	private void startNewSong() throws Exception {
		if(currentSong != null) {
			if(songProcess != null && songProcess.isAlive()) {
     		    songProcess.destroy();
	            (new ProcessBuilder(MEDIA_DIR+"termsong.sh", currentSong.getFileName())).start();
				songProcess = null;
				if(nerve != null && nerve instanceof MouthNerve) {
					((MouthNerve)nerve).onSongComplete(currentSong);
				}
				currentSong = null;
			}
		}
		songProcess = (new ProcessBuilder("/usr/bin/omxplayer", "-o", "local", "--vol", String.valueOf(newSong.getVolume()), MEDIA_DIR+newSong.getFileName())).start();
		currentSong = newSong;
		synchronized(this) {
    		isTerminateSongProcess = false;
		}	
	}
	
	private void terminateCurrentSongProcess() throws Exception {
		synchronized(this) {
			newSong = null;
			isTerminateSongProcess = false;
		}	
		if(songProcess != null && songProcess.isAlive()) {
		    songProcess.destroy();
	       (new ProcessBuilder(MEDIA_DIR+"termsong.sh", currentSong.getFileName())).start();
			
			songProcess = null;
			currentSong = null;
			if(nerve != null && nerve instanceof MouthNerve) {
				((MouthNerve)nerve).onSongComplete(currentSong);
			}
		}
		return;
	}
	
	private void onSongCompleted() throws Exception {
		synchronized(this) {
			newSong = null;
			isTerminateSongProcess = false;
		}
		if(nerve != null && nerve instanceof MouthNerve) {
			((MouthNerve)nerve).onSongComplete(currentSong);
		}
		currentSong = null;
		songProcess = null;
	}
	
	private void terminateSpeechProcess() throws Exception {
		if(speechProcess != null && speechProcess.isAlive()) {
			speechProcess.destroy();
            (new ProcessBuilder(MEDIA_DIR+"termspeech.sh")).start();
			if(nerve != null && nerve instanceof MouthNerve) {
				((MouthNerve)nerve).onSpeechComplete(currentText);
			}
			synchronized(this) {
				newText = null;
				isTerminateSpeechProcess = false;
			}
			currentText = null;
		}
		speechProcess = null;
		
	}

	private void startNewSpeech() throws Exception {
		if(currentText != null) {
			if(speechProcess != null && speechProcess.isAlive()) {
				speechProcess.destroy();
	            (new ProcessBuilder(MEDIA_DIR+"termspeech.sh")).start();
				speechProcess = null;
				if(nerve != null && nerve instanceof MouthNerve) {
					((MouthNerve)nerve).onSpeechComplete(currentText);
				}
				currentText = null;
			}
		}

		speechProcess = (new ProcessBuilder(MEDIA_DIR+"say.sh", newText)).start();
		currentText = newText;
		
		synchronized(this) {
			isTerminateSpeechProcess = false;
		}
		
	}
	
	private void onSpeechComplete() {
		if(nerve != null && nerve instanceof MouthNerve) {
			((MouthNerve)nerve).onSpeechComplete(currentText);
		}
		synchronized(this) {
			newText = null;
			isTerminateSpeechProcess = false;
		}

		currentText = null;
		speechProcess = null;
	}
    
}
