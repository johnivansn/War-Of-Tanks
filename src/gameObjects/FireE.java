package gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import input.Constants;
import math.Vector2D;
import states.GameState;

public class FireE extends MovingObject {

	public FireE(
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
				gameState.playExplosion2(getCenter());
			}
		}
		
		position = position.add(velocity);
		if (position.getX() < 0 || 
			position.getX() > Constants.WIDTH || 
			position.getY() < 0 || 
			position.getY() > Constants.HEIGHT) 
		{
			Destroy();
		}
		collidesWith();
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		at = AffineTransform.getTranslateInstance(position.getX() - width / 2, position.getY());
		at.rotate(
				angle, 
				width / 2,
				0);

		g2d.drawImage(
				texture, 
				at, 
				null);
	}

	@Override
	public Vector2D getCenter() {
		return new Vector2D(position.getX() + width / 2, position.getY() + width / 2);
	}
}
