package com.teamwut.plasma.plasmapong.pong.objects;

import msafluid.MSAFluidSolver2D;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.teamwut.plasma.plasmapong.PActivity;
import com.teamwut.plasma.plasmapong.PlasmaFluid;
import com.teamwut.plasma.plasmapong.pong.Const;

public class Ball {
	  final PActivity p;
	  
	  public float x;
	  public float y;
	  
	  final float width;
	  final float height;
	  
	  public float vx=0;
	  public float vy=0;
	  
	  float maxvel = 0.0f;
	  
	  float yPadding = 20;
	  float padding = 30;
	  float upperBoundsY = yPadding;
	  float lowerBoundsY;
	  
	  int gutterCount = 0;
	  
	  
	  float scalingFactor = 500;
	  
	  Bitmap ballimage;
	  Paint ballimagepaint;
	  public Ball(final PActivity p) {
		  this.p = p;
		  this.x = p.width/2;
		  this.y = p.height/2;
		  this.width = p.width;
		  this.height = p.height;
		  this.lowerBoundsY = height - yPadding;
		  ballimage = p.loadImage("puck6b.png");
		  ballimagepaint = new Paint();
	  }
	  
	  
	  float noiseScale, noiseVal, fluidvx,fluidvy;
	  int index;
	  public void draw(final Canvas c, PlasmaFluid fluid, final boolean stepforward) {
		final MSAFluidSolver2D fluidSolver = fluid.fluidSolver;
	    c.save();
//	    noiseScale = 0.01f;
//	    noiseVal = p.noise(x*noiseScale, y*noiseScale)*255;
//	    p.fill(noiseVal,noiseVal,noiseVal,150);
	    index = fluidSolver.getIndexForNormalizedPosition(x/width,y/height);
	    fluidvy = fluidSolver.v[index]*scalingFactor;
	    fluidvx = fluidSolver.u[index]*scalingFactor;
	    //if (abs(fluidvx + fluidvy) < 3) {
	    //  fluidvscale = 100;  //now it flies a lot better when you push it
	    //}
	    

	    //fluidvscale = 100/abs(fluidvx + fluidvy+0.001); 
	    vy = (fluidvy)/Const.BALL_FLUID_MULT+(Const.BALL_FLUID_MULT-1)*vy/Const.BALL_FLUID_MULT;
	    vx = (fluidvx)/Const.BALL_FLUID_MULT+(Const.BALL_FLUID_MULT-1)*vx/Const.BALL_FLUID_MULT;
	    if (vx > 200) vx = 200;
	    if (vy > 200) vy = 200;
//	    p.ellipse(x,y,30,30);
	    
//	    p.image(ballimage, x, y);
	    c.drawBitmap(ballimage, x, y, ballimagepaint);

	    c.restore();
	    if (stepforward) {
		    x = x+vx;
		    y = y+vy;
	    }
	    checkBounds(fluid);
	  }
	  public void checkBounds(final PlasmaFluid fluid) {
	    if (x < padding) {
	      x=padding;
	      vx = -vx;
	      fluid.addForce(x/width, y/height, vx/width*Const.SIDEBAR_HIT_VEL_MULT, 0, 270, Const.SIDEBAR_HIT_COLOR_MULT);
	    }
	    else if (x > width-padding) {
	      x=width-padding;
	      vx = -vx;
	      fluid.addForce( x/width, y/height, vx/width*Const.SIDEBAR_HIT_VEL_MULT, 0, 270, Const.SIDEBAR_HIT_COLOR_MULT);
	    }
	    gutterFix(fluid);
	    if (y < upperBoundsY) {
	      y=upperBoundsY;
	      vy = -vy;
	    }
	    else if (y > lowerBoundsY) {
	      y=lowerBoundsY;
	      vy = -vy;
	    }

	  }
	  
	  public void gutterFix(final PlasmaFluid fluid) {
		  if (x > width-padding*2) {
		    	gutterCount ++;
		    	if (gutterCount > Const.SIDEBAR_GUTTER_COUNT) {
		  	      fluid.addForce( 1, y/height, Const.SIDEBAR_GUTTER_FIX_MULT, 0, 270, Const.SIDEBAR_GUTTER_FIX_COLOR);
		  	      gutterCount = 0;
		    	}
		    }
		    else if (x < padding*2) {
		    	gutterCount ++;
		    	if (gutterCount > Const.SIDEBAR_GUTTER_COUNT) {
		  	      fluid.addForce( 0, y/height, Const.SIDEBAR_GUTTER_FIX_MULT, 0, 270, Const.SIDEBAR_GUTTER_FIX_COLOR);
		  	      gutterCount = 0;
		    	}
		    }
		    else {
		    	gutterCount = 0;
		    }
	  }
	  
	  
	  public void resetBall() {
	    x = width/2;
	    y = height/2;
	    vx = 0;
	    vy = 0;
	    scalingFactor = 500;
	  }

}
