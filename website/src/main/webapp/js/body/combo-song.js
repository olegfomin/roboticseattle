/**
 * Song Selection
 *
 * Created by Robotic Seattle on 6/1/2016.
 */

var onSongSelection = function () {
    electronicbrain.playSong($(this).val());
}

$( "#dd-song" ).change(onSongSelection);

function emptySong() {
    $( "#dd-song" ).val("NONE");
}

function disSong() {
    emptySong();
    $( "#dd-song" ).attr('disabled', 'disabled');
}

function enSong() {
    $( "#dd-song" ).removeAttr("disabled");
}

