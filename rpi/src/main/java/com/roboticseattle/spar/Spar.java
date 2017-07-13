package com.roboticseattle.spar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.roboticseattle.spar.bigkahuna.BigKahuna;
import com.roboticseattle.spar.body.Ear;
import com.roboticseattle.spar.body.Eye;
import com.roboticseattle.spar.body.Face;
import com.roboticseattle.spar.body.Legs;
import com.roboticseattle.spar.body.Mouth;
import com.roboticseattle.spar.body.Neck;
import com.roboticseattle.spar.body.Portola;
import com.roboticseattle.spar.body.PortolaSparImpl;
import com.roboticseattle.spar.body.Sonars;
import com.roboticseattle.spar.mind.ElectronicBrain;

import org.eclipse.jetty.websocket.client.WebSocketClient;

/**
 * The main class responsible for creation of the electronic brain and all its components
 * @author Robotic Seattle
 *
 */
public class Spar {
	
	public Face newFace() {
		return new Face();
	}
	
	public Mouth newMouth() {
		return new Mouth();
	}
	
	public Eye newEye() {
		return new Eye();
	}
	
	public Ear newEar() {
		return new Ear();
	}
	
	public Neck newNeck(Portola portola) {
		return new Neck(portola);
	}
	
	public Legs newLegs(Portola portola) {
		return new Legs(portola);
	}
	
	public Sonars newSonars() {
		return new Sonars();
	}
	
	public BigKahuna newKahuna(String url) {
		return new BigKahuna(url); 
	}
	
	public Portola newPortola() {
		return new PortolaSparImpl();
	}
	
	public ElectronicBrain newBrain(String url) {
		Portola portola = new PortolaSparImpl();
		return new ElectronicBrain(this, newFace(), newMouth(), newEye(), newEar(), newNeck(portola), newLegs(portola), newSonars(), newKahuna(url), portola);
	}

	public static void main(String[] args) throws Exception {
	    if(args.length < 1)  throw new Exception("The websocket address needs to be specified like that ws://<ip>/device");
		Spar spar = new Spar();
		ElectronicBrain brain = spar.newBrain(args[0]); // "ws://192.168.1.101:8080/device"
		brain.start();
	}
	

}
