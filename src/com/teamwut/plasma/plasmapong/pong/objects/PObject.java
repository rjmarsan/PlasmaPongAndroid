package com.teamwut.plasma.plasmapong.pong.objects;

import processing.core.PApplet;

import com.teamwut.plasma.plasmapong.PActivity;

public class PObject {
	final PActivity p;
	final float width;
	final float height;

	public PObject(final PActivity p) {
		this.p = p;
		this.width = p.width;
		this.height = p.height;
	}
	
	public void draw(final PActivity p) {
		
	}
}
