package com.roboticseattle.spar.mind;

import java.util.Map;


/**
 * This interface is called every time when anything going wrong. Usually it logged and going back to the operator
 * 
 * @author robotic seattle 
 * @version 1.0
 */
public interface Nerve
{
    void onPainfulEvent(Object causingClass, Exception painCause, String paramsAndStuff); 
}
