package com.roboticseattle.spar.body;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public interface Portola {
	
	SerialPort getUART() throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException;

}
