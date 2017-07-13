/**
 * Created by Robotic Seattle on 7/7/2016.
 */

$(function() {
    $("#ta-msg").focus(function() {
        if($("#ta-msg").val() == 'Please enter your text here' || $("#ta-msg").val() == 'Thanks for your message') $("#ta-msg").val('');
    });

    $("#btn-msg-submit").click(function() {
        if($("#ta-msg").val().trim() != '') {
          $.ajax({
             type: "POST",
             url: "msg",
             data: {txt: $("#ta-msg").val().trim()},
             dataType: "application/json"
          });
            $("#ta-msg").val('Thanks for your message');
        }
    } );


});
