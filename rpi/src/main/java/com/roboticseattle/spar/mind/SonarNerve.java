package com.roboticseattle.spar.mind;

import com.roboticseattle.common.SonarReading;


/**
 * Delivers sonar readings
 * 
 * @author Robotic Seattle
 * @version 
 */
public interface SonarNerve extends Nerve
{
    void onNewSonarReading(SonarReading sr);
}
