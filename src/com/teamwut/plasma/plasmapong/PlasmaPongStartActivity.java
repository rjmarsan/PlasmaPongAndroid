package com.teamwut.plasma.plasmapong;

import java.util.ArrayList;
import java.util.Random;

import processing.core.PApplet;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.teamwut.plasma.plasmapong.mt.Cursor;
import com.teamwut.plasma.plasmapong.mt.MTCallback;
import com.teamwut.plasma.plasmapong.mt.MTManager;
import com.teamwut.plasma.plasmapong.pong.Const;
import com.teamwut.plasma.plasmapong.pong.Drawbl;
import com.teamwut.plasma.plasmapong.pong.Prefs;
import com.teamwut.plasma.plasmapong.pong.objects.Goals;

public class PlasmaPongStartActivity extends PApplet implements MTCallback {


	public int sketchWidth() { return this.screenWidth; }
	public int sketchHeight() { return this.screenHeight; }
	public String sketchRenderer() { return PApplet.OPENGL; }

	PlasmaFluid fluid;
	
	MTManager mtManager;
	
	Goals goals;
	
	Random r = new Random();
	
	public void onCreate(final Bundle savedinstance) {
		super.onCreate(savedinstance);
    	final View v = this.getLayoutInflater().inflate(com.teamwut.plasma.plasmapong.R.layout.main_screen_on, null);
    	this.addContentView(v, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
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

	
	public void setup() {
	    // use OPENGL rendering for bilinear filtering on texture
	    //hint( ENABLE_OPENGL_4X_SMOOTH );    // Turn on 4X antialiasing
		hint(DISABLE_DEPTH_TEST);
		hint(DISABLE_OPENGL_ERROR_REPORT);
	    frameRate(40);
	
	    fluid = Drawbl.getFluidSim(this);
	    fluid.setRandomness(true);
	    mtManager = new MTManager();
	    
//	    goals = new Goals(this);
	    	    
	}
		
	//mt version
	public boolean surfaceTouchEvent(final MotionEvent me) {
		if (mtManager != null) mtManager.surfaceTouchEvent(me);
		return super.surfaceTouchEvent(me);
	}
	
	public void addForce(final float x, final float y) {
		float vx, vy;	
		if (y/height > 0.5f) 
			vx = -25;
		else 
			vx = 20;
		vy = 0;
		
	    fluid.addForce(this, x/width, y/height, vy/width, vx/height);
	    
	    vy = -4;
	    vx = vx / 10;
	    fluid.addForce(this, x/width, y/height, vy/width, vx/height);
	    
	    vy = 4;
	    fluid.addForce(this, x/width, y/height, vy/width, vx/height);
	}
	
	public void addRandomForce() {
		final float x = r.nextInt(width);
		final float y = r.nextInt(height);
		final int max = 200;
		final float dx = r.nextInt(max) - max/2;
		final float dy = r.nextInt(max) - max/2;
		fluid.addForce(this, x/width, y/height, dy/width, dx/height, 0, 50);
	}
	
	public void updateCursors() {
		final ArrayList<Cursor> cursors = (ArrayList<Cursor>) mtManager.cursors.clone();
		for (final Cursor c : cursors ) {
			if (c != null && c.currentPoint != null)
				addForce(c.currentPoint.x, c.currentPoint.y);
		}
	}
	
	
	public void draw() {
		updateCursors();
		
		
	    background(0);
	    fluid.draw(this);
//	    goals.draw(this);
	    
	    if (r.nextInt(20) == 0) addRandomForce();
	    
	    if (this.frameCount % 60 == 0) println(this.frameRate+"");
	
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



}
