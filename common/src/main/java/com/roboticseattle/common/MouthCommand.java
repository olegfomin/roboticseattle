package com.roboticseattle.common;

public class MouthCommand extends Command {
	
	private String text;
	private String song;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSong() {
		return song;
	}

	public void setSong(String song) {
		this.song = song;
	}
	
	public Song getMouthSong() {
		return song != null ? Song.valueOf(song.toUpperCase()) : null;
	}
	
}
