package com.roboticseattle.spar.mind;

import com.roboticseattle.common.Acknowledgement;
import com.roboticseattle.common.EarCommand;
import com.roboticseattle.common.EyeCommand;
import com.roboticseattle.common.FaceCommand;
import com.roboticseattle.common.FaceExpression;
import com.roboticseattle.common.HeartBeat;
import com.roboticseattle.common.MotorCommand;
import com.roboticseattle.common.MouthCommand;
import com.roboticseattle.common.ServoCommand;
import com.roboticseattle.common.SonarReading;
import com.roboticseattle.common.Song;
import com.roboticseattle.spar.Spar;
import com.roboticseattle.spar.bigkahuna.BigKahuna;
import com.roboticseattle.spar.bigkahuna.CannotConnectException;
import com.roboticseattle.spar.body.CannotStartException;
import com.roboticseattle.spar.body.Ear;
import com.roboticseattle.spar.body.Eye;
import com.roboticseattle.spar.body.Face;
import com.roboticseattle.spar.body.Legs;
import com.roboticseattle.spar.body.Mouth;
import com.roboticseattle.spar.body.Neck;
import com.roboticseattle.spar.body.Portola;
import com.roboticseattle.spar.body.Sonars;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;



/**
 * Electronic brain takes away my pain
 * 
 * @author Robotic Seattle
 *
 */

public class ElectronicBrain implements Runnable {
	
	
	private Thread thread = new Thread(this);
	private boolean isShutdown = false;
	private boolean isConnected = false;
	private boolean isBrowserReachable = false;
	
	private Map<String, HeartBeat> id2Beat = new HashMap<>();
	private int lastLapse=0;
	
	private Spar      parent;
	private Face      face;
	private Mouth     mouth;
	private Eye       eye;
	private Ear       ear;
	private Neck      neck;
	private Legs      legs;
	private Sonars    sonars;
	private BigKahuna bigKahuna;
	private Portola   portola;
	
	private boolean canMoveForward = true;
	private boolean canMoveBack = true;
	private volatile FaceExpression newFaceExpression = FaceExpression.THINKING;
	private volatile FaceExpression nextFaceExpression = null;
	
	private PainfulMemory painfulMemory = new PainfulMemory();
	
	public ElectronicBrain(Spar parent,
			               Face face, 
			               Mouth mouth, 
			               Eye eye, 
			               Ear ear,
			               Neck neck,
			               Legs legs, 
			               Sonars sonars,
			               BigKahuna bigKahuna,
			               Portola   portola) {
		this.parent = parent;
		this.face = face;
		this.mouth = mouth;
		this.eye = eye;
		this.ear = ear;
		this.neck = neck;
		this.legs = legs;
		this.sonars = sonars;
		this.bigKahuna = bigKahuna;
		this.portola = portola;
	}
	
	public void start() throws CannotStartException {
		face.start(new FaceNerveReceptor());
		mouth.start(new MouthNerveReceptor());
		neck.start(null);
		legs.start(null);
		bigKahuna.start(new BigKahunaReceptor());
		thread.start();
	}
	
	private class PainfulMemory implements Nerve {
		@Override
		public void onPainfulEvent(Object causingClass, Exception painCause, String paramsAndStuff) {
			Logger logger = Logger.getLogger(causingClass.getClass().getName());
			logger.log(Level.SEVERE, paramsAndStuff, painCause);
		}
	}
	
	private class FaceNerveReceptor extends PainfulMemory implements FaceNerve {

		@Override
		public void onEpressionComplete(FaceExpression state) {
		    if(nextFaceExpression != null && newFaceExpression == FaceExpression.HIGH_FIVE) {
		        newFaceExpression = nextFaceExpression;
		        nextFaceExpression = null;
		    }    
		}
		
	}
	
	private class SonarNerveReceptor extends PainfulMemory implements SonarNerve {
		@Override
		public void onNewSonarReading(SonarReading sr) {
		   switch(sr.getDirection()) {
		       case "forward":
		         if(sr.getDistance() < 10) {
		             if(canMoveForward) { 
		                 System.out.println("Halting engines because forward sonar: "+sr.getDistance());
		                 haltMotors();
		              }    
		             canMoveForward = false;
		         } else canMoveForward = true;    
		         break; 
		       case "back": 
		         if(sr.getDistance() < 10) {
		             if(canMoveBack) {
		                 System.out.println("Halting engines because back sonar: "+sr.getDistance());
		                 haltMotors();
		              }    
		             canMoveBack = false;
		         } else canMoveBack = true;
		         break; 
		   }    
		   bigKahuna.sendCommand(sr);
		}    
	}    
	
	private class MouthNerveReceptor extends PainfulMemory implements MouthNerve {

		@Override
		public void onSongComplete(Song song) {
		    if(newFaceExpression == FaceExpression.PLAYING_MUSIC && !mouth.isSinging()) newFaceExpression = FaceExpression.IDLE; 
		}

		@Override
		public void onSpeechComplete(String phrase) {
		    if(newFaceExpression == FaceExpression.SPEAKING) newFaceExpression = FaceExpression.IDLE; 
		}
		
	}
	
	private class EyeNerveReceptor extends PainfulMemory implements EyeNerve {

		@Override
		public void onNewVisual(byte[] bytes, int offset, int len) {
			try {
				bigKahuna.sendEyeBytes(bytes, offset, len);
			} catch (IOException e) {
				onPainfulEvent(this, e, "Should have tried to re-connect to BigKahuna");
			}
		}
	}
	
	private class EarNerveReceptor extends PainfulMemory implements EarNerve {

		@Override
		public void onNewAudible(byte[] bytes, int offset, int len) {
			try {
				bigKahuna.sendEarBytes(bytes, offset, len);
			} catch (IOException e) {
				onPainfulEvent(this, e, "Should have tried to re-connect to BigKahuna");
			}
		}
	}
	
	private class BigKahunaReceptor extends PainfulMemory implements BigKahunaNerve {
	    
  		@Override
		public void onPainfulEvent(Object causingClass, Exception painCause, String paramsAndStuff) {
		    super.onPainfulEvent(causingClass, painCause, paramsAndStuff);
		    if(painCause instanceof CannotConnectException) { 
		        newFaceExpression = FaceExpression.SAD;
                System.out.println("newFaceExpression = FaceExpression.SAD");
		    }

		}
		@Override
		public void onServoCommand(ServoCommand servoCommand) {
			try {
			    if(servoCommand.getPan() != Integer.MIN_VALUE) {
			        servoCommand.setPan(servoCommand.getPan()+panCorrection());
			        neck.pan(servoCommand.getPanAsPwmX4());
			        if(servoCommand.getPan() > 20+panCorrection() && isIdleFaceExpression()) newFaceExpression = FaceExpression.LOOKING_LEFT;
			        else if(servoCommand.getPan() < -20+panCorrection() && isIdleFaceExpression()) newFaceExpression = FaceExpression.LOOKING_RIGHT;
			        else if(isIdleFaceExpression()) newFaceExpression = FaceExpression.IDLE;
			        
			    }    
			    if(servoCommand.getTilt() != Integer.MIN_VALUE) neck.tilt(servoCommand.getTiltAsPwmX4());
			} catch(IOException ioe) {
				onPainfulEvent(this, ioe, "Should have notified the BigKahuna");
			}
		}

		@Override
		public void onMotorCommand(MotorCommand motorCommand) {
		    if(!canMoveForward && motorCommand.getLeft() > 0 && motorCommand.getRight() > 0) {
		        System.out.println("Halting engines because cannot move forward");
		        haltMotors();
		        return;
		    }
		    
		    if(!canMoveBack && motorCommand.getLeft() < 0 && motorCommand.getRight() < 0) {
		        System.out.println("Halting engines because cannot move back");
		        haltMotors();
		        return;
		    }    
		    
			try {
			    int left  = motorCommand.getLeft() + correction4LeftMotor(motorCommand.getLeft());
			    int right = motorCommand.getRight() + correction4RightMotor(motorCommand.getRight());
				legs.motors((byte)left, (byte)right);
			} catch (IOException e) {
				onPainfulEvent(this, e, "Should have notified the BigKahuna");
			}
		}
		
		@Override
		public void onMouthCommand(MouthCommand mouthCommand) {
			if(mouthCommand.getMouthSong() != null)  {
				if(mouthCommand.getMouthSong() ==  Song.NONE) { 
				    mouth.stopSinging();
				    if(newFaceExpression == FaceExpression.PLAYING_MUSIC) newFaceExpression = FaceExpression.IDLE;
				} else { mouth.sing(mouthCommand.getMouthSong());
     				if(isIdleFaceExpression()) newFaceExpression = FaceExpression.PLAYING_MUSIC; 
		    	}	   
			}
			if(mouthCommand.getText() != null) {
			    mouth.speak(mouthCommand.getText());
  				if(isIdleFaceExpression()) newFaceExpression = FaceExpression.SPEAKING;
			}    
		}

		@Override
		public void onHeartBeat(HeartBeat heartBeat) {
			HeartBeat origHeartBeat = id2Beat.get(heartBeat.getId());
			if(origHeartBeat != null) {
     			lastLapse = (int)(System.currentTimeMillis() - origHeartBeat.getCurrentTime());  
			    id2Beat.remove(heartBeat.getId());
			} 
			
			if(heartBeat.getStatus() == 2) {
			    if(!isBrowserReachable) onBrowserReached();
			} else {
			    if(isBrowserReachable) onBrowserLost();
			}    
		}

		@Override
		public void onConnected() {
			System.out.println("Connected to server "+bigKahuna.getAddress());
			newFaceExpression = FaceExpression.IDLE;
			isConnected = true;
			sonars.start(new SonarNerveReceptor());
		}
		
		@Override 
		public void onDisconnect(int status, String reason) {
			System.out.println("Disconnected from server "+bigKahuna.getAddress()+"; status="+status+"; reason="+reason);
			id2Beat.clear();
			newFaceExpression = FaceExpression.SAD;
			nextFaceExpression = null;
		    isConnected = false;
		    isBrowserReachable = false;
		    sonars.shutdown();
		    System.out.println("Halting engines because disconnected from server");
		    haltMotors();
		}
		
		

		@Override
		public void onFaceCommand(FaceCommand faceCommand) {
			newFaceExpression = faceCommand.getFaceState();
			nextFaceExpression = null;
		}
		
		@Override
		public void onEyeCommand(EyeCommand eyeCommand) {
		    if(eyeCommand.isConnect()) {
    			eye.start(new EyeNerveReceptor());
		    } else {
		    	eye.shutdown();
		    }
			bigKahuna.sendAcknowledgement(new Acknowledgement(eyeCommand));
		}


		@Override
		public void onEarCommand(EarCommand earCommand) {
			if(earCommand.isConnect()) {
    			ear.start(new EarNerveReceptor());
			} else {
				ear.shutdown();
			}
			bigKahuna.sendAcknowledgement(new Acknowledgement(earCommand));
		}    
		
	}

	public void onBrowserReached() {
        newFaceExpression =	FaceExpression.HIGH_FIVE;
	    nextFaceExpression = FaceExpression.IDLE;
	    isBrowserReachable = true;
	    ServoCommand servoCommand = new ServoCommand();
	    servoCommand.setTilt(26);
	    servoCommand.setPan(0+panCorrection());
	    try {
	      neck.tilt(servoCommand.getTiltAsPwmX4());
	      neck.pan(servoCommand.getPanAsPwmX4());
	   } catch(IOException ioe) {
			painfulMemory.onPainfulEvent(this, ioe, "centering servos");
	   }    
	}
	
	public void onBrowserLost() {
        newFaceExpression =	FaceExpression.BORED;
	    nextFaceExpression = null;
	    isBrowserReachable = false;
        System.out.println("Halting engines because disconnected from browser");
	    haltMotors();
	}
	
	public void haltMotors() {
		try {
			legs.motors((byte)0, (byte)0);
		} catch (IOException e) {
			painfulMemory.onPainfulEvent(this, e, "Halting engines");
		}
	}
	
	private int correction4RightMotor(int value) {
	    if(Math.abs(value) < 13) return (int)Math.signum(value)*2;
	    else if(Math.abs(value) < 21) return (int)Math.signum(value)*1;
	    else return 0;
	}
	
	private int correction4LeftMotor(int value) {
	    return 0;
	}
	
	private int panCorrection() {
	    return -10;
	}
	
	private boolean isIdleFaceExpression() {
	    return newFaceExpression == FaceExpression.IDLE || newFaceExpression == FaceExpression.LOOKING_LEFT || newFaceExpression == FaceExpression.LOOKING_RIGHT;
	}    
	   
	@Override
	public void run() {
	    int count = 0;
	    int heartBeatId = 0;
		while(!isShutdown) {
			if(!isConnected) { // Before anything we gotta connect to the server
			    System.out.println("Connecting to the server "+bigKahuna.getAddress());
			    bigKahuna.connect();
			} else { // The most interesting stuff is in here after connection
			    if(count%10 == 0) { // sending ping every second
			        long now = System.currentTimeMillis();
			        HeartBeat heartBeat = new HeartBeat(heartBeatId, now, lastLapse);
			        id2Beat.put(String.valueOf(heartBeatId), heartBeat);
			        bigKahuna.sendCommand(heartBeat);
			        heartBeatId++;
     			    
			        List<String> what2Remove = new LinkedList<>();
			        for(Map.Entry<String, HeartBeat> entry : id2Beat.entrySet()) { // Anything longer than 8 seconds should go
			            if(now - entry.getValue().getCurrentTime() > 8000) what2Remove.add(entry.getKey());       
			        }   
			        for(String key : what2Remove) {id2Beat.remove(key);}
			    
	    		    if(id2Beat.size() > 5) { // It is spooky we got more than five unanswered pings
		    	        bigKahuna.disconnect(2, "ping");
			        }    
			    }
			    
			}
			
			face.setNewState(newFaceExpression);
			
			count++;
			try {Thread.sleep(100); } catch(InterruptedException ie) {}
		}
		
	}

}
