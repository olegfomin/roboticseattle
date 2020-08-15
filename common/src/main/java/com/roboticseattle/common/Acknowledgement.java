package com.roboticseattle.common;


/**
 * Contains id of the command just executed
 * 
 * @author Robotic Seattle 
 * @version 05/30/16 6:31PM
 */
public class Acknowledgement
{
    private  String dest = "ack";
    private  String commandId;
    
    public Acknowledgement() {}
    
    public Acknowledgement(String aCommandId) {
        commandId = aCommandId;
    }
    
    public Acknowledgement(Command aCommand) {
        commandId = aCommand.getId();
    }    
    
    public String getCommandId() {
        return commandId;
    }
    
    public void setCommandId(String aCommandId) {
        commandId = aCommandId;
    }
    
    public String getDest() {
        return dest;
    }    
    
    public void setDest(String aDest) {
        dest = aDest;
    }    
}
