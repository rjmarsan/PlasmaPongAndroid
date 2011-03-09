package com.teamwut.plasma.plasmapong.pong.bots;

import processing.core.PApplet;
import processing.core.PConstants;

import com.teamwut.plasma.plasmapong.PlasmaFluid;
import com.teamwut.plasma.plasmapong.pong.Game;
import com.teamwut.plasma.plasmapong.pong.objects.Ball;

/**
 * Watson AI
 * @author Jake McGinty <mcginty@illinois.edu>
 *
 * Currently a completely non-intelligent AI that shoots balls back when needed. Pretty damn stupid.
 */
public class RevengeBot extends Bot {	
	float target_x=0, target_y=0, x=-1, y=-1;
	
	static final float MAX_FORCE = 30;
	static final float MAX_VELOCITY_Y = 2.0f;
	static final float MAX_VELOCITY_X = 4.0f; // pixels per frame
	static final float DESIRED_DIST_BEHIND = 160;
	
	public RevengeBot(final Game parent, final PlasmaFluid fluid, final Ball ball) {
			super(parent,fluid,ball);
	}
	
	public void thinkAndMove(final PApplet p) {
		// initial frame use only
		if (x == -1) x = p.width/2;
		if (y == -1) y = p.height/3;
		
		target_x = ball.x;
		target_y = ball.y - DESIRED_DIST_BEHIND; //want to get behind the ball
		
		final float diff_x = target_x - x;
		final float diff_y = target_y - y;
		if (diff_x < MAX_VELOCITY_X) {
			x = target_x;
		}
		else {
			if (x < target_x) x += MAX_VELOCITY_X;
			else if (x > target_x) x -= MAX_VELOCITY_X;
		}
		if (diff_y < MAX_VELOCITY_Y) {
			y = target_y;
		}
		else {
			if (y > target_y) y -= MAX_VELOCITY_Y;
			else if (y < target_y) y += MAX_VELOCITY_Y;
		}
		
		p.colorMode(PConstants.RGB);
		p.fill(255,0, 0);
		p.ellipse(x, y, 5, 5);
		
		if (y < p.height / 3) {
			fluid.addForce(p, (x-5)/p.width, y/p.height, 0, MAX_FORCE/p.height);
			fluid.addForce(p, (x)/p.width, y/p.height, 0, MAX_FORCE/p.height);
			fluid.addForce(p, (x+5)/p.width, y/p.height, 0, MAX_FORCE/p.height);
		}
	}
}
