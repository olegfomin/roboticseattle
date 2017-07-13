/**
 * Created by Robotic Seattle on 6/28/2016.
 *
 * Responsible for centering the main div right now. 
 */

function move2Center() {
    if(window.innerWidth > 720) {
        var leftMargin = (window.innerWidth-720)/2;
        $("body > div").css('margin-left', leftMargin);
    } else {
        $("body > div").css('margin-left', 1);
    }
}

$(function() {
    move2Center();
    $( window ).resize(move2Center);
});

