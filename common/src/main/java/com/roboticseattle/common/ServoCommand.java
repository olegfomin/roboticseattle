package com.roboticseattle.common;

public class ServoCommand extends Command {
	private int pan = Integer.MIN_VALUE;
	private int tilt = Integer.MIN_VALUE;
	
	public int getPan() {
		return pan;
	}
	public void setPan(int pan) {
		this.pan = pan;
	}
	public int getTilt() {
		return tilt;
	}
	public void setTilt(int tilt) {
		this.tilt = tilt;
	}

	public int getPanAsPwm() {
	    return degreeToPwm(pan);
	}
	
	public int getPanAsPwmX4() {
		return getPanAsPwm()*4;
	}
	
	public int getTiltAsPwm() {
		return degreeToPwm(tilt);
	}

	public int getTiltAsPwmX4() {
		return getTiltAsPwm()*4;
	}
	
	@Override
	public String toString() {
		return "ServoCommand [getPanAsPwmX4()=" + getPanAsPwmX4() + ", getTiltAsPwmX4()=" + getTiltAsPwmX4() + "]";
	}
	private static int degreeToPwm(int degree) {
		return (int)Math.round(1000.0+(degree+60.0)*8.33);
		
	}

}
