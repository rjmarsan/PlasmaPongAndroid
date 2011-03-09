package com.teamwut.plasma.plasmapong.pong.bots;

import processing.core.PApplet;

import com.teamwut.plasma.plasmapong.PlasmaFluid;
import com.teamwut.plasma.plasmapong.pong.Game;
import com.teamwut.plasma.plasmapong.pong.objects.Ball;

/**
 * Watson AI
 * @author Jake McGinty <mcginty@illinois.edu>
 *
 * Currently a completely non-intelligent AI that shoots balls back when needed. Pretty damn stupid.
 */
public abstract class Bot {
	Game parent;
	PlasmaFluid fluid;
	Ball ball;
		
	public Bot(final Game parent, final PlasmaFluid fluid, final Ball ball) {
		this.parent = parent;
		this.fluid = fluid;
		this.ball = ball;
	}
	
	public abstract void thinkAndMove(PApplet p);
	public void setup(final PApplet p) {}
	
	public void youJustLost() {}
}
