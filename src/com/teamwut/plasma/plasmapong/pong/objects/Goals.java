package com.teamwut.plasma.plasmapong.pong.objects;

import processing.core.PApplet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.teamwut.plasma.plasmapong.PActivity;
import com.teamwut.plasma.plasmapong.pong.Const;
import com.teamwut.plasma.plasmapong.pong.Drawbl;

public class Goals {
	final PActivity p;

	final float width;
	final float height;

	int goalBoarder = 80;
	int goalColor;

	Bitmap top;
	Bitmap bottom;
	Bitmap middle;
	Paint paint = new Paint();
	
	public Goals(final PActivity p) {
		this.p = p;
		this.width = p.width;
		this.height = p.height;
		goalColor = Color.argb(150, 150, 150, 150);
		
		top = Drawbl.getGoalTop(p);
		bottom = Drawbl.getGoalBottom(p);
		middle = Drawbl.getGoalMiddle(p);
	}

	public void draw(final Canvas c) {
		// for now
//		p.stroke(goalColor);
//		p.strokeWeight(3);
//		p.line(goalBoarder, goalBoarder, goalBoarder, height - goalBoarder);
//		p.line(width - goalBoarder, goalBoarder, width - goalBoarder, height
//				- goalBoarder);
//		p.imageMode(PApplet.CORNER);
//		p.image(top, 0, 0);
//		p.image(bottom, p.width-bottom.width, p.height-bottom.height);
//		p.imageMode(PApplet.CENTER);
//		p.image(middle, width/2, height/2);
//		p.imageMode(PApplet.CORNER);
		
		c.drawBitmap(bottom, width-bottom.getWidth(), height-bottom.getHeight(), paint);
		c.drawBitmap(middle, 0, height/2-middle.getHeight()/2, paint);
		c.drawBitmap(top, 0, 0, paint);
		
	}
	
	
	
	public int puckGoalStatus(final Ball b) {
		if (b.y < goalBoarder) {
			return Const.PLAYER_1;
		} else if (b.y > height - goalBoarder) {
			return Const.PLAYER_2;
		} else {
			return Const.NO_PLAYER;
		}
	}
	
}
