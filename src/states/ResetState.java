package states;

import java.awt.Graphics;
import java.util.ArrayList;

import graphics.Assets;
import input.Constants;
import input.KeyBoard;
import ui.Action;
import ui.Button;

public class ResetState extends KeyBoard{
	private int level;
	private ArrayList<Button> buttons;
	public ResetState()  {
		buttons = new ArrayList<>();
		buttons.add(new Button(
				Assets.grey_button, 
				Assets.blue_button,
				Constants.WIDTH/2 - Assets.grey_button.getWidth()/2,
				(int)(Constants.HEIGHT/2 - Assets.grey_button.getHeight()*1.5),
				Constants.PLAY, 
				new Action() {
					@Override
					public void doAction() {
						KeyBoard.BtEsc = false;
					}
				}));

		buttons.add(new Button(
				Assets.grey_button, 
				Assets.red_button,
				Constants.WIDTH/2 - Assets.grey_button.getWidth()/2,
				Constants.HEIGHT/2,
				"RESET", 
				new Action() {
					@Override
					public void doAction() {
						State.changeState(new GameState(level));
					}
				}));
		
		buttons.add(new Button(
				Assets.grey_button, 
				Assets.red_button,
				Constants.WIDTH/2 - Assets.grey_button.getWidth()/2,
				(int)(Constants.HEIGHT/2 + Assets.grey_button.getHeight()*1.5),
				"MENU", 
				new Action() {
					@Override
					public void doAction() {
						new MenuState().stopBackgroundMusic();
						new MenuState().loopTheme();
						State.changeState(new MenuState());
					}
				}));
	}

	public void update() {
		for (Button b : buttons) {
			b.update();
		}
	}

	public void draw(Graphics g) {
		for (Button b : buttons) {
			b.draw(g);
		}
	}

	public int level(int n) {
		return level = n;
	}
}
