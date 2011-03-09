package com.teamwut.plasma.plasmapong;

import java.util.ArrayList;

import processing.core.PApplet;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.teamwut.plasma.plasmapong.mt.Cursor;
import com.teamwut.plasma.plasmapong.mt.MTCallback;
import com.teamwut.plasma.plasmapong.mt.MTManager;
import com.teamwut.plasma.plasmapong.pong.Const;
import com.teamwut.plasma.plasmapong.pong.Drawbl;
import com.teamwut.plasma.plasmapong.pong.Game;

public class PlasmaPong extends PApplet implements MTCallback {
	public final static String PLAYER_KEY = PlasmaPong.class+"PLAYER";
	public final static String ONE_PLAYER_PLAY = "ONE_PLAYER_PLAY";
	public final static String TWO_PLAYER_PLAY = "TWO_PLAYER_PLAY";

	public int sketchWidth() { return this.screenWidth; }
	public int sketchHeight() { return this.screenHeight; }
	public String sketchRenderer() { return Const.RENDER_MODE; }
	public boolean sketchTranslucency() { return false;  }

	PlasmaFluid fluid;
	
	MTManager mtManager;
		
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
	    // use OPENGL rendering for bilinear filtering on texture
	    //size(screen.width * 49/50, screen.height * 49/50, OPENGL);
	    //hint( ENABLE_OPENGL_4X_SMOOTH );    // Turn on 4X antialiasing
		hint(DISABLE_DEPTH_TEST);
		hint(DISABLE_OPENGL_ERROR_REPORT);
		smooth();
	    frameRate(40);
	
	    fluid = Drawbl.getFluidSim(this);
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
	    initPong(); 
	    
	    debug();
	}
	
	
	public void debug() {
		  // Place this inside your setup() method
		  final DisplayMetrics dm = new DisplayMetrics();
		  getWindowManager().getDefaultDisplay().getMetrics(dm);
		  final float density = dm.density; 
		  final int densityDpi = dm.densityDpi;
		  println("density is " + density); 
		  println("densityDpi is " + densityDpi);
		  println("HEY! the screen size is "+width+"x"+height);
	}
	
	
	//mt version
	public boolean surfaceTouchEvent(final MotionEvent me) {
		if (mtManager != null) mtManager.surfaceTouchEvent(me);
		return super.surfaceTouchEvent(me);
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
			println("Redirecting!");
			final float diffx = (targetx-x)/width;
			final float diffy = Math.abs((targety-y)/height);
			final float diffangle = (float)Math.atan2(diffy, diffx);
			vx = (float) Math.cos(diffangle)*30;
			vy = (float) Math.sin(diffangle)*30;
		}
		
		if (y/height > 0.5f) 
			vy = -vy;
		
		if (y / height > 0.5f) {
			fluid.addForce(this, x/width, y/height, vx/width, vy/height, Const.PLAYER_1_OFFSET);
			fluid.addForce(this, (x+5)/width, y/height, -vy/width/4, vy/height/2, Const.PLAYER_1_OFFSET);
			fluid.addForce(this, (x-5)/width, y/height, vy/width/4, vy/height/2, Const.PLAYER_1_OFFSET);
		}
		else {
			fluid.addForce(this, x/width, y/height, vx/width, vy/height, Const.PLAYER_2_OFFSET);
			fluid.addForce(this, (x+5)/width, y/height, vy/width/4, vy/height/2, Const.PLAYER_2_OFFSET);
			fluid.addForce(this, (x-5)/width, y/height, -vy/width/4, vy/height/2, Const.PLAYER_2_OFFSET);
		}
	}
	
	public void updateCursors() {
		final ArrayList<Cursor> cursors = (ArrayList<Cursor>) mtManager.cursors.clone();
		for (final Cursor c : cursors ) {
			if (c != null && c.currentPoint != null)
				addForce(c.currentPoint.x, c.currentPoint.y, g.getBall().x, g.getBall().y);
		}
	}
	
	
	public void draw() {
		updateCursors();
		
	    background(0);
	    fluid.draw(this, !paused);
	    drawPong();
	    
	    if (this.frameCount % 60 == 0) println(this.frameRate+"");
	}

	public void initPong() {
		g.initPong();
	}

	public void drawPong() {
		g.drawPong(!paused);
	}
	@Override
	public void touchEvent(final MotionEvent me, final int i, final float x, final float y, final float vx,
			final float vy, final float size) {
	}
	
	public void onBackPressed() {
//		if (paused) unpause();
	}
	
	public void keyPressed() {
//		keyCode = 0; // don't quit by default
		// doing other things here, and then:
		if (key == CODED && keyCode == KeyEvent.KEYCODE_BACK) {
			if (paused) {
				keyCode = 0; // don't quit by default
				this.runOnUiThread(new Runnable() {
				public void run() {unpause();}
				});
			}
		}
	}
}
