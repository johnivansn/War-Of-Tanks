package states;

import java.awt.Graphics;
import java.util.ArrayList;

import graphics.Assets;
import input.Constants;
import ui.Action;
import ui.Button;

public class MenuState extends State{
	
	private ArrayList<Button> buttons;
	private NameState nm;
	public MenuState() {
		playTheme();
		nm = new NameState(Constants.WIDTH / 2 - 300 / 2, Constants.HEIGHT / 2 + Assets.grey_button.getHeight());
		buttons = new ArrayList<>();
		
		buttons.add(new Button(
				Assets.grey_button,
				Assets.red_button,
				Constants.WIDTH/2 - Assets.grey_button.getWidth()/2,
				(int)(Constants.HEIGHT - Assets.grey_button.getHeight()*2.8),
				Constants.PLAY,
				new Action() {
					@Override
					public void doAction() {
						State.changeState(new LevelState());
					}
				}));
		
		buttons.add(new Button(
				Assets.grey_button,
				Assets.blue_button,
				(int)(Constants.WIDTH/2 + Assets.grey_button.getWidth()*0.7),
				(int)(Constants.HEIGHT - Assets.grey_button.getHeight()*2.8),
				Constants.EXIT,
				new Action() {
					@Override
					public void doAction() {
						System.exit(0);
					}
				}));
		
		buttons.add(new Button(
				Assets.grey_button,
				Assets.blue_button,
				(int)(Constants.WIDTH/2 - Assets.grey_button.getWidth()*1.7),
				(int)(Constants.HEIGHT - Assets
						.grey_button.getHeight()*2.8),
				Constants.HIGH_SCORES, 
				new Action() {
					@Override
					public void doAction() {
						State.changeState(new ScoreState());
					}
				}));
	}
	
	@Override
	public void update(float dt) {
		nm.update();
		for(Button b: buttons) {
			b.update();
		}
	}

	@Override
	public void draw(Graphics g) {
		
		g.drawImage(Assets.menu, 0,0, null);
		nm.draw(g);
		for(Button b: buttons) {
			b.draw(g);
		}
		
	}

}
