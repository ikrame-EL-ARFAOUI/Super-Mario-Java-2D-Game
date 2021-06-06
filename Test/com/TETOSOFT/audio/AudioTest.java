package com.TETOSOFT.audio;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

//import static org.junit.Assert.assertTrue;
//import static org.junit.jupiter.api.Assertions.*;
//import junit.framework.*;
import org.junit.jupiter.api.Test;

// This is to test if the music is working.

public class AudioTest {
	
   
	@Test
	void test() {
    	 Audio[] music = new Audio[] {
    	            new Audio("/audio/ecrasePersonnage.wav"),
    	            new Audio("/audio/game-over.wav"),
    	            new Audio("/audio/saut.wav"),
    	            new Audio("/audio/partiePerdue.wav"),
    	            new Audio("/audio/piece.wav"),
    	            new Audio("/audio/power-up.wav"),
    	            new Audio("/audio/new-map.wav")
    	        };
    	        
    	        for (Audio s : music) {
    	        	assertTrue("WAV file '" + s.getClip() + "' could not be loaded", s.isPlaying() == false);
    	        }
    	        
	}

}
