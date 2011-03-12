package com.teamwut.plasma.plasmapong.pong;

import processing.core.PApplet;

import com.teamwut.plasma.plasmapong.PlasmaPong;

public class Const {

	public final static String RENDER_MODE = PApplet.A2D;
	
	public final static String SHARED_PREF_NAME = PlasmaPong.class+"plasmapongprefs";
	
	
	public static final long ANIMATION_THREAD_SLEEP_MS = 2;
	
	public static boolean IS_PORTRAIT = true;
	
	public final static int PLASMA_QUALITY = 66;
	public final static int PLASMA_ITERS = 3;

	
	public final static int PLAYER_1 = 1;
	public final static int PLAYER_2 = 2;
	public final static int NO_PLAYER = 0;
	
	
	
	public final static int PREGAME_WAIT_COUNT = 45;
	public final static int JUST_SCORED_COUNT = 45;
	public final static int JUST_SCORED_WAIT_COUNT = 45;
	public final static int GAME_OVER_COUNT = 60;

	
	
	public final static int PLAYER_1_OFFSET = 0;
	public final static int PLAYER_2_OFFSET = 135;
	public final static int OTHER_OFFSET = 270;
	
	
	public final static int AUTO_AIM_RADIUS_SQRD = 30010;
	
	
	public final static String WINNER = "winner";
	public final static String PLAYER_1_SCORE = "p1score";
	public final static String PLAYER_2_SCORE = "p2score";
	
	
	public final static String GAMES_KEY = "games";
	public final static String PLAYER_1_KEY = "p1score";
	public final static String PLAYER_2_KEY = "p2score";
	
	
	public final static float FLUID_VISC_MULT = 1.0f;
	public final static float FLUID_DELTAT_MULT = 1.0f;

	
	public final static float FLUID_VEL_MULT = 30f;
	public final static float FLUID_COLOR_MULT = 1.2f;                                                                                                           ;
	
	public final static float BALL_FLUID_MULT = 20f;
	public final static float SIDEBAR_HIT_COLOR_MULT = 290f;
	public final static float SIDEBAR_HIT_VEL_MULT = 3f;
	public final static float SIDEBAR_GUTTER_FIX_MULT = 10f;
	public final static float SIDEBAR_GUTTER_FIX_COLOR = 400f;
	public final static int SIDEBAR_GUTTER_COUNT = 30;


}
