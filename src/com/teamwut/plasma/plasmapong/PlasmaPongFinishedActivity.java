package com.teamwut.plasma.plasmapong;

import java.util.ArrayList;
import java.util.Random;

import processing.core.PApplet;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.teamwut.plasma.plasmapong.mt.Cursor;
import com.teamwut.plasma.plasmapong.mt.MTCallback;
import com.teamwut.plasma.plasmapong.mt.MTManager;
import com.teamwut.plasma.plasmapong.pong.Const;
import com.teamwut.plasma.plasmapong.pong.Drawbl;
import com.teamwut.plasma.plasmapong.pong.objects.Goals;

public class PlasmaPongFinishedActivity extends PApplet implements MTCallback {


	public int sketchWidth() { return this.screenWidth; }
	public int sketchHeight() { return this.screenHeight; }
	public String sketchRenderer() { return PApplet.OPENGL; }

	PlasmaFluid fluid;
	
	MTManager mtManager;
	
	Goals goals;
	
	Random r = new Random();
	
	
	int p1score;
	int p2score;
	int winner;
	
	int games;
	int p1record;
	int p2record;
	
	String p1name = "Player 1";
	String p2name = "Player 2";
	
	public void onCreate(Bundle savedinstance) {
		super.onCreate(savedinstance);
		
		Intent i = this.getIntent();
		p1score = i.getIntExtra(Const.PLAYER_1_SCORE, 0);
		p2score = i.getIntExtra(Const.PLAYER_2_SCORE, 0);
		winner = i.getIntExtra(Const.WINNER, Const.NO_PLAYER);
		
		final SharedPreferences prefs = this.getSharedPreferences("savedgames", 0);
		
		games = prefs.getInt(Const.GAMES_KEY, 0);
		p1record = prefs.getInt(Const.PLAYER_1_KEY, 0);
		p2record = prefs.getInt(Const.PLAYER_2_KEY, 0);
		
		games += 1;
		if (winner == Const.PLAYER_1) 
			p1record += 1;
		else
			p2record += 1;
		
		Editor edit = prefs.edit();
		edit.putInt(Const.GAMES_KEY, games);
		edit.putInt(Const.PLAYER_1_KEY, p1record);
		edit.putInt(Const.PLAYER_2_KEY, p2record);
		edit.commit();
		


		
    	View v = this.getLayoutInflater().inflate(com.teamwut.plasma.plasmapong.R.layout.done_screen_on, null);
    	this.addContentView(v, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    	
    	
    	Button again = (Button) this.findViewById(com.teamwut.plasma.plasmapong.R.id.again);
    	again.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(PlasmaPongFinishedActivity.this, PlasmaPong.class);
				i.putExtra(PlasmaPong.PLAYER_KEY, PlasmaPong.TWO_PLAYER_PLAY);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}});
    	
    	Button quit = (Button) this.findViewById(com.teamwut.plasma.plasmapong.R.id.quit);
    	quit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Editor edit = prefs.edit();
				edit.clear();
				edit.commit();
				Intent i = new Intent(PlasmaPongFinishedActivity.this,PlasmaPongStartActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}});
    	
    	
    	TextView p1scoreview = (TextView)this.findViewById(com.teamwut.plasma.plasmapong.R.id.player1_score);
    	p1scoreview.setText(p1score+"");
    	TextView p2scoreview = (TextView)this.findViewById(com.teamwut.plasma.plasmapong.R.id.player2_score);
    	p2scoreview.setText(p2score+"");
    	
    	TextView p1recordview = (TextView)this.findViewById(com.teamwut.plasma.plasmapong.R.id.player1_record);
    	p1recordview.setText(p1record+"\nof\n"+games);
    	TextView p2recordview = (TextView)this.findViewById(com.teamwut.plasma.plasmapong.R.id.player2_record);
    	p2recordview.setText(p2record+"\nof\n"+games);
    	
    	TextView p1nameview = (TextView)this.findViewById(com.teamwut.plasma.plasmapong.R.id.player1_name);
    	p1nameview.setText(p1name+"");
    	TextView p2nameview = (TextView)this.findViewById(com.teamwut.plasma.plasmapong.R.id.player2_name);
    	p2nameview.setText(p2name+"");
    	
	}
	
	public void onBackPressed() {
		Intent i = new Intent(PlasmaPongFinishedActivity.this,PlasmaPongStartActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
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
	    
	    goals = new Goals(this);
	    	    
	}
		
	//mt version
	public boolean surfaceTouchEvent(MotionEvent me) {
		if (mtManager != null) mtManager.surfaceTouchEvent(me);
		return super.surfaceTouchEvent(me);
	}
	
	public void addForce(float x, float y) {
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
		float x = r.nextInt(width);
		float y = r.nextInt(height);
		int max = 200;
		float dx = r.nextInt(max) - max/2;
		float dy = r.nextInt(max) - max/2;
		fluid.addForce(this, x/width, y/height, dy/width, dx/height, 0, 50);
	}
	
	public void updateCursors() {
		ArrayList<Cursor> cursors = (ArrayList<Cursor>) mtManager.cursors.clone();
		for (Cursor c : cursors ) {
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
	public void touchEvent(MotionEvent me, int i, float x, float y, float vx,
			float vy, float size) {
	}

}
