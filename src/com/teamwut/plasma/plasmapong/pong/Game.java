package com.teamwut.plasma.plasmapong.pong;

import processing.core.PFont;

import com.teamwut.plasma.plasmapong.PlasmaFluid;
import com.teamwut.plasma.plasmapong.PlasmaPong;
import com.teamwut.plasma.plasmapong.pong.objects.Ball;
import com.teamwut.plasma.plasmapong.pong.objects.Goals;
import com.teamwut.plasma.plasmapong.pong.objects.HUD;
import com.teamwut.plasma.plasmapong.pong.objects.StatusOverlay;

public class Game {
	final PlasmaPong p;
	final PlasmaFluid fluid;
	
	final float width;
	final float height;

	Ball ball;
	Goals goals;
	HUD hud;
	StatusOverlay statoverlay;
	
	PFont font;


	int scoreP1 = 0;
	int scoreP2 = 0;

	int maxScore = 5;
	int waitPeriod = 60; // number of frames to keep the status message

	int justScored = 0; // the frame it happened
	int lastScored = 0; // the player who last scored
	int gameOver = 0; // the frame it happened
	int initWait = 0; // the frame it happened

	
	public final static int NOTHING = 0;
	public final static int PREGAME_WAIT = 1;
	public final static int PLAYING = 2;
	public final static int JUST_SCORED = 3;
	public final static int JUST_SCORED_WAIT = 4;
	public final static int GAME_OVER = 5;
	
	int mode = NOTHING;
	
	
	int modeFrameCountdown = -1;
	
	
	String statusMessage;

	public Game(PlasmaPong p, PlasmaFluid fluid) {
		this.fluid = fluid;
		this.p = p;
		this.width = p.width;
		this.height = p.height;
	}

	public void initPong() {
		glInit();
		ball = new Ball(p);
		goals = new Goals(p);
		hud = new HUD(p);
		statoverlay = new StatusOverlay(p);
		initGameLogic();
	}
	
	public void glInit() {
		p.textMode(p.MODEL);
		font = p.loadFont("GillSans-Bold-48.vlw");

		p.textFont(font, 48);
		p.textAlign(p.CENTER);
		p.rectMode(p.CENTER);

	}

	public void initGameLogic() {
		scoreP1 = 0;
		scoreP2 = 0;
	}
	
	
	public void updateGameState() {
		switch (mode) {
		case NOTHING:
			this.initGameLogic();
			break;
		case PREGAME_WAIT:
			break;
		case PLAYING:
			break;
		case JUST_SCORED:
			break;
		case JUST_SCORED_WAIT:
			break;
		case GAME_OVER:
			break;
		}
	}
	

	public void updateGameLogic() {
		if (justScored == 0) {
			int scored = goals.puckGoalStatus(ball);
			if (scored == Const.PLAYER_1) {
				
			} else if (scored == Const.PLAYER_2) {

			}

		}
		makeGameHarder();
		
	}

	public void makeGameHarder() {
		// b.scalingFactor += 2;
		// fluidSolver.setVisc(fluidSolver.getVisc()/1.003f);
		// println(fluidSolver.getVisc());
	}


	public void resetPuck() {
		if (justScored != 0 && p.frameCount - justScored > waitPeriod) {
			ball.resetBall();
			justScored = 0;
			resetFluid();
		}

	}

	public void resetFluid() {
		fluid.setupFluid();
		fluid.fluidSolver.reset();
	}


	
	
	public void drawPong() {
		p.pushStyle();
		p.colorMode(p.RGB, 255, 255, 255, 255);

		ball.draw(fluid.fluidSolver);
		goals.draw(p);
		hud.draw(p);
		statoverlay.draw(p);
		
		
		p.popStyle();
		
		updateGameLogic();
	}



}
