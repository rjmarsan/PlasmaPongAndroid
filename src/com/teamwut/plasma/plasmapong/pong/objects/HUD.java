package com.teamwut.plasma.plasmapong.pong.objects;

import com.teamwut.plasma.plasmapong.pong.Game;

import processing.core.PApplet;
import processing.core.PFont;

public class HUD extends PObject {
	
	int p1score;
	int p2score;
	
	PFont font;
	

	public HUD(PApplet p) {
		super(p);
		font = p.loadFont("Courier10PitchBT-Bold-25.vlw");
	}
	
	public void draw(PApplet p) {
		// Player 1
		p.pushMatrix();
		p.pushStyle();
		p.translate(p.width/2-50, 50);
		p.rotate(PApplet.PI);
	    p.colorMode(p.RGB, 255);
	    p.fill(255, 0, 0);
	    p.textFont(font, 25);
	    p.fill(255);
	    p.text(p1score, 0, 0);
	    p.popStyle();
	    p.popMatrix();
	    
	    
		p.pushMatrix();
		p.pushStyle();
		p.translate(p.width/2+50, 50);
	    p.colorMode(p.RGB, 255);
	    p.rotate(PApplet.PI);
	    p.fill(255, 0, 0);
	    p.textFont(font, 25);
	    p.fill(255);
	    p.text(p2score, 0, 0);
	    p.popStyle();
	    p.popMatrix();
	}
	
	public void setP1Score(int p1) {
		this.p1score = p1;
	}
	
	public void setP2Score(int p2) {
		this.p2score = p2;
	}

}
