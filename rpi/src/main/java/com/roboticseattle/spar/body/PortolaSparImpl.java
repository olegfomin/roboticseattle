package com.roboticseattle.spar.body;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class PortolaSparImpl implements Portola {
	
	private SerialPort serialPort;

	@Override
	public synchronized SerialPort getUART() throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
		if(serialPort == null) {
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier( "/dev/ttyS80" );
			if(portIdentifier.isCurrentlyOwned()) throw new PortInUseException();
			CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
			serialPort = ( SerialPort )commPort;
	        serialPort.setSerialPortParams( 19200,
	                SerialPort.DATABITS_8,
	                SerialPort.STOPBITS_1,
	                SerialPort.PARITY_NONE );
		}    
        return serialPort;
	}
	

}
