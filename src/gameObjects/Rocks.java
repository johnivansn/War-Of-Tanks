package gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import graphics.Assets;
import input.Constants;
import math.Vector2D;
import states.GameState;

public class Rocks extends MovingObject {
	private BufferedImage tex;

	public Rocks(Vector2D position, BufferedImage texture, GameState gameState) {
		super(position, new Vector2D(), 0, texture, gameState);
		this.tex = texture;
	}

	@Override
	public void update(float dt) {

		if (tex.equals(Assets.piedra)) {
			this.Dead = false;
		} else {
			Vector2D playerPos = new Vector2D(gameState.getPlayer().getCenter());

			int distanceToPlayer = (int) playerPos.subtract(getCenter()).getMagnitude();

			if (distanceToPlayer < Constants.SHIELD_DISTANCE / 2 + width / 2) {
				if (gameState.getPlayer().isShieldOn()) {
					Destroy();
				}
			}
		}
	}

	@Override
	public void Destroy() {
		if (tex != Assets.piedra) {
			gameState.playExplosion1(getCenter());
		}
		super.Destroy();
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		at = AffineTransform.getTranslateInstance(position.getX() - width / 2, position.getY());

		g2d.drawImage(texture, at, null);
	}

	@Override
	public Vector2D getCenter() {
		return new Vector2D(position.getX(), position.getY() + height / 2);
	}

	public BufferedImage getTexture() {
		return texture;
	}
}
