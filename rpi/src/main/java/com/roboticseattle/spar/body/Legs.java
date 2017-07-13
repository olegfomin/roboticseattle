package com.roboticseattle.spar.body;

import java.io.IOException;

public class Legs extends Locomotive {

	public Legs(Portola portola) {
		super(portola);
	}
	
	public void motors(byte left, byte right) throws IOException {
        left = (left == -128) ? -127 : left;
        right = (right == -128) ? -127 : right;
        byte motorCommand = ((byte)0xD0) & 0x7F | 0x02 | 0x04; // both motors forward by default;
        if(left < 0) {
            if(right > 0) motorCommand = ((byte)0xD0) & 0x7F | 0x01 | 0x04; // left backward right forward 
            else motorCommand = ((byte)0xD0) & 0x7F | 0x01 | 0x08; // both backward
        } else {
            if(right < 0) motorCommand = ((byte)0xD0) & 0x7F | 0x02 | 0x08; // left forward right backward
        }    

        byte[] byteArray = new byte[5];
        byteArray[0] = (byte) 0x80;
        byteArray[1] = (byte) 0x07;
        byteArray[2] = motorCommand;
        byteArray[3] = (byte)Math.abs(left);
        byteArray[4] = (byte)Math.abs(right);
        send(byteArray);
	}

}
