package com.rj.processing;

import msafluid.MSAFluidSolver2D;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import com.rj.processing.mt.MTCallback;
import com.rj.processing.mt.MTManager;
import com.rj.processing.pong.Ball;
import com.rj.processing.pong.Game;

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
	    mtManager = new MTManager(this);
	    
	    //GAME CODE
	    g = new Game(this);
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
	
	public void touchEvent(MotionEvent me, int i, float x, float y, float vx,
			float vy, float size) {
		
		float velocityScale = 30f;
		float maxVel = 0.2f;
	//	float velocityScale = 30f;
	//	float maxVel = 0.2f;
		
		println(""+vx+","+vy);
	
		vx = vx * velocityScale;
		vy = vy * velocityScale;
		
	    fluid.addForce(this, x/width, y/height, vx/width, vy/height);
		this.x=x;
		this.y=y;
		this.x2=x+vx*100;
		this.y2=y+vy*100;
	}
	
	
	float x,y,x2,y2;
	
	public void draw() {
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

}
