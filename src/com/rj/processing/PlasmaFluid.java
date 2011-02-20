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

public class PlasmaFluid {
	PApplet p;
	
	final float FLUID_WIDTH = 60;
	
	public MSAFluidSolver2D fluidSolver;
	
	PImage imgFluid;
	boolean touchupdated = false;
	
	public PlasmaFluid(PApplet p) {
		this.p = p;
	    // create fluid and set options
	    fluidSolver = new MSAFluidSolver2D((int)(FLUID_WIDTH), (int)(FLUID_WIDTH * p.height/p.width));
	    setupFluid();
	
	    // create image to hold fluid picture
	    imgFluid = p.createImage(fluidSolver.getWidth(), fluidSolver.getHeight(), PApplet.RGB);
	    
	}
	
	public void setupFluid() {
		  fluidSolver.enableRGB(true).setFadeSpeed(0.01f).setDeltaT(0.5f).setVisc(0.0001f).setSolverIterations(3);
		  //fluidSolver.enableRGB(true).setFadeSpeed(0.01f).setDeltaT(1).setVisc(1).setSolverIterations(5);
	}
	
	
	
	public void draw(PApplet p) {
	    p.colorMode(PApplet.RGB, 1);  

	    fluidSolver.update();
	    
	    imgFluid.loadPixels();
	    int cellcount = fluidSolver.getNumCells();
	    for(int i=0; i<cellcount; i++) { //optimize here.
	        imgFluid.pixels[i] = p.color(fluidSolver.r[i], fluidSolver.g[i], fluidSolver.b[i]);
	    }  
	    imgFluid.updatePixels();//  fastblur(imgFluid, 2);
	    
	    p.image(imgFluid, 0, 0, p.width, p.height);
	
	    p.colorMode(PApplet.RGB, 255);  
	
	}
	
	
	// add force and dye to fluid, and create particles
	public void addForce(PApplet p, float x, float y, float dx, float dy) {
	        float colorMult = 5;
	        colorMult=colorMult*y;
	        float velocityMult = 30.0f;
	
	        if (dx > 1) dx = 1;
	        if (dy > 5) dy = 1;
	
	        int drawColor;
	
	        p.colorMode(PApplet.HSB, 360, 1, 1);
	        float hue = ((x + y) * 180 + p.frameCount) % 360;
	        if (x < 0.5f)
	        	hue = 0;
	        else
	        	hue = 180;
	        drawColor = p.color(hue, 1, 1);
	        p.colorMode(PApplet.RGB, 1);  
	        for (int i=0; i<3; i++) {
	        	for (int j=0; j<1; j++) {
		        	int index = fluidSolver.getIndexForNormalizedPosition(x+.01f*i, y+.01f*j);
			        fluidSolver.rOld[index]  += p.red(drawColor) * colorMult;
			        fluidSolver.gOld[index]  += p.green(drawColor) * colorMult;
			        fluidSolver.bOld[index]  += p.blue(drawColor) * colorMult;
			
			        fluidSolver.uOld[index] += dx * velocityMult;
			        fluidSolver.vOld[index] += dy * velocityMult;
	        	}
	        }
	        //experimental code. interpolate between points.
	        //convert back to normal
	//        x = x * width;
	//        y = y * height;
	//        dx = dx * width/30f * 1000f/this.frameRate; //the 30 is a hack because this was usuallly called with dx * 30
	//        dy = dy * height/30f * 1000f/this.frameRate;
	//        
	//        float steps = (float)Math.sqrt(dx * dx + dy * dy);
	//        float stepX = dx/steps;
	//        float stepY = dy/steps;
	//
	//        float vx = x-dx;
	//        float vy = y-dy;
	//        for (float i=0; i < steps; i += 1f ) {
	//        	vx += stepX;
	//        	vy += stepY;
	//        	int index = fluidSolver.getIndexForNormalizedPosition(vx/width, vy/height);
	//	        fluidSolver.rOld[index]  += red(drawColor) * colorMult;
	//	        fluidSolver.gOld[index]  += green(drawColor) * colorMult;
	//	        fluidSolver.bOld[index]  += blue(drawColor) * colorMult;
	//	
	//	        fluidSolver.uOld[index] += dx/width * 1f;
	//	        fluidSolver.vOld[index] += dy/height * 1f;
	//        }        
	
	}
	


}
