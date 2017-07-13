package com.roboticseattle.socket.browser;

import javax.servlet.annotation.WebServlet;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@WebServlet(urlPatterns="/browser")
public class BrowserWebSocketServlet extends WebSocketServlet{
	@Override
	public void configure(WebSocketServletFactory factory) {
		
	      factory.register(BrowserWebSocket.class);
		
	}

}
