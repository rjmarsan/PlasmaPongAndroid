package com.teamwut.plasma.plasmapong.pong.objects;

import processing.core.PApplet;

import com.teamwut.plasma.plasmapong.pong.Game;

public class StatusOverlay extends PObject {

	public StatusOverlay(PApplet p) {
		super(p);
	}
	
	public void draw(PApplet p, Game g) {
		
		if (g.mode == Game.PREGAME_WAIT) {
			//draw pre-game overlay
		}
		
		else if (g.mode == Game.JUST_SCORED) {
			//draw just scored overlay
		}
		
		else if (g.mode == Game.GAME_OVER) {
			//draw game over dialog
		}
		
	}


}
