package com.roboticseattle.socket.spar;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class MicrophoneFeedSocket extends FeedSocket {

	public MicrophoneFeedSocket() {
	}

	@OnWebSocketMessage
	public void onBinaryData(Session session, byte buf[], int offset, int length) throws IOException {
		eBrain.onMicrophoneBinaryData(session, buf, offset, length);
	} 
	
}
