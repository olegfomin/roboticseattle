/**
 * Electornic brain takes away my pain
 * Created by Robotic Seattle on 6/1/2016.
 */

var StateEnum = {"DISABLED" : 0, "ENABLED" : 1, "BEING_DISABLED" : 2, "BEING_ENABLED" : 3};
var ConnectionStateEnum = {"DISCONNECTED" : 0, "WEB_ONLY" : 1, "SPAR" :2};

function ElectronicBrain() {
    this.connectionState = ConnectionStateEnum.DISCONNECTED;
    this.cameraState = StateEnum.DISABLED;
    this.microphoneState = StateEnum.DISABLED;

    this.currentLeftMotor = 0;
    this.currentRightMotor = 0;
    this.isMotorsHaltDisplayed = false;

    this.connectionLost = function () {
        this.cameraState = StateEnum.DISABLED;
        this.microphoneState = StateEnum.DISABLED;
        this.currentLeftMotor = 0;
        this.currentRightMotor = 0;
        this.isMotorsHaltDisplayed = false;

        disCamphone();
        disSay();
        disSong();
        disMotors();
        disDisplay();
        disMood();
        disPanTilt();
    }

    this.connectionEstablished = function() {
        enCamphone();
        enSay();
        enSong();
        enMotors();
        enDisplay();
        enMood();
        enPanTilt();
    }
    
    this.start = function () {
        bigkahuna.start();
    }

    this.enableCam = function() {
        var command = comgen("eye", true);
        command.turn = 'on';
        this.cameraState = StateEnum.BEING_ENABLED;
        bigkahuna.send(command);
        displayPicture("images/changingcamera.jpg");
    }

    this.disableCam = function () {
        var command = comgen("eye", true);
        command.turn = 'off';
        this.cameraState = StateEnum.BEING_DISABLED;
        bigkahuna.send(command);
        displayPicture("images/changingcamera.jpg");
    }

    this.enableMicrophone = function() {
        var command = comgen("ear", true);
        command.turn = 'on';
        play4microphone();
        this.microphoneState = StateEnum.BEING_ENABLED;
        bigkahuna.send(command);
    }

    this.disableMicrophone = function() {
        var command = comgen("ear", true);
        command.turn = 'off';
        pause4microphone();
        this.microphoneState = StateEnum.BEING_DISABLED;
        bigkahuna.send(command);
    }

    this.showMood = function(mood) {
        var faceCommand = comgen("face", false);
        faceCommand.expression = mood;
        bigkahuna.send(faceCommand);
    }

    this.playSong = function(song) {
        var mouthCommand = comgen("mouth", false);
        mouthCommand.song = song;
        bigkahuna.send(mouthCommand);
    }

    this.motors = function(leftMotor, rightMotor) {
        if(leftMotor != this.currentLeftMotor || rightMotor != this.currentRightMotor) {
            this.currentLeftMotor = leftMotor;
            this.currentRightMotor = rightMotor;
            var motorCommand = comgen("motor", false);
            motorCommand.left = leftMotor;
            motorCommand.right = rightMotor;
            bigkahuna.send(motorCommand);
        }
        this.isMotorsHaltDisplayed = false;
        displayMotors(leftMotor, rightMotor);
    }
    
    this.say = function (speech) {
        var mouthCommand = comgen("mouth", false);
        mouthCommand.text = speech; 
        mouthCommand.song = "NONE";
        bigkahuna.send(mouthCommand);
        emptySpeech();
        emptySong();
    }

    this.pan = function(value) {
        $( "#btn-halt" ).click();
        var servoCommand = comgen("servo", false);
        servoCommand.pan = value;
        bigkahuna.send(servoCommand);
        this.haltTheMotors();
    }

    this.tilt = function(value) {
        $( "#btn-halt" ).click();
        var servoCommand = comgen("servo", false);
        servoCommand.tilt = value;
        bigkahuna.send(servoCommand);
        this.haltTheMotors();
    }
    
    this.haltTheMotors = function () {
        if(this.currentLeftMotor != 0 || this.currentRightMotor !=0) this.motors(0, 0);
        if(!this.isMotorsHaltDisplayed) {
            displayMotorsHalt();
            this.isMotorsHaltDisplayed = true;
        }
    }

    this.onError = function(detailedMessage) {
        alert("I shall notify the big kahuna about error: "+detailedMessage);
    }
    
    this.onConnectionLost = function () {
        if(this.connectionState == ConnectionStateEnum.DISCONNECTED) {
            alert("Could not establish connection to the server");
        } else if(this.connectionState == ConnectionStateEnum.SPAR) {
            this.connectionLost();
            alert("The connection to the server is lost");
        } else {
            alert("The connection to the server is lost");
        }
    }

    this.onMessage = function(data) {
       var command = JSON.parse(data);
       switch(command.dest) {
           case "ack":
               var key=command.commandId;
               if(ack[key] != null) {
                   this.onAck(ack[key]);
                   delete ack[key];
               } else this.onError("Ack is not found: "+data);
               break;
           case "img": displayPicture(img_host+command.src); break;
           case "heart":
               showLapse(command.lastLapse);
               if(command.status == 1) {
                   if(this.connectionState == ConnectionStateEnum.SPAR) this.connectionLost();
                   this.connectionState =  ConnectionStateEnum.WEB_ONLY;
               } else if(command.status == 2) {

                   if(this.connectionState != ConnectionStateEnum.SPAR) this.connectionEstablished();
                   this.connectionState = ConnectionStateEnum.SPAR;
               } else {
                   this.onError("Unknown heart beat status: "+JSON.stringify(command));
               }

               bigkahuna.send(command);
               break;
           case "sonar":
               this.onSonar(command);
               break;
           case "eye":
               if(command.turn == "off") {
                   camDisabled();
                   displayPicture("images/nocamera.jpg");
                   this.cameraState = StateEnum.DISABLED;
                   alert("Camera has been disabled due to an error please try to enable it again");
               } else this.onError("Unexpected value for camera command: "+JSON.stringify(command));
           default: this.onError("Unexpected command destination: "+command.dest+" -- "+data);
       }
    }
    
    this.onAck = function (origCommand) {
        switch(origCommand.dest) {
            case "eye":
                if(origCommand.turn == "off") {
                    if(this.cameraState != StateEnum.BEING_DISABLED) this.onError("Unexpected camera state ("+origCommand.turn+"; "+this.cameraState+")");
                    camDisabled();
                    displayPicture("images/nocamera.jpg");
                    this.cameraState = StateEnum.DISABLED;
                } else if(origCommand.turn == "on") {
                    if(this.cameraState != StateEnum.BEING_ENABLED) this.onError("Unexpected camera state ("+origCommand.turn+"; "+this.cameraState+")");
                    camEnabled();
                    this.cameraState = StateEnum.ENABLED;
                } else {
                    this.onError("Unexpected eye ack: "+JSON.stringify(origCommand));
                }
                break;
            case "ear":
                if(origCommand.turn == "off") {
                    if(this.microphoneState != StateEnum.BEING_DISABLED) this.onError("Unexpected microphone state ("+origCommand.turn+"; "+this.microphoneState+")");
                    mphoneDisabled();
                    this.microphoneState = StateEnum.DISABLED;
                } else if(origCommand.turn == "on") {
                    if(this.microphoneState != StateEnum.BEING_ENABLED) this.onError("Unexpected microphone state ("+origCommand.turn+"; "+this.microphoneState+")");
                    mphoneEnabled();
                    this.microphoneState = StateEnum.ENABLED;
                } else {
                    this.onError("Unexpected ear ack: "+JSON.stringify(origCommand));
                }
                break;
            default: this.onError("Unexpected ack destination: "+JSON.stringify(origCommand));
        }
    }

    this.onSonar = function(sonarReading) {
        var aSpan;
        switch(sonarReading.direction) {
            case "forward": aSpan = $("#forward-distance"); break;
            case "back": aSpan = $("#back-distance");   break;
            case "left": aSpan = $("#left-distance"); break;
            case "right": aSpan = $("#right-distance"); break;
            default: this.onError("Unexpected sonar direction: "+JSON.stringify(sonarReading)); return;
        }
        var distanceInInch = Math.round(sonarReading.distance);
        aSpan.text(distanceInInch);
    }
    
}

var electronicbrain = new ElectronicBrain();