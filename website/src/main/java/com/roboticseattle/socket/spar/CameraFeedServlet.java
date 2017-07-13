package com.roboticseattle.socket.spar;

import javax.servlet.annotation.WebServlet;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@WebServlet(urlPatterns="/spar/camera")
public class CameraFeedServlet extends WebSocketServlet {

 	private static final long serialVersionUID = 5856635276920235414L;

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.register(CameraFeedSocket.class);
	}

}
