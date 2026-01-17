package gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import math.Vector2D;
import graphics.Assets;
import graphics.Sound;
import input.Constants;
import states.GameState;

public class Enemy2 extends MovingObject {

	private ArrayList<Vector2D> path;
	private Vector2D currentNode, toPlayer;
	private int index;
	private boolean following, fireE;
	private long fireRate, fireRateM, p, x, y, z;
	private Sound shoot;
	private double angleD, currentAngleD, angleF, a, d;

	public Enemy2(
			Vector2D position,
			Vector2D velocity,
			double maxVel,
			BufferedImage texture,
			ArrayList<Vector2D> path,
			GameState gameState) {
		super(position, velocity, maxVel, texture, gameState);
		this.path = path;
		index = 0;
		following = true;
		fireE = false;
		angleD = currentAngleD = a = d = 0;
		fireRate = fireRateM = x = y = z = 0;
		angle = angleF = Math.PI / 2;
		shoot = new Sound(Assets.enemyShoot);
	}

	private Vector2D pathFollowing() {

		currentNode = path.get(index);

		double distanceToNode = currentNode.subtract(getCenter()).getMagnitude();// dirige el seguimiento

		if (distanceToNode < Constants.NODE_RADIUS) {
			index++;
			if (index >= path.size()) {
				following = false;

			}
		}
		return seekForce(currentNode);
	}

	private Vector2D seekForce(Vector2D target) {

		Vector2D desiredVelocity = target.subtract(getCenter());
		return desiredVelocity.subtract(velocity);

	}

	@Override
	public void update(float dt) {
		x += dt;
		y += dt;
		z += dt;
		fireRate += dt;

		Vector2D pathFollowing;
		if (following)
			pathFollowing = pathFollowing();
		else
			pathFollowing = new Vector2D();

		if (index != path.size()) {// rotacion

			angleD = pathFollowing.getAngle();

			angleD += Math.PI / 2;

			if (index == 1) {
				angle = angleD;
			} else {
				pathFollowing = currentNode.subtract(getCenter()).normalize().scale(maxVel)
						.scale(1 / Constants.ENEMY_MASS * Constants.SEEK_FORCE_MULTIPLIER);

				angle += (angleD - angle) * Constants.ROTATION_SMOOTHING_FACTOR;
			}
		} else {
			if (x > Constants.EXPLOSION_INTERVAL_1) {
				gameState.playExplosion2(getCenter().add(new Vector2D(getRandom(0, 30), getRandom(0, 30))));
				x = 0;
			}
			if (y > Constants.EXPLOSION_INTERVAL_2) {
				gameState.playExplosion2(getCenter().add(new Vector2D(-getRandom(0, 30), -getRandom(0, 30))));
				y = 0;
			}
			if (z > Constants.EXPLOSION_INTERVAL_3) {
				gameState.playExplosion2(getCenter().add(new Vector2D(-getRandom(0, 30), getRandom(0, 30))));
				z = 0;
			}
			position = position.subtract(velocity);
		}

		velocity = velocity.add(pathFollowing).limit(maxVel);
		position = position.add(velocity);

		if (position.getX() > Constants.WIDTH ||
				position.getY() > Constants.HEIGHT ||
				position.getX() < -width ||
				position.getY() < -height)

			Destroy();

		// shoot
		if (fireRateM == 0) {//
			datos();
		}

		if (p == 0) {// solo 1 vez
			datos();
			p++;
			angleF = d;
		}

		a = Math.round(angleF * 10.0) / 10.0;
		d = Math.round(d * 10.0) / 10.0;

		if (angleF != d) { // manipula la rotacion
			if (a < d) {
				angleF += 0.1;
			} else if (a > d) {
				angleF -= 0.1;
			}
			fireE = false;
			fireRateM += 1;
		} else {
			angleF = d;
		}
		if (fireRate > 1000) {
			fireE = true;
			fireRate = 0;
			fireRateM = 0;
		}
		if (fireE) {
			//
			if (index == path.size()) {
				datos();
				angleF = d;
			}
			FireE fire = new FireE(getCenter().add(
					toPlayer.scale(width)),
					toPlayer,
					Constants.FIRE_VEL,
					d,
					Assets.fire,
					gameState);

			gameState.getMovingObjects().add(0, fire);
			fireE = false;
			shoot.play();
			// shoot.changeVolume(-10.0f);
		}

		if (shoot.getFramePosition() > 8500) {
			shoot.stop();
		}

		Vector2D playerPos = new Vector2D(gameState.getPlayer().getCenter());

		int distanceToPlayer = (int) playerPos.subtract(getCenter()).getMagnitude();

		if (distanceToPlayer < Constants.SHIELD_DISTANCE / 2 + width / 2) {

			if (gameState.getPlayer().isShieldOn()) {

				Destroy();
			}
		}
	}

	@Override
	public void Destroy() {
		if (position.getX() > Constants.WIDTH || position.getY() > Constants.HEIGHT) {
			super.Destroy();
		} else {
			gameState.playExplosion1(getCenter());
			gameState.addScore(Constants.ENEMY_SCORE, position);
			super.Destroy();
		}
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		at = AffineTransform.getTranslateInstance(position.getX(), position.getY());

		AffineTransform at1 = AffineTransform.getTranslateInstance(position.getX(), position.getY());

		at.rotate(
				angle,
				width / 2,
				height / 2);

		g2d.drawImage(
				texture,
				at,
				null);
		at1.rotate(
				angleF + Math.PI,
				width / 2,
				height / 2);
		g2d.drawImage(
				Assets.canyonA,
				at1,
				null);

		g.setColor(Color.RED);

		/*
		 * for (int i = 1; i < path.size(); i++) {// prueba
		 * g.drawOval((int) path.get(i).getX(), (int) path.get(i).getY(), 10, 10);
		 * }
		 * g.setColor(Color.GREEN);
		 * g.drawOval((int) path.get(0).getX(), (int) path.get(0).getY(), 10, 10);
		 */
	}

	private static int getRandom(int min, int max) { // genera N aleatorio de min a max
		return ((int) (Math.random() * ((max + 1) - min) + min));
	}

	private void datos() {
		toPlayer = gameState.getPlayer().getCenter().subtract(getCenter()).normalize();
		currentAngleD = toPlayer.getAngle();
		currentAngleD += Math.random() * Constants.ENEMY_ANGLE_RANGE - Constants.ENEMY_ANGLE_RANGE / 2;// altera la
																										// punteria

		toPlayer = toPlayer.setDirection(currentAngleD);
		d = currentAngleD += Math.PI / 2;
	}
}
