package states;

import java.util.ArrayList;

import gameObjects.Enemy;
import gameObjects.Enemy2;
import gameObjects.Enemy3;
import gameObjects.Missile;
import gameObjects.PowerUp;
import gameObjects.PowerUpTypes;
import graphics.Assets;
import input.Constants;
import math.Vector2D;
import ui.Action;

public class SpawnManager {
	private final GameState gameState;

	private long enemyCont;
	private long missileTimer;
	private long powerUpTimer;

	private int enemySpawner, enemySpawner2, enemySpawner3;
	private int missileCount;

	public SpawnManager(GameState gameState) {
		this.gameState = gameState;
		enemyCont = 1;
		missileTimer = 0;
		powerUpTimer = 0;
	}

	public void update(float dt) {
		missileTimer += dt;
		powerUpTimer += dt;

		if (missileTimer > 7000) {
			spawnMissiles();
			missileTimer = 0;
		}

		if (powerUpTimer > Constants.POWER_UP_SPAWN_TIME) {
			spawnPowerUp();
			powerUpTimer = 0;
		}
	}

	public void spawnWave(int wave, int difficulty) {
		calculateDifficulty(wave, difficulty);
		spawnEnemies();
	}

	private void calculateDifficulty(int wave, int difficulty) {
		if (difficulty == 0) {
			missileCount = (missileCount <= 4) ? missileCount + 1 : 1;
			enemyCont += (wave % 20 == 0) ? 1 : 0;
		} else if (difficulty == 1) {
			missileCount = (missileCount <= 7) ? missileCount + 1 : 4;
			enemyCont += (wave % 10 == 0) ? 1 : 0;
		} else if (difficulty == 2) {
			missileCount = (missileCount <= 7) ? missileCount + 1 : 4;
			enemyCont += (wave % 10 == 0) ? 1 : 0;
		}

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
	}

	private void spawnEnemies() {
		spawnEnemy();
		spawnEnemy2();
		spawnEnemy3();
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

			ArrayList<Vector2D> path = new ArrayList<>();
			path.add(new Vector2D(x, y));

			double posX, posY;
			int[] nodo = numRandom4();
			for (int j = 0; j < 4; j++) {
				if (nodo[j] == 1) {
					posX = getRandom(50, (Constants.WIDTH / 2) - 100);
					posY = getRandom(50, (Constants.HEIGHT / 2) - 100);
				} else if (nodo[j] == 2) {
					posX = getRandom((Constants.WIDTH / 2) + 50, Constants.WIDTH - 100);
					posY = getRandom(50, (Constants.HEIGHT / 2) - 100);
				} else if (nodo[j] == 3) {
					posX = getRandom(50, (Constants.WIDTH / 2) - 100);
					posY = getRandom((Constants.HEIGHT / 2) + 50, Constants.HEIGHT - 100);
				} else {
					posX = getRandom((Constants.WIDTH / 2) + 50, Constants.WIDTH - 100);
					posY = getRandom((Constants.HEIGHT / 2) + 50, Constants.HEIGHT - 100);
				}
				path.add(new Vector2D(posX, posY));
			}

			gameState.getMovingObjects().add(new Enemy(
					new Vector2D(path.get(0).getX(), path.get(0).getY()),
					new Vector2D(),
					Constants.ENEMY_MAX_VEL,
					Assets.enemy,
					path,
					gameState));
		}
	}

	private void spawnEnemy2() {
		int rand = getRandom(1, 10);
		int a = getRandom(1, 4);

		for (int i = 0; i < enemySpawner2; i++) {
			double x, y, posX, posY;
			ArrayList<Vector2D> path = new ArrayList<>();

			if (a == 1) {
				a++;
				x = rand % 2 == 0 ? getRandom(50, 150) : getRandom(Constants.WIDTH - 150, Constants.WIDTH - 50);
				y = Constants.HEIGHT;
				path.add(new Vector2D(x, y));

				posY = getRandom(50, 150);
				path.add(new Vector2D(x, posY));

				posX = Constants.WIDTH - 10 - x;
				path.add(new Vector2D(posX, posY));
			} else if (a == 2) {
				a++;
				x = Constants.WIDTH;
				y = rand % 2 == 0 ? getRandom(Constants.HEIGHT - 150, Constants.HEIGHT - 50) : getRandom(50, 150);
				path.add(new Vector2D(x, y));

				posX = getRandom(50, 150);
				path.add(new Vector2D(posX, y));

				posY = Constants.HEIGHT - 10 - y;
				path.add(new Vector2D(posX, posY));
			} else if (a == 3) {
				a++;
				x = rand % 2 == 0 ? getRandom(Constants.WIDTH - 150, Constants.WIDTH - 50) : getRandom(50, 150);
				y = 0;
				path.add(new Vector2D(x, y));

				posY = getRandom(Constants.HEIGHT - 150, Constants.HEIGHT - 50);
				path.add(new Vector2D(x, posY));

				posX = Constants.WIDTH - 10 - x;
				path.add(new Vector2D(posX, posY));
			} else {
				a = 1;
				x = 0;
				y = rand % 2 == 0 ? getRandom(50, 150) : getRandom(Constants.HEIGHT - 150, Constants.HEIGHT - 50);
				path.add(new Vector2D(x, y));

				posX = getRandom(Constants.WIDTH - 150, Constants.WIDTH - 50);
				path.add(new Vector2D(posX, y));

				posY = Constants.HEIGHT - 10 - y;
				path.add(new Vector2D(posX, posY));
			}

			gameState.getMovingObjects().add(new Enemy2(
					new Vector2D(path.get(0).getX(), path.get(0).getY()),
					new Vector2D(),
					Constants.ENEMY_MAX_VEL,
					Assets.enemyA,
					path,
					gameState));
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

			ArrayList<Vector2D> path = new ArrayList<>();
			path.add(new Vector2D(x, y));
			path.add(new Vector2D(0, 0));

			gameState.getMovingObjects().add(new Enemy3(
					new Vector2D(path.get(0).getX(), path.get(0).getY()),
					new Vector2D(x, y),
					Constants.ENEMY_MAX_VEL - 2.0,
					Assets.enemyR,
					path,
					gameState));
		}
	}

	private void spawnMissiles() {
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

		for (int i = 0; i < missileCount; i++) {
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

			gameState.getMovingObjects().add(new Missile(
					new Vector2D(x, y),
					headingRandom,
					Constants.MISSILE_VEL * Math.random() + 2,
					angleMissile,
					Assets.missile,
					gameState));
		}
	}

	private void spawnPowerUp() {
		final int x = (int) ((Constants.WIDTH - Assets.orb.getWidth()) * Math.random());
		final int y = (int) ((Constants.HEIGHT - Assets.orb.getHeight()) * Math.random());

		int index = (int) (Math.random() * (PowerUpTypes.values().length));
		PowerUpTypes p = PowerUpTypes.values()[index];

		Action action = gameState.createPowerUpAction(p, new Vector2D(x, y));

		gameState.getMovingObjects().add(new PowerUp(
				new Vector2D(x, y),
				p.texture,
				action,
				gameState));
	}

	private static int getRandom(int min, int max) {
		return ((int) (Math.random() * (max - min + 1) + min));
	}

	private static int[] numRandom4() {
		int pos, nNums = 4;
		int[] arrayRandom = new int[nNums];
		java.util.Stack<Integer> conjunto = new java.util.Stack<>();

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
}
