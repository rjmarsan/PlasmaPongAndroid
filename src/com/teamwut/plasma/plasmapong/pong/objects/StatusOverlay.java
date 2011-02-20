package com.teamwut.plasma.plasmapong.pong.objects;

import processing.core.PApplet;

import com.teamwut.plasma.plasmapong.pong.Game;

public class StatusOverlay extends PObject {

	public StatusOverlay(PApplet p) {
		super(p);
	}
	
	public void draw(PApplet p, Game g) {
		p.pushStyle();
		
		p.stroke(255);
		p.fill(150);
		
		p.rectMode(PApplet.CORNER);
		if (g.mode == Game.PREGAME_WAIT) {
			p.rect(width/2, height/2, g.modeFrameCountdown*3, 30);
		}
		
		else if (g.mode == Game.JUST_SCORED) {
			p.rect(width/2, height/2, g.modeFrameCountdown*3, 30);
		}
		else if (g.mode == Game.JUST_SCORED_WAIT) {
			p.rect(width/2, height/2, g.modeFrameCountdown*3, 30);
		}
		
		else if (g.mode == Game.GAME_OVER) {
			p.rect(width/2, height/2, g.modeFrameCountdown*3, 30);
		}
		
		p.popStyle();
	}


}
