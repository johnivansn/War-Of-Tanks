package gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import input.Constants;
import math.Vector2D;
import states.GameState;

public class Missile extends MovingObject {

	public Missile(
			Vector2D position, 
			Vector2D velocity, 
			double maxVel, 
			double angle, 
			BufferedImage texture,
			GameState gameState)
	{
		super(position, velocity, maxVel, texture, gameState);
		this.angle = angle;
		this.velocity = velocity.scale(maxVel);
	}

	@Override
	public void update(float dt) {

		Vector2D playerPos = new Vector2D(gameState.getPlayer().getCenter());

		int distanceToPlayer = (int) playerPos.subtract(getCenter()).getMagnitude();

		if (distanceToPlayer < Constants.SHIELD_DISTANCE / 2 + width / 2) {
			if (gameState.getPlayer().isShieldOn()) {
				Destroy();
			}
		}

		position = position.add(velocity);
		if (position.getX() > Constants.WIDTH || position.getY() > Constants.HEIGHT) {
			Destroy();
		}

		collidesWith();
	}

	@Override
	public void Destroy() {
		if (position.getX() > Constants.WIDTH || position.getY() > Constants.HEIGHT) {
			super.Destroy();
		} else {
			gameState.playExplosion2(getCenter());
			gameState.addScore(Constants.MISSILE_SCORE, position);
			super.Destroy();
		}
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
		at.rotate(
				angle, 
				width / 2, 
				height / 2);
		g2d.drawImage(
				texture, 
				at, 
				null);
	}
}
