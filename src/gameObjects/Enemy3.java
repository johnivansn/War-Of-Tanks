package gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import graphics.Assets;
import input.Constants;
import math.Vector2D;
import states.GameState;

public class Enemy3 extends MovingObject {

	private ArrayList<Vector2D> path;
	private Vector2D currentNode, playerPos;
	private int index, x, y, z;
	private boolean following;
	private double angleD, angleF;

	public Enemy3(
			Vector2D position,
			Vector2D velocity,
			double maxVel,
			BufferedImage texture,
			ArrayList<Vector2D> path,
			GameState gameState) {
		super(position, velocity, maxVel, texture, gameState);
		this.path = path;
		index = x = y = z = 0;
		following = true;
		angle = Math.PI / 2;
		angleD = angleF = 0;
	}

	private Vector2D pathFollowing() {

		playerPos = new Vector2D(gameState.getPlayer().getCenter());

		path.set(1, playerPos);

		currentNode = path.get(index);

		double distanceToNode = currentNode.subtract(getCenter()).getMagnitude();// dirige el seguimiento

		if (index == 0) { // evita el primer seguimiento de inicio
			distanceToNode = 0;
			angleF = Math.random() * Constants.ENEMY_ANGLE_RANGE;// - Constants.ENEMY_ANGLE_RANGE / 2;
		}

		if (distanceToNode < 10) {// Constants.NODE_RADIUS) {
			index++;
			if (index >= path.size()) {
				following = true;
				Destroy();
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
		if (x > 2000) {
			gameState.playExplosion2(getCenter());// ).add(new Vector2D(getRandom(0, 5), getRandom(0, 5))));
			x = 0;
		}
		if (y > 3000) {
			gameState.playExplosion2(getCenter());// .add(new Vector2D(-getRandom(0, 5), -getRandom(0, 5))));
			y = 0;
		}
		if (z > 5000) {
			gameState.playExplosion2(getCenter());// .add(new Vector2D(-getRandom(0, 5), getRandom(0, 5))));
			z = 0;
		}
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

		velocity = velocity.add(pathFollowing);
		velocity = velocity.limit(maxVel); // limita la velocidad de movimiento
		position = position.add(velocity);
		position = position.add(velocity);

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
				angle + angleF + Math.PI,
				width / 2,
				height / 2);
		g2d.drawImage(
				Assets.canyonR,
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
}
