package com.roboticseattle.spar.body;

import java.io.IOException;

public class Neck extends Locomotive {
	
	public Neck(Portola portola) {
		super(portola);
	}
	
    public synchronized void send(byte servoNumber, int pwm) throws IOException {
        byte[] byteArray = new byte[6];
        byteArray[0] = (byte)0xAA;
        byteArray[1] = (byte)0x0C;
        byteArray[2] = (byte)0x04;
        byteArray[3] = servoNumber; // 0-pan; 5- tilt
        byteArray[4] = (byte)(pwm & 0x7F);
        byteArray[5] = (byte)(pwm >> 7 & 0x7F);
        send(byteArray);
    }	

	public void pan(int panPwmX4) throws IOException {
       send((byte)0, panPwmX4); 
	}
	
	public void tilt(int tiltPwmX4) throws IOException {
       send((byte)5, tiltPwmX4); 
	}

}
