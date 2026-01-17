package gameObjects;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

import graphics.Assets;
import input.Constants;
import math.Vector2D;
import states.GameState;

public class FirePool {
	private static final int POOL_SIZE = 50;
	private final Queue<Fire> available = new LinkedList<>();
	private final GameState gameState;

	public FirePool(GameState gameState) {
		this.gameState = gameState;
		for (int i = 0; i < POOL_SIZE; i++) {
			available.offer(new Fire(
					new Vector2D(),
					new Vector2D(),
					Constants.FIRE_VEL,
					0,
					Assets.fire,
					gameState));
		}
	}

	public Fire acquire(Vector2D position, Vector2D direction, double angle, BufferedImage texture) {
		Fire fire = available.poll();
		if (fire == null) {
			fire = new Fire(position, direction, Constants.FIRE_VEL, angle, texture, gameState);
		} else {
			fire.reset(position, direction, angle, texture);
		}
		return fire;
	}

	public void release(Fire fire) {
		available.offer(fire);
	}
}
