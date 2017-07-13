/**
 * Shows what is being transmitted from the camera
 * Created by Robotic Seattle on 6/1/2016.
 */

var canvas = document.getElementById('robot-eye');
var context = canvas.getContext('2d');
var imageObj = new Image();

imageObj.onload = function() {
    context.drawImage(imageObj, 0, 0);
};

function displayPicture(src) {
    imageObj.src = src;
}

displayPicture("images/nocamera.jpg");

function disDisplay() {
    displayPicture("images/nocamera.jpg");
}

function enDisplay() {
    
}
