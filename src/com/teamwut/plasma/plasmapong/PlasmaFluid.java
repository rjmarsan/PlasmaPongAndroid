package com.teamwut.plasma.plasmapong;

import msafluid.MSAFluidSolver2D;
import processing.core.PApplet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.teamwut.plasma.plasmapong.pong.Const;

public class PlasmaFluid {
	
	final int FLUID_WIDTH = 60;
	
	public final MSAFluidSolver2D fluidSolver;
	
	final Bitmap bitmap;
	final Paint paint;
	int[] pixels;
	boolean touchupdated = false;
	
	private final int fluidwidth;
	private final int fluidheight;
	
	boolean random = false;
	
	public PlasmaFluid(int width, int height) {
	    // create fluid and set options
		if (width < height) {
		    fluidSolver = new MSAFluidSolver2D((FLUID_WIDTH * width/height), (FLUID_WIDTH));
		} else {
			fluidSolver = new MSAFluidSolver2D((FLUID_WIDTH), (FLUID_WIDTH * height/width));
		}
		fluidwidth = fluidSolver.getWidth();
		fluidheight = fluidSolver.getHeight();

	    setupFluid();
	    
	    
	    
        // Create a Paint to draw the lines for our cube
        paint = new Paint();
        paint.setColor(0xfff11fff);
        paint.setAntiAlias(false);
        paint.setStrokeWidth(2);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setFilterBitmap(true);

	
		bitmap = Bitmap.createBitmap(fluidSolver.getWidth(),fluidSolver.getHeight(), Bitmap.Config.ARGB_8888);
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
//		draw(p, true);
	}
	
    float x,y,z;
    int ix, iy, iz;
    int frameCount = 0;
    public void draw(Canvas c) {
    	frameCount ++;
//    	Log.d("PlasmaColorWallpaper","drawing! "+frameCount);
	    fluidSolver.update();

//	    int d = 2;
	    int cellcount = fluidSolver.getNumCells();
//	    int fluidWidth = fluidSolver.getWidth();
//	    int fluidHeight = fluidSolver.getHeight();
	    int color = 0;
	    for(int i=0; i<cellcount; i++) { //optimize here.
	    	x=fluidSolver.r[i];
	    	y=fluidSolver.g[i];
	    	z=fluidSolver.b[i];
//	    	x = (x > 255) ? 255 : (x < 0) ?  0 : x ; maybe
	    	if (x > 255) x = 255; else if (x < 0) x = 0;
	        if (y > 255) y = 255; else if (y < 0) y = 0;
	        if (z > 255) z = 255; else if (z < 0) z = 0;

	        color = 0xff000000 | ((int)x << 16) | ((int)y << 8) | (int)z;
	        pixels[i] = color;
	    	//bitmap.setPixel(i / fluidWidth, i % fluidHeight, color);
	    }  
	    bitmap.setPixels(pixels, 0, fluidwidth, 0, 0, fluidwidth, fluidheight);
	    c.drawBitmap(bitmap, 
	    		new Rect(0,0,bitmap.getWidth(), bitmap.getHeight()), 
//	    		new Rect(0,0,bitmap.getWidth(), bitmap.getHeight()), 
	    		new Rect(0,0,c.getWidth(),c.getHeight()) , 
	    		paint);
//	    Log.d("Rendering", "Rendering");
	    //drawTouchPoint(c);
	}
	
	public void addForce(PApplet p, final float x, final float y, final float dx, final float dy) {
		this.addForce(x, y, dx, dy, 0);
	}
	
	public void addForce(PApplet p, final float x, final float y, final float dx, final float dy, final float colorOffset) {
		this.addForce(x, y, dx, dy, colorOffset, 4);
	}


	
	public void addForce(final float x, final float y, final float dx, final float dy) {
		this.addForce(x, y, dx, dy, 0);
	}
	
	public void addForce(final float x, final float y, final float dx, final float dy, final float colorOffset) {
		this.addForce(x, y, dx, dy, colorOffset, 4);
	}

	// add force and dye to fluid, and create particles
	public void addForce(final float x, final float y, final float dx, final float dy, final float colorOffset, float colorMult) {
			colorMult = colorMult * Const.FLUID_COLOR_MULT;
//	        if (dx > 1) dx = 1;
//	        if (dy > 5) dy = 1;
	
	
			float hue = (frameCount/10 + colorOffset) % 360;
	        int color = Color.HSVToColor(new float[] {hue,1,1});

	        if (!random) {
//		        if (y < 0.5f)
//		        	hue = (hue + 135) % 360;
	        } else {
	        	hue = (((x+10) * (y+10))*10 + frameCount/10 + colorOffset) % 360;
	        }
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
	        
	        int r = (color & 0x00ff0000) >> 16;
	        int g = (color & 0x0000ff00) >> 8;
	        int b = color & 0x000000ff;
//        	fluidSolver.addColorAtPos(x, y, r * colorMult, g * colorMult, b * colorMult );
//    		fluidSolver.addForceAtPos(x, y, dx * Const.FLUID_VEL_MULT, dy * Const.FLUID_VEL_MULT );
        	int index = fluidSolver.getIndexForNormalizedPosition(x, y);
	        fluidSolver.rOld[index]  += r * colorMult;
	        fluidSolver.gOld[index]  += g * colorMult;
	        fluidSolver.bOld[index]  += b * colorMult;
	
	        fluidSolver.uOld[index] += dx * Const.FLUID_VEL_MULT;
	        fluidSolver.vOld[index] += dy * Const.FLUID_VEL_MULT;


	}
	
	


}
