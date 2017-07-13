/**
 * Faces spar shows
 * 
 * Created by Robotic Seattle on 6/1/2016.
 */

var currentMood = $("#btn-robot-idle");

var onMoodClick = function() {
    currentMood.removeClass("btn-mood-active");
    currentMood.addClass("btn-mood-passive");
    $(this).removeClass("btn-mood-passive");
    $(this).addClass("btn-mood-active");

    currentMood = $(this);

    electronicbrain.showMood(currentMood.val());

}

$( "#btn-robot-idle" ).click(onMoodClick);
$( "#btn-robot-bored").click(onMoodClick);
$( "#btn-robot-excited").click(onMoodClick);
$( "#btn-robot-look-left").click(onMoodClick);
$( "#btn-robot-look-right").click(onMoodClick);
$( "#btn-robot-sad").click(onMoodClick);
$( "#btn-robot-speaking").click(onMoodClick);
$( "#btn-robot-play-music").click(onMoodClick);
$( "#btn-robot-think").click(onMoodClick);
$( "#btn-robot-highfive").click(onMoodClick);


function disMood() {
    $( "#btn-robot-idle" ).attr('disabled', 'disabled');
    $( "#btn-robot-bored").attr('disabled', 'disabled');
    $( "#btn-robot-excited").attr('disabled', 'disabled');
    $( "#btn-robot-look-left").attr('disabled', 'disabled');
    $( "#btn-robot-look-right").attr('disabled', 'disabled');
    $( "#btn-robot-sad").attr('disabled', 'disabled');
    $( "#btn-robot-speaking").attr('disabled', 'disabled');
    $( "#btn-robot-play-music").attr('disabled', 'disabled');
    $( "#btn-robot-think").attr('disabled', 'disabled');
    $( "#btn-robot-highfive").attr('disabled', 'disabled');
    currentMood.removeClass("btn-mood-active");
    currentMood.addClass("btn-mood-passive");
}

function enMood() {
    $( "#btn-robot-idle" ).removeAttr("disabled");
    $( "#btn-robot-bored").removeAttr("disabled");
    $( "#btn-robot-excited").removeAttr("disabled");
    $( "#btn-robot-look-left").removeAttr("disabled");
    $( "#btn-robot-look-right").removeAttr("disabled");
    $( "#btn-robot-sad").removeAttr("disabled");
    $( "#btn-robot-speaking").removeAttr("disabled");
    $( "#btn-robot-play-music").removeAttr("disabled");
    $( "#btn-robot-think").removeAttr("disabled");
    $( "#btn-robot-highfive").removeAttr("disabled");
    currentMood = $("#btn-robot-idle");
    currentMood.removeClass("btn-mood-passive");
    currentMood.addClass("btn-mood-active");
}

