package com.roboticseattle.common;

public class MotorCommand extends Command {
	private int left;
	private int right;

	public MotorCommand() {
	}
	
	public int getLeft() {
		return (left < -127 ?  -127 : (left > 127 ? 127 : left));
	}
	
	public byte getLeftAsByte() {
		return (byte)getLeft();
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getRight() {
		return right < -127 ? -127 : (right > 127 ? 127 : right);
	}
	
	public byte getRightAsByte() {
		return (byte) getRight();
	}	

	public void setRight(int right) {
		this.right = right;
	}

	@Override
	public String toString() {
		return "MotorCommand [left=" + left + ", right=" + right + "]";
	}


}
