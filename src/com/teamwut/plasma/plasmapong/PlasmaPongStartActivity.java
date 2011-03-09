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
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.teamwut.plasma.plasmapong.mt.Cursor;
import com.teamwut.plasma.plasmapong.mt.MTCallback;
import com.teamwut.plasma.plasmapong.mt.MTManager;
import com.teamwut.plasma.plasmapong.pong.Const;
import com.teamwut.plasma.plasmapong.pong.Prefs;
import com.teamwut.plasma.plasmapong.pong.objects.Goals;

public class PlasmaPongStartActivity extends Activity implements MTCallback, SurfaceHolder.Callback {



	PlasmaFluid fluid;
	
	MTManager mtManager;
	SurfaceHolder holder;
	
	int width, height;
	
	Goals goals;
	
	Random r = new Random();
	
	public void onCreate(final Bundle savedinstance) {
		super.onCreate(savedinstance);
		
		this.setContentView(R.layout.main_screen_on);
		
		createSurface();
		
    	final Button play1p = (Button) this.findViewById(com.teamwut.plasma.plasmapong.R.id.play_1p);
    	play1p.setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				final Intent i = new Intent(PlasmaPongStartActivity.this, PlasmaPong.class);
				i.putExtra(PlasmaPong.PLAYER_KEY, PlasmaPong.ONE_PLAYER_PLAY);
				startActivity(i);
			}});
    	final Button play2p = (Button) this.findViewById(com.teamwut.plasma.plasmapong.R.id.play_2p);
    	play2p.setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				final Intent i = new Intent(PlasmaPongStartActivity.this, PlasmaPong.class);
				i.putExtra(PlasmaPong.PLAYER_KEY, PlasmaPong.TWO_PLAYER_PLAY);
				startActivity(i);
			}});
	}
	
	private void createSurface() {
		final SurfaceView surface = (SurfaceView) this.findViewById(R.id.surfacevieww);
		holder = surface.getHolder();
		holder.addCallback(this);
	}
	static boolean alreadyone = false;

	private class UpdateThread extends Thread {
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

	
	public void setup() {
	    fluid = new PlasmaFluid(width, height);
	    fluid.setRandomness(true);
	    mtManager = new MTManager();
	    
//	    goals = new Goals(this);
	    new UpdateThread().start();
	}
		
	//mt version
	public boolean onTouchEvent(final MotionEvent me) {
		if (mtManager != null) mtManager.surfaceTouchEvent(me);
		return true;
	}
	
	public void addForce(final float x, final float y) {
		float vx, vy;	
		if (y/height > 0.5f) 
			vx = -25;
		else 
			vx = 20;
		vy = 0;
		
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
		
//		Log.d("asdf","asdf");
		
//		c.drawRGB(20, 100, 200);
		
//	    background(0);
	    fluid.draw(c);
//	    goals.draw(this);
	    
	    if (r.nextInt(20) == 0) addRandomForce();
	    
//	    if (this.frameCount % 60 == 0) println(this.frameRate+"");
	
	}

	@Override
	public void touchEvent(final MotionEvent me, final int i, final float x, final float y, final float vx,
			final float vy, final float size) {
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
	    final MenuInflater inflater = getMenuInflater();
	    inflater.inflate(com.teamwut.plasma.plasmapong.R.menu.main_menu, menu);
	    return true;
	}


	@Override
	public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
	    switch (item.getItemId()) {
	    case com.teamwut.plasma.plasmapong.R.id.game_settings:
	        final Intent i = new Intent(this, PlasmaPongSettings.class);
	        startActivity(i);
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onActivityResult(final int i, final int j, final Intent res) {
		super.onActivityResult(i, j, res);
		readSettings();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		readSettings();
	}

    public void readSettings() {
        final SharedPreferences mPrefs = this.getSharedPreferences(Const.SHARED_PREF_NAME, 0);
        Prefs.botName = mPrefs.getString("bottype", getResources().getString(com.teamwut.plasma.plasmapong.R.string.bot_watson));
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
