package com.teamwut.plasma.plasmapong.pong.objects;

import processing.core.PApplet;

import com.teamwut.plasma.plasmapong.pong.Const;
import com.teamwut.plasma.plasmapong.pong.Game;

public class StatusOverlay extends PObject {

	public StatusOverlay(PApplet p) {
		super(p);
	}
	
	public void draw(PApplet p, Game g) {
		p.pushStyle();
		
		p.stroke(255);
		p.fill(150);
		p.ellipseMode(PApplet.CENTER);
		
		p.rectMode(PApplet.CORNER);
		if (g.mode == Game.PREGAME_WAIT || g.mode == Game.JUST_SCORED || g.mode == Game.JUST_SCORED_WAIT) {
			int count = g.modeFrameCountdown;
			float stepsize = width/6;
			float size = width/6.5f;
			if (count > (Const.PREGAME_WAIT_COUNT * 2 / 3)) {
				p.ellipse(stepsize, height/2, size, size);
				p.ellipse(stepsize*5, height/2, size, size);
			}
			if (count > (Const.PREGAME_WAIT_COUNT / 3)) {
				p.ellipse(stepsize*4, height/2, size, size);
				p.ellipse(stepsize*2, height/2, size, size);
			}
			p.ellipse(stepsize*3, height/2, size, size);
		}
		
		else if (g.mode == Game.GAME_OVER) {
			p.stroke(255);
			p.fill(255);
			p.rect(width/2, height/2, g.modeFrameCountdown*3, 30);
		}
		
		p.popStyle();
	}


}
