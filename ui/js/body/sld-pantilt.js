/**
 * Sliders for servos
 *
 * Created by Robotic Seattle on 6/1/2016.
 */

var onPanSlide = function(event, ui) {
    electronicbrain.pan(ui.value);
}

$("#pan-slider").on("slide", onPanSlide);

var onTiltSlide = function(event, ui) {
    electronicbrain.tilt(-ui.value);
}

$("#tilt-slider").on("slide", onTiltSlide);

function disPanTilt() {
    $("#pan-slider").slider('value',0);
    $("#pan-slider").slider('disable');
    $("#tilt-slider").slider('value',0);
    $("#tilt-slider").slider('disable');
}

function enPanTilt() {
    $("#pan-slider").slider('enable');
    $("#tilt-slider").slider('enable');
}
