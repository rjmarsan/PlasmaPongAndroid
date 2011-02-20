package com.teamwut.plasma.plasmapong.pong.objects;

import com.teamwut.plasma.plasmapong.pong.Const;

import msafluid.MSAFluidSolver2D;
import processing.core.PApplet;

public class Goals {
	final PApplet p;

	final float width;
	final float height;

	int goalBoarder = 70;
	int goalColor;

	public Goals(PApplet p) {
		this.p = p;
		this.width = p.width;
		this.height = p.height;
		goalColor = p.color(150, 150, 150, 150);
	}

	public void draw(PApplet p) {
		// for now
		p.stroke(goalColor);
		p.strokeWeight(3);
		p.line(goalBoarder, goalBoarder, goalBoarder, height - goalBoarder);
		p.line(width - goalBoarder, goalBoarder, width - goalBoarder, height
				- goalBoarder);
	}
	
	
	
	public int puckGoalStatus(Ball b) {
		if (b.x < goalBoarder) {
			return Const.PLAYER_1;
		} else if (b.x > width - goalBoarder) {
			return Const.PLAYER_2;
		} else {
			return Const.NO_PLAYER;
		}
	}
	
}
