package com.teamwut.plasma.plasmapong.pong.objects;

import java.util.Random;

import processing.core.PApplet;

import com.teamwut.plasma.plasmapong.PActivity;
import com.teamwut.plasma.plasmapong.PlasmaFluid;
import com.teamwut.plasma.plasmapong.pong.Const;
import com.teamwut.plasma.plasmapong.pong.Game;

public class StatusOverlay extends PObject {
	Random r = new Random();
	public StatusOverlay(final PActivity p) {
		super(p);
	}
	
	int lastnum = 0;
	
	public void draw(final PApplet p, final Game g, final PlasmaFluid fluid) {
		p.pushStyle();
		
		p.stroke(255);
		p.strokeWeight(3);
		p.fill(150,150);
		p.ellipseMode(PApplet.CENTER);
		
		p.rectMode(PApplet.CORNER);
		if (g.mode == Game.PREGAME_WAIT || g.mode == Game.JUST_SCORED || g.mode == Game.JUST_SCORED_WAIT) {
			
			int numcircles = 0;
			
			
			final int count = g.modeFrameCountdown;
			if (count > (Const.PREGAME_WAIT_COUNT * 2 / 3)) {
				numcircles = 3;
			} else if (count > (Const.PREGAME_WAIT_COUNT / 3)) {
				numcircles = 2;
			} else {
				numcircles = 1;
			}

			final float stepsize = width/8;
			final float size = width/9f;
			if (numcircles > 2) {
				p.ellipse(stepsize*2, height/2, size*0.75f, size*0.75f);
				p.ellipse(stepsize*6, height/2, size*0.75f, size*0.75f);
			}
			if (numcircles > 1) {
				p.ellipse(stepsize*5, height/2, size*0.9f, size*0.9f);
				p.ellipse(stepsize*3, height/2, size*0.9f, size*0.9f);
			}
			p.ellipse(stepsize*4, height/2, size, size);
			
			if (numcircles == 2 && numcircles != lastnum) {
				fluid.addForce((stepsize*2)/width, 0.5f, -forceval*(1+r.nextFloat()), 0, Const.OTHER_OFFSET, colorval);
				fluid.addForce((stepsize*6)/width, 0.5f, forceval*(1+r.nextFloat()), 0, Const.OTHER_OFFSET, colorval);
			}
			if (numcircles == 1 && numcircles != lastnum) {
				fluid.addForce((stepsize*5)/width, 0.5f, 0, forceval*(1+r.nextFloat()), Const.OTHER_OFFSET, colorval);
				fluid.addForce((stepsize*3)/width, 0.5f, 0, -forceval*(1+r.nextFloat()), Const.OTHER_OFFSET, colorval);
			}
//			if (numcircles == 1 && count == 0) {
//				fluid.addForce(p, (stepsize*4.2f)/width, 0.5f, 0, forceval*(1+r.nextFloat()), Const.OTHER_OFFSET, colorval);
//				fluid.addForce(p, (stepsize*3.8f)/width, 0.5f, 0, -forceval*(1+r.nextFloat()), Const.OTHER_OFFSET, colorval);
//			}
			
			lastnum = numcircles;
		}
			
		else if (g.mode == Game.GAME_OVER) {
			p.stroke(255);
			p.fill(255);
			p.rect(width/2, height/2, g.modeFrameCountdown*3, 30);
		}
		
		p.popStyle();
	}
	final float forceval = 1;
	final float colorval = 100;



}
