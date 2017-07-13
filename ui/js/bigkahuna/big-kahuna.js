/**
 * Communications with roboticseatte.com via web-socket
 * Created by Robotic Seattle on 6/1/2016.
 */

function BigKahuna(address) {
    var webSocket;

    this.start = function() {
        webSocket = new WebSocket(address);

        webSocket.onmessage = function (message) {
            electronicbrain.onMessage(message.data);
        }

        webSocket.onclose = function () {
            electronicbrain.onConnectionLost();
        }
    }

    this.send = function(command) {
        webSocket.send(JSON.stringify(command));
    }

}

var bigkahuna = new BigKahuna(web_socket_addr);
