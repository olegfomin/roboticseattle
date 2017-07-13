package com.roboticseattle.spar.mind;


/**
 * Delivers information from microphone 
 * @author Robotic Seattle 
 * @version 0.99
 */
public interface EarNerve extends Nerve {
    void onNewAudible(byte[] chunk, int offset, int len);
}
