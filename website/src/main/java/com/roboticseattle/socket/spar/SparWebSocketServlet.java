package com.roboticseattle.socket.spar;

import javax.servlet.annotation.WebServlet;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@WebServlet(urlPatterns="/device")
public class SparWebSocketServlet extends WebSocketServlet {
	@Override
	public void configure(WebSocketServletFactory factory) {
	      factory.register(SparWebSocket.class);
	}
}
