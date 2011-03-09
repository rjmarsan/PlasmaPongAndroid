package com.teamwut.plasma.plasmapong;

import msafluid.MSAFluidSolver2D;
import processing.core.PApplet;
import processing.core.PImage;

import com.teamwut.plasma.plasmapong.pong.Const;

public class PlasmaFluid {
	final PApplet p;
	
	final int FLUID_WIDTH = 60;
	
	public final MSAFluidSolver2D fluidSolver;
	
	PImage imgFluid;
	int[] pixels;
	boolean touchupdated = false;
	
	boolean random = false;
	
	public PlasmaFluid(final PApplet p) {
		this.p = p;
	    // create fluid and set options
		if (p.width < p.height) {
		    fluidSolver = new MSAFluidSolver2D((FLUID_WIDTH * p.width/p.height), (FLUID_WIDTH));
		} else {
			fluidSolver = new MSAFluidSolver2D((FLUID_WIDTH), (FLUID_WIDTH * p.height/p.width));
		}
	    setupFluid();
	
	    // create image to hold fluid picture
	    imgFluid = p.createImage(fluidSolver.getWidth(), fluidSolver.getHeight(), PApplet.ARGB);
	    pixels = new int[fluidSolver.getNumCells()];
	}
	
	public void setupFluid() {
		  fluidSolver.enableRGB(true).setFadeSpeed(0.01f).setDeltaT(0.5f*Const.FLUID_DELTAT_MULT).setVisc(0.0001f*Const.FLUID_VISC_MULT).setSolverIterations(3);
		  //fluidSolver.enableRGB(true).setFadeSpeed(0.01f).setDeltaT(1).setVisc(1).setSolverIterations(5);
	}
	
	
	public void setRandomness(final boolean random) {
		this.random = random;
	}
	
	
	public void draw(final PApplet p) {
		draw(p, true);
	}
	
	int r,g,b;
	public void draw(final PApplet p, final boolean stepforward) {
//		PGraphicsAndroid3D g3d = (PGraphicsAndroid3D) p.g;
//		g3d.gl.
//	    p.colorMode(PApplet.RGB, 1);  

	    if (stepforward) fluidSolver.update();
	    
	    imgFluid.loadPixels();
	    final int cellcount = fluidSolver.getNumCells();
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
	    p.pushStyle();
	    p.pushMatrix();
	    imgFluid.updatePixels();//  fastblur(imgFluid, 2);
	    
	    p.image(imgFluid, 0, 0, p.width, p.height);
	    p.popStyle();
	    p.popMatrix();

//	    p.colorMode(PApplet.RGB, 255);  
	
	}
	
//    public void draw(PApplet p, boolean stepforward) {
////    	Log.d("PlasmaColorWallpaper","drawing! "+frameCount);
//	    if (stepforward) fluidSolver.update();
//
//	    int d = 2;
//	    int cellcount = fluidSolver.getNumCells();
//	    int fluidWidth = fluidSolver.getWidth();
//	    int fluidHeight = fluidSolver.getHeight();
//	    int color = 0;
//	    float x,y,z;
//	    int ix, iy, iz;
//	    for(int i=0; i<cellcount; i++) { //optimize here.
//	    	x=fluidSolver.r[i];
//	    	y=fluidSolver.g[i];
//	    	z=fluidSolver.b[i];
//	    	if (x > 255) x = 255; else if (x < 0) x = 0;
//	        if (y > 255) y = 255; else if (y < 0) y = 0;
//	        if (z > 255) z = 255; else if (z < 0) z = 0;
//
//	        color = 0xff000000 | ((int)x << 16) | ((int)y << 8) | (int)z;
//	        pixels[i] = color;
//	    	//bitmap.setPixel(i / fluidWidth, i % fluidHeight, color);
//	    }  
//	    Bitmap bitmap = imgFluid.getBitmap();
//	    bitmap.setPixels(pixels, 0, FLUID_WIDTH+2, 0, 0, FLUID_WIDTH+2, FLUID_WIDTH+2);
////	    p.pushStyle();
////	    p.pushMatrix();
////	    imgFluid.updatePixels();//  fastblur(imgFluid, 2);
////	    
////	    p.image(imgFluid, 0, 0, p.width, p.height);
////	    p.popStyle();
////	    p.popMatrix();
////
//////	    p.colorMode(PApplet.RGB, 255);  
//
//    }

	
	public void addForce(final PApplet p, final float x, final float y, final float dx, final float dy) {
		this.addForce(p, x, y, dx, dy, 0);
	}
	
	public void addForce(final PApplet p, final float x, final float y, final float dx, final float dy, final float colorOffset) {
		this.addForce(p, x, y, dx, dy, colorOffset, 4);
	}

	// add force and dye to fluid, and create particles
	public void addForce(final PApplet p, final float x, final float y, final float dx, final float dy, final float colorOffset, float colorMult) {
			colorMult = colorMult * Const.FLUID_COLOR_MULT;
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
	        
        	final int index = fluidSolver.getIndexForNormalizedPosition(x, y);
	        fluidSolver.rOld[index]  += p.red(drawColor) * colorMult;
	        fluidSolver.gOld[index]  += p.green(drawColor) * colorMult;
	        fluidSolver.bOld[index]  += p.blue(drawColor) * colorMult;
	
	        fluidSolver.uOld[index] += dx * Const.FLUID_VEL_MULT;
	        fluidSolver.vOld[index] += dy * Const.FLUID_VEL_MULT;

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
