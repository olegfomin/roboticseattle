/**
 * Created by Robotic Seattle on 6/25/2016.
 */

/**
 * Makes the 'More ...' tab visible and populates it.
 *
 * @param url
 * @param flyoverid
 * @returns {boolean}
 */
function more(url, flyoverid) {
//    alert(JSON.stringify(this));
    $("#tabs").tabs( "enable", 6 );
    $("#tabs").tabs( { "active": 6 } );
    $('#frm-more').remove();
    if(url != null) { // Remove and re-create the frame in order to remove the frame history that interfere with main history
        var frame = document.createElement("IFRAME");
        frame.id = 'frm-more';
        frame.width = 680;
        frame.height = 680;
        frame.src = url;
        $('#frame-container')[0].appendChild(frame);
    }
    if(flyoverid != null) {
      $("#fly-dest")[0].innerHTML =  $("#"+flyoverid)[0].innerHTML;
      $("#fly-dest").css('display', 'block');
    } else {
        $("#fly-dest").css('display','none');
    }

    return false;
}

function morePicUrl(url) {
    $("#tabs").tabs( "enable", 6 );
    $("#tabs").tabs( { "active": 6 } );

    $("#img-more")[0].src = url;

    $("#img-more").css('display','block');
    return false;

}

function morePicRef(imgId) {
    return morePicUrl($("#"+imgId)[0].src.replace('small/',''));
}


/**
 * Disables the 'More ...' tab and navigates back
 */
function closeMore() {
    revomeMoreTab();
    history.back();
}

function revomeMoreTab() {
    $("#tabs").tabs( "disable", 6 );
    $('#frm-more').remove();
    $("#img-more").css('display','none');
    $("#img-more")[0].src = "#";
    $("#fly-dest")[0].innerHTML = "";
}