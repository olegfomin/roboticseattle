package com.roboticseattle.spar.mind;

import com.roboticseattle.common.EarCommand;
import com.roboticseattle.common.EyeCommand;
import com.roboticseattle.common.FaceCommand;
import com.roboticseattle.common.HeartBeat;
import com.roboticseattle.common.MotorCommand;
import com.roboticseattle.common.MouthCommand;
import com.roboticseattle.common.ServoCommand;

public interface BigKahunaNerve extends Nerve {
	void onConnected();
	void onDisconnect(int status, String reason);
	void onServoCommand(ServoCommand servoCommand);
	void onMotorCommand(MotorCommand motorCommand);
	void onMouthCommand(MouthCommand phrase);
    void onFaceCommand(FaceCommand faceCommand);
    void onHeartBeat(HeartBeat heartBeat);
    void onEyeCommand(EyeCommand eyeCommand);
    void onEarCommand(EarCommand earCommand);
    
}
