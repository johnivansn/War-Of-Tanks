package gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import math.Vector2D;
import graphics.Assets;
import graphics.Sound;
import input.Constants;
import states.GameState;

public class Enemy extends MovingObject {

	private ArrayList<Vector2D> path;
	private Vector2D currentNode, toPlayer;
	private int index;
	private boolean following, fireE;
	private long fireRate, fireRateM, p;
	private Sound shoot;
	private double angleD, currentAngleD, angleF, d;

	public Enemy(
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
		angleD = currentAngleD = d = 0;
		fireRate = fireRateM = 0;
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
						.scale(1 / Constants.ENEMY_MASS * 3);

				angle += (angleD - angle) * 0.1;
			}
		}

		velocity = velocity.add(pathFollowing).limit(maxVel);
		position = position.add(velocity);

		if (position.getX() > Constants.WIDTH ||
				position.getY() > Constants.HEIGHT ||
				position.getX() < -width ||
				position.getY() < -height)

			Destroy();

		// shoot
		if (fireRateM == 0) {
			datos();
		}

		// Se obtiene los datos solo una vez
		if (p == 0) {
			datos();
			p++;
			angleF = d;
		}

		// Se redondea el valor de angleF y d a 1 decimal
		BigDecimal op = BigDecimal.valueOf(angleF).setScale(1, RoundingMode.HALF_UP);
		angleF = op.doubleValue();
		BigDecimal io = BigDecimal.valueOf(d).setScale(1, RoundingMode.HALF_UP);
		d = io.doubleValue();

		// Manipula la rotación de acuerdo al valor de a y d
		if (angleF != d) {
			if (angleF < d) {
				angleF += 0.1;
			} else if (angleF > d) {
				angleF -= 0.1;
			}
			fireE = false; // Desactiva el fuego temporalmente
			fireRateM += 1; // Incrementa el contador de disparos
		} else {
			angleF = d; // Establece la rotación correcta
		}
		// Verifica si ha pasado un tiempo suficiente para volver a disparar
		if (fireRate > 1000) {
			fireE = true; // Activa el fuego
			fireRate = 0; // Reinicia el contador de tiempo entre disparos
			fireRateM = 0; // Reinicia el contador de disparos
		}
		if (fireE) {
			FireE fire = new FireE(getCenter().add(
					toPlayer.scale(width)),
					toPlayer, Constants.FIRE_VEL,
					d,
					Assets.fire,
					gameState);

			gameState.getMovingObjects().add(0, fire);
			fireE = false;
			shoot.play();
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

		collidesWith();
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
				Assets.canyon,
				at1,
				null);

		/*
		 * g.setColor(Color.RED);
		 *
		 * for (int i = 1; i < path.size(); i++) {// prueba
		 * g.drawOval((int) path.get(i).getX(), (int) path.get(i).getY(), 10, 10);
		 * }
		 * g.setColor(Color.GREEN);
		 * g.drawOval((int)path.get(0).getX(), (int)path.get(0).getY(), 10, 10);
		 */
	}

	private void datos() {
		toPlayer = gameState.getPlayer().getCenter().subtract(getCenter()).normalize();
		currentAngleD = toPlayer.getAngle();
		currentAngleD += Math.random() * Constants.ENEMY_ANGLE_RANGE - Constants.ENEMY_ANGLE_RANGE / 2;

		toPlayer = toPlayer.setDirection(currentAngleD);
		d = currentAngleD + Math.PI / 2;
	}
}
