package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import graphics.Assets;
import graphics.Resize;
import graphics.Text;
import input.Constants;
import input.Links;
import io.Area;
import io.ObjData;
import math.Vector2D;

public class ChargerObjs {
	private ArrayList<Button> buttons;
	private int prt = 1;
	private int n, cont, y; 
	private int limit = Links.links.size();
	private boolean delete = false;
	public ChargerObjs(int y) {
		
		this.y = y;
		Links.links = Links.getLinks();
		buttons = new ArrayList<>();

		buttons.add(new Button(
				Assets.grey_left, 
				Assets.green_left,
				Constants.WIDTH / 2 - 100 / 2 - Assets.green_left.getWidth() * 2,
				y + Assets.grey_buttonBig.getWidth() / 2 - Assets.green_left.getHeight() / 2 ,
				"", 
				new Action() {
					@Override
					public void doAction() {
						n++;
						if (n == 1) {
							prt--;
							if (prt == 0) {
								prt = limit;
							}
						}
					}
				}));
		
		buttons.add(new Button(
				Assets.grey_right, 
				Assets.green_right,
				Constants.WIDTH / 2 + 100 / 2 + Assets.green_right.getWidth(),
				y + Assets.grey_buttonBig.getWidth() / 2 - Assets.green_right.getHeight() / 2 ,
				"", 
				new Action() {
					@Override
					public void doAction() {
						n++;
						if (n == 1) {
							prt++;
							if (prt == limit + 1) {
								prt = 1;
							}
						}
					}
				}));
		
		buttons.add(new Button(
				Assets.dump, 
				Assets.dump,
				Constants.WIDTH / 2 + 325 / 2 + Assets.green_right.getWidth(),
				y / 2 + Assets.grey_buttonBig.getWidth() / 2 - Assets.green_right.getHeight() / 2 - 5,
				"", 
				new Action() {
					@Override
					public void doAction() {
						n++;
						if (n == 1) {
							if (prt > 1) {
								Constants.SELECT = prt--;
								//Links.deletLink(prt + 1);
								delete = true;
							}
						}
					}
				}));
	}
	
	public void update(float dt) {
		for (Button b : buttons) {
			b.update();
		}
		cont += dt;
		if (cont > 800) {
			n = cont = 0;
		}
	}
	
	public void draw(Graphics g) {
		//pongo el "0.3333" en lugar de "1/3" porque en Resize() mandaba error
		
		g.drawImage(
				Resize.getResize(Assets.miniCampo, 0.3333), 
				Constants.WIDTH / 2 - Constants.WIDTH / 3 / 2,
				y - 210, 
				null);
		//
		BufferedImage image = null;
		if (!delete) {
			try {
				ArrayList<ObjData> dataList = Area.readFile(Links.links.get(Constants.SELECT - 1));
				for (int i = 0; i < dataList.size(); i++) {
					if (dataList.get(i).getType().equals("Item1")) {
						image = Assets.Item1;
					}
					if (dataList.get(i).getType().equals("Item2")) {
						image = Assets.Item2;
					}
					if (dataList.get(i).getType().equals("piedra")) {
						image = Assets.piedra;
					}
					BufferedImage imageN = Resize.getResize(image, 0.3333);
					g.drawImage(
							imageN,
							(dataList.get(i).getPositionX() / 3) + (Constants.WIDTH / 2 - Constants.WIDTH / 3 / 2) - imageN.getWidth() / 2,
							(dataList.get(i).getPositionY() / 3) + y - 210, null);

				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			Links.deleteLink(prt + 1);
			delete = false;
		}
		
		//
		g.fillRect(
				Constants.WIDTH / 2 + 325 / 2 + Assets.green_right.getWidth() - 5,
				y / 2 + Assets.grey_buttonBig.getWidth() / 2 - Assets.green_right.getHeight() / 2 - 10,
				Assets.dump.getWidth() + 10,
				Assets.dump.getHeight() + 10);
		//
		for (Button b : buttons) {
			b.draw(g);
		}
		int width = 100, height = 45, posX, posY;
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(
				posX = Constants.WIDTH / 2 - width / 2,
				posY = y + 2 * 45 / 3, 
				width, 
				height);
		
		Text.drawText(
				g, 
				(Constants.SELECT = prt) + "",
				new Vector2D(
						posX + width / 2, 
						posY + height / 0.75),
				true, 
				Color.DARK_GRAY, 
				Assets.fontBig);
	}
}