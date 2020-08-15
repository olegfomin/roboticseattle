package com.roboticseattle.common;

public enum Song {
	NONE("",0),
    ANTHEM("Robot_Anthem.mp3", -2000), 
    THE_ROBOTS("The_Robots.mp3", -1500), 
    OUROBOROS("Ouroboros.mp3", -2000), 
    MONKEY_SPIN("MonkeySpin.mp3", -2000),
    KICK_SHOCK("KickShock.mp3", -2000),
    FLUFFING_DUCK("FluffingDuck.mp3", -1500);
    
    String fileName;
	int    volume;
    
    private Song(String aFileName, int aVolume) {
        fileName = aFileName; volume = aVolume; 
    }
    
    public String getFileName() {
		return fileName;
	}

	public int getVolume() {
		return volume;
	}
    
}