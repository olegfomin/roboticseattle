package com.roboticseattle.spar.mind;

public interface EyeNerve extends Nerve {
	void onNewVisual(byte[] bytes, int offset, int len);
}
