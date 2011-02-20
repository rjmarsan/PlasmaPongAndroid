package com.teamwut.plasma.plasmapong.pong.bots;

import processing.core.PApplet;
import processing.core.PConstants;
import android.content.Intent;

import com.teamwut.plasma.plasmapong.PlasmaFluid;
import com.teamwut.plasma.plasmapong.pong.Game;
import com.teamwut.plasma.plasmapong.pong.objects.Ball;

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
	
	float target_x=0, target_y=0, x=-1, y=-1;
	
	static final float MAX_FORCE = 0.10f;
	static final float MAX_VELOCITY_Y = 8.0f;
	static final float MAX_VELOCITY_X = 8.0f; // pixels per frame
	static final float DESIRED_DIST_BEHIND = 120;
	
	public WatsonAI(Game parent, PlasmaFluid fluid, Ball ball) {
		this.parent = parent;
		this.fluid = fluid;
		this.ball = ball;
	}
	
	public void thinkAndMove(PApplet p) {
		// initial frame use only
		if (x == -1) x = p.width/2;
		if (y == -1) y = p.height/3;
		
		target_x = ball.x;
		target_y = ball.y - DESIRED_DIST_BEHIND; //want to get behind the ball
		
		float diff_x = target_x - x;
		float diff_y = target_y - y;
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
		
		if (ball.y < p.height / 2.5) {
			fluid.addForce(p, x/p.width, y/p.height, 0, MAX_FORCE);
		}
	}
}
