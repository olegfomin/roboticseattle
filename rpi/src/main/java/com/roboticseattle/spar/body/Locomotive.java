package com.roboticseattle.spar.body;

import java.io.IOException;
import java.io.OutputStream;

import com.roboticseattle.spar.mind.Nerve;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

public class Locomotive implements Startable {

	private Portola portola; 
	protected Nerve nerve;
	private OutputStream outputStream;

	public Locomotive(Portola portola) {
		this.portola = portola;
	}
	
	public void send(byte[] bytes) throws IOException {
		if(outputStream != null) {
			synchronized(portola) {
				outputStream.write(bytes);
				outputStream.flush();
			}
		} else {
			throw new IOException("The class has not been started");
		}
	}

	@Override
	public void start(Nerve nerve) throws CannotStartException {
		this.nerve = nerve;
		try {
			outputStream = portola.getUART().getOutputStream();
		} catch (NoSuchPortException | PortInUseException | UnsupportedCommOperationException | IOException e) {
			throw new CannotStartException(e);
		}
	}

}
