package com.teamwut.plasma.plasmapong;

import java.util.ArrayList;

import processing.core.PApplet;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.teamwut.plasma.plasmapong.mt.Cursor;
import com.teamwut.plasma.plasmapong.mt.MTCallback;
import com.teamwut.plasma.plasmapong.mt.MTManager;
import com.teamwut.plasma.plasmapong.pong.Game;

public class PlasmaPong extends PApplet implements MTCallback {
	public final static String PLAYER_KEY = PlasmaPong.class+"PLAYER";
	public final static String ONE_PLAYER_PLAY = "ONE_PLAYER_PLAY";
	public final static String TWO_PLAYER_PLAY = "TWO_PLAYER_PLAY";

	public int sketchWidth() { return this.screenWidth; }
	public int sketchHeight() { return this.screenHeight; }
	public String sketchRenderer() { return PApplet.OPENGL; }

	PlasmaFluid fluid;
	
	MTManager mtManager;
		
	Game g;
	boolean paused = false;
	
	View pauseoverlay;
	
	public void onCreate(Bundle savedinstance) {
		super.onCreate(savedinstance);
    	pauseoverlay = this.getLayoutInflater().inflate(com.teamwut.plasma.plasmapong.R.layout.pause_screen_on, null);
    	this.addContentView(pauseoverlay, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    	Button unpause = (Button) this.findViewById(com.teamwut.plasma.plasmapong.R.id.unpause);
    	unpause.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pauseoverlay.setVisibility(View.GONE);
				paused = false;
			}});
    	Button quit = (Button) this.findViewById(com.teamwut.plasma.plasmapong.R.id.quit);
    	quit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			}});
    	
    	pauseoverlay.setVisibility(View.GONE);
    	
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		paused = true;
		pauseoverlay.setVisibility(View.VISIBLE);
		return false;
	}

	
	public void setup() {
	    // use OPENGL rendering for bilinear filtering on texture
	    //size(screen.width * 49/50, screen.height * 49/50, OPENGL);
	    //hint( ENABLE_OPENGL_4X_SMOOTH );    // Turn on 4X antialiasing
		hint(DISABLE_DEPTH_TEST);
		hint(DISABLE_OPENGL_ERROR_REPORT);
	    frameRate(40);
	
	    fluid = new PlasmaFluid(this);
	    mtManager = new MTManager();
	    
	    //GAME CODE
	    g = new Game(this, fluid);
	    initPong(); 
	    
	    debug();
	}
	
	
	public void debug() {
		  // Place this inside your setup() method
		  DisplayMetrics dm = new DisplayMetrics();
		  getWindowManager().getDefaultDisplay().getMetrics(dm);
		  float density = dm.density; 
		  int densityDpi = dm.densityDpi;
		  println("density is " + density); 
		  println("densityDpi is " + densityDpi);
		  println("HEY! the screen size is "+width+"x"+height);
	}
	
	
	//mt version
	public boolean surfaceTouchEvent(MotionEvent me) {
		if (mtManager != null) mtManager.surfaceTouchEvent(me);
		return super.surfaceTouchEvent(me);
	}
	
	public void addForce(float x, float y) {
		float vx, vy;	
		
		vy = 30;
		if (y/height > 0.5f) 
			vy = -vy;

		vx = 0;
		if (y / height > 0.5f) {
			fluid.addForce(this, x/width, y/height, vx/width, vy/height, 0);
			fluid.addForce(this, (x+5)/width, y/height, -vy/width/4, vy/height/2, 0);
			fluid.addForce(this, (x-5)/width, y/height, vy/width/4, vy/height/2, 0);
		}
		else {
			fluid.addForce(this, x/width, y/height, vx/width, vy/height, 135);
			fluid.addForce(this, (x+5)/width, y/height, vy/width/4, vy/height/2, 135);
			fluid.addForce(this, (x-5)/width, y/height, -vy/width/4, vy/height/2, 135);
		}
	}
	
	public void updateCursors() {
		ArrayList<Cursor> cursors = (ArrayList<Cursor>) mtManager.cursors.clone();
		for (Cursor c : cursors ) {
			if (c != null && c.currentPoint != null)
				addForce(c.currentPoint.x, c.currentPoint.y);
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
	public void touchEvent(MotionEvent me, int i, float x, float y, float vx,
			float vy, float size) {
	}

}
