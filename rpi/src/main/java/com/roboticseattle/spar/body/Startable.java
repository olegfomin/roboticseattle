package com.roboticseattle.spar.body;

import com.roboticseattle.spar.mind.Nerve;

public interface Startable {
	void start(Nerve nerve) throws CannotStartException;
}
