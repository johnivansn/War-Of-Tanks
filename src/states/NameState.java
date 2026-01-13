package states;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import graphics.Assets;
import graphics.Text;
import input.Name;
import math.Vector2D;
import ui.Action;
import ui.BoxText;
import ui.Button;

public class NameState{
	
	private BoxText boxText;
	private int n = 0;
	private int x, y;
	private boolean visible = false;
	private ArrayList<Button> buttons;

	public NameState(int x, int y) {
		this.x = x;
		this.y = y;

		boxText = new BoxText(x, y);
		
		buttons = new ArrayList<>();
		
		buttons.add(new Button(
				Assets.grey_button,
				Assets.blue_button,
				x - 40, y + 50,
				"cambiar", 
				new Action() {
					@Override
					public void doAction() {
						boxText.act = visible = true;
					}
				})); 
		
		buttons.add(new Button(
				Assets.grey_button,
				Assets.red_button,
				x + 150, y + 50,
				"hecho", 
				new Action() {
					@Override
					public void doAction() {
						n++;
						if (n == 1) {
							Name.name = boxText.getName();
						}
						boxText.act = visible = false;
					}
				}));
		//
	}
	
	public void update() {
		boxText.update();
		//
		if(visible) {
			boxText.update();
		}
		for (Button b : buttons) {
			b.update();
		}
		//
	}

	public void draw(Graphics g) {
		
		if (visible) {
			n = 0;
			boxText.draw(g);
		} else {
			g.setColor(Color.WHITE);
			g.fillRect(x, y, 300, 45);
			Text.drawText(
					g, 
					Name.name, 
					new Vector2D(
							x + 300 / 2, 
							y + 45 * 1.35), 
					true, 
					Color.BLACK,
					Assets.fontBig);
		}
		for (Button b : buttons) {
			b.draw(g);
		}
		//
	}
}
