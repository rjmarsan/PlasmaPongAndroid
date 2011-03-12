package com.teamwut.plasma.plasmapong.pong.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
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
		if (Const.IS_PORTRAIT) {
			c.drawBitmap(bottom, width-bottom.getWidth(), height-bottom.getHeight(), paint);
			c.drawBitmap(middle, 0, height/2-middle.getHeight()/2, paint);
			c.drawBitmap(top, 0, 0, paint);
		} else {
			c.drawBitmap(top, width-bottom.getWidth(), height-bottom.getHeight(), paint);
			c.drawBitmap(middle, width/2-middle.getWidth()/2, 0,  paint);
			c.drawBitmap(bottom, 0, 0, paint);

		}
		
	}
	
	
	
	public int puckGoalStatus(final Ball b) {
		if (Const.IS_PORTRAIT) {
			if (b.y < goalBoarder) {
				return Const.PLAYER_1;
			} else if (b.y > height - goalBoarder) {
				return Const.PLAYER_2;
			} else {
				return Const.NO_PLAYER;
			}
		} else {
			if (b.x < goalBoarder) {
				return Const.PLAYER_1;
			} else if (b.x > width - goalBoarder) {
				return Const.PLAYER_2;
			} else {
				return Const.NO_PLAYER;
			}

		}
	}
	
}
