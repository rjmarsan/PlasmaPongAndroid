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

public class PlasmaPongStartActivity extends PActivity {



	
	Goals goals;
	
	
	public void onCreate(final Bundle savedinstance) {
		super.onCreate(savedinstance);
		
//		this.setContentView(R.layout.main_screen_on);
		
//		createSurface();
    	View overlay = this.getLayoutInflater().inflate(R.layout.main_screen_on, null);
    	this.addContentView(overlay, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

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
	    fluid = new PlasmaFluid(width, height);
	    fluid.setRandomness(true);
	    mtManager = new MTManager();
	    
//	    goals = new Goals(this);
	    new UpdateThread().start();
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
