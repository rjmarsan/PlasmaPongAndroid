package com.teamwut.plasma.plasmapong.pong;

import processing.core.PApplet;
import processing.core.PFont;
import android.content.Intent;
import android.os.Bundle;

import com.teamwut.plasma.plasmapong.PlasmaFluid;
import com.teamwut.plasma.plasmapong.PlasmaPong;
import com.teamwut.plasma.plasmapong.PlasmaPongFinishedActivity;
import com.teamwut.plasma.plasmapong.pong.bots.WatsonAI;
import com.teamwut.plasma.plasmapong.pong.objects.Ball;
import com.teamwut.plasma.plasmapong.pong.objects.Goals;
import com.teamwut.plasma.plasmapong.pong.objects.HUD;
import com.teamwut.plasma.plasmapong.pong.objects.StatusOverlay;

public class Game {
	final PlasmaPong p;
	final PlasmaFluid fluid;
	
	final float width;
	final float height;
	final int players;
	
	public boolean paused;
	
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
	WatsonAI watson;
	
	PFont font;
	
	
	int maxScore = 2;
	int scoreP1 = maxScore;
	int scoreP2 = maxScore;
	
	int whoJustScored = Const.NO_PLAYER;
	
	

	public Game(PlasmaPong p, PlasmaFluid fluid, int players) {
		this.fluid = fluid;
		this.p = p;
		this.width = p.width;
		this.height = p.height;
		this.players = players;
	}
	
	public void pause() {
		paused = true;
	}
	public void unpause() {
		paused = false;
	}

	public void initPong() {
		glInit();
		ball = new Ball(p);
		goals = new Goals(p);
		hud = new HUD(p);
		watson = new WatsonAI(this, fluid, ball);
		updateScores();
		statoverlay = new StatusOverlay(p);
		initGameLogic();
	}
	
	public void glInit() {
		p.textMode(PApplet.MODEL);
		//font = p.loadFont("GillSans-Bold-48.vlw");

//		p.textFont(font, 48);
		p.textAlign(PApplet.CENTER);
		p.rectMode(PApplet.CENTER);
	}

	public void initGameLogic() {
		scoreP1 = maxScore;
		scoreP2 = maxScore;
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
//		PApplet.println("modeFrameCountdown:"+this.modeFrameCountdown + " for mode: "+this.mode);
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
		PApplet.println("Changing mode to:"+mode);
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
			this.scoreP2 -= 1;
			if (this.scoreP2 > 0)
				transitionToJustScored();
			else
				transitionToGameOver();
		} else if (scored == Const.PLAYER_2) {
			this.whoJustScored = Const.PLAYER_2;
			this.scoreP1 -= 1;
			if (this.scoreP1 > 0)
				transitionToJustScored();
			else 
				transitionToGameOver();
		}
		makeGameHarder();
	}
	
	public void transitionFromPregameWait() {
		resetPuck();
		setGameState(PLAYING);
	}
	
	public void transitionFromJustScored() {
		transitionToPlaying();
	}
	public void transitionFromJustScoredWait() {
		transitionToPlaying();
	}
	public void transitionFromGameOver() {
		Intent i = new Intent(p, PlasmaPongFinishedActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		int winner = 0;
		if (this.scoreP1 == 0) winner = Const.PLAYER_2;
		if (this.scoreP2 == 0) winner = Const.PLAYER_1;
		i.putExtra(Const.WINNER, winner);
		i.putExtra(Const.PLAYER_1_SCORE, scoreP1);
		i.putExtra(Const.PLAYER_2_SCORE, scoreP2);
		p.startActivity(i);
		p.finish();
	}
	
	public void transitionToPlaying() {
		resetPuck();
		setGameState(PLAYING);
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
		watson.youJustLost();
	}

	public void resetFluid() {
		fluid.setupFluid();
		fluid.fluidSolver.reset();
	}


	
	
	public void drawPong(boolean stepforward) {
		p.pushStyle();
		p.colorMode(p.RGB, 255, 255, 255, 255);

		goals.draw(p);
		ball.draw(fluid, stepforward && mode == PLAYING);

		hud.draw(p);
		statoverlay.draw(p, this, fluid);
		
		watson.thinkAndMove(p);
		
		p.popStyle();
		
		if (!paused && stepforward) updateGameState();
	}

	public Ball getBall() {
		return ball;
	}

	public Goals getGoals() {
		return goals;
	}

	public int getWhoJustScored() {
		return whoJustScored;
	}

}
