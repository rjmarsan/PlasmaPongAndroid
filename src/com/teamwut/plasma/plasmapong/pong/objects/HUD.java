package com.teamwut.plasma.plasmapong.pong.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.teamwut.plasma.plasmapong.PActivity;
import com.teamwut.plasma.plasmapong.pong.Const;

public class HUD extends PObject {
	
	int p1score;
	int p2score;
	
//	PFont font;
	
	Bitmap[] images = new Bitmap[8];
	Paint paint = new Paint();

	public HUD(final PActivity p) {
		super(p);
//		font = p.loadFont("AmericanTypewriter-48.vlw");
		for (int i=0;i<8;i++) {
			images[i] = p.loadImage("ScoreCirclePNGsSmall/score"+i+"-01.png");
		}
		
	}
	
	public void draw(final Canvas c) {
//		System.out.println("p1score: " + p1score + ", p2score: " + p2score);
		// Player 1
//		p.pushMatrix();
		c.save();
		
		if (Const.IS_PORTRAIT) {
			c.drawBitmap(images[p2score], 50, p.height/2-50, paint);
			c.rotate(180, width/2, height/2);
			c.drawBitmap(images[p1score], 50, p.height/2-50, paint);
		} else {
//			c.rotate(90, width/2, height/2);
			c.drawBitmap(images[p2score],p.width/2-50,  50,   paint);
			c.rotate(180, width/2, height/2);
			c.drawBitmap(images[p1score],  p.width/2-50,  50, paint);
		}
		c.restore();
	}
	
	public void setP1Score(final int p1) {
		this.p1score = p1;
	}
	
	public void setP2Score(final int p2) {
		this.p2score = p2;
	}

}
