package gameObjects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import graphics.Assets;
import input.Constants;
import math.Vector2D;
import states.GameState;

public class Enemy3 extends EnemyBase {

	private long x, y, z;
	private Vector2D playerPos;

	public Enemy3(
			Vector2D position,
			Vector2D velocity,
			double maxVel,
			BufferedImage texture,
			ArrayList<Vector2D> path,
			GameState gameState) {
		super(position, velocity, maxVel, texture, Assets.canyonR, path, gameState);
		x = y = z = 0;
		angleF = Math.random() * Constants.ENEMY_ANGLE_RANGE;
	}

	@Override
	protected Vector2D pathFollowing() {
		playerPos = new Vector2D(gameState.getPlayer().getCenter());
		path.set(1, playerPos);

		currentNode = path.get(index);

		double distanceToNode = currentNode.subtract(getCenter()).getMagnitude();

		if (index == 0) {
			distanceToNode = 0;
		}

		if (distanceToNode < 10) {
			index++;
			if (index >= path.size()) {
				following = true;
				Destroy();
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
			gameState.playExplosion2(getCenter());
			x = 0;
		}
		if (y > Constants.EXPLOSION_INTERVAL_2) {
			gameState.playExplosion2(getCenter());
			y = 0;
		}
		if (z > Constants.EXPLOSION_INTERVAL_3) {
			gameState.playExplosion2(getCenter());
			z = 0;
		}

		position = position.add(velocity);

		int distanceToPlayer = (int) playerPos.subtract(getCenter()).getMagnitude();
		if (distanceToPlayer < Constants.SHIELD_DISTANCE / 2 + width / 2) {
			if (gameState.getPlayer().isShieldOn()) {
				Destroy();
			}
		}
	}

	@Override
	protected void handleShooting(float dt) {
	}
}
