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

	
	
	public final static int NOTHING = 0;
	public final static int PREGAME_WAIT = 1;
	public final static int PLAYING = 2;
	public final static int JUST_SCORED = 3;
	public final static int JUST_SCORED_WAIT = 4;
	public final static int GAME_OVER = 5;
	
	public int mode = NOTHING;
	public int modeFrameCountdown = -1;
	

	
	
	
	Ball ball;
	Goals goals;
	HUD hud;
	StatusOverlay statoverlay;
	
	PFont font;


	int scoreP1 = 0;
	int scoreP2 = 0;

	int maxScore = 7;

	
	int whoJustScored = Const.NO_PLAYER;
	
	

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
		setGameState(PREGAME_WAIT);
	}
	
	
	public void updateGameState() {
		switch (mode) {
		case NOTHING:
			this.initGameLogic();
			break;
		case PREGAME_WAIT:
			break;
		case PLAYING:
			this.updateGameLogic();
			break;
		case JUST_SCORED:
			break;
		case JUST_SCORED_WAIT:
			break;
		case GAME_OVER:
			break;
		}
		this.testGameStateChange();
	}
	
	public void testGameStateChange() {
		if (this.modeFrameCountdown >= 0)
			this.modeFrameCountdown -= 1;
		if (this.modeFrameCountdown == 0) {
			switch (mode) {
			case PREGAME_WAIT:
				this.transitionFromPregameWait(); break;
			case JUST_SCORED:
				this.transitionFromJustScored(); break;
			case JUST_SCORED_WAIT:
				this.transitionFromJustScoredWait(); break;
			case GAME_OVER:
				this.transitionFromGameOver();	break;
			}
		}
	}
	
	public void setGameState(int mode) {
		this.mode = mode;
		this.modeFrameCountdown = -1;
		switch (mode) {
		case PREGAME_WAIT:
			this.modeFrameCountdown = Const.PREGAME_WAIT_COUNT;
			break;
		case JUST_SCORED:
			this.modeFrameCountdown = Const.JUST_SCORED_COUNT;
			break;
		case JUST_SCORED_WAIT:
			this.modeFrameCountdown = Const.JUST_SCORED_WAIT_COUNT;
			break;
		case GAME_OVER:
			this.modeFrameCountdown = Const.GAME_OVER_COUNT;
			break;
		}
	}
	

	public void updateGameLogic() {
		int scored = goals.puckGoalStatus(ball);
		if (scored == Const.PLAYER_1) {
			this.whoJustScored = Const.PLAYER_1;
			transitionToJustScored();
		} else if (scored == Const.PLAYER_2) {
			this.whoJustScored = Const.PLAYER_2;
			transitionToJustScored();
		}
		makeGameHarder();
	}
	
	
	public void transitionFromPregameWait() {
		resetPuck();
		setGameState(PLAYING);
	}
	
	public void transitionFromJustScored() {
		setGameState(JUST_SCORED_WAIT);
	}
	public void transitionFromJustScoredWait() {
		setGameState(PLAYING);
	}
	public void transitionFromGameOver() {
		//finish!
	}
	
	public void transitionToJustScored() {
		updateScores();
		setGameState(JUST_SCORED);
	}
	public void transitionToJustScoredWait() {
		setGameState(JUST_SCORED_WAIT);
	}
	public void transitionToGameOver() {
		updateScores();
		setGameState(GAME_OVER);
	}

	
	
	public void updateScores() {
		hud.setP1Score(scoreP1);
		hud.setP2Score(scoreP2);
	}

	public void makeGameHarder() {
		// b.scalingFactor += 2;
		// fluidSolver.setVisc(fluidSolver.getVisc()/1.003f);
		// println(fluidSolver.getVisc());
	}


	public void resetPuck() {
		ball.resetBall();
		resetFluid();
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
