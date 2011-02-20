package com.teamwut.plasma.plasmapong;

import java.util.ArrayList;

import processing.core.PApplet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import com.teamwut.plasma.plasmapong.mt.Cursor;
import com.teamwut.plasma.plasmapong.mt.MTCallback;
import com.teamwut.plasma.plasmapong.mt.MTManager;
import com.teamwut.plasma.plasmapong.pong.Game;

public class PlasmaPong extends PApplet implements MTCallback {


	public int sketchWidth() { return this.screenWidth; }
	public int sketchHeight() { return this.screenHeight; }
	public String sketchRenderer() { return PApplet.OPENGL; }

	PlasmaFluid fluid;
	
	MTManager mtManager;
	
	boolean evenframe=true;
	
	Game g;
	
	public void setup() {
	    // use OPENGL rendering for bilinear filtering on texture
	    //size(screen.width * 49/50, screen.height * 49/50, OPENGL);
	    //hint( ENABLE_OPENGL_4X_SMOOTH );    // Turn on 4X antialiasing
		hint(DISABLE_DEPTH_TEST);
		hint(DISABLE_OPENGL_ERROR_REPORT);
	    frameRate(60);
	
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
		if (x/width > 0.5f) 
			vx = -50;
		else 
			vx = 40;
		vy = 0;
		
	    fluid.addForce(this, x/width, y/height, vx/width, vy/height);
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
	
	    fluid.draw(this);
	
	    drawPong();
	    
	    if (this.frameCount % 60 == 0) println(this.frameRate+"");
	
	}

	public void initPong() {
		g.initPong();
	}

	public void drawPong() {
		g.drawPong();
	}
	@Override
	public void touchEvent(MotionEvent me, int i, float x, float y, float vx,
			float vy, float size) {
		// TODO Auto-generated method stub
		
	}

}
