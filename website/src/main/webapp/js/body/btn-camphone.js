/**
 * It is all about camera and microphone control buttons
 * 
 * Created by Robotic Seattle on 6/1/2016.
 */


var microphoneBtn = $("#btn-microphone");
var cameraBtn = $("#btn-camera");
var heartBtn = $("#btn-heart");

var onCameraClick = function () {
    cameraBtn.attr('disabled', 'disabled');
    if(cameraBtn.hasClass("btn-camera-off")) { electronicbrain.enableCam(); cameraBtn.removeClass("btn-camera-off"); }
    else if(cameraBtn.hasClass("btn-camera-on")) { electronicbrain.disableCam(); cameraBtn.removeClass("btn-camera-on"); }
    else electronicbrain.onError("The enabled camera button had neither .btn-camera-off nor .btn-camera-on");
    cameraBtn.addClass("btn-camera-chg");
}

function camEnabled() {
    cameraBtn.removeClass("btn-camera-chg");
    cameraBtn.addClass("btn-camera-on");
    cameraBtn.removeAttr('disabled');
}

function camDisabled() {
    cameraBtn.removeClass("btn-camera-chg");
    cameraBtn.addClass("btn-camera-off");
    cameraBtn.removeAttr('disabled');
}


cameraBtn.click(onCameraClick);

 var onMicrophoneClick = function () {
     microphoneBtn.attr('disabled', 'disabled');
     if(microphoneBtn.hasClass("btn-microphone-off")) { electronicbrain.enableMicrophone(); microphoneBtn.removeClass("btn-microphone-off")}
     else if(microphoneBtn.hasClass("btn-microphone-on")) {electronicbrain.disableMicrophone(); microphoneBtn.removeClass("btn-microphone-on")}
     else electronicbrain.onError("The enabled microphone button had neither .btn-microphone-off nor .btn-microphone-on");
     microphoneBtn.addClass("btn-microphone-chg");
}

microphoneBtn.click(onMicrophoneClick);

function mphoneEnabled() {
    microphoneBtn.removeClass("btn-microphone-chg");
    microphoneBtn.addClass("btn-microphone-on");
    microphoneBtn.removeAttr("disabled");
}

function mphoneDisabled() {
    microphoneBtn.removeClass("btn-microphone-chg");
    microphoneBtn.addClass("btn-microphone-off");
    microphoneBtn.removeAttr("disabled");
}

var myAudio;

function play4microphone() {
    myAudio = document.createElement("AUDIO");
    myAudio.setAttribute("src", img_host+"/sound");
    myAudio.setAttribute("autoplay", true);
    document.body.appendChild(myAudio);
}

function pause4microphone() {
    if (myAudio != null) {
        myAudio.pause();
        document.body.removeChild(myAudio);
    }
}

function showLapse(lapse) {
    if(heartBtn.hasClass("btn-heart-arrest")) {
        heartBtn.removeClass("btn-heart-arrest");
        heartBtn.addClass("btn-heart-1");
    } else if(heartBtn.hasClass("btn-heart-1")){
        heartBtn.removeClass("btn-heart-1");
        heartBtn.addClass("btn-heart-2");
    } else {
        heartBtn.removeClass("btn-heart-2");
        heartBtn.addClass("btn-heart-1");
    }
    var str = "" + lapse;
    var pad = "0000"
    var ans = pad.substring(0, pad.length - str.length) + str;
    if(ans.length <= 4 && lapse != 0) {heartBtn.text(ans);}
    else {heartBtn.text("????");}
}

function disCamphone() {
    microphoneBtn.removeClass("btn-microphone-chg");
    microphoneBtn.removeClass("btn-microphone-on");
    microphoneBtn.addClass("btn-microphone-off");
    microphoneBtn.attr('disabled', 'disabled');
    pause4microphone();

    cameraBtn.removeClass("btn-camera-chg");
    cameraBtn.removeClass("btn-camera-on");
    cameraBtn.addClass("btn-camera-off");
    cameraBtn.attr('disabled', 'disabled');

    heartBtn.removeClass("btn-heart-1");
    heartBtn.removeClass("btn-heart-2");
    heartBtn.addClass("btn-heart-arrest");
    heartBtn.text("");
}


function enCamphone() {
    microphoneBtn.removeAttr("disabled");
    cameraBtn.removeAttr("disabled");
    heartBtn.removeClass("btn-heart-arrest");
    heartBtn.addClass("btn-heart-1");
    heartBtn.text("????");
}