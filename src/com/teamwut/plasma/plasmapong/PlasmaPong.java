package com.teamwut.plasma.plasmapong;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.teamwut.plasma.plasmapong.PActivity.UpdateThread;
import com.teamwut.plasma.plasmapong.mt.Cursor;
import com.teamwut.plasma.plasmapong.mt.MTManager;
import com.teamwut.plasma.plasmapong.pong.Const;
import com.teamwut.plasma.plasmapong.pong.Drawbl;
import com.teamwut.plasma.plasmapong.pong.Game;

public class PlasmaPong extends PActivity {
	public final static String PLAYER_KEY = PlasmaPong.class+"PLAYER";
	public final static String ONE_PLAYER_PLAY = "ONE_PLAYER_PLAY";
	public final static String TWO_PLAYER_PLAY = "TWO_PLAYER_PLAY";

	Game g;
	boolean paused = false;
	
	View pauseoverlay;
	
	public void onCreate(final Bundle savedinstance) {
		super.onCreate(savedinstance);
		
    	pauseoverlay = this.getLayoutInflater().inflate(com.teamwut.plasma.plasmapong.R.layout.pause_screen_on, null);
    	this.addContentView(pauseoverlay, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    	final Button unpause = (Button) this.findViewById(com.teamwut.plasma.plasmapong.R.id.unpause);
    	unpause.setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				unpause();
			}});
    	final Button quit = (Button) this.findViewById(com.teamwut.plasma.plasmapong.R.id.quit);
    	quit.setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				finish();
			}});
    	
    	pauseoverlay.setVisibility(View.GONE);
    	
	}
	
	public void dopause() {
		paused = true;
		pauseoverlay.setVisibility(View.VISIBLE);
	}
	
	public void unpause() {
		pauseoverlay.setVisibility(View.GONE);
		paused = false;
	}
	
	public void togglePause() {
		if (paused) unpause();
		else dopause();
	}

	
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		togglePause();
		return false;
	}

	
	public void setup() {
	    fluid = Drawbl.getFluidSim(width, height);
	    fluid.setRandomness(false);
	    mtManager = new MTManager();
	    
	    //GAME CODE
	    final Intent lastIntent = this.getIntent();
		final String playerKey = lastIntent.getStringExtra(PLAYER_KEY);
		int players = 0;
		if (playerKey.equals(ONE_PLAYER_PLAY))
			players = 1;
		else if (playerKey.equals(TWO_PLAYER_PLAY))
			players = 2;
			g = new Game(this, fluid, players);
//	    initPong(); UNDO
			
		    new UpdateThread().start();

	    
	}
	
	
	
	
	public void addForce(final float x, final float y) {
		addForce(x,y,x,y);
	}
	public void addForce(final float x, final float y, final float targetx, final float targety) {
		float vx, vy;	
		
		vy = 30;

		vx = 0;
		
		final float distancesqrt = (x-targetx)*(x-targetx) + (y-targety)*(y-targety);
		if (distancesqrt < 30010) {
			final float diffx = (targetx-x)/width;
			final float diffy = Math.abs((targety-y)/height);
			final float diffangle = (float)Math.atan2(diffy, diffx);
			vx = (float) Math.cos(diffangle)*30;
			vy = (float) Math.sin(diffangle)*30;
		}
		
		if (y/height > 0.5f) 
			vy = -vy;
		
		if (y / height > 0.5f) {
			fluid.addForce(x/width, y/height, vx/width, vy/height, Const.PLAYER_1_OFFSET);
			fluid.addForce((x+5)/width, y/height, -vy/width/4, vy/height/2, Const.PLAYER_1_OFFSET);
			fluid.addForce((x-5)/width, y/height, vy/width/4, vy/height/2, Const.PLAYER_1_OFFSET);
		}
		else {
			fluid.addForce(x/width, y/height, vx/width, vy/height, Const.PLAYER_2_OFFSET);
			fluid.addForce((x+5)/width, y/height, vy/width/4, vy/height/2, Const.PLAYER_2_OFFSET);
			fluid.addForce((x-5)/width, y/height, -vy/width/4, vy/height/2, Const.PLAYER_2_OFFSET);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void updateCursors() {
		final ArrayList<Cursor> cursors = (ArrayList<Cursor>) mtManager.cursors.clone();
		for (final Cursor c : cursors ) {
			if (c != null && c.currentPoint != null)
				addForce(c.currentPoint.x, c.currentPoint.y, g.getBall().x, g.getBall().y);
		}
	}
	
	
	public void draw(Canvas c) {
		updateCursors();
		
		c.drawRGB(1,5,100);
//	    background(0);
	    fluid.draw(c);
//	    drawPong();
	    
//	    if (this.frameCount % 60 == 0) println(this.frameRate+"");
	}

	public void initPong() {
		g.initPong();
	}

	public void drawPong() {
		g.drawPong(!paused);
	}
	
	public void onBackPressed() {
		if (paused) {
			this.runOnUiThread(new Runnable() {
			public void run() {unpause();}
			});
		}
	}
	
}
