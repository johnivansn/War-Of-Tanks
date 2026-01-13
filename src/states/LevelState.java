package states;

import java.awt.Graphics;
import java.util.ArrayList;

import graphics.Assets;
import input.Constants;
import ui.Action;
import ui.Button;
import ui.ChargerObjs;

public class LevelState extends State {
	private ChargerObjs chargerObjs;
	private Button returnButton;
	private ArrayList<Button> buttons;

	public LevelState() {
		//boxText = new BoxText(x = 210, y = 75);
		//nm = new NameState(210, 75);
		chargerObjs = new ChargerObjs(340);
		returnButton = new Button(
				Assets.grey_button, 
				Assets.blue_button, 
				Assets.grey_button.getHeight(),
				(int) (Constants.HEIGHT - Assets.grey_button.getHeight() * 2.3), 
				Constants.RETURN, 
				new Action() {
					@Override
					public void doAction() {
						State.changeState(new MenuState());
					}
				});

		buttons = new ArrayList<>();

		buttons.add(new Button(
				Assets.grey_button, 
				Assets.blue_button,
				(int) (Constants.WIDTH / 2 - Assets.grey_button.getWidth() * 1.7),
				(Constants.HEIGHT - Assets.grey_button.getHeight() * 4), 
				Constants.FACIL, 
				new Action() {
					@Override
					public void doAction() {
						stopTheme();
						State.changeState(new GameState(0));
					}
				}));

		buttons.add(new Button(
				Assets.grey_button, 
				Assets.blue_button,
				Constants.WIDTH / 2 - Assets.grey_button.getWidth() / 2,
				(Constants.HEIGHT - Assets.grey_button.getHeight() * 4), 
				Constants.NORMAL, 
				new Action() {
					@Override
					public void doAction() {
						stopTheme();
						State.changeState(new GameState(1));
					}
				}));

		buttons.add(new Button(
				Assets.grey_button, 
				Assets.blue_button,
				(int) (Constants.WIDTH / 2 + Assets.grey_button.getWidth() * 0.7),
				(Constants.HEIGHT - Assets.grey_button.getHeight() * 4), 
				Constants.AVANZADO, 
				new Action() {
					@Override
					public void doAction() {
						stopTheme();
						State.changeState(new GameState(2));
					}
				}));
		
		buttons.add(new Button(
				Assets.grey_button, 
				Assets.blue_button,
				Constants.WIDTH - Assets.grey_button.getWidth() - Assets.grey_button.getHeight(),
				(int) (Constants.HEIGHT - Assets.grey_button.getHeight() * 2.3), 
				"new map", 
				new Action() {
					@Override
					public void doAction() {
						stopTheme();
						State.changeState(new SelectEdit());
					}
				}));
		//
	}

	@Override
	public void update(float dt) {
		chargerObjs.update(dt);
		//nm.update();
		for (Button b : buttons) {
			b.update();
		}
		returnButton.update();
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(Assets.lvlMenu, 0, 0, null);
		chargerObjs.draw(g);
		//nm.draw(g);
		for (Button b : buttons) {
			b.draw(g);
		}

		returnButton.draw(g);
		
	}
}