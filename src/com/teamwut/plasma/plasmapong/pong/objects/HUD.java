package com.teamwut.plasma.plasmapong.pong.objects;

import processing.core.PApplet;

public class HUD extends PObject {
	
	int p1score;
	int p2score;

	public HUD(PApplet p) {
		super(p);
	}
	
	public void draw(PApplet p) {
		
	}
	
	public void setP1Score(int p1) {
		this.p1score = p1;
	}
	
	public void setP2Score(int p2) {
		this.p2score = p2;
	}

}
