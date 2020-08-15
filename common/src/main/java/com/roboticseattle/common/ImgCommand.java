package com.roboticseattle.common;

public class ImgCommand extends Command {
	private String src;
	
	public ImgCommand() {
		setDest("img");
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}
	
}
