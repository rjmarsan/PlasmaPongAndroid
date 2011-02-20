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
		font = p.loadFont("AmericanTypewriter-48.vlw");
	}
	
	public void draw(PApplet p) {
		System.out.println("p1score: " + p1score + ", p2score: " + p2score);
		// Player 1
		p.pushMatrix();
		p.pushStyle();
		p.translate(50, p.height/2-50);
		p.rotate(PApplet.PI);
	    p.colorMode(p.RGB, 255);
	    p.fill(255, 0, 0);
	    p.textFont(font, 48);
	    p.fill(255);
	    p.text(p2score, 0, 0);

		p.translate(0, -100);
		p.rotate(PApplet.PI);
	    p.fill(255, 0, 0);
	    p.textFont(font, 48);
	    p.fill(255);
	    p.text(p1score, 0, 0);
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
