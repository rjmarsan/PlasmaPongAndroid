package com.teamwut.plasma.plasmapong.pong.bots;

import processing.core.PApplet;
import processing.core.PConstants;

import com.teamwut.plasma.plasmapong.PlasmaFluid;
import com.teamwut.plasma.plasmapong.pong.Const;
import com.teamwut.plasma.plasmapong.pong.Game;
import com.teamwut.plasma.plasmapong.pong.objects.Ball;

/**
 * Watson AI
 * @author Jake McGinty <mcginty@illinois.edu>
 *
 * Currently a completely non-intelligent AI that shoots balls back when needed. Pretty damn stupid.
 */
public class WatsonAI extends Bot {
	float target_x=-1, target_y=-1, x=-1, y=-1;
	
	static final float MAX_FORCE = 30;
	static final float MAX_VELOCITY = 20.0f; // pixels per frame
	static final float DESIRED_DIST_BEHIND = 160;
	
	public WatsonAI(final Game parent, final PlasmaFluid fluid, final Ball ball) {
		super(parent, fluid,ball);
	}
	
	public void youJustLost() {
		x = -1;
		y = -1;
		target_x = -1;
		target_y = -1;
	}
	
	public void thinkAndMove(final PApplet p) {
		// initial frame use only
		if (x == -1) x = p.width/2;
		if (y == -1) y = p.height/3;
		
		target_x = ball.x + ball.vx*2;
		target_y = ball.y + ball.vy*2 - DESIRED_DIST_BEHIND; //want to get behind the ball
		
		final float diff_x = target_x - x;
		final float diff_y = target_y - y;
		final float d_sqrd = diff_x*diff_x + diff_y*diff_y;
		final double angle = Math.atan2(Math.abs(diff_y), Math.abs(diff_x));
		if (d_sqrd < MAX_VELOCITY*MAX_VELOCITY) {
			x = target_x;
			y = target_y;
		}
		else {
			if (x < target_x) x += MAX_VELOCITY * Math.cos(angle);
			else if (x > target_x) x -= MAX_VELOCITY * Math.cos(angle);
			
			if (y < target_y) y += MAX_VELOCITY * Math.sin(angle);
			else if (y > target_y) y -= MAX_VELOCITY * Math.sin(angle);
		}
		
		if (y <= 75) y = 75;
		if (y > p.height-75) y = p.height-75; 
		
//		p.pushStyle();
		p.colorMode(PConstants.RGB);
		p.fill(255,0, 0);
		p.ellipseMode(PApplet.CENTER);
		p.ellipse(x, y, 5, 5);
		p.fill(255, 0, 0, 120);
		p.stroke(200);
		p.ellipse(x, y, 15, 15);
//		p.popStyle();
		
		if (y < p.height / 3) {
			fluid.addForce(p, (x-5)/p.width, y/p.height, 0, MAX_FORCE/p.height, Const.PLAYER_2_OFFSET);
			fluid.addForce(p, (x)/p.width, y/p.height, 0, MAX_FORCE/p.height, Const.PLAYER_2_OFFSET);
			fluid.addForce(p, (x+5)/p.width, y/p.height, 0, MAX_FORCE/p.height, Const.PLAYER_2_OFFSET);
		}
	}
}
