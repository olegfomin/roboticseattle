package com.roboticseattle.common;


/**
 * Write a description of class SonarReading here.
 * 
 * @author Robotic Seattle 
 * @version Jun-10-2016 12:18PM
 */
public class SonarReading extends Command
{
    private String direction; // left, right, forward, back
    private int distance; // time between sound signal sent and received
    
    public SonarReading() {
        super();
        setDest("sonar");
    }    
    
    public void setDirection(String aDirection) {
        direction = aDirection;
    }
    
    public String getDirection() {
        return direction;
    }
    
    public void setDistance(int aDistance) {
        distance = aDistance;
    }
    
    public int getDistance() {
        return distance;
    }    
}
