package states;

import java.awt.Graphics;

import javax.sound.sampled.Clip;

import graphics.Assets;

public abstract class State {

	private static State currentState = null;

	public static State getCurrentState() {
		return currentState;
	}

	public static void changeState(State newState) {
		currentState = newState;
	}

	public abstract void update(float dt);

	public abstract void draw(Graphics g);

	//
	public Clip theme = Assets.theme;

	public void loopTheme() {
		theme.setFramePosition(0);
		theme.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void playTheme() {
		theme.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stopTheme() {
		theme.stop();
	}

	//
	public Clip backgroundMusic = Assets.backgroundMusic;

	public void loopBackgroundMusic() {
		backgroundMusic.setFramePosition(0);
		backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void playBackgroundMusic() {
		backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stopBackgroundMusic() {
		backgroundMusic.stop();
	}

	//
	public Clip backEdit = Assets.backEdit;

	public void loopbackEdit() {
		backEdit.setFramePosition(0);
		backEdit.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void playbackEdit() {
		backEdit.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stopBackEdit() {
		backEdit.stop();
	}
	//
}