package com.teamwut.plasma.plasmapong.pong.objects;

import processing.core.PApplet;
import android.content.Intent;

import com.teamwut.plasma.plasmapong.PlasmaFluid;
import com.teamwut.plasma.plasmapong.pong.Game;

/**
 * Watson AI
 * @author Jake McGinty <mcginty@illinois.edu>
 *
 * Currently a completely non-intelligent AI that shoots balls back when needed. Pretty damn stupid.
 */
public class WatsonAI {
	Game parent;
	PlasmaFluid fluid;
	Ball ball;
	
	public WatsonAI(Game parent, PlasmaFluid fluid, Ball ball) {
		this.parent = parent;
		this.fluid = fluid;
		this.ball = ball;
	}
	
	public void thinkAndMove(PApplet p) {
		if (ball.y < p.height / 2) {
			fluid.addForce(p, ball.x/p.width, (ball.y)/p.height, 0, 1.0f);
		}
	}
}
