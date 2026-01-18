package gameObjects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import graphics.Assets;
import input.Constants;
import math.Vector2D;
import states.GameState;

public class Enemy2 extends EnemyBase {

	private long x, y, z;

	public Enemy2(
			Vector2D position,
			Vector2D velocity,
			double maxVel,
			BufferedImage texture,
			ArrayList<Vector2D> path,
			GameState gameState) {
		super(position, velocity, maxVel, texture, Assets.canyonA, path, gameState);
		x = y = z = 0;
	}

	@Override
	protected Vector2D pathFollowing() {
		currentNode = path.get(index);

		double distanceToNode = currentNode.subtract(getCenter()).getMagnitude();

		if (distanceToNode < Constants.NODE_RADIUS && index < path.size() - 1) {
			index++;
			if (index >= path.size()) {
				following = false;
			}
		}
		return seekForce(currentNode);
	}

	@Override
	protected void handleSpecialBehavior(float dt) {
		x += dt;
		y += dt;
		z += dt;

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

		if (index == path.size()) {
			position = position.subtract(velocity);
		}
	}

	@Override
	protected void fireProjectile() {
		if (index == path.size()) {
			calculateTargetAngle();
			angleF = d;
		}
		super.fireProjectile();
	}

	private static int getRandom(int min, int max) {
		return ((int) (Math.random() * ((max + 1) - min) + min));
	}
}
