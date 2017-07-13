package com.roboticseattle.spar.mind;

import com.roboticseattle.common.Song;

public interface MouthNerve extends Nerve {
	
	void onSongComplete(Song song);
	void onSpeechComplete(String text);

}
