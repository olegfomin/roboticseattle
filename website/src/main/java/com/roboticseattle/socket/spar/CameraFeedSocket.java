package com.roboticseattle.socket.spar;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class CameraFeedSocket extends FeedSocket {
	
	@OnWebSocketMessage
	public void onBinaryData(Session session, byte buf[], int offset, int length) throws IOException {
		eBrain.onCameraBinaryData(session, buf, offset, length);
	} 

}
