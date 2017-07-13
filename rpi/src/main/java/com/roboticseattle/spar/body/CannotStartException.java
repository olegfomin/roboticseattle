package com.roboticseattle.spar.body;

public class CannotStartException extends Exception {

	private static final long serialVersionUID = -1169108346194087659L;

	public CannotStartException() {
		super();
	}

	public CannotStartException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public CannotStartException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public CannotStartException(String arg0) {
		super(arg0);
	}

	public CannotStartException(Throwable arg0) {
		super(arg0);
	}

}
