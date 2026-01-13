package states;

import java.awt.Graphics;
import java.util.ArrayList;

import graphics.Assets;
import input.Constants;
import ui.Action;
import ui.Button;
import ui.ChargerObjs;

public class SelectEdit extends State {
	private ChargerObjs chargerObjs;
	private Button returnButton;
	private ArrayList<Button> buttons;
	
	public SelectEdit() {
		loopbackEdit();
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
						stopBackEdit();
						State.changeState(new MenuState());
					}
				});

		buttons = new ArrayList<>();

		buttons.add(new Button(
				Assets.grey_button, 
				Assets.blue_button,
				200,
				368, 
				"NUEVO MAPA", 
				new Action() {
					@Override
					public void doAction() {
						stopTheme();
						State.changeState(new EditState());
					}
				}));

		buttons.add(new Button(
				Assets.grey_button, 
				Assets.blue_button,
				700,
				368, 
				"SEGUIR EDITANDO", 
				new Action() {
					@Override
					public void doAction() {
						stopTheme();
						State.changeState(new EditState2());
					}
				}));
	}

	@Override
	public void update(float dt) {
		chargerObjs.update(dt);
		for (Button b : buttons) {
			b.update();
		}
		returnButton.update();
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(Assets.lvlMenu, 0, 0, null);
		chargerObjs.draw(g);
		for (Button b : buttons) {
			b.draw(g);
		}
		returnButton.draw(g);
	}
}
