/**
 * Generates a command with a sequential number as an command id. It may also store the command generated into an
 * ACK map
 *
 * Created by Robotic Seattle on 6/1/2016.
 */

var ack={}; // HashMap for the command acknowledgements

var ComGenClass = function () {
    var counter = 0;

    function next(dest, isAck) {
        var command = {};
        command.id = counter;
        command.dest = dest;
        if(isAck) ack[counter] = command;
        counter++;
        return command;
    }

    return next;
}

var comgen = new ComGenClass();
