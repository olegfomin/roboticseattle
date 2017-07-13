/**
 * Created by Robotic Seattle on 6/24/2016.
 */

var voteFor = function () {
    $(this).removeClass("btn-thumb-up");
    $(this).addClass("btn-thumb-up-a");
    $(this).attr("disabled", "disabled");
    var id = $(this).attr("id");
    var spanId = "spn"+id.substr(3);
    var newVoteNumber = Number($("#"+spanId).text())+1;
    $("#"+spanId).text(newVoteNumber);

    var dislikeButtonId = id.substr(0, id.length-4)+"dislike";
    $("#"+dislikeButtonId).attr("disabled", "disabled");

    $.ajax({
        type: "POST",
        url: "voting",
        data: {button: id},
        dataType: "application/json"
    });
}

var voteAgainst = function () {
    $(this).removeClass("btn-thumb-down");
    $(this).addClass("btn-thumb-down-a");
    $(this).attr("disabled", "disabled");
    var id = $(this).attr("id");
    var spanId = "spn"+id.substr(3);
    var newVoteNumber = Number($("#"+spanId).text())+1;
    $("#"+spanId).text(newVoteNumber);

    var dislikeButtonId = id.substr(0, id.length-7)+"like";
    $("#"+dislikeButtonId).attr("disabled", "disabled");

    $.ajax({
        type: "POST",
        url: "voting",
        data: {button: id},
        dataType: "application/json"
    });

}

$(function() {
    $(".btn-thumb-up").click(voteFor);
    $(".btn-thumb-down").click(voteAgainst);
});


$.getJSON("voting", function (data) {
  $.each(data, function (key, val) {
      $("#" + key).text(val);
  });
});
