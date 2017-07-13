/**
 * Motor mode and joystick controls
 *
 * Created by Robotic Seattle on 6/1/2016.
 */

var currentlyActive = $( "#btn-halt" );

var onMotorModeClick = function() {
    currentlyActive.removeClass(currentlyActive.attr("id")+"-a");
    currentlyActive.addClass(currentlyActive.attr("id"));
    $(this).removeClass($(this).attr("id"));
    $(this).addClass($(this).attr("id")+"-a");

    $( "#motor-control" ).removeClass("knob"+currentlyActive.attr("id").substring(3));
    $( "#motor-control" ).addClass("knob"+$(this).attr("id").substring(3));
    $( "#motor-control" ).css("left", $(this).attr("knob-x"));
    $( "#motor-control" ).css("top", $(this).attr("knob-y"));

    if($(this).attr("id") == "btn-halt") {
        $("#motor-control").hide();
    } else {
        $("#motor-control").show();
    }

    if($(this).attr("id") == "btn-rotate") {
        $( "#motor-control" ).css("height", "150px");
    } else {
        $( "#motor-control" ).css("height", "32px");
    }

    currentlyActive = $(this);
    electronicbrain.motors(0, 0);
};
$( "#btn-halt" ).click(onMotorModeClick);
$( "#btn-forward" ).click(onMotorModeClick);
$( "#btn-rotate" ).click(onMotorModeClick);
$( "#btn-back" ).click(onMotorModeClick);

$("#motor-control").draggable({ containment: "parent", scroll: false, cursor: "move", cursorAt: { top: 16, left: 16 },
    drag: function() {
        var $this = $(this);
        var thisPos = $this.position();
        var parentPos = $this.parent().position();

        var x = thisPos.left - parentPos.left;
        var y = thisPos.top - parentPos.top;

        var leftRight = Math.round(x)-141;
        var upDown = 120-Math.round(y);

        var rightMotor;
        var leftMotor;
        var behind;
        var ahead;
        if(currentlyActive.attr("id") == "btn-forward") {
            if(upDown < 2) {
                rightMotor = 0;
                leftMotor = 0;
            } else {
                rightMotor = Math.round(Math.pow(1.038, upDown)) + 10;
                leftMotor = rightMotor;
            }
            if(Math.abs(leftRight) > 2) {
                behind = Math.pow(1.032, Math.abs(leftRight));
                ahead =  Math.pow(1.019, Math.abs(leftRight));
                if (leftRight > 0) {
                    rightMotor = Math.round(rightMotor-rightMotor*behind/100.0);
                    leftMotor = Math.round(leftMotor+leftMotor*ahead/100.0);
                } else {
                    rightMotor = Math.round(rightMotor+rightMotor*ahead/100.0);
                    leftMotor = Math.round(leftMotor-leftMotor*behind/100.0);
                }
            }
        } else if(currentlyActive.attr("id") == "btn-back") {
            if(Math.abs(120-upDown) < 2) {
                rightMotor = 0;
                leftMotor = 0;
            } else {
                rightMotor = -Math.round(Math.pow(1.038, Math.abs(120-upDown))) - 11;
                leftMotor =  rightMotor;
            }

            if(Math.abs(leftRight) > 2) {
                behind = Math.pow(1.032, Math.abs(leftRight));
                ahead =  Math.pow(1.019, Math.abs(leftRight));
                if (leftRight > 0) {
                    rightMotor = Math.round(rightMotor-rightMotor*behind/100.0);
                    leftMotor = Math.round(leftMotor+leftMotor*ahead/100.0);
                } else {
                    rightMotor = Math.round(rightMotor+rightMotor*ahead/100.0);
                    leftMotor = Math.round(leftMotor-leftMotor*behind/100.0);
                }
            }
        } else if(currentlyActive.attr("id") == "btn-rotate") {
            var lr120 = Math.round(Math.pow(1.032, Math.abs(leftRight)));
            if(Math.abs(leftRight) > 3) {
                if(leftRight > 0) {
                    rightMotor = -lr120-13;
                    leftMotor  =  lr120+13;
                } else {
                    rightMotor =  lr120+13;
                    leftMotor  = -lr120-13;
                }
            } else {
                rightMotor = 0;
                leftMotor  = 0;
            }
        }
        electronicbrain.motors(leftMotor, rightMotor);
    }
});

function displayMotors(leftMotor, rightMotor) {
    $("#left-motor").text(leftMotor);
    $("#right-motor").text(rightMotor);
}

function displayMotorsHalt() {
    displayMotors(0, 0);
    currentlyActive.removeClass(currentlyActive.attr("id")+"-a");
    currentlyActive.addClass(currentlyActive.attr("id"));
    $("#btn-halt").removeClass("btn-halt");
    $("#btn-halt").addClass("btn-halt-a");
    $("#motor-control").hide();
    currentlyActive = $("#btn-halt");
}

function disMotors() {
    displayMotorsHalt();
    $( "#btn-halt" ).attr('disabled', 'disabled');
    $( "#btn-forward" ).attr('disabled', 'disabled');
    $( "#btn-rotate" ).attr('disabled', 'disabled');
    $( "#btn-back" ).attr('disabled', 'disabled');
}

function enMotors() {
    $( "#btn-halt" ).removeAttr("disabled");
    $( "#btn-forward" ).removeAttr("disabled");
    $( "#btn-rotate" ).removeAttr("disabled");
    $( "#btn-back" ).removeAttr("disabled");
    $( "#btn-forward" ).click();
}
