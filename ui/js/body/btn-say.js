/**
 *
 * Created by Robotic Seattle on 6/1/2016.
 */

var sayValueSaved;

var onSayClick = function(event) {
    event.preventDefault();
    electronicbrain.say($("#txt-say").val());
}

function emptySpeech() {
    $("#txt-say").val("");
}

$("#btn-say").click(onSayClick);

function disSay() {
    $("#btn-say").button('disable');
    sayValueSaved = $("#txt-say").val();
    emptySpeech();
    $("#txt-say").attr('disabled', 'disabled');
}

function enSay() {
    $("#btn-say").button('enable');
    $("#txt-say").removeAttr("disabled");
    if(sayValueSaved != null) $("#txt-say").val(sayValueSaved);
}

