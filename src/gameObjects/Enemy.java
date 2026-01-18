package gameObjects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import graphics.Assets;
import math.Vector2D;
import states.GameState;

public class Enemy extends EnemyBase {

	public Enemy(
			Vector2D position,
			Vector2D velocity,
			double maxVel,
			BufferedImage texture,
			ArrayList<Vector2D> path,
			GameState gameState) {
		super(position, velocity, maxVel, texture, Assets.canyon, path, gameState);
	}

	@Override
	protected void handleSpecialBehavior(float dt) {
	}
}
