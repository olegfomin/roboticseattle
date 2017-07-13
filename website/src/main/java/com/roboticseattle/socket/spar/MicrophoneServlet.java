package com.roboticseattle.socket.spar;

import javax.servlet.annotation.WebServlet;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@WebServlet(urlPatterns="/spar/microphone")
public class MicrophoneServlet extends WebSocketServlet {

	private static final long serialVersionUID = -3215014122034072049L;

	public MicrophoneServlet() {
	}

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.register(MicrophoneFeedSocket.class);
	}

}
