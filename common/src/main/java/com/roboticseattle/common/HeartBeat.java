package com.roboticseattle.common;

public class HeartBeat extends Command {
    private int  status; // 2- Reached all the way to the browser; 1 - reached only www.roboticseattle.com (no browser) 
    private long currentTime;
    private int  lastLapse;
    
    public HeartBeat() {
        super();
        setDest("heart");
    }
    
    public HeartBeat(int aId, long aCurrentTime, int aLastLapse) {
        super();
        setDest("heart");
        setId(String.valueOf(aId));
        currentTime = aCurrentTime;
        lastLapse = aLastLapse;
    }    
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int aStatus) {
        status = aStatus;
    }
    
    public long getCurrentTime() {
        return currentTime;
    }
    
    public void setCurrentTime(long aCurrentTime) {
        currentTime = aCurrentTime;
    }
    
    public int getLastLapse() {
        return lastLapse;
    }
    
    public void setLastLapse(int aLastLapse) {
        lastLapse = aLastLapse;   
    }    

}
