package com.roboticseattle.common;

public enum FaceExpression {
	IDLE("idle/robot_idle",new int[][]{{33,55},{71,83}}, 350, 1700), 
	LOOKING_LEFT("lookleft/robot_look_left", new int[][]{{33,55},{71,83}}, 350, 1700),
	LOOKING_RIGHT("lookright/robot_look_right", new int[][]{{33,55},{71,83}}, 350, 1700),
	HIGH_FIVE("highfive/robot_highfive", new int[][]{{33,55},{60,66},{81,86}}, 300, 2000), 
	SAD("sad/robot_sad", new int[][]{{20,95}}, 400), 
	SPEAKING("speaking/robot_speaking", new int[][]{{20,90}}, 200), 
	BORED("bored/robot_bored", new int[][]{{14,90}}, 500),
	THINKING("thinking/robot_think_puzzle_v02", new int[][]{{5,106}}, 200),
	EXCITED("excited/robot_puzzle_excited", new int[][]{{18, 99}}, 250),
	PLAYING_MUSIC("playingmusic/robot_play_music", new int[][]{{10, 90}}, 250);
	
	private String prefix;
	private int[][] ranges;
	int frameDelay;
	int rangeDelay;
	
	private FaceExpression(String aPrefix, int[][] theRanges, int aFrameDelay, int aRangeDelay) {
		prefix = aPrefix;
		ranges = theRanges;
		frameDelay = aFrameDelay;
		rangeDelay = aRangeDelay;
	}
	
	private FaceExpression(String aPrefix, int[][] theRanges, int aFrameDelay) {
		prefix = aPrefix;
		ranges = theRanges;
		frameDelay = aFrameDelay;
		rangeDelay = aFrameDelay;
	}

	public String getPrefix() {
		return prefix;
	}

	public int[][] getRanges() {
		return ranges;
	}

	public int getFrameDelay() {
		return frameDelay;
	}

	public int getRangeDelay() {
		return rangeDelay;
	}
	
}