package com.teamwut.plasma.plasmapong.pong.objects;

import processing.core.PApplet;

public class PObject {
	final PApplet p;
	final float width;
	final float height;

	public PObject(PApplet p) {
		this.p = p;
		this.width = p.width;
		this.height = p.height;
	}
	
	public void draw(PApplet p) {
		
	}
}
