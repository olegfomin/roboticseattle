package com.roboticseattle.common;

/**
 * Specifies whether anything is off or on
 * @author Robotic Seattle
 *
 */
public class OnOffCommand extends Command {
	
    private String turn;
    
    public void setTurn(String aTurn) {
        turn = aTurn;
    }    
    
    public String getTurn() {
        return turn;
    }    
    
    public boolean isConnect() {
        return "on".equals(turn) ? true : false;
    }    

}
