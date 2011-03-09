package com.teamwut.plasma.plasmapong.pong.objects;

import processing.core.PApplet;
import processing.core.PImage;

public class HUD extends PObject {
	
	int p1score;
	int p2score;
	
//	PFont font;
	
	PImage[] images = new PImage[8];
	

	public HUD(final PApplet p) {
		super(p);
//		font = p.loadFont("AmericanTypewriter-48.vlw");
		for (int i=0;i<8;i++) {
			images[i] = p.loadImage("ScoreCirclePNGsSmall/score"+i+"-01.png");
		}
	}
	
	public void draw(final PApplet p) {
//		System.out.println("p1score: " + p1score + ", p2score: " + p2score);
		// Player 1
		p.pushMatrix();
		p.pushStyle();
		p.imageMode(PApplet.CENTER);
		
		p.translate(50, p.height/2-50);
		p.rotate(PApplet.PI);
//	    p.colorMode(p.RGB, 255);
//	    p.fill(255, 0, 0);
//	    p.textFont(font, 48);
//	    p.fill(255);
//	    p.text(p2score, 0, 0);
		p.image(images[p2score], 0, 0);

		p.translate(-p.width+100, -100);
		p.rotate(PApplet.PI);
//	    p.fill(255, 0, 0);
//	    p.textFont(font, 48);
//	    p.fill(255);
//	    p.text(p1score, 0, 0);
		p.image(images[p1score], 0, 0);

	    p.popStyle();
	    p.popMatrix();
	}
	
	public void setP1Score(final int p1) {
		this.p1score = p1;
	}
	
	public void setP2Score(final int p2) {
		this.p2score = p2;
	}

}
