package com.TETOSOFT.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Audio {
	// VARIABLES	
	private Clip clip;
	private boolean Playing;
	
		
	// CONSTRUCTEUR
	public Audio(String son){
		
		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(getClass().getResource(son));
			clip = AudioSystem.getClip();
			clip.open(audio);
		} catch (Exception e) {}		
	}
					
	// GETTERS		
	public Clip getClip(){return clip;}
	
				
	// METHODES
	public void play(){
		Playing = true;
		clip.start();}
		
	public void stop(){
		Playing = false;
		clip.stop();}
	public boolean isPlaying () {
		return Playing;
	}
		
		
	public static void playSound(String son){
		Audio s = new Audio(son);
		s.play();
	}	
}