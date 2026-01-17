package states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import graphics.Assets;
import input.Constants;
import input.Links;
import io.Area;
import io.ObjData;
import math.Vector2D;
import ui.Action;
import ui.Button;
import ui.Obj;

public class EditState2 extends State {

	private ArrayList<Button> buttons;
	private ArrayList<Button> buttonsN;
	private ArrayList<Obj> objs = new ArrayList<>();
	private Obj dump;
	private String type;
	private static String link;
	private int posX, posY;
	private int n, cont;
	private double heightEdit = Constants.HEIGHT - Constants.HEIGHT / Constants.EDITOR_SCALE;

	public EditState2() {
		loopbackEdit();
		buttons = new ArrayList<>();
		buttonsN = new ArrayList<>();
		load();
		dump = new Obj(
				Assets.dump,
				Color.WHITE,
				new Vector2D(50, (int) (heightEdit - 50)),
				true,
				false);

		buttonsN.add(new Button(
				Assets.Item1,
				Assets.Item1,
				935,230,
				"",
				new Action() {
					@Override
					public void doAction() {
						n++;
						if (n == 1) {
							objs.add(new Obj(Assets.Item1, Color.CYAN, new Vector2D(), true, true));
						}
					}
				}));

		buttonsN.add(new Button(
				Assets.Item2,
				Assets.Item2,
				935,363,
				"",
				new Action() {
					@Override
					public void doAction() {
						n++;
						if (n == 1) {
							objs.add(new Obj(Assets.Item2, Color.CYAN, new Vector2D(), true, true));
						}
					}
				}));

		buttonsN.add(new Button(
				Assets.piedra,
				Assets.piedra,
				950,540,
				"",
				new Action() {
					@Override
					public void doAction() {
						n++;
						if (n == 1) {
							objs.add(new Obj(Assets.piedra, Color.CYAN, new Vector2D(), true, true));
						}
					}
				}));

		buttons.add(new Button(
				Assets.grey_button,
				Assets.blue_button,
				100,50,
				"MENU",
				new Action() {
					@Override
					public void doAction() {
						n++;
						if (n == 1) {
							stopBackEdit();
							State.changeState(new MenuState());
							loopTheme();
						}
					}
				}));

		buttons.add(new Button(
				Assets.grey_button,
				Assets.blue_button,
				900 / 2 - Assets.blue_button.getWidth() / 2, 50,
				"REINICIAR",
				new Action() {
					@Override
					public void doAction() {
						n++;
						if (n == 1) {
							objs.clear();
						}
					}
				}));

		buttons.add(new Button(
				Assets.grey_button,
				Assets.blue_button,
				800 - Assets.blue_button.getWidth(), 50,
				"GUARDAR",
				new Action() {
					@Override
					public void doAction() {
						n++;
						if (n == 1) {
							save();
							Links.links = Links.getLinks();
							stopBackEdit();
							State.changeState(new LevelState());
							loopTheme();
						}
					}
				}));
	}

	@Override
	public void update(float dt) {
		cont += dt;
		if (cont > 2000) {
			n = cont = 0;
		}

		dump.update();
		dump.vis = true;
		for (int i = objs.size() - 1; i >= 0; i--) {
			if (objs.get(i).mouseIn && dump.mouseIn) {
				objs.remove(i);
			}
		}
		//


		for (Button b : buttonsN) {
			b.update();
		}

		for (Button b : buttons) {
			b.update();
		}

		for (Obj a : objs) {
			a.update();
			a.limit = true;

		}

		for (int i = 0; i < objs.size(); i++) {
			if (objs.get(i).mouseIn) {
				int num = i;
				objs.get(i).vis = false;
				for (int j = i; j < objs.size(); j++) {
					if (num != j) {
						objs.get(j).vis = true;
						objs.get(j).mouseIn = false;
					}
				}
			}
		}
		//
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(Assets.editMenu, 0, 0, null);

		dump.draw(g);

		for (Button b : buttonsN) {
			b.resize = b.mouseIn;
			b.draw(g);
		}

		for (Button b : buttons) {
			b.draw(g);
		}

		for (Obj a : objs) {
			a.draw(g);
		}
	}

	public  ArrayList<Obj> getObjs() {
		return objs ;
	}

	public  void save() {
		link = Links.newLink();
		for (int i = 0; i < objs.size(); i++) {
			// type = "NotIf";
			if (objs.get(i).getTexture().equals(Assets.Item1)) {
				type = "Item1";
			}
			if (objs.get(i).getTexture().equals(Assets.Item2)) {
				type = "Item2";
			}
			if (objs.get(i).getTexture().equals(Assets.piedra)) {
				type = "piedra";
			}
			posX = (int) (objs.get(i).getPosition().getX() * Constants.GAME_SCALE);
			posY = (int) (objs.get(i).getPosition().getY() * Constants.GAME_SCALE - heightEdit);
			try {
				ArrayList<ObjData> dataList = Area.readFile(link);
				dataList.add(new ObjData(type, posX, posY));
				Area.writeFile(dataList, link);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void load()  {
		Links.links = Links.getLinks();
		BufferedImage texture = null;
		try {
			ArrayList<ObjData> dataList = Area.readFile(Links.links.get(Constants.SELECT - 1));
			for (int i = 0; i < dataList.size(); i++) {
				if (dataList.get(i).getType().equals("Item1")) {
					texture = Assets.Item1;
				}
				if (dataList.get(i).getType().equals("Item2")) {
					texture = Assets.Item2;
				}
				if (dataList.get(i).getType().equals("piedra")) {
					texture = Assets.piedra;
				}
				objs.add(new Obj(
						texture,
						Color.CYAN,
						new Vector2D(
								dataList.get(i).getPositionX() / Constants.GAME_SCALE,
								dataList.get(i).getPositionY() / Constants.GAME_SCALE + (heightEdit - 50)),
						true,
						false));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
