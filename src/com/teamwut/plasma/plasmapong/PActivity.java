package com.teamwut.plasma.plasmapong;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.teamwut.plasma.plasmapong.mt.Cursor;
import com.teamwut.plasma.plasmapong.mt.MTCallback;
import com.teamwut.plasma.plasmapong.mt.MTManager;
import com.teamwut.plasma.plasmapong.pong.Const;
import com.teamwut.plasma.plasmapong.pong.Prefs;
import com.teamwut.plasma.plasmapong.pong.objects.Goals;

public abstract class PActivity extends Activity implements MTCallback, SurfaceHolder.Callback {



	PlasmaFluid fluid;
	
	MTManager mtManager;
	SurfaceHolder holder;
	
	public int width, height;
	final Random r = new Random();
		
	public void onCreate(final Bundle savedinstance) {
		super.onCreate(savedinstance);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        createSurface();
		
	}
	
	protected void createSurface() {
		final SurfaceView surface = new SurfaceView(this);
		holder = surface.getHolder();
		holder.addCallback(this);
		this.setContentView(surface);
	}
	static boolean alreadyone = false;

	protected class UpdateThread extends Thread {
		public void run() {
			if (!alreadyone) {
				alreadyone = true;
				Canvas c;
				while(1==1) {
					c = holder.lockCanvas();
					if (c != null) {
						draw(c);
						holder.unlockCanvasAndPost(c);
					}
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	
	public abstract void setup();
		
	//mt version
	public boolean onTouchEvent(final MotionEvent me) {
		if (mtManager != null) mtManager.surfaceTouchEvent(me);
		return true;
	}
	
	public void addForce(final float x, final float y) {
		float vx, vy;	
		if (width < height) {
			if (y/height > 0.5f) 
				vx = -25;
			else 
				vx = 25;
			vy = 0;
		} else {
			if (x/width > 0.5f) 
				vy = -25;
			else 
				vy = 25;
			vx = 0;

		}
		
	    fluid.addForce( x/width, y/height, vy/width, vx/height);
	    
	    vy = -4;
	    vx = vx / 10;
	    fluid.addForce( x/width, y/height, vy/width, vx/height);
	    
	    vy = 4;
	    fluid.addForce( x/width, y/height, vy/width, vx/height);
	}
	
	public void addRandomForce() {
		final float x = r.nextInt(width);
		final float y = r.nextInt(height);
		final int max = 200;
		final float dx = r.nextInt(max) - max/2;
		final float dy = r.nextInt(max) - max/2;
		fluid.addForce( x/width, y/height, dy/width, dx/height, 0, 50);
	}
	
	public void updateCursors() {
		final ArrayList<Cursor> cursors = (ArrayList<Cursor>) mtManager.cursors.clone();
		for (final Cursor c : cursors ) {
			if (c != null && c.currentPoint != null)
				addForce(c.currentPoint.x, c.currentPoint.y);
		}
	}
	
	
	public void draw(Canvas c) {
		updateCursors();	
	}

	@Override
	public void touchEvent(final MotionEvent me, final int i, final float x, final float y, final float vx,
			final float vy, final float size) {
	}
	
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		this.width = width;;
		this.height = height;;
		setup();
	}


	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}



}
