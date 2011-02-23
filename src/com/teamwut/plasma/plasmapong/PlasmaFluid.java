package com.teamwut.plasma.plasmapong;

import msafluid.MSAFluidSolver2D;
import processing.core.PApplet;
import processing.core.PImage;

public class PlasmaFluid {
	PApplet p;
	
	final float FLUID_WIDTH = 60;
	
	public final MSAFluidSolver2D fluidSolver;
	
	PImage imgFluid;
	boolean touchupdated = false;
	
	boolean random = false;
	
	public PlasmaFluid(PApplet p) {
		this.p = p;
	    // create fluid and set options
		if (p.width < p.height) {
		    fluidSolver = new MSAFluidSolver2D((int)(FLUID_WIDTH * p.width/p.height), (int)(FLUID_WIDTH));
		} else {
			fluidSolver = new MSAFluidSolver2D((int)(FLUID_WIDTH), (int)(FLUID_WIDTH * p.height/p.width));
		}
	    setupFluid();
	
	    // create image to hold fluid picture
	    imgFluid = p.createImage(fluidSolver.getWidth(), fluidSolver.getHeight(), PApplet.RGB);
	}
	
	public void setupFluid() {
		  fluidSolver.enableRGB(true).setFadeSpeed(0.01f).setDeltaT(0.5f).setVisc(0.0001f).setSolverIterations(3);
		  //fluidSolver.enableRGB(true).setFadeSpeed(0.01f).setDeltaT(1).setVisc(1).setSolverIterations(5);
	}
	
	
	public void setRandomness(boolean random) {
		this.random = random;
	}
	
	
	public void draw(PApplet p) {
		draw(p, true);
	}
	
	int r,g,b;
	public void draw(PApplet p, boolean stepforward) {
//	    p.colorMode(PApplet.RGB, 1);  

	    if (stepforward) fluidSolver.update();
	    
	    imgFluid.loadPixels();
	    int cellcount = fluidSolver.getNumCells();
	    for(int i=0; i<cellcount; i++) { //optimize here.
//	        imgFluid.pixels[i] = p.color(fluidSolver.r[i], fluidSolver.g[i], fluidSolver.b[i]);
            r=(int)(fluidSolver.r[i]*255);
            g=(int)(fluidSolver.g[i]*255);
            b=(int)(fluidSolver.b[i]*255);
            if (r > 255) r = 255; else if (r < 0) r = 0;
            if (g > 255) g = 255; else if (g < 0) g = 0;
            if (b > 255) b = 255; else if (b < 0) b = 0;

            imgFluid.pixels[i] = 0xff000000 | (r << 16) | (g << 8) | b;
	    }  
	    imgFluid.updatePixels();//  fastblur(imgFluid, 2);
	    
	    p.image(imgFluid, 0, 0, p.width, p.height);
	
//	    p.colorMode(PApplet.RGB, 255);  
	
	}
	
	public void addForce(PApplet p, float x, float y, float dx, float dy) {
		this.addForce(p, x, y, dx, dy, 0);
	}
	
	public void addForce(PApplet p, float x, float y, float dx, float dy, float colorOffset) {
		this.addForce(p, x, y, dx, dy, colorOffset, 4);
	}


	// add force and dye to fluid, and create particles
	public void addForce(PApplet p, float x, float y, float dx, float dy, float colorOffset, float colorMult) {
	        float velocityMult = 30.0f;
	
//	        if (dx > 1) dx = 1;
//	        if (dy > 5) dy = 1;
	
	        int drawColor;
	
	        p.colorMode(PApplet.HSB, 360, 1, 1);
	        float hue = (p.frameCount/10 + colorOffset) % 360;
	        if (!random) {
//		        if (y < 0.5f)
//		        	hue = (hue + 135) % 360;
	        } else {
	        	hue = (((x+10) * (y+10))*10 + p.frameCount/10 + colorOffset) % 360;
	        }
	        drawColor = p.color(hue, 1, 1);
	        p.colorMode(PApplet.RGB, 1);  
//	        for (int i=0; i<3; i++) {
//	        	for (int j=0; j<1; j++) {
//		        	int index = fluidSolver.getIndexForNormalizedPosition(x+.01f*i, y+.01f*j);
//			        fluidSolver.rOld[index]  += p.red(drawColor) * colorMult;
//			        fluidSolver.gOld[index]  += p.green(drawColor) * colorMult;
//			        fluidSolver.bOld[index]  += p.blue(drawColor) * colorMult;
//			
//			        fluidSolver.uOld[index] += dx * velocityMult;
//			        fluidSolver.vOld[index] += dy * velocityMult;
//	        	}
//	        }
	        
        	int index = fluidSolver.getIndexForNormalizedPosition(x, y);
	        fluidSolver.rOld[index]  += p.red(drawColor) * colorMult;
	        fluidSolver.gOld[index]  += p.green(drawColor) * colorMult;
	        fluidSolver.bOld[index]  += p.blue(drawColor) * colorMult;
	
	        fluidSolver.uOld[index] += dx * velocityMult;
	        fluidSolver.vOld[index] += dy * velocityMult;

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
