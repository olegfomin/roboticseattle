package com.roboticseattle.socket.spar;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;

import com.roboticseattle.mind.ElectronicBrain;

public class FeedSocket {

	protected ElectronicBrain eBrain;
	protected Session session;
	
	@OnWebSocketConnect
	public synchronized void onConnect(Session session) throws IOException {
		this.session = session;
		eBrain = ElectronicBrain.getBrain();
	}
	
	public synchronized void close(int status, String reason) {
		if(session != null) session.close(status, reason);
	}
}
