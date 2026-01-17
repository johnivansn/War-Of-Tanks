package states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import gameObjects.Enemy;
import gameObjects.Enemy2;
import gameObjects.Enemy3;
import gameObjects.Message;
import gameObjects.Missile;
import gameObjects.MovingObject;
import gameObjects.Player;
import gameObjects.PowerUp;
import gameObjects.PowerUpTypes;
import gameObjects.Rocks;
import graphics.Animation;
import graphics.Assets;
import graphics.Sound;
import graphics.Text;
import input.Constants;
import input.KeyBoard;
import input.Links;
import input.Name;
import io.Area;
import io.JSONParser;
import io.ObjData;
import io.ScoreData;
import math.Vector2D;
import ui.Action;

public class GameState extends State {

	public static final Vector2D PLAYER_START_POSITION = new Vector2D(
			Constants.WIDTH / 2 - Assets.player.getWidth() / 2,
			Constants.HEIGHT / 2 - Assets.player.getHeight() / 2);

	private Player player;
	private ArrayList<MovingObject> movingObjects = new ArrayList<>();
	private ArrayList<Animation> explosions = new ArrayList<>();
	private ArrayList<Message> messages = new ArrayList<>();

	private ResetState panel = new ResetState();
	private String nickname = Name.name;

	private int score = 0;
	private int lives = 3;

	private int missile;
	private int waves = 0;

	private long gameOverTimer;
	private boolean gameOver;
	private boolean poder = false;
	private boolean expFinal = false;

	private long enemyCont, enemySpawner, enemySpawner2, enemySpawner3;
	private long powerUpSpawner;
	private long spawnTimeMissile;
	private int n, pau;
	private Sound explosion;
	// private Random r = new Random();

	public GameState(int m) {
		n = m;

		player = new Player(
				PLAYER_START_POSITION,
				new Vector2D(),
				Constants.PLAYER_MAX_VEL,
				Assets.player,
				this);

		gameOver = false;
		movingObjects.add(player);
		//
		loopBackgroundMusic();

		gameOverTimer = 0;
		// enemySpawner = enemySpawner2 = enemySpawner3 = enemySpawner4 = 1;
		enemyCont = 1;
		powerUpSpawner = 0;
		spawnTimeMissile = 0;
		pau = 0;
		gameOver = false;
		explosion = new Sound(Assets.poderShoot);

	}

	public void addScore(int value, Vector2D position) {
		Color c = Color.WHITE;
		String text = "+" + value + " score";

		if (player.isDoubleScoreOn()) {
			c = Color.YELLOW;
			value *= 2;
			text = "+" + value + " score" + " (X2)";
		}

		score += value;
		messages.add(new Message(
				position,
				true,
				text,
				c,
				false,
				Assets.fontMed));
	}

	private void spawnMissile() {

		int a = getRandom(1, 4);
		Vector2D headingRandom;
		double angleMissile;

		if (a == 1) {// izq arriba a der abajo
			headingRandom = new Vector2D(0, 1).setDirection(Math.random());
			angleMissile = headingRandom.getAngle() + Math.PI / 2;
		} else if (a == 2) {// der abajo a izq arriba
			headingRandom = new Vector2D(0, 1).setDirection(Math.random() + Math.PI);
			angleMissile = headingRandom.getAngle() + Math.PI / 2;
		} else if (a == 3) {// der arriba a izq abajo
			headingRandom = new Vector2D(0, 1).setDirection(Math.random() + Math.PI / 2);
			angleMissile = headingRandom.getAngle() + Math.PI / 2;
		} else {// izq abajo a der arriba
			headingRandom = new Vector2D(0, 1).setDirection(Math.random() - Math.PI / 2);
			angleMissile = headingRandom.getAngle() + Math.PI / 2;
		}

		for (int i = 0; i < missile; i++) {
			double x, y;
			if (a == 1) {
				// izq arriba a der abajo
				x = i % 2 == 0 ? (Math.random() * Constants.WIDTH - 100) : 0;
				y = i % 2 == 0 ? 0 : (Math.random() * Constants.HEIGHT - 100);
			} else if (a == 2) {
				// der abajo a izq arriba
				x = i % 2 == 0 ? (Math.random() * Constants.WIDTH - 100) : Constants.WIDTH - 10;
				y = i % 2 == 0 ? Constants.HEIGHT - 10 : (Math.random() * Constants.HEIGHT - 100);
			} else if (a == 3) {
				// der arriba a izq abajo
				x = i % 2 == 0 ? (Math.random() * Constants.WIDTH - 100) : Constants.WIDTH - 10;
				y = i % 2 == 0 ? 0 : (Math.random() * Constants.HEIGHT - 100);
			} else {
				// izq abajo a der arriba
				x = i % 2 == 0 ? (Math.random() * Constants.WIDTH - 100) : 0;
				y = i % 2 == 0 ? Constants.HEIGHT - 10 : (Math.random() * Constants.HEIGHT - 100);
			}

			movingObjects.add(new Missile(
					new Vector2D(x, y),
					headingRandom,
					Constants.MISSILE_VEL * Math.random() + 2,
					angleMissile,
					Assets.missile,
					this));
		}
	}

	public void playExplosion1(Vector2D position) {

		explosions.add(new Animation(
				Assets.exp1,
				1,
				position.subtract(new Vector2D(
						Assets.exp1[0].getWidth() / 2,
						Assets.exp1[0].getHeight() / 2))));
	}

	public void playExplosion2(Vector2D position) {

		explosions.add(new Animation(
				Assets.exp2,
				10,
				position.subtract(new Vector2D(
						Assets.exp2[0].getWidth() / 2,
						Assets.exp2[0].getHeight() / 2))));
	}

	public void playExplosion3(Vector2D position) {

		explosions.add(new Animation(
				Assets.exp3,
				1,
				position.subtract(new Vector2D(
						Assets.exp3[0].getWidth() / 2,
						Assets.exp3[0].getHeight() / 2))));

	}

	private void spawnEnemy() {
		int rand = (int) (Math.random() * 2);
		int a = getRandom(1, 10);

		for (int i = 0; i < enemySpawner; i++) {
			double x, y;
			if (a % 2 == 0) {
				x = rand == 0 ? (Math.random() * Constants.WIDTH) : Constants.WIDTH;
				y = rand == 0 ? Constants.HEIGHT : (Math.random() * Constants.HEIGHT);
			} else {
				x = rand == 0 ? (Math.random() * Constants.WIDTH) : 0;
				y = rand == 0 ? 0 : (Math.random() * Constants.HEIGHT);
			}
			ArrayList<Vector2D> path = new ArrayList<>(); // almacena puntos de seguimiento

			path.add(new Vector2D(x, y)); // punto de inicio

			double posX, posY;
			int[] nodo = numRandom4();// int nodo = getRandom(1, 4);
			for (int j = 0; j < 4; j++) {// genera puntos de seguimientos
				if (nodo[j] == 1) {
					// arriba izq
					posX = getRandom(50, (Constants.WIDTH / 2) - 100);
					posY = getRandom(50, (Constants.HEIGHT / 2) - 100);
				} else if (nodo[j] == 2) {
					// arriba der
					posX = getRandom((Constants.WIDTH / 2) + 50, Constants.WIDTH - 100);
					posY = getRandom(50, (Constants.HEIGHT / 2) - 100);
				} else if (nodo[j] == 3) {
					// abajo izq
					posX = getRandom(50, (Constants.WIDTH / 2) - 100);
					posY = getRandom((Constants.HEIGHT / 2) + 50, Constants.HEIGHT - 100);
				} else {
					// abajo der
					posX = getRandom((Constants.WIDTH / 2) + 50, Constants.WIDTH - 100);
					posY = getRandom((Constants.HEIGHT / 2) + 50, Constants.HEIGHT - 100);
				}
				path.add(new Vector2D(posX, posY));
			}
			movingObjects.add(new Enemy(
					new Vector2D(path.get(0).getX(), path.get(0).getY()),
					new Vector2D(),
					Constants.ENEMY_MAX_VEL,
					Assets.enemy,
					path, // pega los puntos de seguimientos
					this));
		}
	}

	private void spawnEnemy2() {
		int rand = getRandom(1, 10);// ((int) (Math.random() * ((10 + 1) - 1) + 1));//de 1 a 10
		int a = getRandom(1, 4);

		for (int i = 0; i < enemySpawner2; i++) {
			double x, y, posX, posY;
			ArrayList<Vector2D> path = new ArrayList<>(); // almacena puntos de seguimiento
			if (a == 1) {
				a++;
				x = rand % 2 == 0 ? getRandom(50, 150) : getRandom(Constants.WIDTH - 150, Constants.WIDTH - 50);
				y = Constants.HEIGHT;
				path.add(new Vector2D(x, y)); // punto de inicio

				posY = getRandom(50, 150);
				path.add(new Vector2D(x, posY));

				posX = Constants.WIDTH - 10 - x;
				path.add(new Vector2D(posX, posY));
			} else if (a == 2) {
				a++;
				x = Constants.WIDTH;
				y = rand % 2 == 0 ? getRandom(Constants.HEIGHT - 150, Constants.HEIGHT - 50) : getRandom(50, 150);
				path.add(new Vector2D(x, y)); // punto de inicio

				posX = getRandom(50, 150);
				path.add(new Vector2D(posX, y));

				posY = Constants.HEIGHT - 10 - y;
				path.add(new Vector2D(posX, posY));
			} else if (a == 3) {
				a++;
				x = rand % 2 == 0 ? getRandom(Constants.WIDTH - 150, Constants.WIDTH - 50) : getRandom(50, 150);
				y = 0;
				path.add(new Vector2D(x, y)); // punto de inicio

				posY = getRandom(Constants.HEIGHT - 150, Constants.HEIGHT - 50);
				path.add(new Vector2D(x, posY));

				posX = Constants.WIDTH - 10 - x;
				path.add(new Vector2D(posX, posY));
			} else {
				a = 1;
				x = 0;
				y = rand % 2 == 0 ? getRandom(50, 150) : getRandom(Constants.HEIGHT - 150, Constants.HEIGHT - 50);
				path.add(new Vector2D(x, y)); // punto de inicio

				posX = getRandom(Constants.WIDTH - 150, Constants.WIDTH - 50);
				path.add(new Vector2D(posX, y));

				posY = Constants.HEIGHT - 10 - y;
				path.add(new Vector2D(posX, posY));
			}

			movingObjects.add(new Enemy2(
					new Vector2D(path.get(0).getX(), path.get(0).getY()),
					new Vector2D(),
					Constants.ENEMY_MAX_VEL,
					Assets.enemyA,
					path, // pega los puntos de seguimientos
					this));
		}
	}

	private void spawnEnemy3() {

		double x, y;
		int a = getRandom(1, 10);

		for (int i = 0; i < enemySpawner3; i++) {

			if (a % 2 == 0) {
				x = i % 2 == 0 ? (Math.random() * Constants.WIDTH) : Constants.WIDTH - 10;
				y = i % 2 == 0 ? Constants.HEIGHT - 10 : (Math.random() * Constants.HEIGHT);
			} else {
				x = i % 2 == 0 ? (Math.random() * Constants.WIDTH) : 0;
				y = i % 2 == 0 ? 0 : (Math.random() * Constants.HEIGHT);
			}

			ArrayList<Vector2D> path = new ArrayList<>(); // almacena puntos de seguimiento

			path.add(new Vector2D(x, y)); // punto de inicio
			path.add(new Vector2D(0, 0));

			movingObjects.add(new Enemy3(
					new Vector2D(path.get(0).getX(), path.get(0).getY()),
					new Vector2D(x, y),
					Constants.ENEMY_MAX_VEL - 2.0,
					Assets.enemyR,
					path,
					this));
		}
	}

	private void spawnObj() {
		Links.links = Links.getLinks();
		BufferedImage texture = null;

		try {
			ArrayList<ObjData> dataList = Area.readFile(Links.links.get(Constants.SELECT - 1));
			for (int i = 0; i < dataList.size(); i++) {
				if (waves < 2) {
					if (dataList.get(i).getType().equals("Item1")) {
						texture = Assets.Item1;
					}
					if (dataList.get(i).getType().equals("Item2")) {
						texture = Assets.Item2;
					}
					if (dataList.get(i).getType().equals("piedra")) {
						texture = Assets.piedra;
					}

					movingObjects.add(new Rocks(
							new Vector2D(
									dataList.get(i).getPositionX(),
									dataList.get(i).getPositionY()),
							texture,
							this));
				} else {
					if (dataList.get(i).getType().equals("Item1")) {
						texture = Assets.Item1;
					}
					if (dataList.get(i).getType().equals("Item2")) {
						texture = Assets.Item2;
					}
					if (!dataList.get(i).getType().equals("piedra")) {
						movingObjects.add(new Rocks(
								new Vector2D(
										dataList.get(i).getPositionX(),
										dataList.get(i).getPositionY()),
								texture,
								this));
					}

				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private void spawnPowerUp() {

		final int x = (int) ((Constants.WIDTH - Assets.orb.getWidth()) * Math.random());
		final int y = (int) ((Constants.HEIGHT - Assets.orb.getHeight()) * Math.random());

		int index = (int) (Math.random() * (PowerUpTypes.values().length));

		PowerUpTypes p = PowerUpTypes.values()[index];

		final String text = p.text;
		Action action = null;
		Vector2D position = new Vector2D(x, y);

		switch (p) {
			case FIRES:
				action = new Action() {
					@Override
					public void doAction() {
						player.setMulti();
						messages.add(new Message(
								position,
								false,
								text,
								Color.BLUE,
								false,
								Assets.fontMed));
					}
				};
				break;
			case LIFE:
				action = new Action() {
					@Override
					public void doAction() {
						lives++;
						messages.add(new Message(
								position,
								false,
								text,
								Color.GREEN,
								false,
								Assets.fontMed));
					}
				};
				break;
			case SHIELD:
				action = new Action() {
					@Override
					public void doAction() {
						player.setShield();
						messages.add(new Message(
								position,
								false,
								text,
								Color.DARK_GRAY,
								false,
								Assets.fontMed));
					}
				};
				break;
			case SCORE_X2:
				action = new Action() {
					@Override
					public void doAction() {
						player.setDoubleScore();
						messages.add(new Message(
								position,
								false,
								text,
								Color.YELLOW,
								false,
								Assets.fontMed));
					}
				};
				break;
			case FASTER_FIRE:
				action = new Action() {
					@Override
					public void doAction() {
						player.setFastFire();
						messages.add(new Message(
								position,
								false,
								text,
								Color.BLUE,
								false,
								Assets.fontMed));
					}
				};
				break;
			case SCORE_STACK:
				action = new Action() {
					@Override
					public void doAction() {
						score += 1000;
						messages.add(new Message(
								position,
								false,
								text,
								Color.MAGENTA,
								false,
								Assets.fontMed));
					}
				};
				break;
			case DOUBLE_GUN:
				action = new Action() {
					@Override
					public void doAction() {
						player.setDoubleGun();
						messages.add(new Message(
								position,
								false,
								text,
								Color.ORANGE,
								false,
								Assets.fontMed));
					}
				};
				break;
			default:
				break;
		}

		this.movingObjects.add(new PowerUp(
				position,
				p.texture,
				action,
				this));

	}

	private void detectCollisions() {
		int size = movingObjects.size();

		for (int i = 0; i < size; i++) {
			MovingObject objA = movingObjects.get(i);
			if (objA.isDead())
				continue;

			for (int j = i + 1; j < size; j++) {
				MovingObject objB = movingObjects.get(j);
				if (objB.isDead())
					continue;

				double distance = objA.getCenter().subtract(objB.getCenter()).getMagnitude();
				double minDistance = (objA.getWidth() + objB.getWidth()) / 2;

				if (distance < minDistance) {
					if (objA instanceof Rocks && ((Rocks) objA).getTexture().equals(Assets.piedra)) {
						objA.handleCollisionWithRocks(objB);
					} else if (objB instanceof Rocks && ((Rocks) objB).getTexture().equals(Assets.piedra)) {
						objB.handleCollisionWithRocks(objA);
					} else {
						objA.handleCollision(objB);
					}
				}
			}
		}
	}

	public void update(float dt) {
		pau += dt;
		if (gameOver)
			gameOverTimer += dt;

		powerUpSpawner += dt;
		spawnTimeMissile += dt;

		if (KeyBoard.BtEsc) {
			pau = 0;
			panel.level(n);
			panel.update();
		}
		if (pau > 0) {

			for (int i = movingObjects.size() - 1; i >= 0; i--) {
				MovingObject mo = movingObjects.get(i);
				mo.update(dt);
				if (mo.isDead())
					movingObjects.remove(i);
			}

			detectCollisions();

			//
			for (int i = explosions.size() - 1; i >= 0; i--) {
				Animation anim = explosions.get(i);
				anim.update(dt);
				if (!anim.isRunning()) explosions.remove(i);
			}
			//
			if (gameOverTimer > Constants.GAME_OVER_TIME) {

				try {
					ArrayList<ScoreData> dataList = JSONParser.readFile();
					dataList.add(new ScoreData(score, nickname));
					JSONParser.writeFile(dataList);

				} catch (IOException e) {
					e.printStackTrace();
				}
				stopBackgroundMusic();
				State.changeState(new MenuState());
				loopTheme();
			}
			//
			if (powerUpSpawner > Constants.POWER_UP_SPAWN_TIME) {
				spawnPowerUp();
				powerUpSpawner = 0;
			}

			if (score > 500 && expFinal == false) {
				Message lifeLostMesg1 = new Message(
						player.getPosition(),
						false,
						"ONDA EXPLOSIVA DISPONIBLE",
						Color.RED,
						false,
						Assets.fontMed);
				messages.add(lifeLostMesg1);
				expFinal = true;
			}

			if (score > 500 && poder == false) {
				if (KeyBoard.x) {
					playExplosion3(player.getPosition());
					for (int i = 0; i < movingObjects.size(); i++) {
						if (movingObjects.get(i) instanceof Player) {

						} else if (movingObjects.get(i) instanceof Rocks) {

						} else {
							movingObjects.get(i).Destroy();
						}
						explosion.play();
						poder = true;
					}
				}
			}

			if (spawnTimeMissile > 7000) {
				spawnMissile();
				spawnTimeMissile = 0;
			}

			for (int i = 0; i < movingObjects.size(); i++)
				if (movingObjects.get(i) instanceof Enemy)
					return;
			for (int i = 0; i < movingObjects.size(); i++)
				if (movingObjects.get(i) instanceof Enemy2)
					return;
			for (int i = 0; i < movingObjects.size(); i++)
				if (movingObjects.get(i) instanceof Enemy3)
					return;
			spawnObj();
			waves++;
			messages.add(new Message(
					new Vector2D(Constants.WIDTH / 2, Constants.HEIGHT / 2),
					true,
					"WAVE " + waves,
					Color.WHITE,
					true,
					Assets.fontBig));

			difficulty(n);
		}
	}

	public void draw(Graphics g) {
		//
		g.drawImage(Assets.area, 0, 0, null);
		//

		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		for (int i = messages.size() - 1; i >= 0; i--) {
			messages.get(i).draw(g2d);
			if (messages.get(i).isDead())
				messages.remove(i);
		}

		for (int i = 0; i < movingObjects.size(); i++)
			movingObjects.get(i).draw(g);

		for (int i = 0; i < explosions.size(); i++) {
			Animation anim = explosions.get(i);
			g2d.drawImage(
					anim.getCurrentFrame(),
					(int) anim.getPosition().getX(),
					(int) anim.getPosition().getY(),
					null);
		}
		drawName(g);
		drawScore(g);
		drawLives(g);

		// zona de spawn de enemy
		boolean mostrar = false;

		if (mostrar) {
			g.setColor(Color.WHITE);
			g2d.drawRect(50, 50, (Constants.WIDTH / 2) - 100, (Constants.HEIGHT / 2) - 100);
			g2d.drawRect((Constants.WIDTH / 2) + 50, 50, (Constants.WIDTH / 2) - 100, (Constants.HEIGHT / 2) - 100);
			g2d.drawRect(50, (Constants.HEIGHT / 2) + 50, (Constants.WIDTH / 2) - 100,
					(Constants.HEIGHT / 2) - 100);
			g2d.drawRect((Constants.WIDTH / 2) + 50, 325 + 50, (Constants.WIDTH / 2) -
					100, (Constants.HEIGHT / 2) - 100);

			// recorrido de enemy2

			g.setColor(Color.CYAN);
			g2d.drawLine(50, 0, 50, 650);
			g2d.drawLine(150, 0, 150, 650);

			g2d.drawLine(1050, 0, 1050, 650);
			g2d.drawLine(950, 0, 950, 650);

			g2d.drawLine(0, 50, 1100, 50);
			g2d.drawLine(0, 150, 1100, 150);

			g2d.drawLine(0, 500, 1100, 500);
			g2d.drawLine(0, 600, 1100, 600);
		}
		if (KeyBoard.BtEsc) {
			panel.draw(g);
		}
	}

	private void drawScore(Graphics g) {
		Vector2D pos = new Vector2D(850, 25);

		String scoreToString = Integer.toString(score);

		for (int i = 0; i < scoreToString.length(); i++) {
			g.drawImage(
					Assets.num[Integer.parseInt(scoreToString.substring(i, i + 1))],
					(int) pos.getX(),
					(int) pos.getY(),
					null);

			pos.setX(pos.getX() + 20);
		}
	}

	private void drawName(Graphics g) {
		Text.drawText(
				g,
				Name.name,
				new Vector2D(410 + 74, 40),
				false,
				Color.WHITE,
				Assets.fontBig);
	}

	private void drawLives(Graphics g) {
		if (lives < 1)
			return;

		Vector2D livePosition = new Vector2D(50, 25);

		g.drawImage(
				Assets.life,
				(int) livePosition.getX(),
				(int) livePosition.getY() - 10,
				null);

		g.drawImage(
				Assets.num[10],
				(int) livePosition.getX() + 40,
				(int) livePosition.getY() + 5,
				null);

		String livesToString = Integer.toString(lives);

		Vector2D pos = new Vector2D(livePosition.getX(), livePosition.getY());

		for (int i = 0; i < livesToString.length(); i++) {

			int number = Integer.parseInt(livesToString.substring(i, i + 1));

			if (number <= 0)
				break;

			g.drawImage(
					Assets.num[number],
					(int) livePosition.getX() + 60,
					(int) livePosition.getY() + 5,
					null);

			pos.setX(pos.getX() + 20);
		}
	}

	public ArrayList<MovingObject> getMovingObjects() {
		return movingObjects;
	}

	public ArrayList<Message> getMessages() {
		return messages;
	}

	public ArrayList<Animation> getAnimation() {
		return explosions;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean subtractLife(Vector2D position) {
		lives--;

		Message lifeLostMesg = new Message(
				position,
				false,
				"-1 LIFE",
				Color.RED,
				false,
				Assets.fontMed);

		messages.add(lifeLostMesg);
		return lives > 0;
	}

	public void gameOver() {
		Message gameOverMsg = new Message(
				new Vector2D(Constants.WIDTH / 2 - Assets.player.getWidth() / 2,
						Constants.HEIGHT / 2 - Assets.player.getHeight() / 2),
				true,
				"GAME OVER",
				Color.WHITE,
				true,
				Assets.fontBig);

		this.messages.add(gameOverMsg);
		gameOver = true;
	}

	private static int getRandom(int min, int max) { // genera N aleatorio de min a max
		return ((int) (Math.random() * (max - min + 1) + min));
	}

	private static int[] numRandom4() { // genera N° 1,2,3,4 en diferente orden
		int pos, nNums = 4;
		int[] arrayRandom = new int[nNums];
		Stack<Integer> conjunto = new Stack<>();

		for (int i = 0; i < arrayRandom.length; i++) {
			pos = (int) Math.floor(Math.random() * nNums + 1);
			arrayRandom[i] = pos;
			while (conjunto.contains(pos)) {
				pos = (int) Math.floor(Math.random() * nNums + 1);
				arrayRandom[i] = pos;
			}
			conjunto.push(pos);
		}
		return arrayRandom;
	}

	public void difficulty(int n) {
		if (n == 0) {
			missile = (missile <= 4) ? missile + 1 : 1;
			enemyCont += (waves % 20 == 0) ? 1 : 0;
		}
		if (n == 1) {
			missile = (missile <= 7) ? missile + 1 : 4;
			enemyCont += (waves % 10 == 0) ? 1 : 0;
		}
		if (n == 2) {
			missile = (missile <= 7) ? missile + 1 : 4;
			enemyCont += (waves % 10 == 0) ? 1 : 0;
		}
		SpawnEnemyA();
	}

	private void SpawnEnemyA() {
		enemySpawner = enemySpawner2 = enemySpawner3 = 0;
		for (int i = 0; i < enemyCont; i++) {
			int x = getRandom(1, 3);
			if (x == 1) {
				enemySpawner++;
			} else if (x == 2) {
				enemySpawner2++;
			} else {
				enemySpawner3++;
			}
		}
		spawnEnemy();
		spawnEnemy2();
		spawnEnemy3();
	}
}
